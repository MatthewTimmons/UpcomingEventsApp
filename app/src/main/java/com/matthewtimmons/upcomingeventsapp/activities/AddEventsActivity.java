package com.matthewtimmons.upcomingeventsapp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.fragments.AddConcertFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.AddGameFragment;
import com.matthewtimmons.upcomingeventsapp.fragments.AddMovieFragment;
import com.matthewtimmons.upcomingeventsapp.manager.DateHelper;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

import java.util.Calendar;
import java.util.Date;

public class AddEventsActivity extends AppCompatActivity {
    public static String eventPosterUrl;

    String currentUserId;
    TextView addEventTypeTextView, getSuggestionsTextView, dateTextView;
    ImageView posterImageView, editDateIcon;
    Button addToMyEventsButton, addToAllEventsButton;
    Spinner eventTypeSpinner;

    public static Date dateEntered, todaysDate;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        // Set views
        eventTypeSpinner = findViewById(R.id.add_event_spinner);
        addEventTypeTextView = findViewById(R.id.add_event_type);
        getSuggestionsTextView = findViewById(R.id.get_suggestions_button);
        posterImageView = findViewById(R.id.poster_image_view);
        addToMyEventsButton = findViewById(R.id.add_to_my_events_button);
        addToAllEventsButton = findViewById(R.id.add_to_all_events_button);
        dateTextView = findViewById(R.id.date_picker);
        editDateIcon = findViewById(R.id.edit_date_icon);

        currentUserId = UserManager.getInstance().getCurrentUserId();

        setDatePickerDialog(dateTextView, editDateIcon);

        // Set up spinner in toolbar
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.names));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(spinnerAdapter);
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dateTextView.setText(DateHelper.dateFormatHumanReadable.format(todaysDate));
                dateEntered = todaysDate;

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

        // Set up functionality to add custom poster via URL or upload from phone
        posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddEventsActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_submit_photo, null);

                // Set Dialog internal views
                final EditText moviePosterURLEditText = dialogView.findViewById(R.id.image_url_edit_text);
                Button submitButton = dialogView.findViewById(R.id.submit_url_button);
                Button getPhotoFromPhoneButton = dialogView.findViewById(R.id.get_photo_from_phone_button);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventPosterUrl = moviePosterURLEditText.getText().toString();
                        alertDialog.cancel();
                    }
                });

                getPhotoFromPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AddEventsActivity.this, "This feature is not yet available", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setDatePickerDialog(final TextView dateTextView, ImageView editDateIcon) {
        todaysDate = calendar.getTime();
        dateEntered = calendar.getTime();

        editDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setTime(dateEntered);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventsActivity.this,
                        0, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                dateEntered = calendar.getTime();
                dateTextView.setText(DateHelper.dateFormatHumanReadable.format(dateEntered));
            }
        };
    }
}
