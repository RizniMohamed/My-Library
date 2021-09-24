package com.riznicreation.mylibrary;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
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
    private RelativeLayout RLMain;

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