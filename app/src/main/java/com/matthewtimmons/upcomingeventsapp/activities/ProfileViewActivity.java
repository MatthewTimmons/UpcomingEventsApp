package com.matthewtimmons.upcomingeventsapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.fragments.ListOfUsersFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.manager.DevHelper;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ProfileViewActivity extends AppCompatActivity {
    private StorageReference uploadsStorageReference;
    int PICK_IMAGE_REQUEST = 1;
    FirebaseUser currentFirebaseUser;
    DocumentReference profileUserReference;
    User profileUserObject;
    List<String> allFriendIds;
    RecyclerView favoritesRecyclerView;
    TextView displayNameTextView;
    ImageView profilePhotoImageView, editIconImageView;
    Button sendFriendRequestButton;
    String currentUserId, profileUserId;
    boolean isCurrentUserViewer;
    Uri imageUri;
    User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_profile_view);
        profilePhotoImageView = findViewById(R.id.profile_photo);
        displayNameTextView = findViewById(R.id.display_name);
        editIconImageView = findViewById(R.id.edit_icon);
        favoritesRecyclerView = findViewById(R.id.profile_favorite_events_recycler_view);
        sendFriendRequestButton = findViewById(R.id.send_friend_request_button);

        // Get profile data
        profileUserId = getIntent().getStringExtra(User.CURRENT_USER_ID);
        profileUserReference = UserController.getUserReference(profileUserId);
        profileUserObject = getIntent().getParcelableExtra(User.CURRENT_USER_OBJECT);

        // Get signed in user data
        currentFirebaseUser = firebaseAuth.getCurrentUser();
        currentUserId = currentFirebaseUser.getUid();
        isCurrentUserViewer = currentUserId.equals(profileUserId);
        uploadsStorageReference = FirebaseConstants.getStorageReference("uploads");

        // Set Friends header and recycler view
        Fragment friends = ListOfUsersFragment.newInstance(profileUserId);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_friends_header_container, friends).commit();

        //Set Favorite Events header
        RecyclerViewHeaderFragment recyclerViewHeaderFragment = RecyclerViewHeaderFragment.newInstance(profileUserId, "Favorite Events");
        getSupportFragmentManager().beginTransaction().add(R.id.profile_favorite_events_recycler_view_container, recyclerViewHeaderFragment).commit();

        //Set Favorite Events Recycler View
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DevHelper.setFavoritesRecyclerViewAdapter(profileUserObject, favoritesRecyclerView, profileUserId, true);

        displayNameTextView.setText(profileUserObject.getDisplayName());
        try {
            Picasso.get().load(profileUserObject.getProfilePhotoURL()).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
        } catch (NullPointerException e) { }

        profilePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCurrentUserViewer) { openFileChooser(); }

                findViewById(R.id.confirm_photo_change_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageFile();
                    }
                });
            }
        });

        if (isCurrentUserViewer) {
            editIconImageView.setVisibility(View.VISIBLE);
            editIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                            UserController.updateFirebaseUserDisplayName(currentFirebaseUser, updatedDisplayName);

                            // Update user's display name in database
                            UserController.updateDisplayName(profileUserId, updatedDisplayName);
                            UserController.updateAuthDisplayName(profileUserId, updatedDisplayName);

                            Toast.makeText(ProfileViewActivity.this, "Display name has been changed to \"" + updatedDisplayName + "\"", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    });
                } else {
                    Toast.makeText(ProfileViewActivity.this, "You do not have permission to change someone else's display name", Toast.LENGTH_SHORT).show();
                }
                }
            });
        }

        // Set all rules for the button at the bottom of the screen when the current user is looking at another user's profile
        if(!isCurrentUserViewer) {
            sendFriendRequestButton.setVisibility(View.VISIBLE);
            UserController.getUser(currentUserId, new UserController.GetUserListener() {
                @Override
                public void onUserRetrieved(final User currentUserObject) {
                    allFriendIds = currentUserObject.getFriends();
                    setFriendRequestButtonFunctionality(currentUserObject);
                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImageFile() {
        if (imageUri != null) {
            final StorageReference fileStorageReference = uploadsStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileStorageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    profileUserReference.update("profilePhotoURL", url);
                                    findViewById(R.id.confirm_photo_change_button).setVisibility(View.GONE);
                                }
                            });
                            Toast.makeText(ProfileViewActivity.this, "Your profile photo has been uploaded and set.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).error(getResources().getDrawable(R.drawable.ic_default_profile_photo)).into(profilePhotoImageView);
            findViewById(R.id.confirm_photo_change_button).setVisibility(View.VISIBLE);
        }
    }

    // All friend request button functionality
    void setFriendRequestButtonFunctionality(User currentUserObject) {
        if (!allFriendIds.contains(profileUserId)) {
            if (currentUserObject.getPendingFriendRequests().containsKey(profileUserId)) {
                setAcceptFriendRequestFunctionality(currentUserObject);
            } else if (!profileUserObject.getPendingFriendRequests().containsKey(currentUserId)) {
                setSendFriendRequestFunctionality(currentUserObject);
            } else if (profileUserObject.getPendingFriendRequests().containsKey(currentUserId)) {
                sendFriendRequestButton.setText("Your friend request has been sent");
                //TODO Set Dialog to confirm the user wishes to revoke their friend request
            }
        } else {
            setRemoveFunctionality(currentUserObject);
        }
    }
    void setSendFriendRequestFunctionality(final User currentUserObject) {
        sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> profileUserFriends = profileUserObject.getPendingFriendRequests();
                profileUserFriends.put(currentUserId, false);
                profileUserReference.update("pendingFriendRequests", profileUserFriends);
                sendFriendRequestButton.setText("Your friend request has been sent");
                Toast.makeText(ProfileViewActivity.this, "Your friend request has been sent", Toast.LENGTH_SHORT).show();
                setFriendRequestButtonFunctionality(currentUserObject);
            }
        });
    }
    void setAcceptFriendRequestFunctionality(final User currentUserObject) {
        sendFriendRequestButton.setText("Accept friend request");
        sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> friends = currentUserObject.getFriends();
                Map<String, Object> pendingRequests = currentUserObject.getPendingFriendRequests();
                friends.add(profileUserId);
                pendingRequests.remove(profileUserId);
                UserController.getUserReference(currentUserId).update("friends", friends);
                UserController.getUserReference(currentUserId).update("pendingFriendRequests", pendingRequests);
                Toast.makeText(ProfileViewActivity.this, profileUserObject.getDisplayName() + " has been added to your friends list", Toast.LENGTH_SHORT).show();
                setFriendRequestButtonFunctionality(currentUserObject);
            }
        });
    }
    void setRemoveFunctionality(final User currentUserObject) {
        sendFriendRequestButton.setVisibility(View.VISIBLE);
        sendFriendRequestButton.setText("Remove friend");
        sendFriendRequestButton.setBackgroundColor(getResources().getColor(R.color.red));
        sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> friends = currentUserObject.getFriends();
                friends.remove(profileUserId);
                UserController.getUserReference(currentUserId).update("friends", friends);
                Toast.makeText(ProfileViewActivity.this, profileUserObject.getDisplayName() + " has been removed from your friends list", Toast.LENGTH_SHORT).show();
                setFriendRequestButtonFunctionality(currentUserObject);
            }
        });
    }
}
