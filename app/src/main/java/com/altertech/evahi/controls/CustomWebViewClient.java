package com.altertech.evahi.controls;

import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altertech.evahi.core.BaseApplication;

public class CustomWebViewClient extends WebViewClient {
    private BaseApplication application;

    public CustomWebViewClient(BaseApplication application) {
        this.application = application;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        BaseApplication.Profiles.Profile profile = this.application.profiles().get(this.application.id());
        handler
                .proceed(profile.settings.name(), profile.settings.password());
    }
}