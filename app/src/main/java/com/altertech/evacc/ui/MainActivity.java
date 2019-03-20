package com.altertech.evacc.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.altertech.evacc.BuildConfig;
import com.altertech.evacc.R;
import com.altertech.evacc.controls.CustomWebView;
import com.altertech.evacc.core.BaseApplication;
import com.altertech.evacc.core.config.Config;
import com.altertech.evacc.core.config.ConfigHandler;
import com.altertech.evacc.core.exception.CustomException;
import com.altertech.evacc.dialog.CustomDialogs;
import com.altertech.evacc.dialog.obj.CustomAlertDialog;
import com.altertech.evacc.helpers.IntentHelper;
import com.altertech.evacc.helpers.SnackbarHelper;
import com.altertech.evacc.ui.holders.MenuHolder;
import com.altertech.evacc.ui.holders.WebHolder;
import com.altertech.evacc.utils.StringUtil;
import com.altertech.evacc.utils.Utils;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends BaseActivity {

    private CustomWebView web;

    private Button a_main_settings;

    private BaseApplication application;

    private DrawerLayout a_main_drawer;

    private MenuHolder menu;

    private WebHolder webH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.application = BaseApplication.get(this);

        this.a_main_drawer = this.findViewById(R.id.a_main_drawer);

        this.webH = new WebHolder(findViewById(R.id.a_main_web_container));

        this.menu = new MenuHolder(this, findViewById(R.id.a_main_menu), new MenuHolder.CallBack() {
            @Override
            public void click(MenuHolder.Type type) {
                switch (type) {
                    case SETTINGS:
                        MainActivity.this.startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode());
                        break;
                    case RELOAD:
                        MainActivity.this.init(MainActivity.this.web.getUrl(), true);
                        break;
                    case ABOUT:
                        MainActivity.this.startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case EXIT:
                        MainActivity.this.finish();
                        break;
                }
                MainActivity.this.a_main_drawer.closeDrawer(Gravity.START, true);
            }

            @Override
            public void click(MenuHolder.Type type, String url) {
                MainActivity.this.loadUrl(MainActivity.this.web, MainActivity.this.application.getBaseUrl() + "/" + url);
                MainActivity.this.a_main_drawer.closeDrawer(Gravity.START, true);
            }
        }).update(this.application.getServerConfig());

        this.a_main_settings = findViewById(R.id.a_main_settings);
        this.a_main_settings.setOnClickListener(v -> MainActivity.this.startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode()));

        this.web = findViewById(R.id.ui_f_web_view);
        if (Build.VERSION.SDK_INT <= 18) {
            this.web.getSettings().setSavePassword(false);
        }
        this.web.getSettings().setSaveFormData(false);
        this.web.getSettings().setUseWideViewPort(true);
        this.web.getSettings().setBuiltInZoomControls(true);
        this.web.getSettings().setSupportZoom(true);
        this.web.getSettings().setJavaScriptEnabled(true);
        this.web.getSettings().setDisplayZoomControls(false);
        this.web.getSettings().setUserAgentString("EvaHI (" + BuildConfig.VERSION_NAME + ", " + this.web.getSettings().getUserAgentString() + ") ");
        this.web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadUrl(view, url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
               /* if (CustomWebView.ABOUT_BLANK.equals(url) && view.getTag() != null) {
                    String tag = view.getTag().toString();
                    view.loadUrl(tag);
                } else {
                    view.setTag(url);
                }*/
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (error.getErrorCode() == -6) {
                        SnackbarHelper.snack(MainActivity.this.findViewById(R.id.ui_f_web_progress_container), SnackbarHelper.State.ERROR, R.string.app_exception_no_connection_to_the_server, SnackbarHelper.Duration.SHORT);
                    } else {
                        SnackbarHelper.snack(MainActivity.this.findViewById(R.id.ui_f_web_view), SnackbarHelper.State.ERROR, R.string.app_exception_error, SnackbarHelper.Duration.SHORT);
                    }
                } else {
                    SnackbarHelper.snack(MainActivity.this.findViewById(R.id.ui_f_web_view), SnackbarHelper.State.ERROR, R.string.app_exception_error, SnackbarHelper.Duration.SHORT);
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


        this.findViewById(R.id.title_bar_controls_menu_button).setOnClickListener(v -> {
            this.a_main_drawer.openDrawer(Gravity.START, true);
        });

        this.init(true);
    }

    private MainActivity setStateToWebControls(boolean state) {
        this.webH.setVisibility(state);

        this.a_main_settings.setVisibility(!state ? View.VISIBLE : View.GONE);

        return this;
    }

   /* private MainActivity setStateToSettingControl(boolean state) {
        this.a_main_settings.setVisibility(state ? View.VISIBLE : View.GONE);
        return this;
    }*/

    private void init(boolean menu) {
        this.init(null, menu);
    }

    private void init(String url, boolean menu) {
        if (StringUtil.isNotEmpty(this.application.getServerAddress())) {
            if (menu) {
                new ConfigHandler(MainActivity.this.application.getBaseUrl(), this.application.getServerConfig(), new ConfigHandler.CallBack() {
                    private CustomAlertDialog progress;

                    @Override
                    public void start() {
                        this.progress = CustomDialogs.showExecutionDialog(MainActivity.this, R.string.app_dialog_title_execution, R.string.app_remote_wait);

                        MainActivity.this.setStateToWebControls(false);
                    }

                    @Override
                    public void end(boolean updated, Config config) {
                        this.progress.mDismiss();
                        if (updated) {
                            try {
                                MainActivity.this.application.setServerConfig(config);
                                MainActivity.this.menu.update(config);
                            } catch (CustomException e) {
                                SnackbarHelper.snack(MainActivity.this.findViewById(R.id.a_main_body), SnackbarHelper.State.ERROR, e.getCode().getMessage(), SnackbarHelper.Duration.SHORT);
                            }
                        }
                        MainActivity.this.setStateToWebControls(true).loadUrl(web, url != null ? url : MainActivity.this.application.prepareUrl(Utils.isLandscape(MainActivity.this)));
                    }

                    @Override
                    public void error(CustomException e) {
                        this.progress.mDismiss();

                        MainActivity.this.setStateToWebControls(false);

                        SnackbarHelper.snack(MainActivity.this.findViewById(R.id.a_main_body), SnackbarHelper.State.ERROR, e.getCode().getMessage(), SnackbarHelper.Duration.SHORT);
                    }
                }).load();
            } else {
                this.setStateToWebControls(true).loadUrl(this.web, url != null ? url : MainActivity.this.application.prepareUrl(Utils.isLandscape(MainActivity.this)));
            }
        } else {
            this.setStateToWebControls(false);

            SnackbarHelper.snack(MainActivity.this.findViewById(R.id.a_main_body), SnackbarHelper.State.ERROR, R.string.app_exception_no_settings, SnackbarHelper.Duration.SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentHelper.REQUEST_CODES.SETTINGS_ACTIVITY.getCode() && resultCode == Activity.RESULT_OK) {
            this.init(true);
        }
    }

    private void loadUrl(WebView view, String url) {
        if (StringUtil.isNotEmpty(this.application.getServerAddress())) {
            Map<String, String> headers = new HashMap<>();
            headers.put("EvaHI", BuildConfig.VERSION_NAME);
            if (this.application.isEmptyUser()) {
                view.loadUrl(url, headers);
            } else {
                headers.putAll(Utils.addBasicAuth(this.application.getUserName(), this.application.getUserPassword()));
                view.loadUrl(url, headers);
            }
        } else {
            SnackbarHelper.snack(MainActivity.this.findViewById(R.id.ui_f_web_view), SnackbarHelper.State.ERROR, R.string.app_exception_no_settings, SnackbarHelper.Duration.SHORT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.web.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.web.saveState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.loadUrl(this.web, this.application.prepareUrl(false));
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (this.web.getUrl() == null) {
                this.loadUrl(this.web, this.application.prepareUrl(true));
            } else if (this.web.getUrl() != null && !this.web.getUrl().contains("camview")) {
                this.loadUrl(this.web, this.application.prepareUrl(true));
            }
        }
        if (this.menu != null) {
            this.menu.setOrientation(config.orientation);
        }

        super.onConfigurationChanged(config);
    }

}
