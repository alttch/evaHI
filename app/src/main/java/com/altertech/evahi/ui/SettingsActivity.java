package com.altertech.evahi.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.R;
import com.altertech.evahi.core.BaseApplication;
import com.altertech.evahi.helpers.IntentHelper;
import com.altertech.evahi.helpers.SnackbarHelper;
import com.altertech.evahi.models.settings.SSettings;

public class SettingsActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA_PERMISSION = 201;

    private CheckBox a_settings_s_scheme;
    private EditText a_settings_s_address, a_settings_s_port, a_settings_u_name, a_settings_u_password;

    private BaseApplication application;

    private BaseApplication.Profiles.Profile profile;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        this
                .setContentView(R.layout.activity_settings);

        this.application = BaseApplication.get(this);

        this.profile = this.application.profiles().get(this.application.id());

        this.findViewById(R.id.a_settings_s_container).setVisibility(AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? View.GONE : View.VISIBLE);

        this.a_settings_s_scheme = findViewById(R.id.a_settings_s_scheme);
        this.a_settings_s_address = findViewById(R.id.a_settings_s_address);
        this.a_settings_s_port = findViewById(R.id.a_settings_s_port);

        this.findViewById(R.id.a_settings_u_container).setVisibility(AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE);

        this.a_settings_u_name = findViewById(R.id.a_settings_u_name);
        this.a_settings_u_password = findViewById(R.id.a_settings_u_password);


        this.findViewById(R.id.title_bar_controls_back_button).setOnClickListener(v -> this.onBackPressed());

        this.findViewById(R.id.a_settings_save).setOnClickListener(view -> {

            SSettings settings = new SSettings(a_settings_s_scheme.isChecked(),
                    a_settings_s_address
                            .getText().toString(),
                    Integer.parseInt(a_settings_s_port.getText().toString()),
                    a_settings_u_name
                            .getText().toString(),
                    a_settings_u_password
                            .getText().toString()
            );
            try {
                boolean changed = false;

                if (
                        AppConfig.CONFIG == null || !AppConfig.CONFIG.isEnabled()) {

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

                    BaseApplication.Profiles profiles = this.application.profiles();
                    profiles
                            .get(this.profile.id).settings = profile.settings;
                    this.application.profiles(profiles);

                    SettingsActivity.this.setResult(RESULT_OK);
                }
                SettingsActivity.this.finish();
            } catch (SSettings.SettingsException e) {
                SnackbarHelper.snack(SettingsActivity.this, SnackbarHelper.State.ERROR, e.getCustomMessage(), SnackbarHelper.Duration.SHORT);
            }
        });

        this.findViewById(R.id.a_settings_cancel).setOnClickListener(view -> SettingsActivity.this.onBackPressed());

        this.findViewById(R.id.a_settings_q_container).setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                this.startActivityForResult(new Intent(SettingsActivity.this, ScannedBarcodeActivity.class), IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
            } else {
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        });

    }

    @Override
    protected void onResume() {
        this.a_settings_s_scheme
                .setChecked(
                        this.profile.settings.https());
        this.a_settings_s_address
                .setText(
                        this.profile.settings.address());
        this.a_settings_s_port
                .setText(
                        String.valueOf(this.profile.settings.port()));
        this.a_settings_u_name
                .setText(
                        this.profile.settings.name());
        this.a_settings_u_password
                .setText(
                        this.profile.settings.password());
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode() && resultCode == RESULT_OK && data != null && data.hasExtra("settings")) {

            SSettings settings = this.getData(data, "settings", SSettings.class);

            boolean changed = false;
            if (AppConfig.CONFIG == null || !AppConfig.CONFIG.isEnabled()) {

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
                BaseApplication.Profiles profiles = this.application.profiles();
                profiles
                        .get(this.profile.id).settings = profile.settings;
                this.application.profiles(profiles);

                SettingsActivity.this.setResult(RESULT_OK);
                SettingsActivity.this.finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.startActivityForResult(new Intent(SettingsActivity.this, ScannedBarcodeActivity.class), IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public <T> T getData(Intent intent, String key, Class<T> cls) {
        if (intent != null) {
            Object o = intent.getSerializableExtra(key);
            if (o != null) {
                return cls.cast(o);
            }
        }
        return null;
    }

}
