package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollapsedEventListAdapter extends RecyclerView.Adapter<CollapsedEventListAdapter.CollapsedEventViewHolder> {
    List<DocumentSnapshot> viewItems;
    String eventType, eventTitle;
    int eventTypeIcon;

    public CollapsedEventListAdapter(List<DocumentSnapshot> viewItems) {
        this.viewItems = viewItems;
    }

    @NonNull
    @Override
    public CollapsedEventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event_collapsed, viewGroup, false);
        return new CollapsedEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CollapsedEventViewHolder collapsedEventViewHolder, int i) {
        final DocumentSnapshot eventDocumentSnapshot = viewItems.get(i);
        eventType = eventDocumentSnapshot.getString("eventType");

        if (eventType.equals("concerts")) {
            ArrayList<String> allBandNames = (ArrayList<String>) eventDocumentSnapshot.get(FirebaseConstants.KEY_CONCERT_BANDS_ARRAY);
            eventTitle = TextUtils.join(", ", allBandNames);
        } else {
            eventTitle = eventDocumentSnapshot.getString(FirebaseConstants.KEY_TITLE);
        }

//      Set which icon to use
        switch (eventType) {
            case FirebaseConstants.COLLECTION_CONCERTS:
                eventTypeIcon = R.drawable.ic_concerts;
                break;
            case FirebaseConstants.COLLECTION_GAMES:
                eventTypeIcon = R.drawable.ic_games;
                break;
            case FirebaseConstants.COLLECTION_MOVIES:
                eventTypeIcon = R.drawable.ic_movies;
                break;
        }

        // Set icon and title views
        Picasso.get().load(eventTypeIcon).error(R.drawable.ic_default_profile_photo).into(collapsedEventViewHolder.thirdColumnImageView);
        collapsedEventViewHolder.firstColumnNameTextView.setText(eventTitle);

        // Set click listener
        collapsedEventViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), eventDocumentSnapshot.getId(), eventDocumentSnapshot.getString("eventType"), eventDocumentSnapshot.getString("eventCreator"));
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() { return viewItems != null ? viewItems.size() : 0; }

    public class CollapsedEventViewHolder extends RecyclerView.ViewHolder {
        TextView firstColumnNameTextView;

        ImageView thirdColumnImageView;

        public CollapsedEventViewHolder(@NonNull View itemView) {
            super(itemView);
            firstColumnNameTextView = itemView.findViewById(R.id.event_title);

            thirdColumnImageView = itemView.findViewById(R.id.checkbox);
        }
    }
}
