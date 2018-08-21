package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.AddConcertFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.AddGameFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.AddMovieFragment;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

import java.util.Calendar;
import java.util.Date;

public class AddEventsActivity extends AppCompatActivity {
    public static String moviePosterUrl;
    String currentUserId;
    TextView addEventTypeTextView, getSuggestionsTextView;
    ImageView posterImageView;
    Button addToMyMoviesButton, addToAllMoviesButton;
    NumberPicker monthNumberPicker, dayNumberPicker, yearNumberPicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        addEventTypeTextView = findViewById(R.id.add_event_type);
        getSuggestionsTextView = findViewById(R.id.get_suggestions_button);
        posterImageView = findViewById(R.id.poster_image_view);
        addToMyMoviesButton = findViewById(R.id.add_to_my_movies_button);
        addToAllMoviesButton = findViewById(R.id.add_to_all_movies_button);

        currentUserId = UserManager.getInstance().getCurrentUserId();

        setDateNumberPicker();

        final Spinner spinner = findViewById(R.id.add_event_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.names));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    addEventTypeTextView.setText("Add New Movie");
                    AddMovieFragment addMovieFragment = new AddMovieFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.add_event_fragment_container, addMovieFragment).commit();
                } else if (i == 1) {
                    addEventTypeTextView.setText("Add New Game");
                    AddGameFragment addGameFragment = new AddGameFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.add_event_fragment_container, addGameFragment).commit();
                } else if (i == 2) {
                    addEventTypeTextView.setText("Add New Concert");
                    AddConcertFragment addConcertFragment = new AddConcertFragment();
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

    private void setDateNumberPicker() {
        Date date = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        monthNumberPicker = findViewById(R.id.month_picker);
        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setDisplayedValues(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});

        dayNumberPicker = findViewById(R.id.day_picker);
        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        dayNumberPicker.setWrapSelectorWheel(false);

        yearNumberPicker = findViewById(R.id.year_picker);
        yearNumberPicker.setMinValue(1950);
        yearNumberPicker.setMaxValue(2028);
        yearNumberPicker.setWrapSelectorWheel(false);

        monthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if (i1 == 2) {
                    dayNumberPicker.setMaxValue(28);
                } else if (i1 == 9 | i1 == 4 | i1 == 6 | i1 == 11) {
                    dayNumberPicker.setMaxValue(30);
                } else {
                    dayNumberPicker.setMaxValue(31);
                }
            }
        });

        monthNumberPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        dayNumberPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        yearNumberPicker.setValue(calendar.get(Calendar.YEAR));
    }

}
