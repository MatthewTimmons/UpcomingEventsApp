package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.EventsFragment;

public class EventPagerAdapter extends FragmentPagerAdapter {

    private static final int COUNT_PAGES = 3;
    public static final int INDEX_MOVIES = 0;
    public static final int INDEX_GAMES = 1;
    public static final int INDEX_CONCERTS = 2;

    public EventPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case INDEX_MOVIES:
                fragment = EventsFragment.newInstance(FirebaseConstants.COLLECTION_MOVIES);
                break;
            case INDEX_GAMES:
                fragment = EventsFragment.newInstance(FirebaseConstants.COLLECTION_GAMES);
                break;
            case INDEX_CONCERTS:
                fragment = EventsFragment.newInstance(FirebaseConstants.COLLECTION_CONCERTS);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT_PAGES;
    }
}
