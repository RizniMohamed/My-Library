package com.riznicreation.mylibrary;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class FetchBook extends AsyncTask<String,Void,String>{

    // Class name for Log tag
    private static final String LOG_TAG = FetchBook.class.getSimpleName();
    /* Variables for the search input field, and results TextViews */
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final ArrayList<Book> bookList = new ArrayList<>();

    // Constructor providing a reference to the views in MainActivity
    public FetchBook(Context context) {
        this.context = context;
    }

    /**
     * Makes the Books API call off of the UI thread.
     *
     * @param params String array containing the search data.
     * @return Returns the JSON string from the Books API or
     *         null if the connection failed.
     */
    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            Log.d(LOG_TAG, "onPostExecute: "+ itemsArray);

            // Initialize iterator and results fields.
            int i = 0;

            String title = "";
            String authors = "";
            String BuyLink;
            int id;
            int pageCount;
            String longDesc;
            String shortDesc;
            String imageURL = null;

            StringBuilder categorires = new StringBuilder();

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (authors.equals("") && title.equals(""))) {
                // Get the current item information.
                JSONObject jsonBook = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = jsonBook.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.

                try {
                    title = volumeInfo.getString("title");
                    BuyLink = volumeInfo.getString("infoLink");
                    id = (int) (System.nanoTime()/1000000000) + i;
                    longDesc = volumeInfo.getString("description");
                    pageCount = Integer.parseInt(volumeInfo.getString("pageCount"));


                    JSONObject ImageInfo = volumeInfo.getJSONObject("imageLinks");
                    Iterator<String> keys = ImageInfo.keys();
                    while(keys.hasNext()) {
                        imageURL = ImageInfo.getString(keys.next());
                    }

                    JSONArray authorInfo = volumeInfo.getJSONArray("authors");
                    for ( int j = 0; j < authorInfo.length(); j++){
                        authors += " " + authorInfo.getString(j);
                    }

                    JSONArray catInfo = volumeInfo.getJSONArray("categories");
                    for ( int j = 0; j < catInfo.length(); j++){
                        categorires.append(" ").append(catInfo.getString(j));
                    }
                    shortDesc = "category " + categorires + "\n\n" +
                            "published Date " + volumeInfo.getString("publishedDate");


                    Book book = new Book();
                    book.setName(title);
                    book.setAuthor(authors);
                    book.setBuyLink(BuyLink);
                    book.setId(id);
                    book.setLongDesc(longDesc);
                    book.setPages(pageCount);
                    book.setShortDesc(shortDesc);
                    book.setImgURL(imageURL);
                    bookList.add(book);

                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (!title.equals("") && !authors.equals("")){
                ListBooksActivity.loadSearchBooks(context,bookList);
            } else {
                // If none are found, update the UI to show failed results.
                Toast.makeText(context, "No results", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            Toast.makeText(context, "No Such term", Toast.LENGTH_SHORT).show();
            ListBooksActivity.prgDialog.cancel();
            e.printStackTrace();
        }
    }
}