package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewWithHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends AppCompatActivity {
    public static final String CURRENT_USER_KEY = "Matt";
    RecyclerView recyclerView;
    ImageView profilePhotoImageView;
    TextView displayNameTextView;

    private DocumentReference currentUserReference = FirebaseFirestore.getInstance().document("users/Matt");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        profilePhotoImageView = findViewById(R.id.profile_photo);
        displayNameTextView = findViewById(R.id.display_name);

//        TODO
//        Set profile photo
//        Set display name
//        Set Favorites recycler view



        RecyclerViewWithHeaderFragment recyclerViewWithHeaderFragment = RecyclerViewWithHeaderFragment.newInstance(CURRENT_USER_KEY);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_favorite_events_recycler_view, recyclerViewWithHeaderFragment).commit();


        currentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot currentUserDocumentSnapshot = task.getResult();
                Picasso.get().load(currentUserDocumentSnapshot.getString("profilePhotoURL")).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                displayNameTextView.setText(currentUserDocumentSnapshot.getString("displayName"));
            }
        });





//        FirebaseConstants.COLLECTION_CONCERTS;

//        Fragment friendRecyclerViewFragment = FriendInfoFragment.newInstance(eventType, eventId);

    }
}
