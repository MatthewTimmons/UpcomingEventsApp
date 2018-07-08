package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailsFragment extends Fragment {
    ImageView wideMovieImageView;
    TextView movieTitleTextView;
    TextView movieRatingTextView;
    TextView movieGenreTextView;
    TextView movieReleaseDateTextView;

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

        // Get all values for this Movie
        String movieImageUrl = thisMovie.getMovieImageUrl();
        String movieTitle = thisMovie.getMovieTitle();
        String movieRating= thisMovie.getMovieRating();
        String movieGenre = thisMovie.getMovieGenre();
        String movieReleaseDate = thisMovie.getMovieReleaseDate();

        // Assign values to each view
        Picasso.get().load(movieImageUrl).error(R.drawable.ic_movies_blue).into(wideMovieImageView);
        movieTitleTextView.setText(movieTitle);
        movieRatingTextView.setText(movieRating);
        movieGenreTextView.setText(movieGenre);
        movieReleaseDateTextView.setText(movieReleaseDate);

        return v;
    }
}
