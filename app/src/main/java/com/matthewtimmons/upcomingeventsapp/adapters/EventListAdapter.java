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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.activities.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder>{
    List<DocumentSnapshot> events;
    String eventType;
    int backupImage;

    public EventListAdapter(List<DocumentSnapshot> events, String eventType) {
        this.events = events;
        this.eventType = eventType;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder viewHolder, int position) {
        final DocumentSnapshot eventDocumentSnapshot = events.get(position);

        // Set the text and image for all views
        switch (eventType) {
            case FirebaseConstants.COLLECTION_CONCERTS:
                backupImage = R.drawable.ic_concerts_blue;
                setAllSharedFields(eventDocumentSnapshot, viewHolder, FirebaseConstants.KEY_CONCERT_IMAGE_URL, "NA",
                        FirebaseConstants.KEY_CONCERT_LOCATION, FirebaseConstants.KEY_CONCERT_DATE);
                setBandNameValues(eventDocumentSnapshot, viewHolder);
                break;
            case FirebaseConstants.COLLECTION_GAMES:
                backupImage = R.drawable.ic_games_blue;
                setAllSharedFields(eventDocumentSnapshot, viewHolder, FirebaseConstants.KEY_GAME_IMAGE_URL,
                        FirebaseConstants.KEY_GAME_TITLE, FirebaseConstants.KEY_GAME_LOCATION, FirebaseConstants.KEY_GAME_DATE);
                break;
            case FirebaseConstants.COLLECTION_MOVIES:
                backupImage = R.drawable.ic_movies_blue;
                setAllSharedFields(eventDocumentSnapshot, viewHolder, FirebaseConstants.KEY_MOVIE_IMAGE_URL,
                        FirebaseConstants.KEY_MOVIE_TITLE, FirebaseConstants.KEY_MOVIE_RATING, FirebaseConstants.KEY_MOVIE_GENRE);
                viewHolder.optionalFourthEventInfoTextView.setVisibility(View.VISIBLE);
                viewHolder.optionalFourthEventInfoTextView.setText(eventDocumentSnapshot.getString(FirebaseConstants.KEY_MOVIE_DATE));
                break;
        }

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailsActivity.newIntent(view.getContext(), eventDocumentSnapshot.getId(), eventType);
                    view.getContext().startActivity(intent);
                }
            });


        FirebaseFirestore.getInstance().collection("users").document("Matt").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               ArrayList<String> favorites = (ArrayList<String>) task.getResult().get(FieldPath.of("myFavorites", "movies"));
               if (favorites.contains(eventDocumentSnapshot.getId())) {
                   viewHolder.favoriteStarImageView.setVisibility(View.VISIBLE);
               };
            }
        });

    }

    public void setAllSharedFields(DocumentSnapshot eventDocumentSnapshot, EventViewHolder viewHolder,
                                   String imageUrlFirebaseKey, String titleFirebaseKey,
                                   String locationFirebaseKey, String eventDateTextView) {
        Picasso.get().load(eventDocumentSnapshot.getString(imageUrlFirebaseKey)).error(backupImage).into(viewHolder.eventPictureImageView);
        viewHolder.titleTextView.setText(eventDocumentSnapshot.getString(titleFirebaseKey));
        viewHolder.secondEventInfoTextView.setText(eventDocumentSnapshot.getString(locationFirebaseKey));
        viewHolder.thirdEventInfoTextView.setText(eventDocumentSnapshot.getString(eventDateTextView));
    }

    public void setBandNameValues(DocumentSnapshot concertDocumentSnapshot, EventViewHolder viewHolder) {
        ArrayList<String> listOfBandsAtConcert = (ArrayList<String>) concertDocumentSnapshot.get("concertBandsArray");
        viewHolder.titleTextView.setText(listOfBandsAtConcert.get(0));
        // Rules for second and third bands at concert
        if (listOfBandsAtConcert.size() > 1) {
            viewHolder.subtitleTextView.setVisibility(View.VISIBLE);
            viewHolder.subtitleTextView.setText(listOfBandsAtConcert.get(1));
            if (listOfBandsAtConcert.size() > 2) {
                viewHolder.andMoreTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView eventPictureImageView;
        private ImageView favoriteStarImageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        private TextView andMoreTextView;
        private TextView secondEventInfoTextView;
        private TextView thirdEventInfoTextView;
        private TextView optionalFourthEventInfoTextView;

        EventViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.event_card_view);
            eventPictureImageView = itemView.findViewById(R.id.event_picture);
            favoriteStarImageView = itemView.findViewById(R.id.event_view_favorite_star);
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.optional_subtitle);
            andMoreTextView = itemView.findViewById(R.id.and_more);
            secondEventInfoTextView = itemView.findViewById(R.id.optional_second_subtitle_field);
            thirdEventInfoTextView = itemView.findViewById(R.id.second_info_field);
            optionalFourthEventInfoTextView = itemView.findViewById(R.id.third_info_field);
        }
    }
}
