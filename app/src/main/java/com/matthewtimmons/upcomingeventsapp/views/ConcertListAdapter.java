package com.matthewtimmons.upcomingeventsapp.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Concert;

import java.util.List;

public class ConcertListAdapter extends RecyclerView.Adapter<ConcertListAdapter.ConcertViewHolder>{
    List<Concert> concerts;

    public ConcertListAdapter(List<Concert> concerts) {
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
        Concert currentConcert = concerts.get(position);
        viewHolder.bandsTextView.setText(currentConcert.getBandsDisplayName());
        viewHolder.concertLocationTextView.setText(currentConcert.getConcertLocation());
        viewHolder.concertDateTextView.setText(currentConcert.getDate());
//
//      Update to actual image later
        viewHolder.concertPictureImageView.setImageResource(R.drawable.ic_concerts_blue);
    }

    @Override
    public int getItemCount() {
        return concerts != null ? concerts.size() : 0;
    }

    class ConcertViewHolder extends RecyclerView.ViewHolder {
        private ImageView concertPictureImageView;
        private TextView bandsTextView;
        private TextView concertLocationTextView;
        private TextView concertDateTextView;

        ConcertViewHolder(@NonNull View itemView) {
            super(itemView);
            concertPictureImageView = itemView.findViewById(R.id.concert_picture);
            bandsTextView = itemView.findViewById(R.id.band_names);
            concertLocationTextView = itemView.findViewById(R.id.concert_location);
            concertDateTextView = itemView.findViewById(R.id.concert_date);
        }
    }
}
