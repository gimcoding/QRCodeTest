package com.study.qrcodetest;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeWriteActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageView imgQRCode;
    private Button btnQRCodeWrite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_write);

        setTitle("");

        this.txtTitle = findViewById(R.id.txtTitle);
        this.imgQRCode = findViewById(R.id.imgQRCode);
        this.btnQRCodeWrite = findViewById(R.id.btnQRCodeWrite);

        this.btnQRCodeWrite.setOnClickListener(mClickListener);
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
            if (view.getId() == R.id.btnQRCodeWrite) {
                String title = "주차장4";
                txtTitle.setText(title);
                btnQRCodeWrite.setVisibility(View.GONE);
                generateQRCode("A004");
            }
        }
    };
}
