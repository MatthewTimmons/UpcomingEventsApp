package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendInfoFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendSelectorFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewWithHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileViewActivity extends AppCompatActivity {
    String currentUserId;
    DocumentReference currentUserReference;
    RecyclerView friendsRecyclerView;
    RecyclerView favoritesRecyclerView;
    ImageView profilePhotoImageView;
    TextView displayNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        profilePhotoImageView = findViewById(R.id.profile_photo);
        displayNameTextView = findViewById(R.id.display_name);

        currentUserId = getIntent().getStringExtra("CURRENT_USER");
        currentUserReference = FirebaseFirestore.getInstance().document(FirebaseConstants.COLLECTION_USERS + "/" + currentUserId);

//        TODO
//        Set profile photo
//        Set display name
//        Set Friends recycler view
//        Set Favorites recycler view

        Fragment friendInfoFragment = FriendSelectorFragment.newInstance(new ArrayList<String>(), currentUserId, false, true);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_friends_recycler_view_container, friendInfoFragment).commit();


        favoritesRecyclerView = findViewById(R.id.profile_favorite_events_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserHelper.setFavoritesRecyclerViewAdapter(currentUserReference, favoritesRecyclerView, true);

        RecyclerViewWithHeaderFragment recyclerViewWithHeaderFragment = RecyclerViewWithHeaderFragment.newInstance(currentUserId);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_favorite_events_recycler_view_container, recyclerViewWithHeaderFragment).commit();


        currentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot currentUserDocumentSnapshot = task.getResult();
                Picasso.get().load(currentUserDocumentSnapshot.getString("profilePhotoURL")).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                displayNameTextView.setText(currentUserDocumentSnapshot.getString("displayName"));
            }
        });
    }
}
