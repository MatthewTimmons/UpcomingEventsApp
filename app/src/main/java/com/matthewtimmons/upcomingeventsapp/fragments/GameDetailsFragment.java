package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.squareup.picasso.Picasso;

public class GameDetailsFragment extends Fragment {
    ImageView wideGameImageView;
    TextView gameTitleTextView;
    TextView gameReleaseConsolesTextView;
    TextView gameReleaseDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_game, container, false);

        // Get current Concert from activity
        DetailsActivity activity = (DetailsActivity) getActivity();
        Game thisGame = activity.getCurrentGame();

        // Assign all views to variables
        wideGameImageView = v.findViewById(R.id.wide_image);
        gameTitleTextView = v.findViewById(R.id.game_title);
        gameReleaseConsolesTextView = v.findViewById(R.id.game_release_consoles);
        gameReleaseDateTextView = v.findViewById(R.id.game_release_date);

        // Get all values for this Game
        String imageURL = thisGame.getGameImageUrl();
        String gameTitle = thisGame.getTitle();
        String gameReleaseConsoles = thisGame.getReleaseConsoles();
        String gameReleaseDate = thisGame.getGameReleaseDate();

        // Assign values to each view
        Picasso.get().load(imageURL).error(R.drawable.ic_games_blue).into(wideGameImageView);
        gameTitleTextView.setText(gameTitle);
        gameReleaseConsolesTextView.setText(gameReleaseConsoles);
        gameReleaseDateTextView.setText(gameReleaseDate);

        return v;
    }
}
