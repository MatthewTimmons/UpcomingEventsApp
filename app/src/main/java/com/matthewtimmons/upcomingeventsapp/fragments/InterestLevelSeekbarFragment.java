package com.matthewtimmons.upcomingeventsapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;

import java.lang.reflect.Type;
import java.util.Map;

import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.WHITE;

public class InterestLevelSeekbarFragment extends Fragment {
    private static final long DELAY_SEEKBAR_HINT_MS = 1000L;
    Handler seekbarHandler = new Handler();
    String currentUserId, eventId, eventType;
    TextView interestLevelTextView;
    SeekBar interestLevelSeekbar;

    private static final String EVENT_TYPE = "eventType";
    public static final String EVENT_ID = "eventId";
    public static final int INTEREST_LEVEL_LOW = 0;
    public static final int INTEREST_LEVEL_MEDIUM = 1;
    public static final int INTEREST_LEVEL_HIGH = 2;

    public static InterestLevelSeekbarFragment newInstance(String eventType, String eventId) {
        InterestLevelSeekbarFragment interestLevelSeekbarFragment = new InterestLevelSeekbarFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_ID, eventId);
        bundle.putString(EVENT_TYPE, eventType);
        interestLevelSeekbarFragment.setArguments(bundle);
        return interestLevelSeekbarFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_interest_level, container, false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        interestLevelSeekbar = view.findViewById(R.id.slider_bar);
        interestLevelTextView = view.findViewById(R.id.interest_level);
        eventId = getArguments().getString(EVENT_ID);
        eventType = getArguments().getString(EVENT_TYPE);

        setSeekbarToCurrentInterestLevel(eventType, eventId);

        interestLevelSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateText(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekbarHandler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                sendDataAndResetView(seekBar);
            }
        });
    }



        public void updateText(int i) {
            switch(i) {
                case INTEREST_LEVEL_LOW:
                    interestLevelTextView.setText(R.string.interest_level_display_name_low);
                    interestLevelTextView.setBackgroundColor(Color.parseColor("#f44242"));
                    break;
                case INTEREST_LEVEL_MEDIUM:
                    interestLevelTextView.setText(R.string.interest_level_display_name_medium);
                    interestLevelTextView.setBackgroundColor(Color.parseColor("#ffcc00"));
                    break;
                case INTEREST_LEVEL_HIGH:
                    interestLevelTextView.setText(R.string.interest_level_display_name_high);
                    interestLevelTextView.setBackgroundColor(Color.parseColor("#6ef442"));
                    break;
            }
        }

        public void sendDataAndResetView(final SeekBar seekBar) {
            seekbarHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (eventId != null) {
                        String eventTypeFirebaseKey = null;
                        switch (eventType) {
                            case EventConstants.EVENT_TYPE_CONCERT:
                                eventTypeFirebaseKey = FirebaseConstants.COLLECTION_CONCERTS;
                                break;
                            case EventConstants.EVENT_TYPE_GAME:
                                eventTypeFirebaseKey = FirebaseConstants.COLLECTION_GAMES;
                                break;
                            case EventConstants.EVENT_TYPE_MOVIE:
                                eventTypeFirebaseKey = FirebaseConstants.COLLECTION_MOVIES;
                                break;
                        }
                        CollectionReference userCollectionReference = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS);
                        updateEventInterestLevelForUser(eventId, seekBar.getProgress(), eventTypeFirebaseKey, userCollectionReference);
                    } else {
                        Toast.makeText(getContext(), R.string.error_event_id_not_found, Toast.LENGTH_SHORT).show();
                    }
                    resetFontAndColorForTextView();
                }
            }, DELAY_SEEKBAR_HINT_MS);
        }

    private void updateEventInterestLevelForUser(final String eventId,
                                                 final Number newInterestLevel,
                                                 final String eventTypeKey,
                                                 CollectionReference userCollectionReference) {

        userCollectionReference.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                task.getResult().getReference().update(FieldPath.of(FirebaseConstants.KEY_INTEREST_LEVELS_USER, eventTypeKey, eventId), newInterestLevel);
            }
        });
    }

        public void setSeekbarToCurrentInterestLevel(final String eventType, final String eventId) {
            FirebaseFirestore.getInstance().document("users/" + currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        // TODO: Change this get(2) to get the current user
                        DocumentSnapshot userDocumentSnapshot = task.getResult();
                        Map<String, Object> interestLevels = (Map<String, Object>) userDocumentSnapshot.get(FirebaseConstants.KEY_INTEREST_LEVELS_USER);
                        Map<String, Object> events = (Map<String, Object>) interestLevels.get(eventType);
                        Integer interestLevelValue = ((Long) events.get(eventId)).intValue();
                        interestLevelSeekbar.setProgress(interestLevelValue);
                        if (interestLevelValue.equals(0)) { interestLevelTextView.setText(R.string.interest_level_display_name_low); }
                        else if (interestLevelValue.equals(1)) { interestLevelTextView.setText(R.string.interest_level_display_name_medium); }
                        else { interestLevelTextView.setText(R.string.interest_level_display_name_high); }
                        resetFontAndColorForTextView();
                    } catch(NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void resetFontAndColorForTextView() {
            interestLevelTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
}
