package com.study.qrcodetest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class CustomQRActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager captureManager;
    private DecoratedBarcodeView decoratedBarcodeView;

    private Button btnFlash;

    // 플래시가 켜져 있는지
    private boolean isFlashOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_qr);

        this.isFlashOn = false;

        // com.journeyapps.barcodescanner.BarcodeView 에
        // app:zxing_scanner_layout="@layout/custom_barcode_scanner" 를 사용하지 않으면
        // default scanner layout 이 적용됩니다.
        this.decoratedBarcodeView = findViewById(R.id.barcodeView);

        this.decoratedBarcodeView.setTorchListener(this);
        this.captureManager = new CaptureManager(this, this.decoratedBarcodeView);
        this.captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        this.captureManager.decode();

        this.btnFlash = findViewById(R.id.btnFlash);

        /*
        if (!hasFlash()) {
            this.btnFlash.setVisibility(View.GONE);
        }
        */

        this.btnFlash.setOnClickListener(mClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.captureManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.captureManager.onSaveInstanceState(outState);
    }

    @Override
    public void onTorchOn() {
        this.btnFlash.setText("플래시 끄기");
        isFlashOn = true;
    }

    @Override
    public void onTorchOff() {
        this.btnFlash.setText("플래시 켜기");
        isFlashOn = false;
    }

    /* 플래시 기능 가능 여부 체크 */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnFlash:
                    if(isFlashOn){
                        decoratedBarcodeView.setTorchOff();
                    }else{
                        decoratedBarcodeView.setTorchOn();
                    }
                    break;
            }
        }
    };
}