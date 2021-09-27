package com.riznicreation.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class DB_Book {
    private static DB_Book instance;

    private final String ALL_BOOKS_KEY = "All books";
    private final String CURRENT_READING_BOOKS = "Current Reading Books";
    private final String ALREADY_READ_BOOKS = "Already Read Books";
    private final String WISHLIST_BOOKS = "WishList Books";
    private final String FAVOURITE_BOOKS = "Favourite Books";

    private final SharedPreferences sharedPreferences;

    private DB_Book(Context context) {

        sharedPreferences = context.getSharedPreferences("Book_Database",Context.MODE_PRIVATE);

        if (getAllBooks() == null) {
            initData();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        if (getCurrentReadingBooks() == null){
            editor.putString(CURRENT_READING_BOOKS,gson.toJson(new ArrayList<>()));
            editor.apply();
        }

        if (getAlreadyReadBooks() == null){
            editor.putString(ALREADY_READ_BOOKS,gson.toJson(new ArrayList<>()));
            editor.apply();
        }

        if (getWishListBooks() == null){
            editor.putString(WISHLIST_BOOKS,gson.toJson(new ArrayList<>()));
            editor.apply();
        }

        if (getFavouriteBooks() == null){
            editor.putString(FAVOURITE_BOOKS,gson.toJson(new ArrayList<>()));
            editor.apply();
        }
    }

    public ArrayList<Book> getAllBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALL_BOOKS_KEY,null),type);
    }

    public ArrayList<Book> getCurrentReadingBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(CURRENT_READING_BOOKS,null),type);
    }

    public ArrayList<Book> getAlreadyReadBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(ALREADY_READ_BOOKS,null),type);
    }

    public ArrayList<Book> getWishListBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(WISHLIST_BOOKS,null),type);
    }

    public ArrayList<Book> getFavouriteBooks() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(FAVOURITE_BOOKS,null),type);
    }

    public static DB_Book getInstance(Context context) {
        if (instance == null)
            instance = new DB_Book(context);
        return instance;
    }

    private void initData() {
        ArrayList<Book> books = new ArrayList<>();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(ALL_BOOKS_KEY,gson.toJson(books));
        editor.apply();
    }

    public Book getBook(int id){

        ArrayList<Book> books = getAllBooks();
        for ( Book b : books){
            if (b.getId() == id){
                return books.get(books.indexOf(b));
            }
        }
        return null;
    }

    public Book getBook(String name_or_author){

        ArrayList<Book> books = getAllBooks();
        for ( Book b : books){
            if (b.getAuthor().equals(name_or_author) || b.getName().equals(name_or_author) ){
                return books.get(books.indexOf(b));
            }
        }
        return null;
    }

    public void addTo_AllBooks(Book book){
        ArrayList<Book> books = getAllBooks();
        if (books.add(book)){
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(ALL_BOOKS_KEY);
            editor.putString(ALL_BOOKS_KEY,gson.toJson(books));
            editor.apply();
        }
    }

    public boolean addTo_alreadyReadBooks(Book book){
        ArrayList<Book> books = getAlreadyReadBooks();
        if (books.add(book)){
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(ALREADY_READ_BOOKS);
            editor.putString(ALREADY_READ_BOOKS,gson.toJson(books));
            editor.apply();
            return true;
        }
        return  false;
    }

    public boolean addTo_wishlist(Book book) {
        ArrayList<Book> books = getWishListBooks();
        if (books.add(book)){
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(WISHLIST_BOOKS);
            editor.putString(WISHLIST_BOOKS,gson.toJson(books));
            editor.apply();
            return true;
        }
        return  false;
    }

    public boolean addTo_currentlyReadingBook(Book book) {
        ArrayList<Book> books = getCurrentReadingBooks();
        if (books.add(book)){
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CURRENT_READING_BOOKS);
            editor.putString(CURRENT_READING_BOOKS,gson.toJson(books));
            editor.apply();
            return true;
        }
        return  false;
    }

    public boolean addTo_favouriteBooks(Book book) {
        ArrayList<Book> books = getFavouriteBooks();
        if (books.add(book)){
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(FAVOURITE_BOOKS);
            editor.putString(FAVOURITE_BOOKS,gson.toJson(books));
            editor.apply();
            return true;
        }
        return false;
    }

    public void removeFrom_alreadyReadBooks(Book book){
        ArrayList<Book> books = getAlreadyReadBooks();
        for ( Book b : books) {
            if (b.getId() == book.getId()) {
                if (books.remove(b)) {
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(ALREADY_READ_BOOKS);
                    editor.putString(ALREADY_READ_BOOKS, gson.toJson(books));
                    editor.apply();
                }
                return;
            }
        }
    }

    public synchronized void removeFrom_wishlist(Book book) {
        ArrayList<Book> books = getWishListBooks();

        for (Book bk : books) {
            if (bk.getId() == book.getId()) {
                if (books.remove(bk)) {
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(WISHLIST_BOOKS);
                    editor.putString(WISHLIST_BOOKS, gson.toJson(books));
                    editor.apply();
                }
                return;
            }
        }
    }

    public void removeFrom_currentlyReadingBook(Book book) {
        ArrayList<Book> books = getCurrentReadingBooks();
        for ( Book b : books) {
            if (b.getId() == book.getId()) {
                if (books.remove(b)) {
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(CURRENT_READING_BOOKS);
                    editor.putString(CURRENT_READING_BOOKS, gson.toJson(books));
                    editor.apply();
                }
                return;
            }
        }
    }

    public void removeFrom_favouriteBooks(Book book) {
        ArrayList<Book> books = getFavouriteBooks();
        for ( Book b : books){
            if (b.getId() == book.getId()){
                if (books.remove(b)){
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(FAVOURITE_BOOKS);
                    editor.putString(FAVOURITE_BOOKS,gson.toJson(books));
                    editor.apply();
                }
                return;
            }
        }
    }

}
