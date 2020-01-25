package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Date;
import java.util.List;

public class StatystykiFragment extends Fragment {

    WebView webView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_statystyki, container, false);
        webView = view.findViewById(R.id.stawtystyki_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName(document.getElementsByClassName('lr-imso-fix')[2].className)[0].style.display='none'; " +
                        "document.getElementsByClassName('immersive-container')[9].children[1].children[1].children[0].style.height = '47px';})()");
            }

        });
        webView.loadUrl("https://www.google.com/search?q=%C5%9Bl%C4%85sk+wroc%C5%82aw#sie=t;/m/03b7fb;2;/m/09nw6c;mt;fp;1");
        webView.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }

        });

        return view;
    }

    private void webViewGoBack(){
        webView.goBack();
    }


}
