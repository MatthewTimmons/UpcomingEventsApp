package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.matthewtimmons.upcomingeventsapp.DetailsActivity;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameDetailsFragment extends Fragment {
    private static final String ARG_GAME_ID = "gameId";
    String gameId;
    ImageView wideGameImageView;
    TextView gameTitleTextView;
    TextView gameReleaseConsolesTextView;
    TextView gameReleaseDateTextView;

    public static GameDetailsFragment newInstance(String gameId) {
        GameDetailsFragment instance = new GameDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GAME_ID, gameId);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.details_game, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            gameId = bundle.getString(ARG_GAME_ID);
        }


        // Get current Game from activity
        //TODO: Change this to be like movie detail fragment
        CollectionReference gamesCollectionReference = FirebaseFirestore.getInstance().collection(FirebaseConstants.KEY_GAMES);
        gamesCollectionReference.document(gameId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot gameDocumentSnapshot = task.getResult();

                // Assign all views to variables
                wideGameImageView = v.findViewById(R.id.wide_image);
                gameTitleTextView = v.findViewById(R.id.game_title);
                gameReleaseConsolesTextView = v.findViewById(R.id.game_release_consoles);
                gameReleaseDateTextView = v.findViewById(R.id.game_release_date);

                // Get all values for this Game
                String imageURL = gameDocumentSnapshot.getString("gameImageUrl");
                final String gameTitle = gameDocumentSnapshot.getString("gameTitle");
                String gameReleaseConsoles = gameDocumentSnapshot.getString("gameReleaseConsoles");
                String gameReleaseDate = gameDocumentSnapshot.getString("gameReleaseDate");

                // Assign values to each view
                Picasso.get().load(imageURL).error(R.drawable.ic_games_blue).into(wideGameImageView);
                gameTitleTextView.setText(gameTitle);
                gameReleaseConsolesTextView.setText(gameReleaseConsoles);
                gameReleaseDateTextView.setText(gameReleaseDate);

                // TODO: Set seekbar in Events Fragment
//                activity.setSeekbarToCurrentInterestLevel(getContext(), "games", thisGameId);
            }
        });

        return v;
    }
}
