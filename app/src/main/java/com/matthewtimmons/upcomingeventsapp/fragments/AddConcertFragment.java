package com.matthewtimmons.upcomingeventsapp.fragments;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.AddEventsActivity;
import com.matthewtimmons.upcomingeventsapp.adapters.CustomRemovableSpinnerAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.DateHelper;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddConcertFragment extends Fragment {
    TextView getSuggestionsTextView;
    ImageView posterImageView;
    EditText addBandNameEditText, concertLocationEditText;
    Button addBandNameButton, addToMyConcertsButton, addToAllConcertsButton;
    String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foresight_concerts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSuggestionsTextView = getActivity().findViewById(R.id.get_suggestions_button);
        posterImageView = getActivity().findViewById(R.id.poster_image_view);
        addBandNameButton = view.findViewById(R.id.add_band_name_button);
        addBandNameEditText = view.findViewById(R.id.add_band_name_edit_text);
        concertLocationEditText = view.findViewById(R.id.concert_location_edit_text);
        addToMyConcertsButton = getActivity().findViewById(R.id.add_to_my_events_button);
        addToAllConcertsButton = getActivity().findViewById(R.id.add_to_all_events_button);
        currentUserId = UserManager.getInstance().getCurrentUserId();

        getSuggestionsTextView.setVisibility(View.GONE);
        addToMyConcertsButton.setText("Add to my concerts");
        Picasso.get().load(R.drawable.ic_concerts_blue).into(posterImageView);

        final ArrayList<String> bandNames = new ArrayList<>();

        final CustomRemovableSpinnerAdapter customRemovableSpinnerAdapter = new CustomRemovableSpinnerAdapter(bandNames);
        RecyclerView recyclerView = view.findViewById(R.id.band_names_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(customRemovableSpinnerAdapter);

        addBandNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bandNameEntered = addBandNameEditText.getText().toString();
                addBandNameEditText.setText("");
                addBandNameEditText.requestFocus();
                bandNames.add(bandNameEntered);
                customRemovableSpinnerAdapter.notifyDataSetChanged();
            }
        });

        addToMyConcertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomConcert(currentUserId, bandNames);
            }
        });

        addToAllConcertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomConcert("recommendations", bandNames);
            }
        });
    }

    public void addCustomConcert(String eventCreator, ArrayList<String> bandNames) {
        if (!concertLocationEditText.getText().toString().equals("") &&
                !bandNames.isEmpty()) {
            if (AddEventsActivity.imageNeedsToBeUploaded) {
                uploadCustomImageFromPhone(eventCreator, bandNames);
            } else {
                uploadCustomConcert(eventCreator, bandNames);
            }

        } else {
            Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadCustomConcert(final String eventCreator, final ArrayList<String> bandNames) {
        final Map<String, Object> concertData = new HashMap<>();
        concertData.put("concertLocation", concertLocationEditText.getText().toString());
        concertData.put("date", DateHelper.dateFormatDatabaseFriendly.format(AddEventsActivity.dateEntered));
        concertData.put("eventType", "concerts");
        concertData.put("concertBandsArray", bandNames);
        concertData.put("eventCreator", eventCreator);
        if (AddEventsActivity.eventPosterUrl != null && !AddEventsActivity.eventPosterUrl.equals("")) {
            concertData.put("imageUrl", AddEventsActivity.eventPosterUrl);
        } else {
            concertData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
            Toast.makeText(getContext(), "No concert poster detected", Toast.LENGTH_SHORT).show();
        }
        Firestore.collection("concerts").add(concertData);
        String toastMessage = eventCreator.equals("recommendations") ? "recommended for global adoption" : "added to your list of concerts.";
        Toast.makeText(getContext(), "Concert has been " + toastMessage, Toast.LENGTH_SHORT).show();
    }

    //------------------------------Photo methods--------------------------------------------//

    private void uploadCustomImageFromPhone(final String eventCreator, final ArrayList<String> bandNames) {
        Uri imageUri = AddEventsActivity.imageUri;
            if (imageUri != null) {
                StorageReference uploadsStorageReference = FirebaseConstants.getStorageReference("uploads");
                final StorageReference fileStorageReference = uploadsStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                fileStorageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    AddEventsActivity.eventPosterUrl = url;

                                    uploadCustomConcert(eventCreator, bandNames);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Your photo could not be uploaded. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
    }

    private String getFileExtension (Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //----------------------------------------------------------------------------------------//
}
