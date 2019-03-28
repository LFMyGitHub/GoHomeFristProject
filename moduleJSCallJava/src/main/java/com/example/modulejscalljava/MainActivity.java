package com.example.modulejscalljava;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.modulecommon.base.BaseActivity;
import com.example.modulecommon.common.Contants;
import com.example.modulecommon.utils.JSToAndroid;
import com.github.lzyzsd.jsbridge.BridgeWebView;

public class MainActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initWebView();
    }

    private void initView(){
        bridgeWebView = findViewById(R.id.yun_account_webView);
        mProgressBar = findViewById(R.id.yun_account_progress_bar);
    }

    private void initWebView(){
        //https过滤ssl
        bridgeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        bridgeWebView.addJavascriptInterface(new JSToAndroid(this), "meloon");
        //可响应alert
        bridgeWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {//加载完成
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        bridgeWebView.loadUrl(Contants.ModuleJSCallJava.DEFAULT_URL);
    }
}
