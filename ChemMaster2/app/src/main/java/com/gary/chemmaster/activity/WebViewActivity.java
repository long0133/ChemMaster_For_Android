package com.gary.chemmaster.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gary.chemmaster.R;

public class WebViewActivity extends AppCompatActivity {


    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String url = getIntent().getStringExtra("url");

        webView = new WebView(this);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        setContentView(webView);

        Toast.makeText(this,"正在加载网页...",Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (webView.canGoBack())
            {
                webView.goBack();
            }
            else
            {
                finish();
            }
            return false;
        }

        return true;
    }

}
