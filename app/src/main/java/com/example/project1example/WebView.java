package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

public class WebView extends AppCompatActivity {

    android.webkit.WebView webView;

    String weblink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_web_view );


        webView=findViewById( R.id.webView );

         weblink=getIntent().getStringExtra( "link" );

        webView.setWebViewClient(new WebViewClient());
      //  webView.loadUrl("https://www.google.com");

        webView.loadUrl("https://"+weblink.trim());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }




}