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

import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder>{
    List<Game> games;

    public GameListAdapter(List<Game> games) {
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_game, viewGroup, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder gameViewHolder, int position) {
        final Game currentGame = games.get(position);
        Picasso.get().load(currentGame.getGameImageUrl()).error(R.drawable.ic_games_blue).into(gameViewHolder.gameArtImageView);
        gameViewHolder.titleTextView.setText(currentGame.getTitle());
        gameViewHolder.releaseConsolesTextView.setText(currentGame.getReleaseConsoles());
        gameViewHolder.releaseDateTextView.setText(currentGame.getGameReleaseDate());

        gameViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("thisGame", currentGame);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return games != null ? games.size() : 0; }

    class GameViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleTextView;
        TextView releaseConsolesTextView;
        TextView releaseDateTextView;
        ImageView gameArtImageView;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.game_card_view);
            titleTextView = itemView.findViewById(R.id.game_title);
            releaseConsolesTextView = itemView.findViewById(R.id.release_consoles);
            releaseDateTextView = itemView.findViewById(R.id.game_release_date);
            gameArtImageView = itemView.findViewById(R.id.game_picture);
        }
    }
}
