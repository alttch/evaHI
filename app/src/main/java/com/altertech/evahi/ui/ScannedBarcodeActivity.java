package com.altertech.evahi.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.altertech.evahi.R;
import com.altertech.evahi.helpers.SnackbarHelper;
import com.altertech.evahi.models.s.SSettings;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private CameraSource
            camera;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        this
                .setContentView(R.layout.activity_scan_barcode);

        this.findViewById(R.id.title_bar_controls_back_button).setOnClickListener(view -> ScannedBarcodeActivity.this.onBackPressed());
    }

    private void init() {

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
                    SnackbarHelper.snack(ScannedBarcodeActivity.this, SnackbarHelper.State.ERROR, R.string.app_a_settings_exception_qr_io_error, SnackbarHelper.Duration.SHORT);
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
                                ScannedBarcodeActivity.this.showStatus(R.string.app_a_settings_exception_invalid_code);
                            }
                        }

                    }
                }
            }
        });
    }

    private void showStatus(final @StringRes int s) {
        this.findViewById(R.id.status).post(() -> ((TextView) findViewById(R.id.status)).setText(s));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this
                .camera.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this
                .init();
    }
}