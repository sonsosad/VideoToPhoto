package com.son.videotophoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebViewSetup extends AppCompatActivity {
        String url;
        WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        AddEvents();
    }

    private void  AddEvents(){
            webView = findViewById(R.id.webView);
            Intent intent = getIntent();
            url = intent.getStringExtra("PUSHDATA");

            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(url);
    }
}