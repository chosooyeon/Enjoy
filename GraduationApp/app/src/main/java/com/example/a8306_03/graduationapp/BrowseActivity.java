package com.example.a8306_03.graduationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class BrowseActivity extends AppCompatActivity {

    //private String URL = "url";
    private String currentGroupName; //넘겨받은 groupName

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        currentGroupName = getIntent().getExtras().get("groupName").toString(); //groupName으로 넘겨받은 값을 받아온다
        Toast.makeText(BrowseActivity.this,currentGroupName,Toast.LENGTH_SHORT).show();

        //Intent intent = getIntent();
        //String url = intent.getStringExtra(URL);

        WebView webView = (WebView)findViewById(R.id.webView);
        //새창 뜨지 않게 하기 위해서
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(currentGroupName);

    }

}
