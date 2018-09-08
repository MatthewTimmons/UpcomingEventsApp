package com.matthewtimmons.upcomingeventsapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendInfoFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.InterestLevelSeekbarFragment;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.EventDetailsFragment;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

// Details Activity is made of 3 fragments:
//  Details Fragment that contains the image and info for the event
//  Friends Info Fragment that contains a recycler view showing what interest level friends rate the event
//  Interest Level Seekbar Fragment that contains the seekbar for the current user to indicate their interest

public class DetailsActivity extends AppCompatActivity {
    private static final String EXTRA_EVENT_ID = "extraEventId";
    private static final String EXTRA_EVENT_TYPE = "extraEventType";
    private static final String EXTRA_EVENT_CREATOR = "EXTRA_EVENT_CREATOR";

    String currentUserId = UserManager.getInstance().getCurrentUserId();
    String eventId, eventType, eventCreator;

    Button deleteCustomEventButton;

    Fragment eventDetailsFragment, friendRecyclerViewFragment, interestLevelSeekbarFragment = null;

    public static Intent newIntent(Context context, String eventId, String eventType, String eventCreator) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        intent.putExtra(EXTRA_EVENT_TYPE, eventType);
        intent.putExtra(EXTRA_EVENT_CREATOR, eventCreator);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        deleteCustomEventButton = findViewById(R.id.delete_custom_event_button);

        if (getIntent() != null) {
           eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
           eventType = getIntent().getStringExtra(EXTRA_EVENT_TYPE);
           eventCreator = getIntent().getStringExtra(EXTRA_EVENT_CREATOR);
        }

        eventDetailsFragment = EventDetailsFragment.newInstance(eventId, eventType);
        friendRecyclerViewFragment = FriendInfoFragment.newInstance(eventType, eventId);
        interestLevelSeekbarFragment = InterestLevelSeekbarFragment.newInstance(eventType, eventId);
        if (eventCreator.equals(currentUserId)) setDeleteButtonFunctionality();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, eventDetailsFragment);
        fragmentTransaction.add(R.id.interest_level_container, interestLevelSeekbarFragment);
        fragmentTransaction.add(R.id.fragment_friend_recycler_view, friendRecyclerViewFragment);
        fragmentTransaction.commit();
    }

    public void setDeleteButtonFunctionality() {
        deleteCustomEventButton.setVisibility(View.VISIBLE);
        deleteCustomEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Extract this duplicate code into it's own Alert Dialog Builder
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DetailsActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
                Button confirmButton = dialogView.findViewById(R.id.confirm_button);
                Button cancelButton = dialogView.findViewById(R.id.cancel_button);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentReference currentCustomMovieRef = Firestore.collection(eventType).document(eventId);
                        currentCustomMovieRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailsActivity.this, "Custom event was deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
