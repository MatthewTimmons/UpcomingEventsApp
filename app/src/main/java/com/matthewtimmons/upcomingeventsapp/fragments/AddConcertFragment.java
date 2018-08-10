package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class AddConcertFragment extends Fragment {
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    String currentUserId;
    String concertPosterUrl;
    TextView welcomeTextView;
    TextView getSuggestionsTextView;
    ImageView posterImageView;
    ListView bandsListView;
    Integer minHeight;
    Button addBandNameButton;
    EditText addBandNameEditText;
    EditText concertLocationEditText;
    EditText concertDateEditText;
    EditText gameReleaseDateEditText;
    Button addToMyConcertsButton;
    Button addToAllConcertsButton;
    List<String> releaseConsolesChecked;
    List<CheckBox> allCheckboxes;
    CheckBox pcCheckbox;
    CheckBox xboxCheckbox;
    CheckBox playstationCheckbox;
    CheckBox nintendoSwitchCheckbox;
    CheckBox nintendo3DSCheckbox;

    TextView firstColumnNameTextView;
    String eventId;
    String firstColumnName;

    public static AddConcertFragment newInstance(String currentUserId) {
        AddConcertFragment addConcertFragment = new AddConcertFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_USER_ID, currentUserId);
        addConcertFragment.setArguments(bundle);
        return addConcertFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foresight_concerts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcomeTextView = getActivity().findViewById(R.id.welcome_text_view);
        getSuggestionsTextView = getActivity().findViewById(R.id.get_suggestions_button);
        posterImageView = getActivity().findViewById(R.id.poster_image_view);
        bandsListView = view.findViewById(R.id.list_of_bands_linear_layout);
        addBandNameButton = view.findViewById(R.id.add_band_name_button);
        addBandNameEditText = view.findViewById(R.id.add_band_name_edit_text);
        concertLocationEditText = view.findViewById(R.id.concert_location_edit_text);
        concertDateEditText = view.findViewById(R.id.concert_date_edit_text);
        addToMyConcertsButton = getActivity().findViewById(R.id.add_to_my_movies_button);
        addToAllConcertsButton = getActivity().findViewById(R.id.add_to_all_movies_button);
        currentUserId = getArguments().getString(CURRENT_USER_ID);
        minHeight = 0;

        getSuggestionsTextView.setVisibility(View.GONE);
        addToMyConcertsButton.setText("Add to my concerts");
        Picasso.get().load(R.drawable.ic_concerts_blue).into(posterImageView);

        final ArrayList<String> bandNames = new ArrayList<>();
        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, bandNames);
        bandsListView.setAdapter(stringArrayAdapter);

        // Set functionality for adding bands to listview
        addBandNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bandNameEntered = addBandNameEditText.getText().toString();
                stringArrayAdapter.add(bandNameEntered);
                addBandNameEditText.setText("");
                minHeight += 130;
                ViewGroup.LayoutParams params = bandsListView.getLayoutParams();
                params.height = minHeight;
                bandsListView.setLayoutParams(params);
                bandsListView.requestLayout();
                addBandNameEditText.requestFocus();
                Toast.makeText(getContext(), minHeight.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        addToMyConcertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!concertLocationEditText.getText().toString().equals("") &&
                        !concertDateEditText.getText().toString().equals("") &&
                        !bandNames.isEmpty()) {
                    final Map<String, Object> concertData = new HashMap<>();
                    concertData.put("concertLocation", concertLocationEditText.getText().toString());
                    concertData.put("date", concertDateEditText.getText().toString());
                    concertData.put("eventType", "concerts");
                    concertData.put("imageUrl", concertPosterUrl);
                    concertData.put("concertBandsArray", bandNames);
                    concertData.put("isCustomEvent", true);
                    if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
                        concertData.put("imageUrl", AddEventsActivity.moviePosterUrl);
                    } else if (concertPosterUrl != null && !concertPosterUrl.equals("")) {
                        concertData.put("imageUrl", concertPosterUrl);
                    } else {
                        concertData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
                        Toast.makeText(getContext(), "No concert poster detected", Toast.LENGTH_SHORT).show();
                    }
                    Firestore.collection("users").document(currentUserId).collection("concerts").add(concertData);
                    Toast.makeText(getContext(), "Concert has been added to your list of concerts.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addToAllConcertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!concertLocationEditText.getText().toString().equals("") &&
                        !concertDateEditText.getText().toString().equals("") &&
                        !bandNames.isEmpty()) {
                    final Map<String, Object> concertData = new HashMap<>();
                    concertData.put("concertLocation", concertLocationEditText.getText().toString());
                    concertData.put("date", concertDateEditText.getText().toString());
                    concertData.put("eventType", "concerts");
                    concertData.put("imageUrl", concertPosterUrl);
                    concertData.put("concertBandsArray", bandNames);
                    concertData.put("isCustomEvent", true);
                    Firestore.collection("usersAuth").document("Suggested Additions").collection("concerts").add(concertData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(getContext(), "Concert has been recommended for global adoption.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
