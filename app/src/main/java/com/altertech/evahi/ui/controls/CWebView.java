package com.altertech.evahi.ui.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altertech.evahi.BuildConfig;

public class CWebView extends WebView {

    private WebViewClient
            client;

    public CWebView(Context context) {
        super(context);
    }

    public CWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CWebView allowCookies(
            boolean state) {
        CookieManager.getInstance().setAcceptThirdPartyCookies(
                this,
                true
        );
        CookieManager.getInstance().setAcceptCookie(
                true
        );
        return this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public CWebView init() {
        this.getSettings().setSavePassword(
                false
        );
        this.getSettings().setSaveFormData(
                false
        );
        this.getSettings().setUseWideViewPort(
                true
        );
        this.getSettings().setBuiltInZoomControls(
                true
        );
        this.getSettings().setSupportZoom(
                true
        );
        this.getSettings().setJavaScriptEnabled(
                true
        );
        this.getSettings().setDisplayZoomControls(
                false
        );
        this.getSettings().setUserAgentString(
                "evaHI" + " (" + BuildConfig.VERSION_NAME + ", " + this.getSettings().getUserAgentString() + ") "
        );
        return
                this;
    }

    public CWebView client(
            WebViewClient client
    ) {
        this.setWebViewClient(
                this.client = client
        );
        return this;
    }

    public CWebView clientChrome(
            WebChromeClient client
    ) {
        this.setWebChromeClient(
                client
        );
        return this;
    }


}
