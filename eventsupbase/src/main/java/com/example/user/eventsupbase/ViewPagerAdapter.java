package com.example.user.eventsupbase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.eventsupbase.Fragments.FragLogin;
import com.example.user.eventsupbase.Fragments.FragRegister;

/**
 * Created by User on 13.05.2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Вход", "Регистрация"};

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragLogin fragLogin = new FragLogin();
                return fragLogin;
            case 1:
                FragRegister fragRegister = new FragRegister();
                return fragRegister;
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
