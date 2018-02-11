package com.harsh.trackairbooth;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

public class MainActivity extends AppCompatActivity implements Listener{

    private ImageView myImage;
    private String convert;
    private EditText inputText;
    private Button submitButton;
    private TextView showHint;
    private Button writetoTag;
    private NfcAdapter myNfcAdapter;
    private boolean isWrite;
    private WriteNfcData mWriteFragment;
    private boolean isDialogDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = findViewById(R.id.editText);
        myImage = findViewById(R.id.imageView);
        showHint = findViewById(R.id.textView2);
        writetoTag = findViewById(R.id.button3);
        showHint.setVisibility(View.GONE);
        myImage.setVisibility(View.GONE);
        submitButton = findViewById(R.id.button2);
        writetoTag.setVisibility(View.GONE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert = inputText.getText().toString();
                Bitmap myBitmap = QRCode.from(convert).bitmap();
                showHint.setVisibility(View.VISIBLE);
                myImage.setImageBitmap(myBitmap);
                myImage.setVisibility(View.VISIBLE);
                writetoTag.setVisibility(View.VISIBLE);
            }
        });
        myNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        writetoTag.setOnClickListener(view -> showWriteFragment());
    }

    private void showWriteFragment() {
        isWrite = true;

        mWriteFragment = (WriteNfcData) getFragmentManager().findFragmentByTag(WriteNfcData.TAG);

        if (mWriteFragment == null) {

            mWriteFragment = WriteNfcData.newInstance();
        }
        mWriteFragment.show(getFragmentManager(),WriteNfcData.TAG);
    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(myNfcAdapter!= null)
            myNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myNfcAdapter!= null)
            myNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d("TAG", "onNewIntent: " + intent.getAction());

        if (tag != null) {
            Toast.makeText(this, "Tag Detected", Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {

                if (isWrite) {

                    String messageToWrite = inputText.getText().toString();
                    mWriteFragment = (WriteNfcData) getFragmentManager().findFragmentByTag(WriteNfcData.TAG);
                    mWriteFragment.onNfcDetected(ndef, messageToWrite);

                }
            }
        }
    }
}
