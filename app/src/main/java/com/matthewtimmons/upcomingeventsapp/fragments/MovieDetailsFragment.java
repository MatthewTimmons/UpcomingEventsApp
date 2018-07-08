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
        View v = inflater.inflate(R.layout.details_movie, container, false);

        // Get current Concert from activity
        DetailsActivity activity = (DetailsActivity) getActivity();
        Movie thisMovie = activity.getCurrentMovie();

        // Assign all views to variables
        wideMovieImageView = v.findViewById(R.id.wide_image);
        movieTitleTextView = v.findViewById(R.id.movie_title);
        movieRatingTextView = v.findViewById(R.id.movie_rating);
        movieGenreTextView = v.findViewById(R.id.movie_genre);
        movieReleaseDateTextView = v.findViewById(R.id.movie_release_date);
        movieHasBeenSeenCheckbox = v.findViewById(R.id.movie_has_been_seen_checkbox);

        // Get all values for this Movie
        String movieImageUrl = thisMovie.getMovieImageUrl();
        String movieTitle = thisMovie.getMovieTitle();
        String movieRating= thisMovie.getMovieRating();
        String movieGenre = thisMovie.getMovieGenre();
        String movieReleaseDate = thisMovie.getMovieReleaseDate();
        Boolean movieHasBeenSeen = thisMovie.getMovieHasBeenSeen();

        // Assign values to each view
        Picasso.get().load(movieImageUrl).error(R.drawable.ic_movies_blue).into(wideMovieImageView);
        movieTitleTextView.setText(movieTitle);
        movieRatingTextView.setText(movieRating);
        movieGenreTextView.setText(movieGenre);
        movieReleaseDateTextView.setText(movieReleaseDate);
        if (movieHasBeenSeen) {toggleButton(false);}

        movieHasBeenSeenCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleButton(b);
            }
        });

        return v;
    }

    public void toggleButton(Boolean b) {
        if (b) {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.parseColor("#6ef442"));
        } else {
            movieHasBeenSeenCheckbox.setBackgroundColor(Color.WHITE);
        }
    }
}
