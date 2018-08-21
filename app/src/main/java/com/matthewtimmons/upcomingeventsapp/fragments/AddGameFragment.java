package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.AddEventsActivity;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGameFragment extends Fragment{
    String currentUserId, gamePosterUrl, gameRating;
    TextView welcomeTextView, getSuggestionsTextView;
    ImageView posterImageView;
    EditText gameTitleEditText;
    Spinner gameRatingSpinner;
    Button addToMyGamesButton, addToAllGamesButton;
    List<String> releaseConsolesChecked;
    List<CheckBox> allCheckboxes;
    NumberPicker monthNumberPicker, dayNumberPicker, yearNumberPicker;

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
        gameTitleEditText = view.findViewById(R.id.game_title_edit_text);
        gameRatingSpinner = view.findViewById(R.id.game_rating_spinner);
        addToMyGamesButton = getActivity().findViewById(R.id.add_to_my_movies_button);
        addToAllGamesButton = getActivity().findViewById(R.id.add_to_all_movies_button);
        currentUserId = UserManager.getInstance().getCurrentUserId();
        gameRating = "Rating Pending";
        releaseConsolesChecked = new ArrayList<>();

        // Set Checkboxes
        allCheckboxes = new ArrayList<>();
        allCheckboxes.add((CheckBox) view.findViewById(R.id.pc_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.xbox_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.playstation_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.nintendo_switch_checkbox));
        allCheckboxes.add((CheckBox) view.findViewById(R.id.nintendo_3ds_checkbox));

        // Set up date number picker
        monthNumberPicker = getActivity().findViewById(R.id.month_picker);
        dayNumberPicker = getActivity().findViewById(R.id.day_picker);
        yearNumberPicker = getActivity().findViewById(R.id.year_picker);

        // Set up rating spinner
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.gameRatings, android.R.layout.simple_list_item_1);
        gameRatingSpinner.setAdapter(adapter);
        gameRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gameRating = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getSuggestionsTextView.setVisibility(View.GONE);
        addToMyGamesButton.setText("Add to my games");
        Picasso.get().load(R.drawable.ic_games_blue).into(posterImageView);

        addToMyGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameTitleEditText.getText().toString().equals("") &&
                    !releaseConsolesChecked.isEmpty()) {
                    StringBuilder formattedDate = new StringBuilder();
                    formattedDate.append((String.format("%02d", monthNumberPicker.getValue())) + "/" +
                            String.format("%02d", dayNumberPicker.getValue()) + "/" +
                            String.valueOf(yearNumberPicker.getValue()));

                    final Map<String, Object> gameData = new HashMap<>();
                    gameData.put("date", formattedDate.toString());
                    gameData.put("eventType", "games");
                    gameData.put("rating", gameRating);
                    gameData.put("title", gameTitleEditText.getText().toString());
                    gameData.put("releaseConsoles", releaseConsolesChecked);
                    gameData.put("eventCreator", currentUserId);
                    if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
                        gameData.put("imageUrl", AddEventsActivity.moviePosterUrl);
                    } else if (gamePosterUrl != null && !gamePosterUrl.equals("")) {
                        gameData.put("imageUrl", gamePosterUrl);
                    } else {
                        gameData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
                        Toast.makeText(getContext(), "No game poster detected", Toast.LENGTH_SHORT).show();
                    }
                    Firestore.collection("games").document(currentUserId).collection("games").add(gameData);
                    Toast.makeText(getContext(), "Game has been added to your list of games.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
//        addToAllGamesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!gameTitleEditText.getText().toString().equals("") &&
//                    !gameRatingEditText.getText().toString().equals("") &&
//                    !gameReleaseDateEditText.getText().toString().equals("") &&
//                    !releaseConsolesChecked.isEmpty()) {
//                    final Map<String, Object> gameData = new HashMap<>();
//                    gameData.put("date", gameReleaseDateEditText.getText().toString());
//                    gameData.put("eventType", "movies");
//                    gameData.put("imageUrl", gamePosterUrl);
//                    gameData.put("rating", gameRatingEditText.getText().toString());
//                    gameData.put("title", gameTitleEditText.getText().toString());
//                    gameData.put("releaseConsoles", releaseConsolesChecked);
//                    gameData.put("isCustomEvent", true);
//                    Firestore.collection("usersAuth").document("Suggested Additions").collection("games").add(gameData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            Toast.makeText(getContext(), "Game has been recommended for global adoption.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

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
