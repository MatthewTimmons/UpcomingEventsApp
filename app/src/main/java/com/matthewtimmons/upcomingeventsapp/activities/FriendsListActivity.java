package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.ListOfUsersFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.List;
import java.util.Map;


public class FriendsListActivity extends AppCompatActivity {
    String currentUserId;
    Fragment pendingFriendRequestFragment;
    RecyclerView pendingFriendRequestsRecyclerView;
    Fragment friendsFragment;
    RecyclerView friendsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        currentUserId = getIntent().getStringExtra(User.CURRENT_USER_ID);

        // Set friends header and recycler view
        Fragment listOfUsersHeader = RecyclerViewHeaderFragment.newInstance(currentUserId, "Friends");
        ListOfUsersFragment friendsFragment = ListOfUsersFragment.newInstance(currentUserId, "Grid");
        getSupportFragmentManager().beginTransaction().add(R.id.friends_header, listOfUsersHeader).add(R.id.friends_header, friendsFragment).commit();

        Firestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Boolean> pendingFriendRequests = (Map<String, Boolean>) task.getResult().get("pendingFriendRequests");
                if (pendingFriendRequests != null && pendingFriendRequests.size() > 0) {
                    // Set pending friend requests header and recycler view
                    Fragment header = RecyclerViewHeaderFragment.newInstance(currentUserId, "Pending Friend Requests");
                    ListOfUsersFragment pendingRequests = ListOfUsersFragment.newInstance(currentUserId, "pendingFriendRequests");
                    getSupportFragmentManager().beginTransaction().add(R.id.pending_requests_header, header).add(R.id.pending_requests_header, pendingRequests).commit();
                }
            }
        });
    }
}
