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

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder>{
    List<DocumentSnapshot> games;

    public GameListAdapter(List<DocumentSnapshot> games) {
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_game, viewGroup, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder gameViewHolder, int position) {

        final DocumentSnapshot currentGameDocumentSnapshot = games.get(position);

//        final Game currentGame = games.get(position);
//        Picasso.get().load(currentGame.getGameImageUrl()).error(R.drawable.ic_games_blue).into(gameViewHolder.gameArtImageView);
//        gameViewHolder.titleTextView.setText(currentGame.getTitle());
//        gameViewHolder.releaseConsolesTextView.setText(currentGame.getReleaseConsoles());
//        gameViewHolder.releaseDateTextView.setText(currentGame.getGameReleaseDate());

        Picasso.get().load(currentGameDocumentSnapshot.getString("gameImageUrl")).error(R.drawable.ic_games_blue).into(gameViewHolder.gameArtImageView);
        gameViewHolder.titleTextView.setText(currentGameDocumentSnapshot.getString("gameTitle"));
        gameViewHolder.releaseConsolesTextView.setText(currentGameDocumentSnapshot.getString("gameReleaseConsoles"));
        gameViewHolder.releaseDateTextView.setText(currentGameDocumentSnapshot.getString("gameReleaseDate"));

        gameViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DetailsActivity.newIntent(view.getContext(), currentGameDocumentSnapshot.getId(), EventConstants.EVENT_TYPE_GAME);
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
