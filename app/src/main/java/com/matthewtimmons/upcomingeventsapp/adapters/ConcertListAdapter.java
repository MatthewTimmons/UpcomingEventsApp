package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.squareup.picasso.Picasso;

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
        Picasso.get().load(currentConcert.getImageUrl()).error(R.drawable.ic_concerts_blue).into(viewHolder.concertPictureImageView);
        viewHolder.firstBandNameTextView.setText(currentConcert.getBandName(0));
        viewHolder.secondBandNameTextView.setText(currentConcert.getBandName(1));
        viewHolder.concertLocationTextView.setText(currentConcert.getConcertLocation());
        viewHolder.concertDateTextView.setText(currentConcert.getDate());

        // Rules for second text view
        if (concerts.get(position).getBands().size() < 2) {
            viewHolder.secondBandNameTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return concerts != null ? concerts.size() : 0;
    }

    class ConcertViewHolder extends RecyclerView.ViewHolder {
        private ImageView concertPictureImageView;
        private TextView firstBandNameTextView;
        private TextView secondBandNameTextView;
        private TextView concertLocationTextView;
        private TextView concertDateTextView;

        ConcertViewHolder(@NonNull View itemView) {
            super(itemView);
            concertPictureImageView = itemView.findViewById(R.id.concert_picture);
            firstBandNameTextView = itemView.findViewById(R.id.first_band_name);
            secondBandNameTextView = itemView.findViewById(R.id.second_band_name);
            concertLocationTextView = itemView.findViewById(R.id.concert_location);
            concertDateTextView = itemView.findViewById(R.id.concert_date);
        }
    }
}
