package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.AddEventsActivity;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGameFragment extends Fragment{
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    String currentUserId, gamePosterUrl;
    TextView welcomeTextView, getSuggestionsTextView;
    ImageView posterImageView;
    EditText gameTitleEditText, gameRatingEditText, gameReleaseDateEditText;
    Button addToMyGamesButton, addToAllGamesButton;
    List<String> releaseConsolesChecked;
    List<CheckBox> allCheckboxes;

    public static AddGameFragment newInstance(String currentUserId) {
        AddGameFragment addGameFragment = new AddGameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_USER_ID, currentUserId);
        addGameFragment.setArguments(bundle);
        return addGameFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foresight_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcomeTextView = getActivity().findViewById(R.id.add_event_type);
        getSuggestionsTextView = getActivity().findViewById(R.id.get_suggestions_button);
        posterImageView = getActivity().findViewById(R.id.poster_image_view);
        gameTitleEditText = view.findViewById(R.id.game_title_text_view);
        gameRatingEditText = view.findViewById(R.id.game_rating_text_view);
        gameReleaseDateEditText = view.findViewById(R.id.game_release_date_text_view);
        addToMyGamesButton = getActivity().findViewById(R.id.add_to_my_movies_button);
        addToAllGamesButton = getActivity().findViewById(R.id.add_to_all_movies_button);
        currentUserId = getArguments().getString(CURRENT_USER_ID);
        releaseConsolesChecked = new ArrayList<>();

        // Set Checkboxes
        allCheckboxes = new ArrayList<>();
        allCheckboxes.add((CheckBox) view.findViewById(R.id.pc_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.xbox_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.playstation_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.nintendo_switch_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.nintendo_3ds_checkbox));

        getSuggestionsTextView.setVisibility(View.GONE);
        addToMyGamesButton.setText("Add to my games");
        Picasso.get().load(R.drawable.ic_games_blue).into(posterImageView);

        addToMyGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameTitleEditText.getText().toString().equals("") &&
                    !gameRatingEditText.getText().toString().equals("") &&
                    !gameReleaseDateEditText.getText().toString().equals("") &&
                    !releaseConsolesChecked.isEmpty()) {
                    final Map<String, Object> gameData = new HashMap<>();
                    gameData.put("date", gameReleaseDateEditText.getText().toString());
                    gameData.put("eventType", "games");
                    gameData.put("rating", gameRatingEditText.getText().toString());
                    gameData.put("title", gameTitleEditText.getText().toString());
                    gameData.put("releaseConsoles", releaseConsolesChecked);
                    gameData.put("isCustomEvent", true);
                    if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
                        gameData.put("imageUrl", AddEventsActivity.moviePosterUrl);
                    } else if (gamePosterUrl != null && !gamePosterUrl.equals("")) {
                        gameData.put("imageUrl", gamePosterUrl);
                    } else {
                        gameData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
                        Toast.makeText(getContext(), "No game poster detected", Toast.LENGTH_SHORT).show();
                    }
                    Firestore.collection("users").document(currentUserId).collection("games").add(gameData);
                    Toast.makeText(getContext(), "Game has been added to your list of games.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addToAllGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameTitleEditText.getText().toString().equals("") &&
                    !gameRatingEditText.getText().toString().equals("") &&
                    !gameReleaseDateEditText.getText().toString().equals("") &&
                    !releaseConsolesChecked.isEmpty()) {
                    final Map<String, Object> gameData = new HashMap<>();
                    gameData.put("date", gameReleaseDateEditText.getText().toString());
                    gameData.put("eventType", "movies");
                    gameData.put("imageUrl", gamePosterUrl);
                    gameData.put("rating", gameRatingEditText.getText().toString());
                    gameData.put("title", gameTitleEditText.getText().toString());
                    gameData.put("releaseConsoles", releaseConsolesChecked);
                    gameData.put("isCustomEvent", true);
                    Firestore.collection("usersAuth").document("Suggested Additions").collection("games").add(gameData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(getContext(), "Game has been recommended for global adoption.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        for (final CheckBox checkBox : allCheckboxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        releaseConsolesChecked.add(checkBox.getText().toString());
                    } else {
                        releaseConsolesChecked.remove(checkBox.getText().toString());
                    }
                }
            });
        }
    }
}
