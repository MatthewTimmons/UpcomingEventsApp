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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        final DetailsActivity activity = (DetailsActivity) getActivity();
//        Movie thisMovie = activity.getCurrentMovie();
        final String thisMovieId = activity.getCurrentMovie();


        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("movies");

        Task<QuerySnapshot> task = collectionReference.whereEqualTo("movieId", thisMovieId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final DocumentSnapshot thisMovie = queryDocumentSnapshots.getDocuments().get(0);


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
//                boolean movieHasBeenSeen = thisMovie.getBoolean("hasBeenSeen");

                // Assign values to each view
                Picasso.get().load(movieImageUrl).error(R.drawable.ic_movies_blue).into(wideMovieImageView);
                movieTitleTextView.setText(movieTitle);
                movieRatingTextView.setText(movieRating);
                movieGenreTextView.setText(movieGenre);
                movieReleaseDateTextView.setText(movieReleaseDate);

                // See if movie is listed under current user's moviesSeen Collection
                final Task<DocumentSnapshot> moviesSeen = firebaseFirestore.collection("users").document("Matt").get();
                moviesSeen.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final DocumentSnapshot userSnapshot = task.getResult();
                        final ArrayList<String> arrayOfMoviesSeen = (ArrayList<String>) userSnapshot.get("moviesSeenByMovieId");
//                        Toast.makeText(getContext(), "arrayOfMoviesSeen =" + arrayOfMoviesSeen, Toast.LENGTH_SHORT).show();
                        final boolean movieHasBeenSeen = arrayOfMoviesSeen.contains(thisMovieId);
//                        Toast.makeText(getContext(), "movieHasBeenSeen =" + movieHasBeenSeen, Toast.LENGTH_SHORT).show();
                        if (movieHasBeenSeen) {movieHasBeenSeenCheckbox.performClick();
                        toggleButton(movieHasBeenSeen, userSnapshot, thisMovie);}


                        movieHasBeenSeenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isNowChecked) {
                                toggleButton(isNowChecked, userSnapshot, thisMovie);
                            }
                        });
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

    public void toggleButton(Boolean isNowChecked, DocumentSnapshot userSnapshot, DocumentSnapshot thisMovie) {
        ArrayList<String> listOfMoviesSeenByMovieId;
        try {
            listOfMoviesSeenByMovieId = (ArrayList<String>) userSnapshot.get("moviesSeenByMovieId");
        } catch (IllegalArgumentException e) {
            listOfMoviesSeenByMovieId = new ArrayList<String>();
        }

        if (isNowChecked) {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.parseColor("#6ef442"));
            if (!listOfMoviesSeenByMovieId.contains(thisMovie.get("movieId"))) {
                listOfMoviesSeenByMovieId.add(0, (String) thisMovie.get("movieId"));
                userSnapshot.getReference().update("moviesSeenByMovieId", listOfMoviesSeenByMovieId);
            }
            Toast.makeText(getContext(), (CharSequence) thisMovie.get("movieId"), Toast.LENGTH_SHORT).show();
//            moviesSeen.document((String) thisMovie.get("movieTitle")).set(thisMovie);
//            thisMovie.getReference().update("hasBeenSeen", true);
        } else {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.WHITE);
            int index = 0;
            for(String value : listOfMoviesSeenByMovieId) {
                if (value.equals(thisMovie.get("movieId").toString())) {
                    listOfMoviesSeenByMovieId.remove(index);
                }
                index++;
            }
//            int index = listOfMoviesSeenByMovieId.indexOf(thisMovie.get("movieId").toString());
//            listOfMoviesSeenByMovieId.remove(index);
            if (listOfMoviesSeenByMovieId.equals(null)) {
                listOfMoviesSeenByMovieId = new ArrayList<String>();
            }
            userSnapshot.getReference().update("moviesSeenByMovieId", listOfMoviesSeenByMovieId);
//            userSnapshot.getReference().update("moviesSeenByMovieId", listOfMoviesSeenByMovieId);
//            Toast.makeText(getContext(), (CharSequence) thisMovie.get("movieId"), Toast.LENGTH_SHORT).show();
//            movieHasBeenSeenCheckbox.setBackgroundColor(Color.WHITE);
//            ArrayList<String> listOfMoviesSeenByMovieId = (ArrayList<String>) userSnapshot.get("moviesSeenByMovieId");
//            int index = listOfMoviesSeenByMovieId.indexOf(thisMovie.get("movieId"));
//            listOfMoviesSeenByMovieId.remove(index);
//            userSnapshot.getReference().update("moviesSeenByMovieId", listOfMoviesSeenByMovieId);
//            Toast.makeText(getContext(), index, Toast.LENGTH_SHORT).show();
//            moviesSeen.document((String) thisMovie.get("movieTitle")).delete();
//            thisMovie.getReference().update("hasBeenSeen", false);

        }
    }
}
