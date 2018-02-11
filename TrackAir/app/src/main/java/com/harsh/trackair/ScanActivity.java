package com.harsh.trackair;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import static android.view.View.GONE;

public class ScanActivity extends AppCompatActivity implements OnQRCodeReadListener {

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private String scanop;
    private ProgressBar spinner;
    private TextView loading;
    private ImageView imagebg;
    private ImageView imagebg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loading = findViewById(R.id.settingup);
    spinner = findViewById(R.id.progressBar);
    imagebg = findViewById(R.id.imageView2);
    imagebg2 = findViewById(R.id.imageView3);
    imagebg2.setVisibility(GONE);
    imagebg.setVisibility(GONE);
    spinner.setVisibility(GONE);
    loading.setVisibility(GONE);


    qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        resultTextView = findViewById(R.id.result_text_view);
        resultTextView.setText(R.string.tooltip);

    // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

    // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

    // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(false);

    // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

    // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
}

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(final String text, PointF[] points) {
        resultTextView.setText(R.string.setup);
        scanop = text;
        loading.setText(R.string.settingup);
        imagebg2.setVisibility(View.VISIBLE);
        imagebg.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(ScanActivity.this, HomeActivity.class);
                homeIntent.putExtra("idno", text);
                startActivity(homeIntent);
                finish();
            }
        }, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


}

