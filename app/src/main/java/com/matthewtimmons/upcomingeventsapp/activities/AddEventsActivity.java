package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.AddConcertFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.AddGameFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.AddMovieFragment;
import com.matthewtimmons.upcomingeventsapp.models.User;

public class AddEventsActivity extends AppCompatActivity {
    ConstraintLayout fragmentContainer;
    String currentUserId;
    public static String moviePosterUrl;
    TextView getSuggestionsTextView;
    ImageView posterImageView;
    Button addToMyMoviesButton;
    Button addToAllMoviesButton;

    EditText movieTitleEditText;
    EditText movieGenreEditText;
    EditText movieRatingEditText;
    EditText movieReleaseDateEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        fragmentContainer = findViewById(R.id.add_event_fragment_container);
        getSuggestionsTextView = findViewById(R.id.get_suggestions_button);
        posterImageView = findViewById(R.id.poster_image_view);
        addToMyMoviesButton = findViewById(R.id.add_to_my_movies_button);
        addToAllMoviesButton = findViewById(R.id.add_to_all_movies_button);

        currentUserId = getIntent().getStringExtra(User.CURRENT_USER_ID);

        final Spinner spinner = findViewById(R.id.add_event_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.names));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    AddMovieFragment addMovieFragment = AddMovieFragment.newInstance(currentUserId);
                    getSupportFragmentManager().beginTransaction().replace(R.id.add_event_fragment_container, addMovieFragment).commit();
                } else if (i == 1) {
                    AddGameFragment addGameFragment = AddGameFragment.newInstance(currentUserId);
                    getSupportFragmentManager().beginTransaction().replace(R.id.add_event_fragment_container, addGameFragment).commit();
                } else if (i == 2) {
                    AddConcertFragment addConcertFragment = AddConcertFragment.newInstance(currentUserId);
                    getSupportFragmentManager().beginTransaction().replace(R.id.add_event_fragment_container, addConcertFragment).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddEventsActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.dialog_submit_photo, null);
                final EditText moviePosterURLEditText = dialogView.findViewById(R.id.image_url_edit_text);
                Button submitButton = dialogView.findViewById(R.id.submit_url_button);
                Button getPhotoFromPhoneButton = dialogView.findViewById(R.id.get_photo_from_phone_button);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moviePosterUrl = moviePosterURLEditText.getText().toString();
                        alertDialog.cancel();
                    }
                });
            }
        });
    }

}
