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
import android.view.Gravity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.BuildConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.controls.CustomWebView;
import com.altertech.evahi.controls.CustomWebViewClient;
import com.altertech.evahi.core.BApp;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.core.config.ConfigHandler;
import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.dialog.CustomDialogs;
import com.altertech.evahi.dialog.obj.CustomAlertDialog;
import com.altertech.evahi.helpers.IntentHelper;
import com.altertech.evahi.helpers.SnackHelper;
import com.altertech.evahi.models.profiles.Profile;
import com.altertech.evahi.models.profiles.Profiles;
import com.altertech.evahi.models.s.SSettings;
import com.altertech.evahi.ui.base.ABase2;
import com.altertech.evahi.ui.holders.MenuHolder;
import com.altertech.evahi.ui.holders.WebHolder;
import com.altertech.evahi.utils.StringUtil;
import com.altertech.evahi.utils.Utils;

import java.util.concurrent.TimeUnit;

public class AMain extends ABase2<BApp> {

    private CustomWebView
            web;

    private WebHolder
            webH;

    private MenuHolder
            menu;

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.activity_main;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void created() {

        this.title()
                .h
                .click(R.id.a_main_settings, view -> AMain.this.startActivityForResult(new Intent(AMain.this, ASettings.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode()))
                .click(R.id.title_bar_controls_menu_button, view -> ((DrawerLayout) this.findViewById(R.id.a_main_drawer)).openDrawer(Gravity.START, true));

        this.webH = new WebHolder(findViewById(R.id.a_main_web_container));

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
                        AMain.this.initialization(AMain.this.web.getUrl());
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
                AMain.this.url(AMain.this.web, AMain.this.app.profiles().get(AMain.this.app.id()).settings.url() + url);
                ((DrawerLayout) AMain.this.findViewById(R.id.a_main_drawer)).closeDrawer(Gravity.START, true);
            }
        }).update(this.app.profiles().get(AMain.this.app.id()).config);

        this.web = findViewById(R.id.ui_f_web_view);

        this.web.getSettings().setSavePassword(false);
        this.web.getSettings().setSaveFormData(false);
        this.web.getSettings().setUseWideViewPort(true);
        this.web.getSettings().setBuiltInZoomControls(true);
        this.web.getSettings().setSupportZoom(true);
        this.web.getSettings().setJavaScriptEnabled(true);
        this.web.getSettings().setDisplayZoomControls(false);
        this.web.getSettings().setUserAgentString("evaHI" + " (" + BuildConfig.VERSION_NAME + ", " + this.web.getSettings().getUserAgentString() + ") ");
        this.web.setWebViewClient(new CustomWebViewClient(this.app) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AMain.this.url(view, url);
                return
                        true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                switch (error.getErrorCode()) {
                    case -6:
                        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, R.string.app_exception_connection_refused, SnackHelper.Duration.SHORT);
                        break;
                    case -11:
                        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, R.string.app_exception_handshake, SnackHelper.Duration.SHORT);
                        break;
                    case -2:
                        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, R.string.app_a_settings_exception_invalid_address, SnackHelper.Duration.SHORT);
                        break;
                    default:
                        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, getResources().getString(R.string.app_exception_error) + ", code = " + error.getErrorCode(), SnackHelper.Duration.SHORT);
                        break;
                }
                AMain.this.setStateToWebControls(false);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                SnackHelper.snack(AMain.this.findViewById(R.id.ui_f_web_view), SnackHelper.State.ERROR, R.string.app_exception_ssl_error, SnackHelper.Duration.SHORT);
            }
        });

        this.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                AMain.this.webH.setProgress(progress);
            }
        });

        if (this.app.first()) {
            this.app.first(false);
            this
                    .setStateToWebControls(false).startActivityForResult(new Intent(AMain.this, ASettings.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
        } else {
            this.initialization();
        }
    }

    @Override
    public void resume(boolean state) {
        if (!state && this.getTPause() > 0 && this.web != null && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.getTPause()) >= AppConfig.RELOAD_AFTER_SLEEP) {
            this.web.reload();
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
        }
    }

    private AMain title() {
        this.h.text(R.id.title_bar_controls_text, StringUtil.isNotEmpty(AppConfig.NAME) ? AppConfig.NAME + " (" + this.app.profiles().get(this.app.id()).name + ")" : getResources().getString(R.string.app_name) + " (" + this.app.profiles().get(this.app.id()).name + ")");
        return
                this;
    }

    private void initialization() {
        this
                .initialization(null);
    }

    private void initialization(String url) {

        new ConfigHandler(this.app.profiles().get(this.app.id()).settings.url(), this.app.profiles().get(this.app.id()).settings.name(), this.app.profiles().get(this.app.id()).settings.password(), new ConfigHandler.CallBack() {
            private CustomAlertDialog progress;

            @Override
            public void start() {
                this
                        .progress = CustomDialogs.showExecutionDialog(AMain.this, R.string.app_remote_wait);

                AMain.this.setStateToWebControls(false);
            }

            @Override
            public void end(Config config) {
                this
                        .progress.mDismiss();

                Profiles profiles = AMain.this.app.profiles();
                profiles
                        .get(AMain.this.app.id()).config(config);
                AMain.this.app.profiles(profiles);

                AMain.this.menu.update(config).and(AMain.this).setStateToWebControls(true).url(web, url != null && !url.equals("/") ? url : AMain.this.app.profiles().get(AMain.this.app.id()).prepareUrl(Utils.Views.isLandscape(AMain.this)));
            }

            @Override
            public void error(CustomException e) {
                this
                        .progress.mDismiss();

                Profiles profiles = AMain.this.app.profiles();
                profiles
                        .get(AMain.this.app.id()).config(null);
                AMain.this.app.profiles(profiles);

                AMain.this.menu.update(null).and(AMain.this).setStateToWebControls(false).message(e.getCode().getMessage());
            }
        }).load();
    }

    private AMain setStateToWebControls(boolean state) {
        this.webH.setVisibility(state);

        this
                .h
                .visible(R.id.a_main_settings, (!state ? (AppConfig.CONFIG != null && !AppConfig.CONFIG.isEnabled()) || AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE : View.GONE));

        return this;
    }

    private void message(@StringRes int id) {
        SnackHelper.snack(AMain.this, SnackHelper.State.ERROR, id, SnackHelper.Duration.SHORT);
    }

    private void url(WebView view, String url) {

        if (this.app.profiles().get(this.app.id()).settings.address() != null && !this.app.profiles().get(this.app.id()).settings.address().isEmpty()) {
            view
                    .loadUrl(url);
        } else {
            SnackHelper.snack(AMain.this.findViewById(R.id.ui_f_web_view), SnackHelper.State.ERROR, R.string.app_exception_no_settings, SnackHelper.Duration.SHORT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this
                .web.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this
                .web.saveState(savedInstanceState);
    }

    @Override
    public void configuration(Configuration config) {

        super.configuration(
                config
        );

        Config c = this.app.profiles().get(this.app.id()).config;
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (c != null) {
                this.url(this.web, this.app.profiles().get(this.app.id()).prepareUrl(false));
            }
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (c != null && c.hasLandscape()) {
                this.url(this.web, this.app.profiles().get(this.app.id()).prepareUrl(true));
            }
        }

    }
}
