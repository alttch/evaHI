package com.altertech.evahi.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.models.profiles.Profile;
import com.altertech.evahi.core.models.profiles.Profiles;
import com.altertech.evahi.core.models.s.SSettings;
import com.altertech.evahi.ui.base.ABase;
import com.altertech.evahi.ui.holders.view.VHBase;

public class ASettings extends ABase<App> {

    public static final int REQUEST_CAMERA_PERMISSION = 201;

    private Profile profile;

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.a_settings;
    }

    @Override
    public void init() {
        this.profile = this.app.profiles().get(this.app.id());
    }

    @Override
    public void created() {

        this.h
                .text(R.id.title_bar_controls_text,
                        this.getResources().getString(R.string.app_name_setting) + " (" + this.app.profiles().get(this.app.id()).name + ")")
                .visible(R.id.a_settings_s_container, AppConfig.CONFIG.isEnabled() ? View.GONE : View.VISIBLE)
                .visible(R.id.a_settings_u_container, AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE)
                .click(R.id.title_bar_controls_back_button, view -> this.back())
                .click(R.id.a_settings_cancel, view -> this.back())
                .click(R.id.a_settings_save, view -> {
                    SSettings settings = new SSettings(this.h.checked(R.id.a_settings_s_scheme),
                            this.h.text(R.id.a_settings_s_address),
                            Integer.parseInt(this.h.text(R.id.a_settings_s_port)),
                            this.h.text(R.id.a_settings_u_name),
                            this.h.text(R.id.a_settings_u_password)
                    );
                    try {
                        boolean changed = false;

                        if (
                                !AppConfig.CONFIG.isEnabled()) {

                            settings.validSSettings();

                            this.profile.settings
                                    .https(settings.https())
                                    .address(settings.address())
                                    .port(settings.port());

                            changed = true;
                        }
                        if (
                                AppConfig.AUTHENTICATION) {

                            settings.validASettings();

                            this.profile.settings
                                    .name(settings.name())
                                    .password(settings.password());

                            changed = true;
                        }
                        if (changed) {

                            Profiles profiles = this.app.profiles();
                            profiles
                                    .get(this.profile.id).settings = profile.settings;
                            this.app.profiles(profiles);

                            ASettings.this.result(RESULT_OK);
                        }
                        ASettings.this.finish();
                    } catch (SSettings.SettingsException e) {
                        ASettings.this.h.snack(VHBase.Messages.Snack.Type.E, e.getCustomMessage(), VHBase.Messages.Snack.Duration.SHORT);
                    }
                })
                .click(R.id.a_settings_q_container, view -> {
                    if (ActivityCompat.checkSelfPermission(
                            ASettings.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                        this.startActivityForResult(
                                new Intent(ASettings.this, ABarcode.class), App.RQ_A_BARCODE
                        );
                    } else {
                        ActivityCompat.requestPermissions(
                                ASettings.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION
                        );
                    }
                });


    }

    @Override
    public void resume() {
        this.h
                .checked(R.id.a_settings_s_scheme, this.profile.settings.https())
                .text(R.id.a_settings_s_address, this.profile.settings.address())
                .text(R.id.a_settings_s_port, String.valueOf(this.profile.settings.port()))
                .text(R.id.a_settings_u_name, this.profile.settings.name())
                .text(R.id.a_settings_u_password, this.profile.settings.password());
    }


    @Override
    public void result(int request, int result, Intent data) {
        if (request == App.RQ_A_BARCODE && result == RESULT_OK && data != null && data.hasExtra("settings")) {

            SSettings settings = this.data(data, "settings", SSettings.class);

            boolean changed = false;
            if (!AppConfig.CONFIG.isEnabled()) {

                this.profile.settings
                        .https(settings.https())
                        .address(settings.address())
                        .port(settings.port());

                changed = true;
            }
            if (AppConfig.AUTHENTICATION) {

                this.profile.settings
                        .name(settings.name())
                        .password(settings.password());

                changed = true;
            }

            if (changed) {
                Profiles profiles = this.app.profiles();
                profiles
                        .get(this.profile.id).settings = profile.settings;
                this.app.profiles(profiles);

                ASettings.this.result(RESULT_OK).finish();
            }
        }
    }

    @Override
    public void permissions(int request, @NonNull String[] permissions, @NonNull int[] results) {
        if (request == REQUEST_CAMERA_PERMISSION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            this.startActivityForResult(
                    new Intent(ASettings.this, ABarcode.class), App.RQ_A_BARCODE
            );
        }
    }
}
