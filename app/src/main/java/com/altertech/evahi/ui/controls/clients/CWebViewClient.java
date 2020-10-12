package com.altertech.evahi.ui.controls.clients;

import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altertech.evahi.core.App;
import com.altertech.evahi.core.models.profiles.Profile;

public class CWebViewClient extends WebViewClient {

    private App
            application;

    public CWebViewClient(
            App application) {
        this.application = application;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          HttpAuthHandler handler, String host, String realm) {
       /* super.onReceivedHttpAuthRequest(
                view,
                handler,
                host,
                realm);*/
        Profile profile = this.application.profiles().get(this.application.id());
        handler
                .proceed(profile.settings.name(), profile.settings.password());
    }
}