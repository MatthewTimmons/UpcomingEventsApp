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
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameDetailsFragment extends Fragment {
    ImageView wideGameImageView;
    TextView gameTitleTextView;
    TextView gameReleaseConsolesTextView;
    TextView gameReleaseDateTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.details_game, container, false);

        // Get current Game from activity
        DetailsActivity activity = (DetailsActivity) getActivity();
        final String thisGameId = activity.getCurrentGame();

        CollectionReference gamesCollectionReference = FirebaseFirestore.getInstance().collection("games");
        gamesCollectionReference.whereEqualTo("gameId", thisGameId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final DocumentSnapshot thisGameDocumentSnapshot = task.getResult().getDocuments().get(0);

                // Assign all views to variables
                wideGameImageView = v.findViewById(R.id.wide_image);
                gameTitleTextView = v.findViewById(R.id.game_title);
                gameReleaseConsolesTextView = v.findViewById(R.id.game_release_consoles);
                gameReleaseDateTextView = v.findViewById(R.id.game_release_date);

                // Get all values for this Game
                String imageURL = thisGameDocumentSnapshot.getString("gameImageUrl");
                final String gameTitle = thisGameDocumentSnapshot.getString("gameTitle");
                String gameReleaseConsoles = thisGameDocumentSnapshot.getString("gameReleaseConsoles");
                String gameReleaseDate = thisGameDocumentSnapshot.getString("gameReleaseDate");

                // Assign values to each view
                Picasso.get().load(imageURL).error(R.drawable.ic_games_blue).into(wideGameImageView);
                gameTitleTextView.setText(gameTitle);
                gameReleaseConsolesTextView.setText(gameReleaseConsoles);
                gameReleaseDateTextView.setText(gameReleaseDate);

                FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        DocumentSnapshot MattDocumentSnapshot = task.getResult().getDocuments().get(0);
//                        if (MattDocumentSnapshot.get("games") != null) {
//                            Map<String, Object> games = (Map<String, Object>) MattDocumentSnapshot.get("games");
//                            if (games.get(gameTitle) != null) {
//                                Number interest = (Number) games.get("Super Mario Party");
//                                Toast.makeText(getContext(), "Matt's Interest Level = " + interest, Toast.LENGTH_LONG).show();
//                                if (games.get("TestHeader") != null) {
//                                    Object testHeader = games.get("TestHeader");
//                                    Toast.makeText(getContext(), "No way!!" + testHeader, Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }

                        try {
                            Map<String, Object> interestLevels = (Map<String, Object>) MattDocumentSnapshot.get("interestLevels");
                            Map<String, Object> games = (Map<String, Object>) interestLevels.get("games");
                            Number interestLevelValue = (Number) games.get(gameTitle);
                            String interestLevelIntValue = interestLevelValue.toString();
                            Toast.makeText(getContext(), interestLevelIntValue, Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });



            }
        });

        return v;
    }
}
