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
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

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
import com.altertech.evahi.helpers.SnackbarHelper;
import com.altertech.evahi.models.profiles.Profile;
import com.altertech.evahi.models.profiles.Profiles;
import com.altertech.evahi.models.s.SSettings;
import com.altertech.evahi.ui.holders.MenuHolder;
import com.altertech.evahi.ui.holders.WebHolder;
import com.altertech.evahi.utils.StringUtil;
import com.altertech.evahi.utils.Utils;

import java.util.concurrent.TimeUnit;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends ABase2<BApp> {

    private CustomWebView
            web;

    private WebHolder
            webH;

    private Button
            a_main_settings;

    private MenuHolder
            menu;

    private MainActivity title() {
        ((TextView) this.findViewById(R.id.title_bar_controls_text)).setText(StringUtil.isNotEmpty(AppConfig.NAME) ? AppConfig.NAME + " (" + this.app.profiles().get(this.app.id()).name + ")" : getResources().getString(R.string.app_name) + " (" + this.app.profiles().get(this.app.id()).name + ")");
        return this;
    }

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.activity_main;
    }

    @Override
    public void created() {
        this.title();

        this.webH = new WebHolder(findViewById(R.id.a_main_web_container));

        this.menu = new MenuHolder(this, findViewById(R.id.a_main_menu), new MenuHolder.CallBack() {
            @Override
            public void click(MenuHolder.Type type) {
                switch (type) {
                    case PROFILES:
                        MainActivity.this.startActivityForResult(new Intent(MainActivity.this, AProfiles.class), IntentHelper.REQUEST_CODES.PROFILES_ACTIVITY.getCode());
                        break;
                    case QR:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            MainActivity.this.startActivityForResult(new Intent(MainActivity.this, ScannedBarcodeActivity.class), IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, SettingsActivity.REQUEST_CAMERA_PERMISSION);
                        }
                        break;
                    case SETTINGS:
                        MainActivity.this.startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
                        break;
                    case RELOAD:
                        MainActivity.this.initialization(MainActivity.this.web.getUrl());
                        break;
                    case ABOUT:
                        MainActivity.this.startActivityForResult(new Intent(MainActivity.this, AboutActivity.class), IntentHelper.REQUEST_CODES.ABOUT_ACTIVITY.getCode());
                        break;
                    case EXIT:
                        MainActivity.this.finish();
                        break;
                }
                ((DrawerLayout) MainActivity.this.findViewById(R.id.a_main_drawer)).closeDrawer(Gravity.START, true);
            }

            @Override
            public void click(MenuHolder.Type type, String url) {
                MainActivity.this.loadUrl(MainActivity.this.web, MainActivity.this.app.profiles().get(MainActivity.this.app.id()).settings.url() + url);
                ((DrawerLayout) MainActivity.this.findViewById(R.id.a_main_drawer)).closeDrawer(Gravity.START, true);
            }
        }).update(this.app.profiles().get(MainActivity.this.app.id()).config);

        this.a_main_settings = findViewById(R.id.a_main_settings);
        this.a_main_settings.setOnClickListener(v -> MainActivity.this.startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode()));

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
                MainActivity.this.loadUrl(view, url);
                return
                        true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                switch (error.getErrorCode()) {
                    case -6:
                        SnackbarHelper.snack(MainActivity.this, SnackbarHelper.State.ERROR, R.string.app_exception_connection_refused, SnackbarHelper.Duration.SHORT);
                        break;
                    case -11:
                        SnackbarHelper.snack(MainActivity.this, SnackbarHelper.State.ERROR, R.string.app_exception_handshake, SnackbarHelper.Duration.SHORT);
                        break;
                    case -2:
                        SnackbarHelper.snack(MainActivity.this, SnackbarHelper.State.ERROR, R.string.app_a_settings_exception_invalid_address, SnackbarHelper.Duration.SHORT);
                        break;
                    default:
                        SnackbarHelper.snack(MainActivity.this, SnackbarHelper.State.ERROR, getResources().getString(R.string.app_exception_error) + ", code = " + error.getErrorCode(), SnackbarHelper.Duration.SHORT);
                        break;
                }

                MainActivity.this.setStateToWebControls(false);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                SnackbarHelper.snack(MainActivity.this.findViewById(R.id.ui_f_web_view), SnackbarHelper.State.ERROR, R.string.app_exception_ssl_error, SnackbarHelper.Duration.SHORT);
            }
        });

        this.web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                MainActivity.this.webH.setProgress(progress);
            }
        });

        this.findViewById(R.id.title_bar_controls_menu_button).setOnClickListener(v -> ((DrawerLayout) this.findViewById(R.id.a_main_drawer)).openDrawer(Gravity.START, true));

        if (this.app.first()) {
            this.app.first(false);
            this
                    .setStateToWebControls(false).startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
        } else {
            this.initialization();
        }

        Log.e("ttt", app.profiles().toString());
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
        if (request == SettingsActivity.REQUEST_CAMERA_PERMISSION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            MainActivity.this.startActivityForResult(
                    new Intent(MainActivity.this, ScannedBarcodeActivity.class),
                    IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
        }
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
                        .progress = CustomDialogs.showExecutionDialog(MainActivity.this, R.string.app_remote_wait);

                MainActivity.this.setStateToWebControls(false);
            }

            @Override
            public void end(Config config) {
                this
                        .progress.mDismiss();

                Profiles profiles = MainActivity.this.app.profiles();
                profiles
                        .get(MainActivity.this.app.id()).config(config);
                MainActivity.this.app.profiles(profiles);

                MainActivity.this.menu.update(config).and(MainActivity.this).setStateToWebControls(true).loadUrl(web, url != null && !url.equals("/") ? url : MainActivity.this.app.profiles().get(MainActivity.this.app.id()).prepareUrl(Utils.isLandscape(MainActivity.this)));
            }

            @Override
            public void error(CustomException e) {
                this
                        .progress.mDismiss();

                Profiles profiles = MainActivity.this.app.profiles();
                profiles
                        .get(MainActivity.this.app.id()).config(null);
                MainActivity.this.app.profiles(profiles);

                MainActivity.this.menu.update(null).and(MainActivity.this).setStateToWebControls(false).message(e.getCode().getMessage());
            }
        }).load();
    }

    private MainActivity setStateToWebControls(boolean state) {
        this.webH.setVisibility(state);

        this.a_main_settings.setVisibility(!state ? (AppConfig.CONFIG != null && !AppConfig.CONFIG.isEnabled()) || AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE : View.GONE);

        return this;
    }

    private void message(@StringRes int id) {
        SnackbarHelper.snack(MainActivity.this, SnackbarHelper.State.ERROR, id, SnackbarHelper.Duration.SHORT);
    }

    private void loadUrl(WebView view, String url) {

        if (this.app.profiles().get(this.app.id()).settings.address() != null && !this.app.profiles().get(this.app.id()).settings.address().isEmpty()) {
            view
                    .loadUrl(url);
        } else {
            SnackbarHelper.snack(MainActivity.this.findViewById(R.id.ui_f_web_view), SnackbarHelper.State.ERROR, R.string.app_exception_no_settings, SnackbarHelper.Duration.SHORT);
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
                this.loadUrl(this.web, this.app.profiles().get(this.app.id()).prepareUrl(false));
            }
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (c != null && c.hasLandscape()) {
                this.loadUrl(this.web, this.app.profiles().get(this.app.id()).prepareUrl(true));
            }
        }

    }
}
