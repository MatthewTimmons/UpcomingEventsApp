package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.squareup.picasso.Picasso;


public class ConcertDetailsFragment extends Fragment {
    ImageView wideConcertImageView;
    TextView firstBandNameTextView;
    TextView secondBandNameTextView;
    TextView concertLocationTextView;
    TextView concertDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_concert, container, false);

        // Get current Concert from activity
        DetailsActivity activity = (DetailsActivity) getActivity();
        Concert thisConcert = activity.getCurrentConcert();

        // Assign all views to variables
        wideConcertImageView = v.findViewById(R.id.wide_image);
        firstBandNameTextView = v.findViewById(R.id.first_band_name);
        secondBandNameTextView = v.findViewById(R.id.second_band_name);
        concertLocationTextView = v.findViewById(R.id.concert_location);
        concertDateTextView = v.findViewById(R.id.concert_date);

        // Get all values for this Concert
        String imageURL = thisConcert.getImageUrl();
        String firstBandName = thisConcert.getBandName(0);
        String secondBandName = thisConcert.getBandName(1);
        String concertLocation = thisConcert.getConcertLocation();
        String concertDate = thisConcert.getDate();

        // Assign values to each view
        Picasso.get().load(imageURL).error(R.drawable.ic_concerts_blue).into(wideConcertImageView);
        firstBandNameTextView.setText(firstBandName);
        secondBandNameTextView.setText(secondBandName);
        concertLocationTextView.setText(concertLocation);
        concertDateTextView.setText(concertDate);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
