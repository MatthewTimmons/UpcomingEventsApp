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

//This is an entirely generic RecyclerView that binds data to a list of events with the ViewHolder, RecyclerViewWithHeaderViewHolder //TODO: Change this to generic also

public class RecyclerViewWithHeaderListAdapter extends RecyclerView.Adapter<RecyclerViewWithHeaderListAdapter.RecyclerViewWithHeaderViewHolder> {
    List<DocumentSnapshot> viewItems;
    String eventType;
    int eventTypeIcon;

    public RecyclerViewWithHeaderListAdapter(List<DocumentSnapshot> viewItems) {
        this.viewItems = viewItems;
    }

    @NonNull
    @Override
    public RecyclerViewWithHeaderListAdapter.RecyclerViewWithHeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event_collapsed, viewGroup, false);
        return new RecyclerViewWithHeaderListAdapter.RecyclerViewWithHeaderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWithHeaderListAdapter.RecyclerViewWithHeaderViewHolder recyclerViewWithHeaderViewHolder, int i) {
        final DocumentSnapshot eventDocumentSnapshot = viewItems.get(i);
        eventType = eventDocumentSnapshot.getString("eventType");

//      TextView firstColumnNameTextView;
        recyclerViewWithHeaderViewHolder.firstColumnNameTextView.setText(eventDocumentSnapshot.getString(FirebaseConstants.KEY_TITLE));

//      ImageView thirdColumnImageView;
        switch (eventType) {
            case FirebaseConstants.COLLECTION_CONCERTS:
                eventTypeIcon = R.drawable.ic_concerts;
                ArrayList<String> allBandNames = (ArrayList<String>) eventDocumentSnapshot.get(FirebaseConstants.KEY_CONCERT_BANDS_ARRAY);
                recyclerViewWithHeaderViewHolder.firstColumnNameTextView.setText(TextUtils.join(", ", allBandNames));
                break;
            case FirebaseConstants.COLLECTION_GAMES:
                eventTypeIcon = R.drawable.ic_games;
                break;
            case FirebaseConstants.COLLECTION_MOVIES:
                eventTypeIcon = R.drawable.ic_movies;
                break;
        }
        Picasso.get().load(eventTypeIcon).error(R.drawable.ic_default_profile_photo).into(recyclerViewWithHeaderViewHolder.thirdColumnImageView);

        recyclerViewWithHeaderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), eventDocumentSnapshot.getId(), eventDocumentSnapshot.getString("eventType"));
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() { return viewItems != null ? viewItems.size() : 0; }

    public class RecyclerViewWithHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView firstColumnNameTextView;

        ImageView thirdColumnImageView;

        public RecyclerViewWithHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            firstColumnNameTextView = itemView.findViewById(R.id.event_title);

            thirdColumnImageView = itemView.findViewById(R.id.checkbox);
        }
    }
}
