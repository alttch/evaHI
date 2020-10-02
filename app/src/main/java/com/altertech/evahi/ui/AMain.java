package com.altertech.evahi.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.ui.controls.clients.CWebViewClient;
import com.altertech.evahi.core.BApp;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.core.config.ConfigHandler;
import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.ui.dialog.Dialogs;
import com.altertech.evahi.ui.dialog.obj.Dialog;
import com.altertech.evahi.helpers.IntentHelper;
import com.altertech.evahi.helpers.SnackHelper;
import com.altertech.evahi.core.models.profiles.Profile;
import com.altertech.evahi.core.models.profiles.Profiles;
import com.altertech.evahi.core.models.s.SSettings;
import com.altertech.evahi.services.SService;
import com.altertech.evahi.ui.base.ABase2;
import com.altertech.evahi.ui.holders.MenuHolder;
import com.altertech.evahi.ui.holders.WebHolder;
import com.altertech.evahi.utils.StringUtil;
import com.altertech.evahi.utils.Utils;

import java.util.concurrent.TimeUnit;

public class AMain extends ABase2<BApp> {

    public static final int REQUEST_GPS_PERMISSION = 2067;

    private WebHolder
            web;

    private MenuHolder
            menu;

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.activity_main;
    }

    @Override
    public void init() {
        this.web = new WebHolder(findViewById(R.id.a_main_web_container));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void created() {

        this.title()
                .h
                .click(R.id.a_main_settings, view -> AMain.this.startActivityForResult(new Intent(AMain.this, ASettings.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode()))
                .click(R.id.title_bar_controls_menu_button, view -> ((DrawerLayout) this.findViewById(R.id.a_main_drawer)).openDrawer(Gravity.START, true));

        this
                .initMenu();

        this.web.getWeb().allowCookies(true).init().client(new CWebViewClient(this.app) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AMain.this.url(view, url);
                return
                        true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                AMain.this.state(
                        true
                );
                String cookies = CookieManager.getInstance().getCookie(url);
                Log.e("1715", cookies);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                switch (error.getErrorCode()) {
                    case -6:
                        message(R.string.app_exception_connection_refused)
                                .state(false);
                        break;
                    case -11:
                        message(R.string.app_exception_handshake)
                                .state(false);
                        break;
                    case -2:
                        message(R.string.app_a_settings_exception_invalid_address)
                                .state(false);
                        break;
                    default:
                        message(getResources().getString(R.string.app_exception_error) + ", code = " + error.getErrorCode())
                                .state(false);
                        break;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                AMain.this.message(
                        R.string.app_exception_ssl_error
                );
            }
        }).clientChrome(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                AMain.this.web.setProgress(progress);
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback l) {
                l
                        .invoke(origin, true, false);
            }
        });

        if (this.app.first()) {
            this.app.first(false);
            this
                    .state(false).startActivityForResult(new Intent(AMain.this, ASettings.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
        } else {
            this.initialization();
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.startService(new Intent(
                    AMain.this,
                    SService.class
            ));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_GPS_PERMISSION);
        }

    }

    @Override
    public void resume(boolean state) {
        if (
                !state
                        && this.getTPause() > 0
                        && this.web != null
                        && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.getTPause()) >= AppConfig.RELOAD_AFTER_SLEEP) {
            this.web.getWeb().reload();
        }
    }

    @Override
    public void result(int request, int result, Intent data) {

        this
                .setState(true);

        if (request == IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode() && result == Activity.RESULT_OK) {
            this
                    .initialization();
        } else if (request == IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode() && result == RESULT_OK && data != null && data.hasExtra("settings")) {

            boolean
                    changed = false;

            SSettings settings = this.data(data, "settings", SSettings.class);

            Long id = 10 + (long) (Math.random() * (999999999 - 10));

            Profile profile = new Profile(id, this.getResources().getString(R.string.app_profile_unknown) + "_" + id);

            if (AppConfig.CONFIG == null || !AppConfig.CONFIG.isEnabled()) {

                profile.settings
                        .https(settings.https())
                        .address(settings.address())
                        .port(settings.port());

                changed = true;
            }
            if (AppConfig.AUTHENTICATION) {

                profile.settings
                        .name(settings.name())
                        .password(settings.password());

                changed = true;
            }

            if (changed) {
                Profiles profiles = this.app.profiles();
                profiles
                        .add(profile);

                this.app.id(profile.id).profiles(profiles);
            }

            this
                    .title().initialization();
        } else if (request == IntentHelper.REQUEST_CODES.PROFILES_ACTIVITY.getCode() && result == Activity.RESULT_OK) {
            this
                    .title().initialization();
        }
    }

    @Override
    public void permissions(int request, @NonNull String[] permissions, @NonNull int[] results) {
        if (request == ASettings.REQUEST_CAMERA_PERMISSION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            AMain.this.startActivityForResult(
                    new Intent(AMain.this, ScannedBarcodeActivity.class),
                    IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
        } else if (request == REQUEST_GPS_PERMISSION && results.length == 2 && results[0] == PackageManager.PERMISSION_GRANTED && results[1] == PackageManager.PERMISSION_GRANTED) {
            this.startService(new Intent(
                    AMain.this,
                    SService.class
            ));
        }
    }

    private AMain title() {
        this.h.text(R.id.title_bar_controls_text, StringUtil.isNotEmpty(AppConfig.NAME) ? AppConfig.NAME + " (" + this.app.profiles().get(this.app.id()).name + ")" : getResources().getString(R.string.app_name) + " (" + this.app.profiles().get(this.app.id()).name + ")");
        return
                this;
    }

    private AMain initMenu() {
        this.menu = new MenuHolder(this, findViewById(R.id.a_main_menu), new MenuHolder.CallBack() {
            @Override
            public void click(MenuHolder.Type type) {
                switch (type) {
                    case PROFILES:
                        AMain.this.startActivityForResult(new Intent(AMain.this, AProfiles.class), IntentHelper.REQUEST_CODES.PROFILES_ACTIVITY.getCode());
                        break;
                    case QR:
                        if (ActivityCompat.checkSelfPermission(AMain.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            AMain.this.startActivityForResult(new Intent(AMain.this, ScannedBarcodeActivity.class), IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
                        } else {
                            ActivityCompat.requestPermissions(AMain.this, new String[]{Manifest.permission.CAMERA}, ASettings.REQUEST_CAMERA_PERMISSION);
                        }
                        break;
                    case SETTINGS:
                        AMain.this.startActivityForResult(new Intent(AMain.this, ASettings.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
                        break;
                    case RELOAD:
                        AMain.this.initialization(AMain.this.web.getWeb().getUrl());
                        break;
                    case ABOUT:
                        AMain.this.startActivityForResult(new Intent(AMain.this, AAbout.class), IntentHelper.REQUEST_CODES.ABOUT_ACTIVITY.getCode());
                        break;
                    case EXIT:
                        AMain.this.finish();
                        break;
                }
                ((DrawerLayout) AMain.this.findViewById(R.id.a_main_drawer)).closeDrawer(Gravity.START, true);
            }

            @Override
            public void click(MenuHolder.Type type, String url) {
                AMain.this.url(
                        AMain.this.web.getWeb(),
                        AMain.this.app.profiles().get(AMain.this.app.id()).settings.url() + url);
                ((DrawerLayout) AMain.this.findViewById(R.id.a_main_drawer)).closeDrawer(Gravity.START, true);
            }
        }).update(this.app.profiles().get(AMain.this.app.id()).config);
        return
                this;
    }

    private void initialization() {
        this
                .initialization(null);
    }

    private void initialization(String url) {

        new ConfigHandler(this.app.profiles().get(this.app.id()).settings.url(), this.app.profiles().get(this.app.id()).settings.name(), this.app.profiles().get(this.app.id()).settings.password(), new ConfigHandler.CallBack() {
            private Dialog progress;

            @Override
            public void start() {
                this
                        .progress = Dialogs.execution(AMain.this, R.string.app_remote_wait);

                AMain.this.state(false);
            }

            @Override
            public void end(Config config) {
                this
                        .progress.dismiss();

                Profiles profiles = AMain.this.app.profiles();
                profiles
                        .get(AMain.this.app.id()).config(config);
                AMain.this.app.profiles(profiles);

                AMain.this.menu.update(config).and(AMain.this).state(true).url(web.getWeb(), url != null && !url.equals("/") ? url : AMain.this.app.profiles().get(AMain.this.app.id()).prepareUrl(Utils.Views.isLandscape(AMain.this)));
            }

            @Override
            public void error(CustomException e) {
                this
                        .progress.dismiss();

                Profiles profiles = AMain.this.app.profiles();
                profiles
                        .get(AMain.this.app.id()).config(null);
                AMain.this.app.profiles(profiles);

                AMain.this.menu.update(null).and(AMain.this).state(false).message(e.getCode().getMessage());
            }
        }).load();
    }

    private AMain state(boolean state) {
        this.web.visibility(
                state
        );
        this
                .h
                .visible(R.id.a_main_settings, (!state ? (AppConfig.CONFIG != null && !AppConfig.CONFIG.isEnabled()) || AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE : View.GONE));

        return this;
    }

    private AMain message(@StringRes int id) {
        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, id, SnackHelper.Duration.SHORT);
        return this;
    }

    private AMain message(String text) {
        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, text, SnackHelper.Duration.SHORT);
        return this;
    }

    private void url(WebView view, String url) {

        if (this.app.profiles().get(this.app.id()).settings.address() != null && !this.app.profiles().get(this.app.id()).settings.address().isEmpty()) {
            view
                    .loadUrl(url);
        } else {
            this.message(R.string.app_exception_no_settings);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this
                .web.getWeb().saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this
                .web.getWeb().saveState(savedInstanceState);
    }

    @Override
    public void configuration(Configuration config) {

        super.configuration(
                config
        );

        Config c = this.app.profiles().get(this.app.id()).config;
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (c != null) {
                this.url(this.web.getWeb(), this.app.profiles().get(this.app.id()).prepareUrl(false));
            }
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (c != null && c.hasLandscape()) {
                this.url(this.web.getWeb(), this.app.profiles().get(this.app.id()).prepareUrl(true));
            }
        }

    }
}
