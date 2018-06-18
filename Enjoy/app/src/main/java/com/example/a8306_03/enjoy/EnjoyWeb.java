package com.example.a8306_03.enjoy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class EnjoyWeb extends Activity {
    WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enjoy_web);

        web=(WebView) findViewById(R.id.web);
        web.setWebViewClient(new MyWebView());
        web.loadUrl("http://www.google.com");//url을 바꿔준다.

    }

    public class MyWebView extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    public void mClick(View view){
        switch (view.getId()){
            case R.id.btngo:
                EditText edit = (EditText)findViewById(R.id.edit);
                String url = edit.getText().toString();
                break;
            case R.id.btnback:
            case R.id.btnforward:
            case R.id.btnLocal:
        }
    }
}
