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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ConcertDetailsFragment extends Fragment {
    private static final String ARGS_CONCERT_ID = "concertId";
    String concertId;
    ArrayList<String> listOfBandsAtConcert;
    ImageView wideConcertImageView;
    TextView firstBandNameTextView;
    TextView secondBandNameTextView;
    TextView remainingBandsTextView;
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
        final View v = inflater.inflate(R.layout.fragment_details_event, container, false);

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
                firstBandNameTextView = v.findViewById(R.id.title);
                secondBandNameTextView = v.findViewById(R.id.subtitle);
                remainingBandsTextView = v.findViewById(R.id.second_info_field);
                concertLocationTextView = v.findViewById(R.id.third_info_field);
                concertDateTextView = v.findViewById(R.id.fourth_info_field);

                // Get all values for this Concert
                String imageURL = concertDocumentSnapshot.getString("concertImageUrl");
                String firstBandName = listOfBandsAtConcert.get(0);
                String concertLocation = concertDocumentSnapshot.getString("concertLocation");
                String concertDate = concertDocumentSnapshot.getString("concertDate");

                // Assign values to each view
                Picasso.get().load(imageURL).error(R.drawable.ic_concerts_blue).into(wideConcertImageView);
                firstBandNameTextView.setText(firstBandName);
                concertLocationTextView.setText(concertLocation);
                concertDateTextView.setText(concertDate);

                setConditionalBandTextViews(listOfBandsAtConcert, v);
            }
        });

        return v;
    }

    public void setConditionalBandTextViews(ArrayList<String> listOfBandsAtConcert, View v) {
        if (listOfBandsAtConcert.size() > 1) {
            String secondBandName = listOfBandsAtConcert.get(1);
            secondBandNameTextView.setVisibility(View.VISIBLE);
            secondBandNameTextView.setText(secondBandName);
            if (listOfBandsAtConcert.size() > 2) {
                listOfBandsAtConcert.remove(0);
                listOfBandsAtConcert.remove(0);
                String remainingBands = "";
//                for (String band : listOfBandsAtConcert) {
//                    remainingBands = remainingBands.concat(band + ", ");
//                }
//                Toast.makeText(getContext(), remainingBands, Toast.LENGTH_SHORT).show();
                // New Method
                for (int i = 0; i < listOfBandsAtConcert.size(); i++) {
                    if (i == listOfBandsAtConcert.size() - 2) {
                        remainingBands = remainingBands.concat(listOfBandsAtConcert.get(i) + ", and ");
                    } else if (i == listOfBandsAtConcert.size() - 1) {
                        remainingBands = remainingBands.concat(listOfBandsAtConcert.get(i));
                    } else if (i < listOfBandsAtConcert.size()) {
                        remainingBands = remainingBands.concat(listOfBandsAtConcert.get(i) + ", ");
                    }
                }
                remainingBandsTextView.setVisibility(View.VISIBLE);
                remainingBandsTextView.setText(remainingBands);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
