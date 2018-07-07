package com.matthewtimmons.upcomingeventsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventDetailsPagerAdapter extends FragmentPagerAdapter {

    public static final int INDEX_CONCERTS = 0;
    public static final int INDEX_GAMES = 1;
    public static final int INDEX_MOVIES = 2;

    public EventDetailsPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ConcertDetailsFragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
