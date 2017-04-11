package com.shawnway.nav.app.yylg.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shawnway.nav.app.yylg.style.WebViewStyleFactory.LaunchDisplayer;

/**
 * Created by Eiffel on 2015/11/16.
 */
public class MyWebViewClient extends WebViewClient {
    private LaunchDisplayer _launchDisplayer;
    public MyWebViewClient(LaunchDisplayer launchDisplayer) {
        _launchDisplayer = launchDisplayer;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i(getClass().getSimpleName(), "on pageStarted:" + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        _launchDisplayer.connectSuccess();// to update UI
        Log.i(getClass().getSimpleName(), "onPageFinished:" + url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        Log.i(getClass().getSimpleName(), "errorCode:" + errorCode
                + " failingUrl:" + failingUrl);

        _launchDisplayer.connectFalse();

    }

}
