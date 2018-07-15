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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsFragment extends Fragment {
    public static final String ARG_MOVIE_ID = "argMovieId";
    private String movieId;
    private ImageView wideMovieImageView;
    private TextView movieTitleTextView;
    private TextView movieRatingTextView;
    private TextView movieGenreTextView;
    private TextView movieReleaseDateTextView;
    private CheckBox movieHasBeenSeenCheckbox;

    //TODO: Change to current user
    public static final String CURRENT_USER_ID = "Matt";

    public static MovieDetailsFragment newInstance(String movieId) {
        MovieDetailsFragment instance = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID, movieId);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_details_event, container, false);

        // Get current MovieId from activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieId = bundle.getString(ARG_MOVIE_ID);
        }

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("movies");

        Task<DocumentSnapshot> value = collectionReference.document(movieId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              final DocumentSnapshot movie = documentSnapshot;

                // Assign all views to variables
                wideMovieImageView = v.findViewById(R.id.wide_image);
                movieTitleTextView = v.findViewById(R.id.title);
                movieRatingTextView = v.findViewById(R.id.second_info_field);
                movieGenreTextView = v.findViewById(R.id.third_info_field);
                movieReleaseDateTextView = v.findViewById(R.id.optional_fourth_info_field);
                movieHasBeenSeenCheckbox = v.findViewById(R.id.checkbox);

                // Get all values for this Movie
                String movieImageUrl = movie.getString("movieImageUrl");
                final String movieTitle = movie.getString("movieTitle");
                String movieRating = movie.getString("movieRating");
                String movieGenre = movie.getString("movieGenre");
                String movieReleaseDate = movie.getString("movieReleaseDate");

                // Assign values to each view
                Picasso.get().load(movieImageUrl).error(R.drawable.ic_movies_blue).into(wideMovieImageView);
                movieTitleTextView.setText(movieTitle);
                String rating = "Rated ";
                rating = rating.concat(movieRating);
                movieRatingTextView.setVisibility(View.VISIBLE);
                movieRatingTextView.setTextSize(14);
                movieRatingTextView.setText(rating);
                movieGenreTextView.setText(movieGenre);
                movieReleaseDateTextView.setText(movieReleaseDate);
                movieHasBeenSeenCheckbox.setVisibility(View.VISIBLE);
                movieHasBeenSeenCheckbox.setText("Seen");

                // See if movie is listed under current user's moviesSeen Collection
                final Task<DocumentSnapshot> currentUser = FirebaseFirestore.getInstance().collection("users")
                        .document(CURRENT_USER_ID)
                        .get();
                currentUser.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final DocumentSnapshot userSnapshot = task.getResult();
                        final ArrayList<String> arrayOfMoviesSeen = (ArrayList<String>) userSnapshot.get("moviesSeenByMovieId");
//                        Toast.makeText(getContext(), "arrayOfMoviesSeen =" + arrayOfMoviesSeen, Toast.LENGTH_SHORT).show();
                        final boolean movieHasBeenSeen = arrayOfMoviesSeen.contains(movieId);
//                        Toast.makeText(getContext(), "movieHasBeenSeen =" + movieHasBeenSeen, Toast.LENGTH_SHORT).show();
                        if (movieHasBeenSeen) {movieHasBeenSeenCheckbox.setChecked(true);
                        toggleButton(movieHasBeenSeen, userSnapshot, movie);}


                        movieHasBeenSeenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isNowChecked) {
                                toggleButton(isNowChecked, userSnapshot, movie);
                            }
                        });

                        // TODO: Will move Seekbar into fragment
//                        ((DetailsActivity) getActivity()).setSeekbarToCurrentInterestLevel(getContext(), "movies", movieId);
                    }
                });
            }
        });
        return v;
    }

    public void toggleButton(Boolean movieHasBeenSeen, DocumentSnapshot userSnapshot, DocumentSnapshot currentMovieDocumentSnapshot) {
        ArrayList<String> listOfMoviesSeenByMovieId;
        ArrayList<String> listOfUsersWhoHaveSeen;
        try {
            listOfMoviesSeenByMovieId = (ArrayList<String>) userSnapshot.get("moviesSeenByMovieId");
            listOfUsersWhoHaveSeen = (ArrayList<String>) currentMovieDocumentSnapshot.get("usersWhoHaveSeen");
        } catch (IllegalArgumentException e) {
            listOfMoviesSeenByMovieId = new ArrayList<String>();
            listOfUsersWhoHaveSeen = new ArrayList<String>();
        }

        if (movieHasBeenSeen) {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.parseColor("#6ef442"));
            if (!listOfMoviesSeenByMovieId.contains(currentMovieDocumentSnapshot.getId())) {
                listOfMoviesSeenByMovieId.add(currentMovieDocumentSnapshot.getId());
                userSnapshot.getReference().update("moviesSeenByMovieId", listOfMoviesSeenByMovieId);
            }

            if (!listOfUsersWhoHaveSeen.contains(CURRENT_USER_ID)) {
                listOfUsersWhoHaveSeen.add(CURRENT_USER_ID);
                currentMovieDocumentSnapshot.getReference().update("usersWhoHaveSeen", listOfUsersWhoHaveSeen);
            }
        } else {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.WHITE);
            try {
                listOfMoviesSeenByMovieId.remove(movieId);
                userSnapshot.getReference().update("moviesSeenByMovieId", listOfMoviesSeenByMovieId);
                listOfUsersWhoHaveSeen.remove(CURRENT_USER_ID);
                currentMovieDocumentSnapshot.getReference().update("usersWhoHaveSeen", listOfUsersWhoHaveSeen);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
