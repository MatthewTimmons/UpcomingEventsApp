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

import java.util.ArrayList;
import java.util.List;

public class ConcertListAdapter extends RecyclerView.Adapter<ConcertListAdapter.ConcertViewHolder>{
    List<DocumentSnapshot> concerts;

    public ConcertListAdapter(List<DocumentSnapshot> concerts) {
        this.concerts = concerts;
    }

    @NonNull
    @Override
    public ConcertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_event, viewGroup, false);
        return new ConcertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertViewHolder viewHolder, int position) {
        final DocumentSnapshot concertDocumentSnapshot = concerts.get(position);
        ArrayList<String> listOfBandsAtConcert = (ArrayList<String>) concertDocumentSnapshot.get("concertBandsArray");

        Picasso.get().load(concertDocumentSnapshot.getString("concertImageUrl")).error(R.drawable.ic_concerts_blue).into(viewHolder.concertPictureImageView);
        viewHolder.titleTextView.setText(listOfBandsAtConcert.get(0));
        setConditionalTextViews(listOfBandsAtConcert, viewHolder);
        viewHolder.concertLocationTextView.setText(concertDocumentSnapshot.getString("concertLocation"));
        viewHolder.concertDateTextView.setText(concertDocumentSnapshot.getString("concertDate"));

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), concertDocumentSnapshot.getId(), EventConstants.EVENT_TYPE_CONCERT);
                view.getContext().startActivity(intent);
            }
        });
    }

    // Rules for second and third bands at concert
    private void setConditionalTextViews(ArrayList<String> listOfBandsAtConcert, ConcertViewHolder viewHolder) {
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
        return concerts != null ? concerts.size() : 0;
    }


    public class ConcertViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView concertPictureImageView;
        private TextView titleTextView;
        private TextView subtitleTextView;
        private TextView andMoreTextView;
        private TextView concertLocationTextView;
        private TextView concertDateTextView;

        ConcertViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.event_card_view);
            concertPictureImageView = itemView.findViewById(R.id.event_picture);
            titleTextView = itemView.findViewById(R.id.title);
            subtitleTextView = itemView.findViewById(R.id.subtitle);
            andMoreTextView = itemView.findViewById(R.id.and_more);
            concertLocationTextView = itemView.findViewById(R.id.third_info_field);
            concertDateTextView = itemView.findViewById(R.id.fourth_info_field);
        }
    }
}
