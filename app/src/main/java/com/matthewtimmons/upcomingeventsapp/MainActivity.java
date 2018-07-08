package com.matthewtimmons.upcomingeventsapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtimmons.upcomingeventsapp.adapters.ConcertListAdapter;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.models.Game;


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
                            viewPager.setCurrentItem(EventPagerAdapter.INDEX_CONCERTS);
                            break;
                        case R.id.action_games:
                            viewPager.setCurrentItem(EventPagerAdapter.INDEX_GAMES);
                            break;
                        case R.id.action_movies:
                            viewPager.setCurrentItem(EventPagerAdapter.INDEX_MOVIES);
                            break;
                    }
                    return true;
            }
        });

        // Selects the current menu icon when swiping left or right
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                View view;
                switch(i) {
                    case 0:
                        view = bottomNavigationView.findViewById(R.id.action_concerts);
                        view.performClick();
                        break;
                    case 1:
                        view = bottomNavigationView.findViewById(R.id.action_games);
                        view.performClick();
                        break;
                    case 2:
                        view = bottomNavigationView.findViewById(R.id.action_movies);
                        view.performClick();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

//    public void writeToast(View v) {
//        CardView cardView = v.findViewById(R.id.card_view);
//        Bundle bundle = new Bundle();
//
//        ImageView concertPictureImageView = v.findViewById(R.id.concert_picture);
//        TextView firstBandNameTextView = v.findViewById(R.id.first_band_name);
//        TextView secondBandNameTextView = v.findViewById(R.id.second_band_name);
//        TextView concertLocationTextView = v.findViewById(R.id.concert_location);
//        TextView concertDateTextView = v.findViewById(R.id.concert_date);
//
//
//
//
//        Intent intent = new Intent(this, DetailsActivity.class);
////        intent.putExtra("CARD_VIEW", view);
//        intent.putExtra("FIRST_BAND_NAME", firstBandNameTextView.getText().toString());
//        intent.putExtra("SECOND_BAND_NAME", secondBandNameTextView.getText().toString());
//        intent.putExtra("LOCATION", concertLocationTextView.getText().toString());
//        intent.putExtra("DATE", concertDateTextView.getText().toString());
//
//
//        startActivity(intent);
//    }

//    public void showDetailsActivity(View v) {
//        Concert thisConcert = Concert.getPlaceholderConcerts().get(0);
//        Intent intent = new Intent(this, DetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("thisConcert", thisConcert);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
}
