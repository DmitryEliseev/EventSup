package com.example.user.eventsupbase.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.user.eventsupbase.R;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class SplashActivity extends AppCompatActivity {
    final int secondsDelayed = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}
