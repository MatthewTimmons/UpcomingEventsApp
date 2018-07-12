package com.matthewtimmons.upcomingeventsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ConcertDetailsFragment extends Fragment {
    private static final String ARGS_CONCERT_ID = "concertId";
    String concertId;
    ArrayList<String> listOfBandsAtConcert;
    ImageView wideConcertImageView;
    TextView firstBandNameTextView;
    TextView secondBandNameTextView;
    TextView concertLocationTextView;
    TextView concertDateTextView;

    public static ConcertDetailsFragment newInstance(String concertId) {
        ConcertDetailsFragment instance = new ConcertDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_CONCERT_ID, concertId);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.details_concert, container, false);

        // TODO: Change this to be more like movie fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            concertId = bundle.getString(ARGS_CONCERT_ID);
        }

        CollectionReference concertsCollectionReference = FirebaseFirestore.getInstance().collection(FirebaseConstants.KEY_CONCERTS);

        concertsCollectionReference.document(concertId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot concertDocumentSnapshot = task.getResult();
                listOfBandsAtConcert = (ArrayList<String>) concertDocumentSnapshot.get("concertBandsArray");

                // Assign all views to variables
                wideConcertImageView = v.findViewById(R.id.wide_image);
                firstBandNameTextView = v.findViewById(R.id.first_band_name);
                secondBandNameTextView = v.findViewById(R.id.second_band_name);
                concertLocationTextView = v.findViewById(R.id.concert_location);
                concertDateTextView = v.findViewById(R.id.concert_date);

                // Get all values for this Concert
                String imageURL = concertDocumentSnapshot.getString("concertImageUrl");
                String firstBandName = listOfBandsAtConcert.get(0);
                String secondBandName = listOfBandsAtConcert.get(1);
                String concertLocation = concertDocumentSnapshot.getString("concertLocation");
                String concertDate = concertDocumentSnapshot.getString("concertDate");

                // Assign values to each view
                Picasso.get().load(imageURL).error(R.drawable.ic_concerts_blue).into(wideConcertImageView);
                firstBandNameTextView.setText(firstBandName);
                secondBandNameTextView.setText(secondBandName);
                concertLocationTextView.setText(concertLocation);
                concertDateTextView.setText(concertDate);

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
