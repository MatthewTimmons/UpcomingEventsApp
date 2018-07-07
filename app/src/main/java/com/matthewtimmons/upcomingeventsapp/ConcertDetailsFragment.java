package com.matthewtimmons.upcomingeventsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.adapters.ConcertListAdapter;
import com.matthewtimmons.upcomingeventsapp.models.Concert;

public class ConcertDetailsFragment extends Fragment {
    TextView firstBandName;
    TextView secondBandName;
    TextView location;
    TextView date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_concert, container, false);

        firstBandName = v.findViewById(R.id.first_band_name);
        secondBandName = v.findViewById(R.id.second_band_name);
        location = v.findViewById(R.id.concert_location);
        date = v.findViewById(R.id.concert_date);

        firstBandName.setText("first band name");
        secondBandName.setText("second band name");
        location.setText("location");
        date.setText("date");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
