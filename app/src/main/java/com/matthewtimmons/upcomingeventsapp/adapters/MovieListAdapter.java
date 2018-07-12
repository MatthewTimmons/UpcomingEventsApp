package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>{
    List<DocumentSnapshot> movies;

    public MovieListAdapter(List<DocumentSnapshot> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        // Replace setting viewholders from instance of Movie to setting them with data from Firestore
//        final Movie currentMovie = movies.get(position);
//        Picasso.get().load(currentMovie.getMovieImageUrl()).error(R.drawable.ic_movies_blue).into(movieViewHolder.moviePictureImageView);
//        movieViewHolder.movieTitleTextView.setText(currentMovie.getMovieTitle());
//        movieViewHolder.movieRatingTextView.setText(currentMovie.getMovieRating());
//        movieViewHolder.movieGenreTextView.setText(currentMovie.getMovieGenre());
//        movieViewHolder.movieReleaseDateTextView.setText(currentMovie.getMovieReleaseDate());

        final DocumentSnapshot currentMovieDocumentSnapshot = movies.get(position);
        Picasso.get().load(currentMovieDocumentSnapshot.getString("movieImageUrl")).error(R.drawable.ic_movies_blue).into(movieViewHolder.moviePictureImageView);
        movieViewHolder.movieTitleTextView.setText(currentMovieDocumentSnapshot.getString("movieTitle"));
        movieViewHolder.movieRatingTextView.setText(currentMovieDocumentSnapshot.getString("movieRating"));
        movieViewHolder.movieGenreTextView.setText(currentMovieDocumentSnapshot.getString("movieGenre"));
        movieViewHolder.movieReleaseDateTextView.setText(currentMovieDocumentSnapshot.getString("movieReleaseDate"));

        movieViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), currentMovieDocumentSnapshot.getId(),
                        EventConstants.EVENT_TYPE_MOVIE);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return movies != null ? movies.size() : 0; }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView movieTitleTextView;
        TextView movieRatingTextView;
        TextView movieGenreTextView;
        TextView movieReleaseDateTextView;
        ImageView moviePictureImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.movie_card_view);
            movieTitleTextView = itemView.findViewById(R.id.movie_title);
            movieRatingTextView = itemView.findViewById(R.id.movie_rating);
            movieGenreTextView = itemView.findViewById(R.id.movie_genre);
            movieReleaseDateTextView = itemView.findViewById(R.id.movie_release_date);
            moviePictureImageView = itemView.findViewById(R.id.movie_picture);
        }
    }
}
