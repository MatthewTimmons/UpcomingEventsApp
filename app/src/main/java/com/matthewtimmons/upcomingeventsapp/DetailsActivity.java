package com.matthewtimmons.upcomingeventsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.ConcertDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.GameDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.MovieDetailsFragment;

import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_EVENT_ID = "extraEventId";
    private static final String EXTRA_EVENT_TYPE = "extraEventType";
    public static final int INTEREST_LEVEL_LOW = 0;
    public static final int INTEREST_LEVEL_MEDIUM = 1;
    public static final int INTEREST_LEVEL_HIGH = 2;

    SeekBar seekBar;
    TextView interestLevelMessage;

    String eventId;
    String eventType;

    //TODO: Fix later
    public static final String MY_NAME = "Matt";

    public static Intent newIntent(Context context, String eventId, String eventType) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        intent.putExtra(EXTRA_EVENT_TYPE, eventType);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent() != null) {
           eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
           eventType = getIntent().getStringExtra(EXTRA_EVENT_TYPE);
        }


        Fragment fragment = null;
        if (eventType.equals(EventConstants.EVENT_TYPE_CONCERT)) {
            fragment = ConcertDetailsFragment.newInstance(eventId);
        } else if (eventType.equals(EventConstants.EVENT_TYPE_GAME)) {
            fragment = GameDetailsFragment.newInstance(eventId);
        } else if (eventType.equals(EventConstants.EVENT_TYPE_MOVIE)) {
            fragment = MovieDetailsFragment.newInstance(eventId);
        }

        // TODO: pull in the Recycler view that contains freind's information
//        RecyclerView recyclerView = findViewById(R.id.friends_event_information);
//        pagerAdatper =
//        recyclerView.setAdapter();
//        viewPager = findViewById(R.id.pager);
//        pagerAdapter = new EventPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);


        getSupportFragmentManager().beginTransaction().replace(R.id.replacable, fragment).commit();

        // Set seekbar functionality
        seekBar = findViewById(R.id.slider_bar);
        interestLevelMessage = findViewById(R.id.interest_level);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public static final String TAG = "BOOOOM";

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateText(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                interestLevelMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                interestLevelMessage.setVisibility(View.GONE);
                if (eventId != null) {
                    String eventTypeFirebaseKey = null;
                    switch (eventType) {
                        case EventConstants.EVENT_TYPE_CONCERT:
                            eventTypeFirebaseKey = FirebaseConstants.KEY_CONCERTS;
                            break;
                        case EventConstants.EVENT_TYPE_GAME:
                            eventTypeFirebaseKey = FirebaseConstants.KEY_GAMES;
                            break;
                        case EventConstants.EVENT_TYPE_MOVIE:
                            eventTypeFirebaseKey = FirebaseConstants.KEY_MOVIES;
                            break;
                    }
                    CollectionReference userCollectionReference = FirebaseFirestore.getInstance().collection("users");
                    updateEventInterestLevelForUser(eventId, seekBar.getProgress(), eventTypeFirebaseKey, userCollectionReference);
                } else {
                    Toast.makeText(DetailsActivity.this, R.string.error_event_id_not_found, Toast.LENGTH_SHORT).show();
                }
            }

            private void updateEventInterestLevelForUser(final String eventId,
                                                         final Number newInterestLevel,
                                                         final String eventTypeKey,
                                                         CollectionReference userCollectionReference) {

                userCollectionReference.document(MY_NAME).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        task.getResult().getReference().update(FieldPath.of(FirebaseConstants.KEY_INTEREST_LEVELS_USER, eventTypeKey, eventId), newInterestLevel);
                    }
                });
            }
        });
    }

    public void updateText(int i) {
        switch(i) {
            case INTEREST_LEVEL_LOW:
                interestLevelMessage.setText(R.string.interest_level_display_name_low);
                interestLevelMessage.setBackgroundColor(Color.parseColor("#f44242"));
                break;
            case INTEREST_LEVEL_MEDIUM:
                interestLevelMessage.setText(R.string.interest_level_display_name_medium);
                interestLevelMessage.setBackgroundColor(Color.parseColor("#f4f142"));
                break;
            case INTEREST_LEVEL_HIGH:
                interestLevelMessage.setText(R.string.interest_level_display_name_high);
                interestLevelMessage.setBackgroundColor(Color.parseColor("#6ef442"));
                break;
        }
    }

    public void setSeekbarToCurrentInterestLevel(final Context context, final String eventType, final String eventId) {
            FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try {
                        DocumentSnapshot userDocumentSnapshot = task.getResult().getDocuments().get(1);
                        Map<String, Object> interestLevels = (Map<String, Object>) userDocumentSnapshot.get(FirebaseConstants.KEY_INTEREST_LEVELS_USER);
                        Map<String, Object> events = (Map<String, Object>) interestLevels.get(eventType);
                        Integer interestLevelValue = (Integer) events.get(eventId);
                        seekBar.setProgress(interestLevelValue);
                        // TODO: Remove debugging code below
                        Toast.makeText(context, interestLevelValue.toString(), Toast.LENGTH_SHORT).show();
                    } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
