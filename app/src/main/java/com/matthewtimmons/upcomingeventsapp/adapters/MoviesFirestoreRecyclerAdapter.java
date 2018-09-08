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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.manager.DateHelper;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoviesFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<Movie, MoviesFirestoreRecyclerAdapter.MovieViewHolder> {
    String currentUserId;
    int backupImage;

    public MoviesFirestoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Movie> options, String currentUserId) {
        super(options);
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MovieViewHolder viewHolder, int position, @NonNull final Movie movie) {
        backupImage = R.drawable.ic_movies_blue;

        // Present Movie
        Picasso.get().load(movie.getImageUrl()).error(backupImage).into(viewHolder.eventPictureImageView);
        Picasso.get().load(movie.getImageUrl()).error(backupImage).into(viewHolder.cardBackgroundImageView);
        viewHolder.titleTextView.setText(movie.getTitle());
        viewHolder.fourthEventInfoTextView.setText(DateHelper.getHumanReadableFormat(movie.getDate()));
        viewHolder.optionalSecondEventInfoTextView.setVisibility(View.VISIBLE);
        viewHolder.optionalSecondEventInfoTextView.setText(movie.getRating());
        viewHolder.thirdEventInfoTextView.setText(movie.getGenre());

        // Add click listener
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), movie.getId(), ("movies"), movie.getEventCreator());
                view.getContext().startActivity(intent);
            }
        });

        // Set star visibility if favorited
        if (UserManager.getInstance().getCurrentUser() == null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    HashMap<String, Object> allFavoritesData = (HashMap<String, Object>) task.getResult().get("myFavorites");
                    ArrayList<String> allFavorites = new ArrayList<>();
                    if (allFavoritesData != null) {
                        allFavorites.addAll((ArrayList<String>) allFavoritesData.get("concerts"));
                        allFavorites.addAll((ArrayList<String>) allFavoritesData.get("games"));
                        allFavorites.addAll((ArrayList<String>) allFavoritesData.get("movies"));
                    }

                    if (allFavorites.contains(movie.getId())) {
                        viewHolder.favoriteStarImageView.setVisibility(View.VISIBLE);
                        viewHolder.favoriteStarTransparentImageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            Map<String, Object> allFavoritesData = UserManager.getInstance().getCurrentUser().getMyFavorites();
            ArrayList<String> allFavorites = new ArrayList<>();
            if (allFavoritesData != null) {
                allFavorites.addAll((ArrayList<String>) allFavoritesData.get("concerts"));
                allFavorites.addAll((ArrayList<String>) allFavoritesData.get("games"));
                allFavorites.addAll((ArrayList<String>) allFavoritesData.get("movies"));
            }

            if (allFavorites.contains(movie.getId())) {
                viewHolder.favoriteStarImageView.setVisibility(View.VISIBLE);
                viewHolder.favoriteStarTransparentImageView.setVisibility(View.VISIBLE);
            }
        }
    }




    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardBackgroundImageView;
        private CardView cardView;
        private ImageView eventPictureImageView;
        private ImageView favoriteStarImageView;
        private ImageView favoriteStarTransparentImageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        private TextView optionalSecondSubtitle;
        private TextView optionalSecondEventInfoTextView;
        private TextView thirdEventInfoTextView;
        private TextView fourthEventInfoTextView;

        MovieViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardBackgroundImageView = itemView.findViewById(R.id.card_background_image_view);
            cardView = itemView.findViewById(R.id.event_card_view);
            eventPictureImageView = itemView.findViewById(R.id.event_picture);
            favoriteStarImageView = itemView.findViewById(R.id.event_view_favorite_ribbon);
            favoriteStarTransparentImageView = itemView.findViewById(R.id.event_view_favorite_ribbon_transparent);
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.optional_subtitle);
            optionalSecondSubtitle = itemView.findViewById(R.id.optional_second_subtitle);
            optionalSecondEventInfoTextView = itemView.findViewById(R.id.optional_second_info_field);
            thirdEventInfoTextView = itemView.findViewById(R.id.third_info_field);
            fourthEventInfoTextView = itemView.findViewById(R.id.fourth_info_field);
        }
    }
}
