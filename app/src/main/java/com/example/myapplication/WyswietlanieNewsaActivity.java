package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class WyswietlanieNewsaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String url_strony = b.getString("url");

        setContentView(R.layout.activity_wyswietlanie_newsa);

        url_strony = "http://slaskwroclaw.pl" + url_strony;

        WebView webView = findViewById(R.id.widok_strony);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("vnd.youtube") || url.startsWith("https://www.youtube.com") || url.startsWith("www.youtube.com") || url.startsWith("http://www.youtube.com")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });
        webView.loadUrl(url_strony);

    }


}
