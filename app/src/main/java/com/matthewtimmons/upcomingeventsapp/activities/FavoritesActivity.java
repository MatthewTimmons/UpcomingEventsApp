package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.manager.DevHelper;
import com.matthewtimmons.upcomingeventsapp.models.CurrentUserSingleton;
import com.matthewtimmons.upcomingeventsapp.models.User;

public class FavoritesActivity extends AppCompatActivity {
    private DocumentReference currentUserReference;
    private String currentUserId;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        currentUserId = CurrentUserSingleton.currentUserObject.getUserId();
        currentUserReference = UserController.getUserReference(currentUserId);

        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DevHelper.setFavoritesRecyclerViewAdapter(currentUserReference, recyclerView, false);
    }
}