package com.matthewtimmons.upcomingeventsapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.matthewtimmons.upcomingeventsapp.R;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new EventPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_concerts:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.action_games:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.action_movies:
                            viewPager.setCurrentItem(2);
                            break;
                    }
                    return true;
            }
        });
    }
}
