package com.matthewtimmons.upcomingeventsapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.views.ConcertListAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


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
                            loadConcertsFragment();
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

    private void loadConcertsFragment() {
        setContentView(R.layout.fragment_concerts);
        ConcertListAdapter concertAdapter = new ConcertListAdapter(Concert.getPlaceholderConcerts());
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(concertAdapter);
    }
}
