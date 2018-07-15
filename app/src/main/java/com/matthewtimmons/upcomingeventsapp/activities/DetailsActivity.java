package com.matthewtimmons.upcomingeventsapp.activities;

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
import com.matthewtimmons.upcomingeventsapp.InterestLevelSeekbar;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.ConcertDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.GameDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.MovieDetailsFragment;

import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_EVENT_ID = "extraEventId";
    private static final String EXTRA_EVENT_TYPE = "extraEventType";

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

        Fragment fragment1 = InterestLevelSeekbar.newInstance(eventId, eventType);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.interest_level_container, fragment1).commit();
    }
}
