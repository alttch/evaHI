package com.altertech.evahi.controls;

import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altertech.evahi.core.BApp;
import com.altertech.evahi.models.profiles.Profile;

public class CustomWebViewClient extends WebViewClient {
    private BApp application;

    public CustomWebViewClient(BApp application) {
        this.application = application;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
        Profile profile = this.application.profiles().get(this.application.id());
        handler
                .proceed(profile.settings.name(), profile.settings.password());
    }
}