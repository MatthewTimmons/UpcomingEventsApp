package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.activities.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
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
            cardView = itemView.findViewById(R.id.event_card_view);
            movieTitleTextView = itemView.findViewById(R.id.title);
            movieRatingTextView = itemView.findViewById(R.id.second_info_field);
            movieGenreTextView = itemView.findViewById(R.id.third_info_field);
            movieReleaseDateTextView = itemView.findViewById(R.id.optional_fourth_info_field);
            moviePictureImageView = itemView.findViewById(R.id.event_picture);
        }
    }
}
