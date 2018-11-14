package com.example.sooyeon.graduationproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class BrowseActivity extends AppCompatActivity {

    private String URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Intent intent = getIntent();
        String url = intent.getStringExtra(URL);

        WebView webView = (WebView)findViewById(R.id.webView);
        //새창 뜨지 않게 하기 위해서
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);

    }
}
