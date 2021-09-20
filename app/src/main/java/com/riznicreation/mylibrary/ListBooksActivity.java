package com.riznicreation.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Objects;

public class ListBooksActivity extends AppCompatActivity {

    private RecyclerView rvBook;
    private BookRecViewAdaptor adaptor;

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


    }

}