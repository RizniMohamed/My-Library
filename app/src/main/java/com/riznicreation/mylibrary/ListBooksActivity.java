package com.riznicreation.mylibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ListBooksActivity extends AppCompatActivity {

    private static RecyclerView rvBook;
    private static BookRecViewAdaptor adaptor;
    private RelativeLayout RLMain;

    private EditText txtSearch;
    private Button btnSearch;
    private static ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        Intent intent = getIntent();
        String activityName = intent.getStringExtra("Activity");

        adaptor = new BookRecViewAdaptor(this, activityName);
        rvBook = findViewById(R.id.rvBook);


        switch (activityName) {
            case "AllBooksActivity": adaptor.setBooks(DB_Book.getInstance(this).getAllBooks()); break;
            case "AlreadyReadBooks": adaptor.setBooks(DB_Book.getInstance(this).getAlreadyReadBooks()); break;
            case "CurrentlyReadingBooks": adaptor.setBooks(DB_Book.getInstance(this).getCurrentReadingBooks()); break;
            case "Wishlist": adaptor.setBooks(DB_Book.getInstance(this).getWishListBooks()); break;
            case "Favourite": adaptor.setBooks(DB_Book.getInstance(this).getFavouriteBooks()); break;
            default:
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }

        rvBook.setAdapter(adaptor);

        rvBook.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        handleNotchScreen();

        txtSearch = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> searchBooks());

    }

    static void loadSearchBooks(Context context, ArrayList<Book> booksList){
        adaptor = new BookRecViewAdaptor(context, "AllBooksActivity");
        adaptor.setBooks(booksList);
        rvBook.setAdapter(adaptor);
        rvBook.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        prgDialog.cancel();
        DB_Book.getInstance(context).addTo_AllBooks(booksList);

    }

    public void searchBooks() {
        // Get the search string from the input field.
        String queryString = txtSearch.getText().toString();

        if (!queryString.isEmpty()){

            // Hide the keyboard when the button is pushed.
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            // Check the status of the network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
            if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
                new FetchBook(this).execute(queryString);
                prgDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
            }
            else {
                if (queryString.length() == 0) {
                    Toast.makeText(this, "No such term", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Enter book name", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNotchScreen() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight > convertDpToPixel(24)) {
            RLMain = findViewById(R.id.RLMain);
            RLMain.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private int convertDpToPixel ( float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

}