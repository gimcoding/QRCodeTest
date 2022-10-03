package com.study.qrcodetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends AppCompatActivity {

    private TextView txtQRCode;
    private ImageView imgQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtQRCode = findViewById(R.id.txtQRCode);
        this.imgQRCode = findViewById(R.id.imgQRCode);

        findViewById(R.id.btnQR).setOnClickListener(mClickListener);
        findViewById(R.id.btnCustomQR).setOnClickListener(mClickListener);
        findViewById(R.id.btnQRCodeWrite).setOnClickListener(mClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if(result != null) {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    this.txtQRCode.setText(result.getContents());
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* QR 코드 생성 */
    private void generateQRCode(String contents) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            Bitmap bitmap = toBitmap(qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, 100, 100));

            // QR 코드 표시
            Glide.with(this)
                    .load(bitmap)
                    .into(this.imgQRCode);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnQR:
                    txtQRCode.setText("");

                    IntentIntegrator intentIntegrator1 = new IntentIntegrator(MainActivity.this);
                    // 바코드 인식시 소리
                    intentIntegrator1.setBeepEnabled(true);
                    intentIntegrator1.initiateScan();
                    break;

                case R.id.btnCustomQR:
                    txtQRCode.setText("");

                    IntentIntegrator intentIntegrator2 = new IntentIntegrator(MainActivity.this);
                    //바코드 인식시 소리
                    intentIntegrator2.setBeepEnabled(true);
                    intentIntegrator2.setCaptureActivity(CustomQRActivity.class);
                    intentIntegrator2.initiateScan();
                    break;

                case R.id.btnQRCodeWrite:
                    //generateQRCode("SV-1243-5678");
                    Intent intent = new Intent(MainActivity.this, QRCodeWriteActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
