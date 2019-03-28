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
import com.altertech.evahi.dialog.CustomDialogs;
import com.altertech.evahi.dialog.listeners.DialogCallBackYesCancel;
import com.altertech.evahi.dialog.obj.CustomAlertDialog;
import com.altertech.evahi.helpers.IntentHelper;
import com.altertech.evahi.helpers.SnackbarHelper;
import com.altertech.evahi.models.settings.SettingsModel;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private CheckBox a_settings_s_scheme;
    private EditText a_settings_s_address, a_settings_s_port, a_settings_u_name, a_settings_u_password;

    private BaseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.application = BaseApplication.get(this);

        View a_settings_s_container = findViewById(R.id.a_settings_s_container);
        a_settings_s_container.setVisibility(AppConfig.CONFIG != null && AppConfig.CONFIG.isEnabled() ? View.GONE : View.VISIBLE);
        this.a_settings_s_scheme = findViewById(R.id.a_settings_s_scheme);
        this.a_settings_s_address = findViewById(R.id.a_settings_s_address);
        this.a_settings_s_port = findViewById(R.id.a_settings_s_port);

        View a_settings_u_container = findViewById(R.id.a_settings_u_container);
        a_settings_u_container.setVisibility(AppConfig.AUTHENTICATION ? View.VISIBLE : View.GONE);
        this.a_settings_u_name = findViewById(R.id.a_settings_u_name);
        this.a_settings_u_password = findViewById(R.id.a_settings_u_password);


        this.findViewById(R.id.title_bar_controls_back_button).setOnClickListener(v -> this.onBackPressed());

        findViewById(R.id.a_settings_save).setOnClickListener(view -> {
            SettingsModel model = new SettingsModel(a_settings_s_scheme.isChecked(),
                    a_settings_s_address.getText().toString(),
                    a_settings_s_port.getText().toString(),
                    a_settings_u_name.getText().toString(),
                    a_settings_u_password.getText().toString()
            );
            try {
                int counter = 0;
                if (AppConfig.CONFIG == null || !AppConfig.CONFIG.isEnabled()) {
                    model.validS().saveS(this.application);
                    counter++;
                }
                if (AppConfig.AUTHENTICATION) {
                    model.validU().saveU(this.application);
                    counter++;
                }
                if (counter > 0) {
                    SettingsActivity.this.setResult(RESULT_OK);
                }
                SettingsActivity.this.finish();
            } catch (SettingsModel.SettingsException e) {
                SnackbarHelper.snack(SettingsActivity.this, SnackbarHelper.State.ERROR, e.getCustomMessage(), SnackbarHelper.Duration.SHORT);
            }
        });

        findViewById(R.id.a_settings_cancel).setOnClickListener(view -> SettingsActivity.this.onBackPressed());

        findViewById(R.id.a_settings_q_container).setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(SettingsActivity.this, ScannedBarcodeActivity.class), IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
            } else {
                ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        });

    }

    @Override
    protected void onResume() {
        this.a_settings_s_scheme.setChecked(this.application.useHttps());
        this.a_settings_s_address.setText(this.application.getServerAddress());
        this.a_settings_s_port.setText(String.valueOf(this.application.getServerPort()));
        this.a_settings_u_name.setText(this.application.getUserName());
        this.a_settings_u_password.setText(this.application.getUserPassword());
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode() && resultCode == RESULT_OK) {
            if (data != null) {
                int code = data.getIntExtra("code", 0);
                if (code == R.string.app_a_settings_exception_invalid_password) {
                    CustomDialogs.showPasswordDialog(SettingsActivity.this, new DialogCallBackYesCancel() {
                        @Override
                        public void dialogYes(CustomAlertDialog dialog, Object o) {
                            if (o != null && o instanceof String && ((String) o).length() > 0) {
                                dialog.mDismiss();
                                SettingsActivity.this.application.setUserPassword((String) o);
                                SettingsActivity.this.setResult(RESULT_OK);
                                SettingsActivity.this.finish();
                            } else {
                                SnackbarHelper.snack(SettingsActivity.this, SnackbarHelper.State.ERROR, R.string.app_a_settings_exception_invalid_password, SnackbarHelper.Duration.SHORT);
                            }
                        }

                        @Override
                        public void dialogCancel(CustomAlertDialog dialog) {
                            dialog.mDismiss();
                        }
                    });
                    return;
                }
            }
            SettingsActivity.this.setResult(RESULT_OK);
            SettingsActivity.this.finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent(SettingsActivity.this, ScannedBarcodeActivity.class), IntentHelper.REQUEST_CODES.BAR_CODE_ACTIVITY.getCode());
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


}
