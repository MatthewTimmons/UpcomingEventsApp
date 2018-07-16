package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class EventDetailsFragment extends Fragment {
    private static final String ARGS_CONCERT_ID = "eventId";
    private static final String ARGS_EVENT_TYPE = "eventKey";
    String eventId;
    String eventKey;
    ImageView eventPictureImageView;
    TextView titleTextView;
    TextView subtitleTextView;
    TextView optionalSecondSubtitleTextView;
    TextView secondEventInfoTextView;
    TextView thirdEventInfoTextView;
    CheckBox optionalCheckbox;


    public static EventDetailsFragment newInstance(String eventId, String eventKey) {
        EventDetailsFragment instance = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_CONCERT_ID, eventId);
        args.putString(ARGS_EVENT_TYPE, eventKey);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_details_event, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString(ARGS_CONCERT_ID);
            eventKey = bundle.getString(ARGS_EVENT_TYPE);
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Assign all views to variables
        eventPictureImageView = view.findViewById(R.id.wide_image);
        titleTextView =view.findViewById(R.id.title);
        subtitleTextView =view.findViewById(R.id.optional_subtitle);
        optionalSecondSubtitleTextView = view.findViewById(R.id.optional_second_subtitle_field);
        secondEventInfoTextView = view.findViewById(R.id.second_info_field);
        thirdEventInfoTextView = view.findViewById(R.id.third_info_field);
        optionalCheckbox = view.findViewById(R.id.optional_checkbox);

        CollectionReference eventsCollectionReference = FirebaseFirestore.getInstance().collection(eventKey);
        eventsCollectionReference.document(eventId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot eventDocumentSnapshot = task.getResult();

                // Assign values to each view
                switch (eventKey) {
                    case FirebaseConstants.KEY_CONCERTS:
                        setAllSharedFields(eventDocumentSnapshot,"concertImageUrl", "NA",
                                "concertLocation", "concertDate");
                        setConditionalBandTextViews(eventDocumentSnapshot);
                        break;
                    case FirebaseConstants.KEY_GAMES:
                        setAllSharedFields(eventDocumentSnapshot, "gameImageUrl", "gameTitle",
                                "gameReleaseConsoles", "gameReleaseDate");
                        break;
                    case FirebaseConstants.KEY_MOVIES:
                        setAllSharedFields(eventDocumentSnapshot, "movieImageUrl", "movieTitle",
                                "movieRating", "movieReleaseDate");
                        optionalSecondSubtitleTextView .setVisibility(View.VISIBLE);
                        optionalSecondSubtitleTextView .setText(eventDocumentSnapshot.getString("movieGenre"));
                        optionalCheckbox.setVisibility(View.VISIBLE);
                        optionalCheckbox.setText("Seen");
                        setCheckmarkFunctionality(eventDocumentSnapshot.getId());
                        break;
                }
            }
        });
    }

    public void setAllSharedFields(DocumentSnapshot eventDocumentSnapshot, String imageUrl,
                                   String titleText, String secondEventInfoText, String thirdEventInfoText) {
        Picasso.get().load(eventDocumentSnapshot.getString(imageUrl)).error(R.drawable.ic_concerts_blue).into(eventPictureImageView);
        titleTextView.setText(eventDocumentSnapshot.getString(titleText));
        secondEventInfoTextView.setText(eventDocumentSnapshot.getString(secondEventInfoText));
        thirdEventInfoTextView.setText(eventDocumentSnapshot.getString(thirdEventInfoText));
    }

    public void setConditionalBandTextViews(DocumentSnapshot eventDocumentSnapshot) {
        List<String> listOfBandsAtConcert = (List<String>) eventDocumentSnapshot.get("concertBandsArray");
        titleTextView.setText(listOfBandsAtConcert.get(0));
        if (listOfBandsAtConcert.size() > 1) {
            subtitleTextView.setVisibility(View.VISIBLE);
            subtitleTextView.setText(listOfBandsAtConcert.get(1));
            if (listOfBandsAtConcert.size() > 2) {
                List<String> remainingBandsList = listOfBandsAtConcert.subList(2, listOfBandsAtConcert.size());
                String remainingBandsDisplayText = TextUtils.join(", ", remainingBandsList);
                optionalSecondSubtitleTextView.setVisibility(View.VISIBLE);
                optionalSecondSubtitleTextView.setText(remainingBandsDisplayText);
            }
        }
    }

    public void setCheckmarkFunctionality(final String movieId) {
        Task<DocumentSnapshot> task = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS)
                .document(UserManager.CURRENT_USER).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentReference currentUserDocument = task.getResult().getReference();
                final List<String> allMoviesSeen = (List<String>) task.getResult().get(FirebaseConstants.KEY_MOVIES_SEEN);
                boolean movieHasBeenSeen = allMoviesSeen.contains(movieId);
                optionalCheckbox.setChecked(movieHasBeenSeen);
                optionalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            allMoviesSeen.add(movieId);
                            currentUserDocument.update(FirebaseConstants.KEY_MOVIES_SEEN, allMoviesSeen);
                        } else {
                            allMoviesSeen.remove(movieId);
                            currentUserDocument.update(FirebaseConstants.KEY_MOVIES_SEEN, allMoviesSeen);
                        }
                    }
                });
            }
        });
    }
}
