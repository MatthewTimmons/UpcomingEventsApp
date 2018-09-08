package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.matthewtimmons.upcomingeventsapp.activities.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.DateHelper;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.models.Event;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder>{
    List<DocumentSnapshot> events;
    String currentUserId;
    String eventType;
    int backupImage;

    public EventListAdapter(List<DocumentSnapshot> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event, viewGroup, false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder viewHolder, int position) {
        final DocumentSnapshot eventDocumentSnapshot = events.get(position);

        // Set the view the first time
        updateView(viewHolder, eventDocumentSnapshot);

        // If document is updated, update the view. If document is deleted, set the view to "No longer exists"
        ListenerRegistration listenerRegistration = eventDocumentSnapshot.getReference().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        updateView(viewHolder, documentSnapshot);
                    } else {
                        // If document deleted, set images to red X icons
                        Picasso.get().load(R.drawable.item_deleted).error(backupImage).into(viewHolder.eventPictureImageView);
                        Picasso.get().load(R.drawable.item_deleted).error(backupImage).into(viewHolder.cardBackgroundImageView);
                        Toast.makeText(viewHolder.itemView.getContext(), "An item has been deleted", Toast.LENGTH_SHORT).show();
                        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(), "This item has been deleted and is no longer available", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        FirebaseFirestore.getInstance().collection("users").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        updateView(viewHolder, eventDocumentSnapshot);
                    }
                }
            }
        });
    }

    private void updateView(final EventViewHolder viewHolder, final DocumentSnapshot eventDocumentSnapshot) {
        eventType = eventDocumentSnapshot.getString("eventType");
        // Set the text and image views
        switch (eventType) {
            case FirebaseConstants.COLLECTION_CONCERTS:
                presentConcert(viewHolder, eventDocumentSnapshot);
                break;
            case FirebaseConstants.COLLECTION_GAMES:
                presentGame(viewHolder, eventDocumentSnapshot);
                break;
            case FirebaseConstants.COLLECTION_MOVIES:
                presentMovie(viewHolder, eventDocumentSnapshot);
                break;
        }

        // Add click listener
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), eventDocumentSnapshot.getId(), eventDocumentSnapshot.getString("eventType"), eventDocumentSnapshot.getString("eventCreator"));
                view.getContext().startActivity(intent);
            }
        });

        // Set star visibility if favorited
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

                if (allFavorites.contains(eventDocumentSnapshot.getId())) {
                    viewHolder.favoriteStarImageView.setVisibility(View.VISIBLE);
                    viewHolder.favoriteStarTransparentImageView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.favoriteStarImageView.setVisibility(View.GONE);
                    viewHolder.favoriteStarTransparentImageView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setAllSharedFields(Event event, EventViewHolder viewHolder) {
        Picasso.get().load(event.getImageUrl()).error(backupImage).into(viewHolder.eventPictureImageView);
        Picasso.get().load(event.getImageUrl()).error(backupImage).into(viewHolder.cardBackgroundImageView);
        viewHolder.titleTextView.setText(event.getTitle());
        viewHolder.fourthEventInfoTextView.setText(DateHelper.getHumanReadableFormat(event.getDate()));
    }

    private void presentConcert(EventViewHolder viewHolder, DocumentSnapshot eventDocumentSnapshot) {
        backupImage = R.drawable.ic_concerts_blue;
        Concert concert = new Concert(eventDocumentSnapshot);
        setAllSharedFields(concert, viewHolder);
        setBandNameValues(concert, viewHolder);
        viewHolder.thirdEventInfoTextView.setText(concert.getConcertLocation());
    }

    private void presentGame(EventViewHolder viewHolder, DocumentSnapshot eventDocumentSnapshot) {
        backupImage = R.drawable.ic_games_blue;
        Game game = new Game(eventDocumentSnapshot);
        viewHolder.optionalSecondEventInfoTextView.setVisibility(View.VISIBLE);
        viewHolder.optionalSecondEventInfoTextView.setText(game.getFormattedRating(viewHolder.itemView.getResources()));
        setAllSharedFields(game, viewHolder);
        viewHolder.thirdEventInfoTextView.setText(game.getReleaseConsolesAsString());
    }

    private void presentMovie(EventViewHolder viewHolder, DocumentSnapshot eventDocumentSnapshot) {
        backupImage = R.drawable.ic_movies_blue;
        Movie movie = new Movie(eventDocumentSnapshot);
        setAllSharedFields(movie, viewHolder);
        viewHolder.optionalSecondEventInfoTextView.setVisibility(View.VISIBLE);
        viewHolder.optionalSecondEventInfoTextView.setText(movie.getFormattedRating(viewHolder.itemView.getResources()));
        viewHolder.thirdEventInfoTextView.setText(movie.getGenre());
    }


    private void setBandNameValues(Concert concert, final EventViewHolder viewHolder) {
        ArrayList<String> listOfBandsAtConcert = (ArrayList<String>) concert.getBands();
        viewHolder.titleTextView.setText(listOfBandsAtConcert.get(0));
        // Rules for second and third bands at concert
        if (listOfBandsAtConcert.size() > 1) {
            viewHolder.subtitleTextView.setVisibility(View.VISIBLE);
            viewHolder.subtitleTextView.setText(listOfBandsAtConcert.get(1));
            viewHolder.subtitleTextView.setTextColor(viewHolder.itemView.getResources().getColor(R.color.darker_gray));
            if (listOfBandsAtConcert.size() > 2) {
                viewHolder.optionalSecondSubtitle.setVisibility(View.VISIBLE);
                viewHolder.optionalSecondSubtitle.setTextSize(18);
                viewHolder.optionalSecondSubtitle.setTextColor(viewHolder.itemView.getResources().getColor(R.color.darker_gray));
            }
        }
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
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

        EventViewHolder(@NonNull final View itemView) {
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
