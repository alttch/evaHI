package com.altertech.evahi.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.config.Config;
import com.altertech.evahi.core.config.ConfigHandler;
import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.core.models.profiles.Profile;
import com.altertech.evahi.core.models.profiles.Profiles;
import com.altertech.evahi.core.models.s.SSettings;
import com.altertech.evahi.ui.base.ABase2;
import com.altertech.evahi.ui.controls.clients.WebClient;
import com.altertech.evahi.ui.dialog.Dialogs;
import com.altertech.evahi.ui.dialog.obj.Dialog;
import com.altertech.evahi.ui.holders.HWeb;
import com.altertech.evahi.ui.holders.MenuHolder;
import com.altertech.evahi.ui.holders.view.VHBase;
import com.altertech.evahi.utils.Utils;

import java.util.concurrent.TimeUnit;

public class AMain extends ABase2<App> {

    private HWeb
            web;

    private MenuHolder
            menu;

    private DrawerLayout lMenu;

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.a_main;
    }

    @Override
    public void init() {

        this.web = new HWeb(
                findViewById(R.id.a_main_web_container)
        );

        this.lMenu = this.findViewById(
                R.id.a_main_drawer
        );
        this.lMenu.setDrawerElevation(
                0
        );
    }

    private WebResourceError
            _current_error;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void created() {

        this.title()
                .h.click(R.id.settings, view -> AMain.this.sfr(new Intent(AMain.this, ASettings.class), App.RQ_A_SETTINGS)).click(R.id.menu, view -> this.lMenu.openDrawer(GravityCompat.START, true));

        this.menu().web.getWeb().allowCookies(true).init().client(new WebClient(this.app) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return
                        false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                AMain.this.state(
                        true
                );
                //AMain.this.state(
                //        AMain.this._current_error == null
                //);
                //AMain.this._current_error = null;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                AMain.this._current_error = error;
                switch (
                        error.getErrorCode()
                ) {
                    case -6:
                        AMain.this.state(false).h.snack(VHBase.Messages.Snack.Type.E, R.string.app_exception_connection_refused, VHBase.Messages.Snack.Duration.SHORT);
                        break;
                    case -10:
                    case -11:
                        AMain.this.state(false).h.snack(VHBase.Messages.Snack.Type.E, R.string.app_exception_handshake, VHBase.Messages.Snack.Duration.SHORT);
                        break;
                    case -2:
                        AMain.this.state(false).h.snack(VHBase.Messages.Snack.Type.E, R.string.app_exception_invalid_url, VHBase.Messages.Snack.Duration.SHORT);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                AMain.this.h.snack(VHBase.Messages.Snack.Type.E, R.string.app_exception_ssl_error, VHBase.Messages.Snack.Duration.SHORT);
            }
        }).clientChrome(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                AMain.this.web.progress(progress);
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback l) {
                l
                        .invoke(origin, true, false);
            }
        });

        if (this.app.first()) {
            this.app.first(false);
            this
                    .state(false).sfr(new Intent(AMain.this, ASettings.class), App.RQ_A_SETTINGS);
        } else {
            this.loadConfig(false);
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

        if (request == App.RQ_A_SETTINGS && result == Activity.RESULT_OK) {
            this
                    .loadConfig(false);
        } else if (request == App.RQ_A_BARCODE && result == RESULT_OK && data != null && data.hasExtra("settings")) {

            boolean
                    changed = false;

            SSettings settings = this.data(data, "settings", SSettings.class);

            Long id = 10 + (long) (Math.random() * (999999999 - 10));

            Profile profile = new Profile(id, this.getResources().getString(R.string.app_profile_unknown) + "_" + id);

            if (!AppConfig.CONFIG.isEnabled()) {

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
                    .title().loadConfig(
                            false
                    );
        } else if (request == App.RQ_A_PROFILES) {
            if (
                    result == Activity.RESULT_OK) {
                this.title().loadConfig(
                        true
                );
            } else {
                this.title(

                );
            }
        }
    }

    @Override
    public void permissions(int request, @NonNull String[] permissions, @NonNull int[] results) {
        if (request == ASettings.REQUEST_CAMERA_PERMISSION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            this.sfr(
                    new Intent(AMain.this, ABarcode.class),
                    App.RQ_A_BARCODE);
        }
    }

    private AMain title() {
        this.h.text(R.id.title_bar_controls_text, Utils.Strings.notEmpty(AppConfig.NAME) ? AppConfig.NAME + " (" + this.app.profiles().get(this.app.id()).name + ")" : getResources().getString(R.string.app_name) + " (" + this.app.profiles().get(this.app.id()).name + ")");
        return
                this;
    }

    private AMain menu() {

        this.menu = new MenuHolder(this, findViewById(R.id.a_main_menu), new MenuHolder.CallBack() {
            @Override
            public void click(MenuHolder.Type type) {

                switch (type) {
                    case PROFILES:
                        AMain.this.sfr(new Intent(AMain.this, AProfiles.class), App.RQ_A_PROFILES);
                        break;
                    case QR:
                        if (AMain.this.has(Manifest.permission.CAMERA)) {
                            AMain.this.sfr(
                                    new Intent(AMain.this, ABarcode.class), App.RQ_A_BARCODE
                            );
                        } else {
                            AMain.this.request(new String[]{Manifest.permission.CAMERA}, ASettings.REQUEST_CAMERA_PERMISSION);
                        }
                        break;
                    case SETTINGS:
                        AMain.this.sfr(new Intent(AMain.this, ASettings.class), App.RQ_A_SETTINGS);
                        break;
                    case RELOAD:
                        AMain.this.loadConfig(AMain.this.web.getWeb().getUrl(), false);
                        break;
                    case ABOUT:
                        AMain.this.sfr(new Intent(AMain.this, AAbout.class), App.RQ_A_ABOUT);
                        break;
                    case EXIT:
                        AMain.this.finish();
                        break;
                }
                AMain.this.lMenu.closeDrawer(GravityCompat.START, true);
            }

            @Override
            public void click(MenuHolder.Type type, String url) {
                AMain.this.url(AMain.this.app.profiles().get(AMain.this.app.id()).settings.url() + url, false).lMenu.closeDrawer(GravityCompat.START, true);
            }
        }).update(this.app.profiles().get(AMain.this.app.id()).config);
        return
                this;
    }

    private void loadConfig(boolean clearCookies) {
        this
                .loadConfig(null, clearCookies);
    }

    private void loadConfig(String url, boolean clearCookies) {

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

                AMain.this.menu.update(config).and(AMain.this).state(true).url(url != null && !url.equals("/") ? url : AMain.this.app.profiles().get(AMain.this.app.id()).prepareUrl(Utils.Views.isLandscape(AMain.this)), clearCookies);
            }

            @Override
            public void error(CustomException e) {
                this
                        .progress.dismiss();

                Profiles profiles = AMain.this.app.profiles();
                profiles
                        .get(AMain.this.app.id()).config(null);
                AMain.this.app.profiles(profiles);

                AMain.this.menu.update(null).and(AMain.this).state(false).h.snack(VHBase.Messages.Snack.Type.E, e.getError().getMessage(), VHBase.Messages.Snack.Duration.SHORT);
            }
        }).load();
    }

    private AMain state(boolean state) {
        this.web.visibility(state);
        this
                .h
                .visible(R.id.settings, !state ? !AppConfig.CONFIG.isEnabled() || AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE : View.GONE);

        return this;
    }

    private AMain url(String url, boolean clearCookies) {

        if (this.app.profiles().get(this.app.id()).settings.address() != null && !this.app.profiles().get(this.app.id()).settings.address().isEmpty()) {
            (clearCookies
                    ? this.web.getWeb()/*.clearCookies()*/
                    : this.web.getWeb()
            )
                    .loadUrl(url/*, Utils.Cookies.headers(this.app.profiles().get(this.app.id()).settings.name(),   this.app.profiles().get(this.app.id()).settings.password())*/);
        } else {
            this.h.snack(VHBase.Messages.Snack.Type.E, R.string.app_exception_no_settings, VHBase.Messages.Snack.Duration.SHORT);
        }
        return this;
    }

    @Override
    public void configuration(Configuration config) {

        super.configuration(
                config
        );

        Config c = this.app.profiles().get(this.app.id()).config;
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (c != null) {
                this.url(this.app.profiles().get(this.app.id()).prepareUrl(
                        false
                ), false);
            }
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (c != null && c.hasLandscape()) {
                this.url(this.app.profiles().get(this.app.id()).prepareUrl(
                        true
                ), false);
            }
        }

    }
}
