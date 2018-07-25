package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendSelectorFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewWithHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileViewActivity extends AppCompatActivity {
    String currentUserId;
    DocumentReference currentUserReference;
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
                displayNameTextView.setText(currentUserDocumentSnapshot.get("displayName").toString());
                try {
                    Picasso.get().load(currentUserDocumentSnapshot.get("profilePhotoURL").toString()).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                } catch (NullPointerException e) {
                }

            }
        });
    }

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName("Matthew").build();
//        user.updateProfile(profileUpdates);
//        String userName = user.getUid();
//        Firestore.collection("users").document(userName).set(user);

//        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();


    private void updateUserDisplayName() {
        String updatedDisplayName = displayNameTextView.getText().toString().trim();
        Firestore.collection("users").document(currentUserId).update("displayName", updatedDisplayName);
        Firestore.collection("usersAuth").document(currentUserId).update("displayName", updatedDisplayName);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(updatedDisplayName).build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
    }
}
