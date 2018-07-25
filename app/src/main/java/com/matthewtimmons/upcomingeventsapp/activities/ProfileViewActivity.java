package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
    String profileUserId;
    DocumentReference profileUserReference;
    RecyclerView favoritesRecyclerView;
    ImageView profilePhotoImageView;
    TextView displayNameTextView;
    boolean isCurrentUserViewer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        profilePhotoImageView = findViewById(R.id.profile_photo);
        displayNameTextView = findViewById(R.id.display_name);

        profileUserId = getIntent().getStringExtra("CURRENT_USER");
        profileUserReference = FirebaseFirestore.getInstance().document(FirebaseConstants.COLLECTION_USERS + "/" + profileUserId);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        isCurrentUserViewer = currentUser.getUid().equals(profileUserId);

//        TODO
//        Set profile photo
//        Set display name
//        Set Friends recycler view
//        Set Favorites recycler view

        Fragment friendInfoFragment = FriendSelectorFragment.newInstance(new ArrayList<String>(), profileUserId, false, true);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_friends_recycler_view_container, friendInfoFragment).commit();


        favoritesRecyclerView = findViewById(R.id.profile_favorite_events_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserHelper.setFavoritesRecyclerViewAdapter(profileUserReference, favoritesRecyclerView, true);

        RecyclerViewWithHeaderFragment recyclerViewWithHeaderFragment = RecyclerViewWithHeaderFragment.newInstance(profileUserId);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_favorite_events_recycler_view_container, recyclerViewWithHeaderFragment).commit();


        profileUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot currentUserDocumentSnapshot = task.getResult();
                displayNameTextView.setText(currentUserDocumentSnapshot.get("displayName").toString());
                try {
                    Picasso.get().load(currentUserDocumentSnapshot.get("profilePhotoURL").toString()).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                } catch (NullPointerException e) { }

            }
        });

        displayNameTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isCurrentUserViewer) {
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
                    TextView titleMessageTextView = dialogView.findViewById(R.id.question_confirmation);
                    final EditText userInputEditText = dialogView.findViewById(R.id.user_input_edittext);
                    Button cancelButton = dialogView.findViewById(R.id.cancel_button);
                    Button confirmButton = dialogView.findViewById(R.id.confirm_button);

                    userInputEditText.setVisibility(View.VISIBLE);
                    titleMessageTextView.setText("Change your display name:");

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileViewActivity.this);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();
                        }
                    });

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Get updated name
                            String updatedDisplayName = userInputEditText.getText().toString().trim();
                            displayNameTextView.setText(updatedDisplayName);

                            // Update FirebaseAuth account's display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(updatedDisplayName).build();
                            currentUser.updateProfile(profileUpdates);

                            // Update user's display name in database
                            Firestore.collection("users").document(profileUserId).update("displayName", updatedDisplayName);
                            Firestore.collection("usersAuth").document(profileUserId).update("displayName", updatedDisplayName);

                            Toast.makeText(ProfileViewActivity.this, "Display name has been changed to \"" + updatedDisplayName + "\"", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    });
                } else {
                    Toast.makeText(ProfileViewActivity.this, "You do not have permission to change someone else's display name", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}
