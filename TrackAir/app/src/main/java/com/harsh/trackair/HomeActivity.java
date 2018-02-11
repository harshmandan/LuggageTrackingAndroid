package com.harsh.trackair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView name;
    private TextView src;
    private TextView dest;
    private TextView departingime;
    private TextView flighttime;
    private CardView trackbutton;
    private String flightno;
    private String luggageid;
    private String idno;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        img = findViewById(R.id.imageView4);
        img.setImageResource(R.raw.plane);
        flighttime = findViewById(R.id.textView10);
        name = findViewById(R.id.textView3);
        src = findViewById(R.id.fromText);
        dest = findViewById(R.id.toText);
        departingime = findViewById(R.id.textView5);
        trackbutton = findViewById(R.id.cardbutton);
        Intent startIntent = getIntent();
        idno = startIntent.getStringExtra("idno");
        Log.d("IDNO", idno);
        super.onStart();
    }

    @Override
    protected void onStart() {
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mPassIdRef = mRootRef.child("pass_id").child(idno);
        mPassIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map <String, String> map = (Map)dataSnapshot.getValue();
                name.setText("Welcome, " + map.get("name"));
                flightno = map.get("flightid");
                luggageid = map.get("baggageid");
                DatabaseReference mFlightsRef = FirebaseDatabase.getInstance().getReference().child("flights").child(flightno);
                mFlightsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map <String, String> map = (Map)dataSnapshot.getValue();
                        src.setText(map.get("from"));
                        dest.setText(map.get("dest"));
                        departingime.setText(map.get("time"));
                        flighttime.setText(map.get("traveltime"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("error", "Second Value listner");

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("error", "First Value listner");
            }
        });


        trackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent luggageIntent = new Intent(HomeActivity.this, TrackLuggageActivity.class);
                luggageIntent.putExtra("name", name.getText());
                luggageIntent.putExtra("departtime", departingime.getText());
                luggageIntent.putExtra("flighttime", flighttime.getText());
                luggageIntent.putExtra("path", "luggageid/"+luggageid+"/location");
                startActivity(luggageIntent);
            }
        });
        super.onStart();
    }
}
