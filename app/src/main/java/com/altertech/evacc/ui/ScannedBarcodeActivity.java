package com.altertech.evacc.ui;

/**
 * Created by oshevchuk on 28.08.2018
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.altertech.evacc.R;
import com.altertech.evacc.helpers.SnackbarHelper;
import com.altertech.evacc.models.settings.SettingsModel;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

@SuppressLint("Registered")
public class ScannedBarcodeActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        this.surfaceView = findViewById(R.id.surfaceView);

        findViewById(R.id.title_bar_controls_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScannedBarcodeActivity.this.onBackPressed();
            }
        });
    }

    private void initialiseDetectorsAndSources() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        this.cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();


        this.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        ScannedBarcodeActivity.this.cameraSource.start(holder);
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    SnackbarHelper.snack(ScannedBarcodeActivity.this, SnackbarHelper.State.ERROR,  R.string.app_a_settings_exception_qr_io_error, SnackbarHelper.Duration.SHORT);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                ScannedBarcodeActivity.this.cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> detectedItems = detections.getDetectedItems();
                if (detectedItems != null && detectedItems.size() >= 1) {
                    Barcode barcode = detectedItems.valueAt(0);
                    if (barcode != null) {
                        SettingsModel model = new SettingsModel();
                        try {
                            model.parse(barcode.rawValue);
                            model.save(ScannedBarcodeActivity.this);
                            ScannedBarcodeActivity.this.setResult(RESULT_OK);
                            ScannedBarcodeActivity.this.finish();
                        } catch (final SettingsModel.SettingsException e) {
                            ScannedBarcodeActivity.this.showStatus(R.string.app_a_settings_exception_bad_code);
                        }
                    }
                }
            }
        });
    }

    private void showStatus(final @StringRes int s) {
        findViewById(R.id.barcode_status_id).post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.barcode_status_id)).setText(s);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.initialiseDetectorsAndSources();
    }
}