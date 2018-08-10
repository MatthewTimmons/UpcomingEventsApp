package com.matthewtimmons.upcomingeventsapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendInfoFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.InterestLevelSeekbarFragment;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.EventDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.models.User;

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_EVENT_ID = "extraEventId";
    private static final String EXTRA_EVENT_TYPE = "extraEventType";
    private static final String EXTRA_IS_CUSTOM_EVENT = "EXTRA_IS_CUSTOM_EVENT";

    String eventId;
    String eventType;
    Boolean isCustomEvent;

    public static Intent newIntent(Context context, String eventId, String eventType, Boolean isCustomEvent) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        intent.putExtra(EXTRA_EVENT_TYPE, eventType);
        intent.putExtra(EXTRA_IS_CUSTOM_EVENT, isCustomEvent);
        return intent;
    }

    public static Intent newIntent(Context context, String eventId, String eventType) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        intent.putExtra(EXTRA_EVENT_TYPE, eventType);
        intent.putExtra(EXTRA_IS_CUSTOM_EVENT, false);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent() != null) {
           eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
           eventType = getIntent().getStringExtra(EXTRA_EVENT_TYPE);
           isCustomEvent = getIntent().getBooleanExtra(EXTRA_IS_CUSTOM_EVENT, false);
        }

        Fragment eventDetailsFragment = null;
        if (!isCustomEvent) {
            eventDetailsFragment = EventDetailsFragment.newInstance(eventId, eventType);
        } else {
            eventDetailsFragment = EventDetailsFragment.newInstance(eventId, eventType, true);
        }


        Fragment interestLevelSeekbarFragment = InterestLevelSeekbarFragment.newInstance(eventType, eventId);
        Fragment friendRecyclerViewFragment = FriendInfoFragment.newInstance(eventType, eventId);

        getSupportFragmentManager().beginTransaction().add(R.id.interest_level_container, interestLevelSeekbarFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_friend_recycler_view, friendRecyclerViewFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventDetailsFragment).commit();
    }
}
