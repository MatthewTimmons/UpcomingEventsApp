package com.matthewtimmons.upcomingeventsapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendInfoListAdapter;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendInfoFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.InterestLevelSeekbarFragment;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.EventDetailsFragment;

import java.util.List;

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

        Fragment eventDetailsFragment = null;
        if (eventType.equals(EventConstants.EVENT_TYPE_CONCERT)) {
            eventDetailsFragment = EventDetailsFragment.newInstance(eventId, FirebaseConstants.KEY_CONCERTS);
        } else if (eventType.equals(EventConstants.EVENT_TYPE_GAME)) {
            eventDetailsFragment = EventDetailsFragment.newInstance(eventId, FirebaseConstants.KEY_GAMES);
        } else if (eventType.equals(EventConstants.EVENT_TYPE_MOVIE)) {
            eventDetailsFragment = EventDetailsFragment.newInstance(eventId, FirebaseConstants.KEY_MOVIES);
        }

        Fragment interestLevelSeekbarFragment = InterestLevelSeekbarFragment.newInstance(eventType, eventId);
        Fragment friendRecyclerViewFragment = FriendInfoFragment.newInstance(eventType, eventId);

        getSupportFragmentManager().beginTransaction().add(R.id.interest_level_container, interestLevelSeekbarFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_friend_recycler_view, friendRecyclerViewFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventDetailsFragment).commit();
    }
}
