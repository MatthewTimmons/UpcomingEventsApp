package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendSelectorFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.SharedGamesFragment;

import java.util.ArrayList;
import java.util.List;

public class SharedGamesActivity extends AppCompatActivity {
    String currentUser;
    ArrayList<String> friendsChecked = new ArrayList<>();
    Button nextButton;
    Button backButton;

    private DocumentReference currentUserReference = FirebaseFirestore.getInstance().document("users/Matt");

    // TODO Get these values from the checked items
    public static String currentUserId = "Matt";
    public static String friendUserId = "Ryan";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_selector);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);

        currentUser = getIntent().getStringExtra("CURRENT_USER");

        FriendSelectorFragment fragment = FriendSelectorFragment.newInstance(friendsChecked, "Matt", true, false);
        getSupportFragmentManager().beginTransaction().add(R.id.friend_selector_fragment_container, fragment).commit();


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUserId = friendsChecked.get(0);
                friendUserId = friendsChecked.get(1);
                Fragment newFragment = SharedGamesFragment.newInstance(currentUserId, friendUserId, friendsChecked);
                getSupportFragmentManager().beginTransaction().replace(R.id.friend_selector_fragment_container, newFragment).commit();
                backButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsChecked.clear();
                Fragment newFragment = FriendSelectorFragment.newInstance(friendsChecked, currentUser, true, false);
                getSupportFragmentManager().beginTransaction().replace(R.id.friend_selector_fragment_container, newFragment).commit();
                backButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
            }
        });


    }

}