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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.squareup.picasso.Picasso;

import java.util.List;


public class EventDetailsFragment extends Fragment {
    private static final String ARGS_CONCERT_ID = "ARGS_CONCERT_ID";
    private static final String ARGS_EVENT_TYPE = "ARGS_EVENT_TYPE";
    String currentUserId;
    String eventId;
    String eventKey;
    ImageView eventPictureImageView;
    TextView titleTextView;
    TextView subtitleTextView;
    TextView optionalSecondSubtitleTextView;
    TextView fourthTextView;
    TextView fifthTextView;
    CheckBox optionalCheckbox;
    CheckBox favoritesCheckbox;


    public static EventDetailsFragment newInstance(String eventId, String eventKey) {
        EventDetailsFragment instance = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_CONCERT_ID, eventId);
        args.putString(ARGS_EVENT_TYPE, eventKey);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details_event, container, false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString(ARGS_CONCERT_ID);
            eventKey = bundle.getString(ARGS_EVENT_TYPE);
        }

        // Assign all views to variables
        eventPictureImageView = view.findViewById(R.id.wide_image);
        titleTextView =view.findViewById(R.id.title);
        subtitleTextView =view.findViewById(R.id.optional_subtitle);
        optionalSecondSubtitleTextView = view.findViewById(R.id.third_info_field);
        fourthTextView = view.findViewById(R.id.fourth_info_field);
        fifthTextView = view.findViewById(R.id.fifth_info_field);
        optionalCheckbox = view.findViewById(R.id.optional_checkbox);
        favoritesCheckbox = view.findViewById(R.id.favorite_checkbox);

        CollectionReference eventsCollectionReference = Firestore.collection(eventKey);
        eventsCollectionReference.document(eventId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot eventDocumentSnapshot = task.getResult();

                // Assign values to each view
                switch (eventKey) {
                    case FirebaseConstants.COLLECTION_CONCERTS:
                        setAllSharedFields(eventDocumentSnapshot, R.drawable.ic_concerts_blue,"concertLocation");
                        setConditionalBandTextViews(eventDocumentSnapshot);
                        break;
                    case FirebaseConstants.COLLECTION_GAMES:
                        String releaseConsolesAsString = Game.fetchGamesAsString(eventDocumentSnapshot);
                        setAllSharedFields(eventDocumentSnapshot, R.drawable.ic_games_blue, "NA");
                        fourthTextView.setText(releaseConsolesAsString);
                        optionalCheckbox.setVisibility(View.VISIBLE);
                        optionalCheckbox.setText("Owned");
                        setCheckmarkFunctionality(eventDocumentSnapshot.getId(), FieldPath.of(FirebaseConstants.KEY_GAMES_OWNED), optionalCheckbox, false);
                        break;
                    case FirebaseConstants.COLLECTION_MOVIES:
                        setAllSharedFields(eventDocumentSnapshot, R.drawable.ic_movies_blue,"movieGenre");
                        optionalSecondSubtitleTextView.setVisibility(View.VISIBLE);
                        String formattedRating = getResources()
                                .getString(R.string.formatted_rating, eventDocumentSnapshot.getString(FirebaseConstants.KEY_MOVIE_RATING));
                        optionalSecondSubtitleTextView.setText(formattedRating);
                        optionalCheckbox.setVisibility(View.VISIBLE);
                        optionalCheckbox.setText("Seen");
                        setCheckmarkFunctionality(eventDocumentSnapshot.getId(), FieldPath.of(FirebaseConstants.KEY_MOVIES_SEEN), optionalCheckbox, false);
                        break;
                }
                setCheckmarkFunctionality(eventId, FieldPath.of("myFavorites", eventKey), favoritesCheckbox, true);
            }
        });
    }

    public void setAllSharedFields(DocumentSnapshot eventDocumentSnapshot, int backupImageId,
                                   String secondEventInfoText) {
        Picasso.get().load(eventDocumentSnapshot.getString(FirebaseConstants.KEY_IMAGE_URL)).error(backupImageId).into(eventPictureImageView);
        titleTextView.setText(eventDocumentSnapshot.getString(FirebaseConstants.KEY_TITLE));
        fourthTextView.setText(eventDocumentSnapshot.getString(secondEventInfoText));
        fifthTextView.setText(eventDocumentSnapshot.getString(FirebaseConstants.KEY_DATE));
    }

    public void setConditionalBandTextViews(DocumentSnapshot eventDocumentSnapshot) {
        List<String> listOfBandsAtConcert = (List<String>) eventDocumentSnapshot.get(FirebaseConstants.KEY_CONCERT_BANDS_ARRAY);
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

    public void setCheckmarkFunctionality(final String eventId, final FieldPath fieldpathToArray, final CheckBox checkBox, final boolean toggleImage) {
        Task<DocumentSnapshot> task = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS)
                .document(currentUserId).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentReference currentUserDocument = task.getResult().getReference();
                final List<String> arrayOfAllInstances = (List<String>) task.getResult().get(fieldpathToArray);
                boolean existsInArray = arrayOfAllInstances.contains(eventId);
                checkBox.setChecked(existsInArray);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            arrayOfAllInstances.add(eventId);
                            currentUserDocument.update(fieldpathToArray, arrayOfAllInstances);
                            if (toggleImage) { compoundButton.setButtonDrawable(R.drawable.ic_star); }
                        } else {
                            arrayOfAllInstances.remove(eventId);
                            currentUserDocument.update(fieldpathToArray, arrayOfAllInstances);
                            if (toggleImage) { compoundButton.setButtonDrawable(R.drawable.ic_hollow_star); }
                        }
                    }
                });
                // TODO Clean this patch up in method
                if (toggleImage) {
                    if (checkBox.isChecked()) { checkBox.setButtonDrawable(R.drawable.ic_star); }
                    else { checkBox.setButtonDrawable(R.drawable.ic_hollow_star); }
                }
            }
        });
    }
}
