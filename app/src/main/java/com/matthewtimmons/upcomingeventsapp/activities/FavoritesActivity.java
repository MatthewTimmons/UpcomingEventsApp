package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.manager.DevHelper;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

public class FavoritesActivity extends AppCompatActivity {
    private User currentUser;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        currentUser = UserManager.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DevHelper.setFavoritesRecyclerViewAdapter(currentUser, recyclerView, false);
    }
}