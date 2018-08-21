package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.ListOfUsersFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

import java.util.Map;


public class FriendsListActivity extends AppCompatActivity {
    String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        currentUserId = UserManager.getInstance().getCurrentUserId();

        // Set friends header and recycler view
        Fragment listOfUsersHeader = RecyclerViewHeaderFragment.newInstance(currentUserId, "Friends");
        ListOfUsersFragment friendsFragment = ListOfUsersFragment.newInstance(currentUserId, "Grid");
        getSupportFragmentManager().beginTransaction().add(R.id.friends_header, listOfUsersHeader).add(R.id.friends_header, friendsFragment).commit();

        // Set pending friend requests as visible if they exist
        Map<String, Object> pendingFriendRequests = UserManager.getInstance().getCurrentUser().getPendingFriendRequests();
        if (pendingFriendRequests != null && pendingFriendRequests.size() > 0) {
            // Set pending friend requests header and recycler view
            Fragment header = RecyclerViewHeaderFragment.newInstance(currentUserId, "Pending Friend Requests");
            ListOfUsersFragment pendingRequests = ListOfUsersFragment.newInstance(currentUserId, "pendingFriendRequests");
            getSupportFragmentManager().beginTransaction().add(R.id.pending_requests_header, header).add(R.id.pending_requests_header, pendingRequests).commit();
        }
    }
}
