package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendInfoListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.ArrayList;
import java.util.List;


public class FriendsListActivity extends AppCompatActivity {
    String currentUserId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        currentUserId = User.getCurrentUserId(FirebaseAuth.getInstance());

        // Find number of columns based on phone screen dimensions
        int numberOfColumns = UserHelper.calculateNumberOfColumns(this, 200);

        recyclerView = findViewById(R.id.list_of_friends_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        UserHelper.setFriendsListAdapter(recyclerView, currentUserId);
    }
}
