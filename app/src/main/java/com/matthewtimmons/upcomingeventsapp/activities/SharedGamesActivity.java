package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.ListOfUsersFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.SharedGamesFragment;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.ArrayList;

public class SharedGamesActivity extends AppCompatActivity {
    String currentUserId;
    String firstUserId;
    String secondUserId;
    Button nextButton;
    Button backButton;
    ArrayList<String> friendsChecked = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_selector);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);

        currentUserId = getIntent().getStringExtra(User.CURRENT_USER_ID);

        final Fragment friendsSelector = ListOfUsersFragment.newInstance(currentUserId, friendsChecked);
        getSupportFragmentManager().beginTransaction().add(R.id.friend_selector_fragment_container, friendsSelector).commit();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendsChecked.size() == 2) {
                    backButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.GONE);
                    firstUserId = friendsChecked.get(0);
                    secondUserId = friendsChecked.get(1);
                    Fragment newFragment = SharedGamesFragment.newInstance(firstUserId, secondUserId);
                    getSupportFragmentManager().beginTransaction().replace(R.id.friend_selector_fragment_container, newFragment).commit();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                friendsChecked.clear();
                Fragment newFragment = ListOfUsersFragment.newInstance(currentUserId, friendsChecked);
                getSupportFragmentManager().beginTransaction().replace(R.id.friend_selector_fragment_container, newFragment).commit();
            }
        });
    }
}