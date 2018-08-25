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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.AddEventsActivity;
import com.matthewtimmons.upcomingeventsapp.manager.DateHelper;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGameFragment extends Fragment{
    final int RATING_NULL = 0;
    final int RATING_E = 1;
    final int RATING_E_10 = 2;
    final int RATING_T= 3;
    final int RATING_M = 4;

    String currentUserId, gamePosterUrl, gameRating;
    TextView getSuggestionsTextView;
    ImageView posterImageView;
    EditText gameTitleEditText;
    SeekBar gameRatingSeekbar;
    Button addToMyGamesButton, addToAllGamesButton;
    List<String> releaseConsolesChecked;
    List<CheckBox> allCheckboxes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foresight_games, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSuggestionsTextView = getActivity().findViewById(R.id.get_suggestions_button);
        posterImageView = getActivity().findViewById(R.id.poster_image_view);
        gameTitleEditText = view.findViewById(R.id.game_title_edit_text);
//        gameRatingSpinner = view.findViewById(R.id.game_rating_spinner);
        gameRatingSeekbar = view.findViewById(R.id.rating_seekbar);
        addToMyGamesButton = getActivity().findViewById(R.id.add_to_my_events_button);
        addToAllGamesButton = getActivity().findViewById(R.id.add_to_all_events_button);
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

        getSuggestionsTextView.setVisibility(View.GONE);
        addToMyGamesButton.setText("Add to my games");
        Picasso.get().load(R.drawable.ic_games_blue).into(posterImageView);

        // Set Checkbox functionality
        for (final CheckBox checkBox : allCheckboxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) releaseConsolesChecked.add(checkBox.getText().toString());
                    else releaseConsolesChecked.remove(checkBox.getText().toString());
                }
            });
        }

        gameRatingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                gameRating = getGameRating(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        // Set up rating spinner
//        SpinnerAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.gameRatings, android.R.layout.simple_list_item_1);
//        gameRatingSpinner.setAdapter(adapter);
//        gameRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                gameRating = (String) adapterView.getItemAtPosition(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        addToMyGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCustomGame(currentUserId);
            }
        });

        addToAllGamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCustomGame("recommendations");
            }
        });
    }

    public void setCustomGame(String eventCreator) {
        if (!gameTitleEditText.getText().toString().equals("") &&
                !releaseConsolesChecked.isEmpty()) {
            final Map<String, Object> gameData = new HashMap<>();
            gameData.put("date", DateHelper.dateFormatDatabaseFriendly.format(AddEventsActivity.dateEntered));
            gameData.put("eventType", "games");
            gameData.put("title", gameTitleEditText.getText().toString());
            gameData.put("releaseConsoles", releaseConsolesChecked);
            gameData.put("eventCreator", eventCreator);
            gameData.put("rating", gameRating);
            if (AddEventsActivity.eventPosterUrl != null && !AddEventsActivity.eventPosterUrl.equals("")) {
                gameData.put("imageUrl", AddEventsActivity.eventPosterUrl);
            } else if (gamePosterUrl != null && !gamePosterUrl.equals("")) {
                gameData.put("imageUrl", gamePosterUrl);
            } else {
                gameData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
            }
            Firestore.collection("games").add(gameData);
            String toastMessage = eventCreator.equals("recommendations") ? "recommended for global adoption" : "added to your list of games.";
            Toast.makeText(getContext(), "Game has been " + toastMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Game title and consoles cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getGameRating(int ratingAsNumber) {
        switch (ratingAsNumber) {
            case RATING_E:
                return "E";
            case RATING_E_10:
                return "E-10";
            case RATING_T:
                return "T";
            case RATING_M:
                return "M";
            default:
                return "Rating Pending";
        }
    }
}
