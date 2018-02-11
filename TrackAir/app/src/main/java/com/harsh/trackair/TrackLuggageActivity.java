package com.harsh.trackair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.GONE;

public class TrackLuggageActivity extends AppCompatActivity {

    private TextView name;
    private TextView currloc;
    private TextView nexloc;
    private TextView departtime;
    private TextView flighttime;
    private TextView seeyou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_luggage);
        name = findViewById(R.id.textView3);
        currloc = findViewById(R.id.textView2);
        seeyou = findViewById(R.id.textView6);
        nexloc = findViewById(R.id.textView5);
        departtime = findViewById(R.id.textView7);
        flighttime = findViewById(R.id.textView10);
        Intent startIntent = getIntent();
        name.setText(startIntent.getStringExtra("name"));
        departtime.setText(startIntent.getStringExtra("departtime"));
        flighttime.setText(startIntent.getStringExtra("flighttime"));
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mLocRef = mRootRef.child(startIntent.getStringExtra("path"));
        mLocRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String loc = dataSnapshot.getValue().toString();
                seeyou.setText("Don't worry, your luggage is on track as intended");
                if (loc.equals("0")) {
                    currloc.setText(R.string.track0);
                    nexloc.setText(R.string.track1);
                }
                else if (loc.equals("1")) {
                    currloc.setText(R.string.track1);
                    nexloc.setText(R.string.track2);
                }
                else if (loc.equals("2")) {
                    currloc.setText(R.string.track2);
                    nexloc.setText(R.string.track3);
                }else if (loc.equals("3")) {
                    currloc.setText(R.string.track3);
                    nexloc.setText(R.string.track4);
                }else if (loc.equals("4")) {
                    currloc.setText(R.string.track4);
                    nexloc.setText(R.string.track5);
                }else if (loc.equals("5")) {
                    currloc.setText(R.string.track6);
                    nexloc.setText(R.string.track7);
                }else if (loc.equals("6")) {
                    currloc.setText(R.string.track6);
                    nexloc.setText(R.string.track7);
                }else if (loc.equals("7")) {
                    currloc.setText(R.string.track7);
                    nexloc.setText("Destination Reached");
                    seeyou.setText("Thank you for travelling with us!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
