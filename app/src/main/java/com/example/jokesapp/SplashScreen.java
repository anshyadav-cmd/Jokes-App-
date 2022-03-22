package com.example.jokesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

public class SplashScreen extends Activity {

    private static boolean isSplashLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isSplashLoaded){
            setContentView(R.layout.splash);
            int secondsDelayed = 1;
            // it use to create new thread in the background
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    // destroy the current activity
                    finish();
                }
            } , secondsDelayed * 1000);

            isSplashLoaded = true;
        }else {
            Intent goToMainActivity = new Intent(SplashScreen.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}
