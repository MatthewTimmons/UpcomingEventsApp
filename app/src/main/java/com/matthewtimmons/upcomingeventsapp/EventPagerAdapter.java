package com.matthewtimmons.upcomingeventsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventPagerAdapter extends FragmentPagerAdapter {

    private static final int COUNT_PAGES = 3;
    public static final int INDEX_CONCERTS = 0;
    public static final int INDEX_GAMES = 1;
    public static final int INDEX_MOVIES = 2;

    public EventPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case INDEX_CONCERTS:
                fragment = new ConcertsFragment();
                break;
            case INDEX_GAMES:
                fragment = new GamesFragment();
                break;
            case INDEX_MOVIES:
                fragment = new MoviesFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT_PAGES;
    }
}
