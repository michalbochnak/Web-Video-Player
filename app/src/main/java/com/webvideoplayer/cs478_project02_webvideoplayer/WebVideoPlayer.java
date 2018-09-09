//
// Michal Bochnak, Netid: mbochn2
// CS 478 - Project #02, Web Video Player
// UIC, March 2, 2018
// Professor: Ugo Buy
//
// WebVideoPlayer.java
//


package com.webvideoplayer.cs478_project02_webvideoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


//
// Opens the url from intent inside the activity (itself)
//
public class WebVideoPlayer extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video_player);

        wv = findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());

        // add "http://" to the url if it was not provided
        String url = getIntent().getStringExtra("url");
        if(!url.contains("http")) {
            url = "http://" + url;
        }

        wv.loadUrl(url);
    }

}
