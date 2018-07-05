package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Movie;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>{
    List<Movie> movies;

    public MovieListAdapter(List<Movie> movies) {
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
        Movie currentMovie = movies.get(position);
        movieViewHolder.movieTitleTextView.setText(currentMovie.getMovieTitle());
        movieViewHolder.movieRatingTextView.setText(currentMovie.getMovieRating());
        movieViewHolder.movieGenreTextView.setText(currentMovie.getMovieGenre());
        movieViewHolder.movieReleaseDateTextView.setText(currentMovie.getMovieReleaseDate());
//
//      Update to actual image later
//      movieViewHolder.moviePictureImageView.setImageURI(Uri.parse("https://www.bleedingcool.com/wp-content/uploads/2017/12/the-grinch-poster-600x750.jpg?x70969"));
        movieViewHolder.moviePictureImageView.setImageResource(R.drawable.ic_movies_blue);
    }

    @Override
    public int getItemCount() { return movies != null ? movies.size() : 0; }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitleTextView;
        TextView movieRatingTextView;
        TextView movieGenreTextView;
        TextView movieReleaseDateTextView;
        ImageView moviePictureImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitleTextView = itemView.findViewById(R.id.movie_title);
            movieRatingTextView = itemView.findViewById(R.id.movie_rating);
            movieGenreTextView = itemView.findViewById(R.id.movie_genre);
            movieReleaseDateTextView = itemView.findViewById(R.id.movie_release_date);
            moviePictureImageView = itemView.findViewById(R.id.movie_picture);
        }
    }
}
