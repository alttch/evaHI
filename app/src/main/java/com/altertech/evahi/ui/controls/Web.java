package com.altertech.evahi.ui.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altertech.evahi.BuildConfig;
import com.altertech.evahi.core.App;

public class Web extends WebView {

    private WebViewClient
            client;

    public Web(Context context) {
        super(context);
    }

    public Web(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Web(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Web(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Web allowCookies(
            boolean state) {

        CookieManager.getInstance().setAcceptThirdPartyCookies(
                this,
                state
        );
        CookieManager.getInstance().setAcceptCookie(
                state
        );
        return this;
    }

    public Web clearCookies() {

        // WebViewDatabase.getInstance(this.getContext()).clearHttpAuthUsernamePassword();

        CookieManager.getInstance().removeAllCookies(
                value -> Log.d(App.TAG, "Cookie removed: " + value)
        );
        CookieManager.getInstance().removeSessionCookies(
                value -> Log.d(App.TAG, "Cookie removed: " + value)
        );
        CookieManager.getInstance().flush();

        return
                this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public Web init() {

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
        this.getSettings().setJavaScriptEnabled(true);

        this.setVerticalScrollBarEnabled(false);
        return
                this;
    }

    public Web client(
            WebViewClient client
    ) {
        this.setWebViewClient(
                this.client = client
        );
        return this;
    }

    public Web clientChrome(
            WebChromeClient client
    ) {
        this.setWebChromeClient(
                client
        );
        return this;
    }


}
