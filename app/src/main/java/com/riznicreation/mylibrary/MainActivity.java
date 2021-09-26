package com.riznicreation.mylibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DisplayCutout;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button btnABooks, btnCRBooks, btnARBooks, btnWishlist, btnFavourite, btnAbout;
    private ScrollView RLMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews();

        handleNotchScreen();

        handleButtons(btnABooks, "AllBooksActivity");
        handleButtons(btnARBooks, "AlreadyReadBooks");
        handleButtons(btnCRBooks, "CurrentlyReadingBooks");
        handleButtons(btnWishlist, "Wishlist");
        handleButtons(btnFavourite, "Favourite");

        btnAbout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This application created for book management. Currently users can find the books via google book api and get links to buy it\n\n" +
                    "Version : 1.0\n" +
                    "Mail    : mnriznimohamed@gmail.com");
            builder.setNegativeButton("Dismiss",(dialog, which) -> {});
            builder.setPositiveButton("Developer profile",(dialog, which) -> {
                startActivity(new Intent(this, WebViewActivity.class).putExtra("URL","https://www.linkedin.com/in/rizni-mohamed-2316301b4/"));
            });
            builder.setCancelable(false);
            builder.create().show();
        });

    }

    private void handleButtons(Button button, String value){
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListBooksActivity.class);
            intent.putExtra("Activity", value);
            startActivity(intent);
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