package com.matthewtimmons.upcomingeventsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventDetailsAdapter extends FragmentPagerAdapter {


    public EventDetailsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

}
