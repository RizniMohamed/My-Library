package com.riznicreation.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private  WebView webView;
    private ConstraintLayout CLMain;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);

        ProgressDialog prgDialog = ProgressDialog.show(WebViewActivity.this, "", "Loading. Please wait...", true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                webView.setVisibility(View.INVISIBLE);
                prgDialog.setProgress(newProgress);

                if(newProgress == 100){
                    prgDialog.cancel();
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });

        handleNotchScreen();
        webView.loadUrl(getIntent().getStringExtra("URL"));
        webView.getSettings().setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    private void handleNotchScreen() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        if (statusBarHeight > convertDpToPixel(24)) {
            CLMain = (ConstraintLayout) findViewById(R.id.CLMain);
            CLMain.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private int convertDpToPixel ( float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}