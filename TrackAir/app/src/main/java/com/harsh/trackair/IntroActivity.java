package com.harsh.trackair;


import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome", "This app lets you track everything", R.raw.track, getResources().getColor(R.color.track)));
        addSlide(AppIntroFragment.newInstance("Track Your Luggage", "You can track your luggage in real time!", R.raw.lugg, getResources().getColor(R.color.lugg)));
        addSlide(AppIntroFragment.newInstance("Never Get Lost", "Inbuilt map lets you easily track nearby points", R.raw.lost,  getResources().getColor(R.color.lost)));
        addSlide(AppIntroFragment.newInstance("Please allow camera permission", "We'll need camera permission to set up your device", R.raw.cam,  getResources().getColor(R.color.cam)));
        setBarColor(getResources().getColor(R.color.colorPrimaryDark));
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 4); // OR
        //setSeparatorColor(getResources().getColor(R.color.colorPrimary));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Log.i("GET NEWUSER VALUE", getPreferences(MODE_PRIVATE).getString("NEWUSER","-1"));
        getPreferences(MODE_PRIVATE).edit().putString("NEWUSER","1").apply();
        Log.i("SET NEWUSER VALUE", getPreferences(MODE_PRIVATE).getString("NEWUSER","-1"));
        Intent i = new Intent(this, ScanActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
