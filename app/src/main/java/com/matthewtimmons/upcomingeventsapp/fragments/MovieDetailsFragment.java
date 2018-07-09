package com.matthewtimmons.upcomingeventsapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {
    ImageView wideMovieImageView;
    TextView movieTitleTextView;
    TextView movieRatingTextView;
    TextView movieGenreTextView;
    TextView movieReleaseDateTextView;
    CheckBox movieHasBeenSeenCheckbox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.details_movie, container, false);

        // Get current Movie from activity
        DetailsActivity activity = (DetailsActivity) getActivity();
//        Movie thisMovie = activity.getCurrentMovie();
        String thisMovieId = activity.getCurrentMovie();


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("movies");

        Task<QuerySnapshot> task = collectionReference.whereEqualTo("movieId", thisMovieId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot thisMovie = queryDocumentSnapshots.getDocuments().get(0);


//        FirebaseApp.initializeApp(getContext());

//        database = FirebaseDatabase.getInstance();


//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Movie thePredator = (Movie) dataSnapshot.getValue();
//                movieTitleTextView.setText(thePredator.);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


                // Assign all views to variables
                wideMovieImageView = v.findViewById(R.id.wide_image);
                movieTitleTextView = v.findViewById(R.id.movie_title);
                movieRatingTextView = v.findViewById(R.id.movie_rating);
                movieGenreTextView = v.findViewById(R.id.movie_genre);
                movieReleaseDateTextView = v.findViewById(R.id.movie_release_date);
                movieHasBeenSeenCheckbox = v.findViewById(R.id.movie_has_been_seen_checkbox);

                // Get all values for this Movie
                String movieImageUrl = thisMovie.getString("movieImageUrl");
                String movieTitle = thisMovie.getString("movieTitle");
                String movieRating = thisMovie.getString("movieRating");
                String movieGenre = thisMovie.getString("movieGenre");
                String movieReleaseDate = thisMovie.getString("movieReleaseDate");
                Boolean movieHasBeenSeen = thisMovie.getBoolean("hasBeenSeen");

                // Assign values to each view
                Picasso.get().load(movieImageUrl).error(R.drawable.ic_movies_blue).into(wideMovieImageView);
                movieTitleTextView.setText(movieTitle);
                movieRatingTextView.setText(movieRating);
                movieGenreTextView.setText(movieGenre);
                movieReleaseDateTextView.setText(movieReleaseDate);
                if (movieHasBeenSeen) {
                    movieHasBeenSeenCheckbox.performClick();
                }


                movieHasBeenSeenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean seen) {
                        toggleButton(seen);
                    }
                });
            }
        });


//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        CollectionReference collectionReference = firebaseFirestore.collection("movies");
//        collectionReference.whereEqualTo()
//        firebaseFirestore.collection("movies").document("ThePredator").addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                String title = documentSnapshot.getString("title");
//                movieTitleTextView.setText("Completed");
//                Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
//            }
//        });


        return v;
    }

    public void toggleButton(Boolean seen) {

        if (seen) {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.parseColor("#6ef442"));
        } else {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.WHITE);
        }
    }
}
