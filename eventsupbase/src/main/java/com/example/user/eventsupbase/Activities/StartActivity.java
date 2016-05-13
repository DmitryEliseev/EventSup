package com.example.user.eventsupbase.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.user.eventsupbase.R;
import com.example.user.eventsupbase.ViewPagerAdapter;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class StartActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }
}

