package com.matthewtimmons.upcomingeventsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtimmons.upcomingeventsapp.fragments.ConcertDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.GameDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.MovieDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.models.Movie;

import java.util.concurrent.TimeUnit;

public class DetailsActivity extends AppCompatActivity {
    Fragment fragment;
    SeekBar seekBar;
    TextView interestLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getCurrentConcert() != null) {
            fragment = new ConcertDetailsFragment();
        } else if (getCurrentGame() != null) {
            fragment = new GameDetailsFragment();
        } else if (getCurrentMovie() != null) {
            fragment = new MovieDetailsFragment();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.replacable, fragment).commit();

        seekBar = findViewById(R.id.slider_bar);
        interestLevel = findViewById(R.id.interest_level);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateText(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                interestLevel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                interestLevel.setVisibility(View.GONE);
            }
        });

//        detailsViewPager = findViewById(R.id.details_view_pager);
//        detailsPagerAdapter = new EventDetailsPagerAdapter(getSupportFragmentManager());
//        detailsViewPager.setAdapter(detailsPagerAdapter);
//
//        detailsViewPager.setCurrentItem(EventDetailsPagerAdapter.INDEX_GAMES);


//        detailsViewPager = findViewById(R.id.details_view_pager);
//        first_band_name = findViewById(R.id.first_band_name);
//        second_band_name = findViewById(R.id.second_band_name);
//        location = findViewById(R.id.concert_location);
//        date = findViewById(R.id.concert_date);

//        Intent intent = getIntent();
//        first_band_name.setText(intent.getStringExtra("FIRST_BAND_NAME").toString());
//        second_band_name.setText(intent.getStringExtra("SECOND_BAND_NAME").toString());
//        location.setText(intent.getStringExtra("LOCATION").toString());
//        date.setText(intent.getStringExtra("DATE").toString());



    }

    public Concert getCurrentConcert() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Concert thisConcert = (Concert) bundle.getSerializable("thisConcert");
        return thisConcert;
    }

    public Game getCurrentGame() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Game thisGame = (Game) bundle.getSerializable("thisGame");
        return thisGame;
    }

    public Movie getCurrentMovie() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Movie thisMovie = (Movie) bundle.getSerializable("thisMovie");
        return thisMovie;
    }

    public void updateText(int i) {
        switch(i) {
            case 0:
                interestLevel.setText("Not interested");
                interestLevel.setBackgroundColor(Color.parseColor("#f44242"));
                break;
            case 1:
                interestLevel.setText("Somewhat Interested");
                interestLevel.setBackgroundColor(Color.parseColor("#f4f142"));
                break;
            case 2:
                interestLevel.setText("Very interested");
                interestLevel.setBackgroundColor(Color.parseColor("#6ef442"));
                break;
        }
    }
}
