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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.FriendSelectorFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.RecyclerViewWithHeaderFragment;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileViewActivity extends AppCompatActivity {
    private StorageReference uploadsStorageReference;
    int PICK_IMAGE_REQUEST = 1;
    FirebaseUser currentUser;
    String profileUserId;
    Uri imageUri;
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
        favoritesRecyclerView = findViewById(R.id.profile_favorite_events_recycler_view);

        // Get profile data
        profileUserId = getIntent().getStringExtra(User.CURRENT_USER_ID);
        profileUserReference = User.getUserReference(profileUserId);

        // Get signed in user data
        currentUser = User.getCurrentUser();
        isCurrentUserViewer = currentUser.getUid().equals(profileUserId);
        uploadsStorageReference = FirebaseConstants.getStorageReference("uploads");

        Fragment friendInfoFragment = FriendSelectorFragment.newInstance(new ArrayList<String>(), profileUserId, false, true);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_friends_recycler_view_container, friendInfoFragment).commit();

        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserHelper.setFavoritesRecyclerViewAdapter(profileUserReference, favoritesRecyclerView, true);

        RecyclerViewWithHeaderFragment recyclerViewWithHeaderFragment = RecyclerViewWithHeaderFragment.newInstance(profileUserId);
        getSupportFragmentManager().beginTransaction().add(R.id.profile_favorite_events_recycler_view_container, recyclerViewWithHeaderFragment).commit();


        profileUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot currentUserDocumentSnapshot = task.getResult();
                displayNameTextView.setText(currentUserDocumentSnapshot.getString("displayName"));
                try {
                    Picasso.get().load(currentUserDocumentSnapshot.getString("profilePhotoURL")).error(R.drawable.ic_default_profile_photo).into(profilePhotoImageView);
                } catch (NullPointerException e) { }

            }
        });

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
                            User.updateFirebaseUserDisplayName(currentUser, updatedDisplayName);

                            // Update user's display name in database
                            User.updateDisplayName(profileUserId, updatedDisplayName);
                            User.updateAuthDisplayName(profileUserId, updatedDisplayName);

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
}
