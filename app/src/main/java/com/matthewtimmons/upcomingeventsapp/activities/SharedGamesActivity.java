package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendSelectorFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.SharedGamesFragment;

import java.util.ArrayList;

public class SharedGamesActivity extends AppCompatActivity {
    EventListAdapter eventListAdapter;
    RecyclerView recyclerView;
    Button nextButton;
    Button backButton;

    private DocumentReference currentUserReference = FirebaseFirestore.getInstance().document("users/Matt");

    public static final String currentUserId = "Matt";
    public static final String friendUserId = "Ryan";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_selector);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);

        FriendSelectorFragment fragment = new FriendSelectorFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.friend_selector_fragment_container, fragment).commit();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = SharedGamesFragment.newInstance(currentUserId, friendUserId);
                getSupportFragmentManager().beginTransaction().replace(R.id.friend_selector_fragment_container, newFragment).commit();
                backButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new FriendSelectorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.friend_selector_fragment_container, newFragment).commit();
                backButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
            }
        });


    }

}