package com.riznicreation.mylibrary;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.concurrent.atomic.AtomicBoolean;

public class BookActivity extends AppCompatActivity{

    private ImageView imgBook;
    private TextView txtID, txtName, txtAuthor, txtPages, txtLongDesc;
    private FloatingActionMenu faMenu;
    private FloatingActionButton btnCRBooks, btnWishlist, btnARBooks, btnFavourite;
    private Button buyNow;
    private ScrollView SVMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initViews();
        handleNotchScreen();

        Intent intent = getIntent();
        if(intent != null) {
            int bookID = intent.getIntExtra("bookID", -1);
            if(bookID != -1){
                Book book = DB_Book.getInstance(this).getBook(bookID);
                if(book == null){
                    Toast.makeText(this, "Null occurred", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                    setData(DB_Book.getInstance(this).getBook(bookID));
                    handleAlreadyReadBook(DB_Book.getInstance(this).getBook(bookID));
                    handleCurrentlyReadingBook(DB_Book.getInstance(this).getBook(bookID));
                    handleWishlist(DB_Book.getInstance(this).getBook(bookID));
                    handleFavourite(DB_Book.getInstance(this).getBook(bookID));
                    handleBuyNow(DB_Book.getInstance(this).getBook(bookID));
                }
            }
        }

        AtomicBoolean fabexpanded = new AtomicBoolean(false);
        faMenu.setOnMenuToggleListener(v -> {
            if (!fabexpanded.get()) {
                faMenu.setBackgroundColor(ContextCompat.getColor(this,R.color.Theme_fab_BG));
                fabexpanded.set(true);
            } else{
                faMenu.setBackgroundColor(Color.TRANSPARENT);
                fabexpanded.set(false);
            }
        });

    }

    private void handleBuyNow(Book book) {
        buyNow.setOnClickListener(v -> {
            if( (book.getBuyLink() != null) && (!book.getBuyLink().isEmpty()) ){
                startActivity(new Intent(this, WebViewActivity.class).putExtra("URL",book.getBuyLink()));
            }else{
                Toast.makeText(this,  "Link not provided", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFavourite(Book book) {
        for (Book book1 : DB_Book.getInstance(this).getFavouriteBooks()) {
            if (book1.getId() == book.getId()) {
                btnFavourite.setEnabled(false);
            }
        }

        if (btnFavourite.isEnabled()){
            btnFavourite.setOnClickListener(v -> {
                if(DB_Book.getInstance(this).addTo_favouriteBooks(book)){
                    Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error occurred, try again", Toast.LENGTH_SHORT).show();
                }

                faMenu.close(true);
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            });
        }
    }

    private void handleWishlist(Book book) {
        for (Book book1 : DB_Book.getInstance(this).getWishListBooks()) {
            if (book1.getId() == book.getId()) {
                btnWishlist.setEnabled(false);
            }
        }

        if (btnWishlist.isEnabled()){
            btnWishlist.setOnClickListener(v -> {
                if(DB_Book.getInstance(this).addTo_wishlist(book)){
                    Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error occurred, try again", Toast.LENGTH_SHORT).show();
                }

                faMenu.close(true);
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            });
        }
    }

    private void handleCurrentlyReadingBook(Book book) {
        for (Book book1 : DB_Book.getInstance(this).getCurrentReadingBooks()) {
            if (book1.getId() == book.getId()) {
                btnCRBooks.setEnabled(false);
            }
        }

        if (btnCRBooks.isEnabled()){
            btnCRBooks.setOnClickListener(v -> {
                if(DB_Book.getInstance(this).addTo_currentlyReadingBook(book)){
                    Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error occurred, try again", Toast.LENGTH_SHORT).show();
                }

                faMenu.close(true);
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            });
        }
    }

    private void handleAlreadyReadBook(Book book) {

        for (Book book1 : DB_Book.getInstance(this).getAlreadyReadBooks()) {
            if (book1.getId() == book.getId()) {
                btnARBooks.setEnabled(false);
            }
        }

        if (btnARBooks.isEnabled()){
            btnARBooks.setOnClickListener(v -> {
                if(DB_Book.getInstance(this).addTo_alreadyReadBooks(book)){
                    Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error occurred, try again", Toast.LENGTH_SHORT).show();
                }

                faMenu.close(true);
                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            });
        }
    }

    private void setData(@NonNull Book book) {
        Glide.with(this)
                .asBitmap()
                .load(book.getImgURL())
                .error(R.drawable.ic_error)
                .into(imgBook);

        txtID.setText(String.valueOf(book.getId()));
        txtName.setText(book.getName());
        txtPages.setText(String.valueOf(book.getPages()));
        txtAuthor.setText(book.getAuthor());
        txtLongDesc.setText(book.getLongDesc());
    }

    private void initViews() {
        imgBook = findViewById(R.id.imgBook);
        txtID = findViewById(R.id.txtID);
        txtName = findViewById(R.id.txtName);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtPages = findViewById(R.id.txtPages);
        txtLongDesc = findViewById(R.id.txtLongDesc);
        faMenu = findViewById(R.id.faMenu);
        btnCRBooks = findViewById(R.id.btnCRBooks);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnARBooks = findViewById(R.id.btnARBooks);
        btnFavourite = findViewById(R.id.btnFavourite);
        buyNow = findViewById(R.id.btnBuyNow);
    }

    private void handleNotchScreen() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight > convertDpToPixel(24)) {
            SVMain = findViewById(R.id.SVMain);
            SVMain.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private int convertDpToPixel ( float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}