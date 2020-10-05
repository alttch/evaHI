package com.altertech.evahi.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.altertech.evahi.R;
import com.altertech.evahi.core.App;
import com.altertech.evahi.core.models.s.SSettings;
import com.altertech.evahi.ui.base.ABase;
import com.altertech.evahi.ui.holders.view.VHBase;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannedBarcodeActivity extends ABase<App> {

    private CameraSource
            camera;

    protected @LayoutRes
    int getLayout() {
        return
                R.layout.a_barcode;
    }

    @Override
    public void created() {
        this.h.click(R.id.title_bar_controls_back_button, view -> this.back());

    }


    private void initialization() {

        BarcodeDetector barcode;

        this.camera = new CameraSource.Builder(this, barcode = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build())
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();


        ((SurfaceView) findViewById(R.id.surface)).getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    ScannedBarcodeActivity.this.camera.start(holder);
                } catch (IOException e) {
                    ScannedBarcodeActivity.this.h.snack(VHBase.Messages.Snack.Type.E, R.string.app_a_settings_exception_qr_io_error, VHBase.Messages.Snack.Duration.SHORT);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                ScannedBarcodeActivity.this.camera.stop();
            }
        });


        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                if (detections.getDetectedItems() != null && detections.getDetectedItems().size() >= 1) {
                    Barcode barcode = detections.getDetectedItems().valueAt(0);
                    if (barcode != null) {
                        SSettings settings = new SSettings();
                        try {
                            ScannedBarcodeActivity.this
                                    .setResult(RESULT_OK, new Intent().putExtra("settings", settings.parse(barcode.rawValue)));
                            ScannedBarcodeActivity.this
                                    .finish();
                        } catch (final SSettings.SettingsException e) {
                            if (
                                    e.getCustomMessage() == R.string.app_a_settings_exception_invalid_password) {
                                ScannedBarcodeActivity.this
                                        .setResult(RESULT_OK, new Intent().putExtra("settings", settings));
                                ScannedBarcodeActivity.this
                                        .finish();
                            } else {
                                ScannedBarcodeActivity.this.status(R.string.app_a_settings_exception_invalid_code);
                            }
                        }

                    }
                }
            }
        });
    }

    private void status(final @StringRes int s) {
        this.findViewById(R.id.status).post(() -> ((TextView) findViewById(R.id.status)).setText(s));
    }

    @Override
    public void resume(

    ) {
        this.initialization();
    }

    @Override
    public void pause(

    ) {
        this.camera.release();
    }

}