package com.altertech.evahi.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.models.s.SSettings;
import com.altertech.evahi.ui.base.ABase;
import com.altertech.evahi.ui.holders.view.VHBase;
import com.altertech.evahi.utils.Utils;

import java.util.Collections;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ABarcode extends ABase<App> implements ZBarScannerView.ResultHandler {

    private ZBarScannerView scanner;

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.a_barcode;
    }

    @Override
    public void created() {
        this.h.click(R.id.title_bar_controls_back_button, view -> this.back());

        this.scanner = this.findViewById(
                R.id.preview
        );
        this.scanner.setFormats(Collections.singletonList(BarcodeFormat.QRCODE));
    }

    @Override
    public void resume(

    ) {
        this.scanner.setResultHandler(
                this
        );
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            this.scanner.startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ASettings.REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void pause(

    ) {
        this.scanner.stopCamera();
    }

    @Override
    public void permissions(int request, @NonNull String[] permissions, @NonNull int[] results) {
        if (request == ASettings.REQUEST_CAMERA_PERMISSION && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            this
                    .scanner.startCamera(

            );
        } else {
            this.finish(

            );
        }
    }

    @Override
    public void handleResult(Result result) {

        if (
                result != null && Utils.Strings.notEmpty(result.getContents())) {
            SSettings settings = new SSettings();
            try {
                ABarcode.this
                        .setResult(RESULT_OK, new Intent().putExtra("settings", settings.parse(result.getContents())));
                ABarcode.this
                        .finish();
            } catch (final SSettings.SettingsException e) {
                if (
                        e.getCustomMessage() == R.string.app_a_settings_exception_invalid_password) {
                    ABarcode.this
                            .setResult(RESULT_OK, new Intent().putExtra("settings", settings));
                    ABarcode.this
                            .finish();
                } else {
                    this.h.snack(VHBase.Messages.Snack.Type.E, R.string.app_a_settings_exception_invalid_code, VHBase.Messages.Snack.Duration.SHORT).and(this).scanner.resumeCameraPreview(this);
                }
            }
        }
    }
}