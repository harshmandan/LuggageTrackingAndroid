package com.harsh.trackairscanner;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Listener, AdapterView.OnItemSelectedListener {

    private Button mBtWrite;
    private Button mBtRead;
    private String passID;

    private NFCReadFragment mNfcReadFragment;

    private Spinner locList;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;
    String luggageID;
    String LocIDvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtWrite = (Button) findViewById(R.id.button2);
        mBtRead = (Button) findViewById(R.id.button);
        locList = findViewById(R.id.spinner);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mBtRead.setOnClickListener(view -> showReadFragment());
        mBtWrite.setVisibility(View.GONE);
        mBtWrite.setOnClickListener(v -> updateLocation());
        locList.setVisibility(View.GONE);
        populateList();
    }

    protected void populateList()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locList.setAdapter(adapter);
        locList.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        LocIDvar = Integer.toString(position);
        Log.d("SET ID VAR FROM LIST", LocIDvar);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    protected void updateLocation()
    {
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mLuggageRef = mRootRef.child("pass_id").child(passID);
        mLuggageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map)dataSnapshot.getValue();
                luggageID = map.get("baggageid");
                Log.d("GOT BAGGAGE ID", luggageID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            Log.d("GET LUGGAGE ID", "ERROR");
            }
        });
        DatabaseReference mLuggageIDRef = mRootRef.child("luggageid").child(luggageID).child("location");
        mLuggageIDRef.setValue(LocIDvar);
        Log.d("UPdated location in the database", LocIDvar);

    }

    private void showReadFragment() {

        mNfcReadFragment = (NFCReadFragment) getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);

        if (mNfcReadFragment == null) {

            mNfcReadFragment = NFCReadFragment.newInstance();
        }
        mNfcReadFragment.show(getFragmentManager(),NFCReadFragment.TAG);
        passID=mNfcReadFragment.getScannedtext();
        Log.d("RETURNED SCANNED TEXT", passID);
        mBtWrite.setVisibility(View.VISIBLE);
        locList.setVisibility(View.VISIBLE);
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
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d("TAG", "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isDialogDisplayed) {
                    mNfcReadFragment = (NFCReadFragment)getFragmentManager().findFragmentByTag(NFCReadFragment.TAG);
                    mNfcReadFragment.onNfcDetected(ndef);
                }
            }
        }
    }


