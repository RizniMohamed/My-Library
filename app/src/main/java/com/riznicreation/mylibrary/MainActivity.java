package com.riznicreation.mylibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnABooks, btnCRBooks, btnARBooks, btnWishlist, btnFavourite, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        btnABooks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListBooksActivity.class);
            intent.putExtra("Activity", "AllBooksActivity");
            startActivity(intent);
        });

        btnARBooks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListBooksActivity.class);
            intent.putExtra("Activity", "AlreadyReadBooks");
            startActivity(intent);
        });

        btnCRBooks.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListBooksActivity.class);
            intent.putExtra("Activity", "CurrentlyReadingBooks");
            startActivity(intent);
        });

        btnWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListBooksActivity.class);
            intent.putExtra("Activity", "Wishlist");
            startActivity(intent);
        });

        btnFavourite.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListBooksActivity.class);
            intent.putExtra("Activity", "Favourite");
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This the book library application. you can show details about books and get get link for buy it\n\n" +
                    "This application is made by MN Rizni Mohamed who is the owner of RizniCreation");
            builder.setNegativeButton("Dismiss",(dialog, which) -> {});
            builder.setPositiveButton("Visit",(dialog, which) -> {
                startActivity(new Intent(this, WebViewActivity.class).putExtra("URL","https://www.google.com/"));
            });
            builder.setCancelable(false);
            builder.create().show();

        });

    }

    private void initViews() {
        btnABooks = findViewById(R.id.btnABooks);
        btnCRBooks = findViewById(R.id.btnCRBooks);
        btnARBooks = findViewById(R.id.btnARBooks);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnFavourite = findViewById(R.id.btnFavourite);
        btnAbout = findViewById(R.id.btnAbout);
    }
}