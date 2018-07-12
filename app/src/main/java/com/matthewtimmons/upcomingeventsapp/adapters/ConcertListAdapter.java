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
import com.matthewtimmons.upcomingeventsapp.models.Concert;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_concert, viewGroup, false);
        return new ConcertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConcertViewHolder viewHolder, int position) {
        final DocumentSnapshot concertDocumentSnapshot = concerts.get(position);
        ArrayList<String> listOfBandsAtConcert = (ArrayList<String>) concertDocumentSnapshot.get("concertBandsArray");

        Picasso.get().load(concertDocumentSnapshot.getString("concertImageUrl")).error(R.drawable.ic_concerts_blue).into(viewHolder.concertPictureImageView);
        viewHolder.firstBandNameTextView.setText(listOfBandsAtConcert.get(0));
        viewHolder.secondBandNameTextView.setText(listOfBandsAtConcert.get(1));
        viewHolder.concertLocationTextView.setText(concertDocumentSnapshot.getString("concertLocation"));
        viewHolder.concertDateTextView.setText(concertDocumentSnapshot.getString("concertDate"));

        // Rules for second text view
        if (listOfBandsAtConcert.size() < 2) {
            viewHolder.secondBandNameTextView.setVisibility(View.GONE);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), concertDocumentSnapshot.getId(), EventConstants.EVENT_TYPE_CONCERT);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return concerts != null ? concerts.size() : 0;
    }


    public class ConcertViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView concertPictureImageView;
        private TextView firstBandNameTextView;
        private TextView secondBandNameTextView;
        private TextView concertLocationTextView;
        private TextView concertDateTextView;

        ConcertViewHolder(@NonNull final View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.concert_card_view);
            concertPictureImageView = itemView.findViewById(R.id.concert_picture);
            firstBandNameTextView = itemView.findViewById(R.id.first_band_name);
            secondBandNameTextView = itemView.findViewById(R.id.second_band_name);
            concertLocationTextView = itemView.findViewById(R.id.concert_location);
            concertDateTextView = itemView.findViewById(R.id.concert_date);
        }
    }
}
