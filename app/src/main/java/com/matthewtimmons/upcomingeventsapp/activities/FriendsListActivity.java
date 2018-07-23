package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendInfoListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;

import java.util.ArrayList;
import java.util.List;

import static com.matthewtimmons.upcomingeventsapp.manager.UserHelper.CURRENT_USER;

public class FriendsListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        recyclerView = findViewById(R.id.list_of_friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).document(CURRENT_USER).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> allFriendIds = (ArrayList<String>) task.getResult().get("friends");
                        List<DocumentSnapshot> friends = UserHelper.fetchFilteredUsersList(allUsers, allFriendIds);

                        FriendListAdapter recyclerAdapter = new FriendListAdapter(friends);
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                });
            }
        });
    }
}
