package com.example.matthewtimmons.examplepleasedelete;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Adapter extends FragmentPagerAdapter{


    public Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        if(i == 0) {
            fragment = new ConcertsFragment();
        } else if (i == 1) {
            fragment = new GamesFragment();
        } else if (i == 2) {
            fragment = new MoviesFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
