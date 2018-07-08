package com.matthewtimmons.upcomingeventsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.fragments.ConcertDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.models.Concert;

public class DetailsActivity extends AppCompatActivity {
    ViewPager detailsViewPager;
    FragmentPagerAdapter detailsPagerAdapter;
    TextView first_band_name;
    TextView second_band_name;
    TextView location;
    TextView date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailsViewPager = findViewById(R.id.details_view_pager);
        detailsPagerAdapter = new EventDetailsPagerAdapter(getSupportFragmentManager());
        detailsViewPager.setAdapter(detailsPagerAdapter);




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
}
