package com.matthewtimmons.upcomingeventsapp.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Game;

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
        Game currentGame = games.get(position);
        gameViewHolder.titleTextView.setText(currentGame.getTitle());
        gameViewHolder.releaseConsolesTextView.setText(currentGame.getReleaseConsoles());
        gameViewHolder.releaseDateTextView.setText(currentGame.getReleaseDate());
//
//      Update to actual image later
        gameViewHolder.gameArtImageView.setImageResource(R.drawable.ic_games_blue);
    }

    @Override
    public int getItemCount() { return games != null ? games.size() : 0; }

    class GameViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView releaseConsolesTextView;
        TextView releaseDateTextView;
        ImageView gameArtImageView;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.game_title);
            releaseConsolesTextView = itemView.findViewById(R.id.release_consoles);
            releaseDateTextView = itemView.findViewById(R.id.game_release_date);
            gameArtImageView = itemView.findViewById(R.id.game_picture);
        }
    }
}
