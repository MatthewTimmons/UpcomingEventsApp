package com.matthewtimmons.upcomingeventsapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.AddEventsActivity;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddMovieFragment extends Fragment{
    public static final int RATING_NULL = 0;
    public static final int RATING_G = 1;
    public static final int RATING_PG = 2;
    public static final int RATING_PG_13= 3;
    public static final int RATING_R = 4;

    String currentUserId, moviePosterUrl, movieRating;
    TextView welcomeTextView, getSuggestionsTextView;
    ImageView posterImageView;
    EditText movieTitleEditText, movieGenreEditText, movieReleaseDateEditText;
    Spinner movieRatingSpinner;
    Button addToMyMoviesButton, addToAllMoviesButton;
    SeekBar ratingsSeekbar;
    LinearLayout movieRatingIcons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foresight_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        welcomeTextView = view.findViewById(R.id.add_event_type);
        getSuggestionsTextView = getActivity().findViewById(R.id.get_suggestions_button);
        posterImageView = getActivity().findViewById(R.id.poster_image_view);
        movieTitleEditText = view.findViewById(R.id.movie_title_edit_text);
        movieGenreEditText = view.findViewById(R.id.movie_genre_edit_text);
        movieRatingSpinner = view.findViewById(R.id.movie_rating_spinner);
        movieReleaseDateEditText = view.findViewById(R.id.movie_release_date_edit_text);
        addToMyMoviesButton = getActivity().findViewById(R.id.add_to_my_movies_button);
        addToAllMoviesButton = getActivity().findViewById(R.id.add_to_all_movies_button);
        ratingsSeekbar = view.findViewById(R.id.rating_seekbar);
        movieRatingIcons = view.findViewById(R.id.movie_rating_icons);

        currentUserId = UserManager.getInstance().getCurrentUserId();
        movieRating = "Rating Pending";

        getSuggestionsTextView.setVisibility(View.VISIBLE);
        addToMyMoviesButton.setText("Add to my movies");
        Picasso.get().load(R.drawable.ic_movies_blue).into(posterImageView);

        // Set up rating spinner
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.movieRatings, android.R.layout.simple_list_item_1);
        movieRatingSpinner.setAdapter(adapter);
        movieRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                movieRating = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addToMyMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movieTitleEditText.getText().toString().equals("") &&
                        !movieGenreEditText.getText().toString().equals("") &&
                        !movieReleaseDateEditText.getText().toString().equals("")) {
                    final Map<String, Object> movieData = new HashMap<>();
                    movieData.put("date", movieReleaseDateEditText.getText().toString());
                    movieData.put("eventType", "movies");
                    movieData.put("genre", movieGenreEditText.getText().toString());
                    movieData.put("rating", movieRating);
                    movieData.put("title", movieTitleEditText.getText().toString());
                    movieData.put("eventCreator", currentUserId);
                    if (moviePosterUrl != null && !moviePosterUrl.equals("")) {
                        movieData.put("imageUrl", moviePosterUrl);
                    } else if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
                        movieData.put("imageUrl", AddEventsActivity.moviePosterUrl);
                    } else {
                        movieData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
                        Toast.makeText(getContext(), "No movie poster detected", Toast.LENGTH_SHORT).show();
                    }
                    Firestore.collection("movies").document(currentUserId).collection("movies").add(movieData);
                    Toast.makeText(getContext(), "Movie has been added to your list of movies.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO Come back and fix these animations
        ratingsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                movieRatingIcons.getChildAt(i).animate().translationY(-10).setDuration(500).start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekbarProgress = seekBar.getProgress();
                movieRatingIcons.getChildAt(seekbarProgress).animate().rotation(360).setDuration(1000);
            }
        });
//
//        addToAllMoviesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!movieTitleEditText.getText().toString().equals("") &&
//                        !movieGenreEditText.getText().toString().equals("") &&
//                        !movieRatingSpinner.getText().toString().equals("") &&
//                        !movieReleaseDateEditText.getText().toString().equals("")) {
//                    final Map<String, Object> movieData = new HashMap<>();
//                    movieData.put("date", movieReleaseDateEditText.getText().toString());
//                    movieData.put("eventType", "movies");
//                    movieData.put("genre", movieGenreEditText.getText().toString());
//                    movieData.put("rating", movieRatingSpinner.getText().toString());
//                    movieData.put("title", movieTitleEditText.getText().toString());
//                    movieData.put("isCustomEvent", true);
//                    if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
//                        movieData.put("imageUrl", AddEventsActivity.moviePosterUrl);
//                    } else if (moviePosterUrl != null && !moviePosterUrl.equals("")) {
//                        movieData.put("imageUrl", moviePosterUrl);
//                    } else {
//                        movieData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
//                        Toast.makeText(getContext(), "No movie poster detected", Toast.LENGTH_SHORT).show();
//                    }
//                    Firestore.collection("usersAuth").document("Suggested Additions").collection("movies").add(movieData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            Toast.makeText(getContext(), "Movie has been recommended for global adoption.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        getSuggestionsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView2 = getLayoutInflater().inflate(R.layout.dialog_get_suggestions, null);
                dialogBuilder.setView(dialogView2);
                final AlertDialog alertDialog2 = dialogBuilder.create();
                alertDialog2.show();

                final EditText searchTitle = dialogView2.findViewById(R.id.search_title_edit_text);
                final EditText searchYear = dialogView2.findViewById(R.id.search_year_edit_text);
                Button searchButton = dialogView2.findViewById(R.id.search_button);


                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                        String url = "http://www.omdbapi.com/?";
                        if (!searchTitle.getText().toString().equals("")) {
                            String titleSearchTerms = "t=" + searchTitle.getText().toString();
                            url += titleSearchTerms;
                            if (!searchYear.getText().toString().equals("")) {
                                String yearSearchTerms = "&y=" + searchYear.getText().toString();
                                url += yearSearchTerms;
                            }
                        }
                        url += "&apikey=33c658ff";

                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("Response").equals("False")) {
                                                Toast.makeText(getContext(), "No movies found", Toast.LENGTH_SHORT).show();
                                            } else {
                                                movieTitleEditText.setText(response.getString("Title"));
                                                movieGenreEditText.setText(response.getString("Genre"));
                                                movieReleaseDateEditText.setText(response.getString("Released"));
                                                moviePosterUrl = response.getString("Poster");

                                                switch (response.getString("Rated")) {
                                                    case "N/A":
                                                        movieRatingSpinner.setSelection(0);
                                                        ratingsSeekbar.setProgress(0);
                                                        break;
                                                    case "G":
                                                        movieRatingSpinner.setSelection(1);
                                                        ratingsSeekbar.setProgress(1);
                                                        break;
                                                    case "PG":
                                                        movieRatingSpinner.setSelection(2);
                                                        ratingsSeekbar.setProgress(2);
                                                        break;
                                                    case "PG-13":
                                                        movieRatingSpinner.setSelection(3);
                                                        ratingsSeekbar.setProgress(3);
                                                        break;
                                                    case "R":
                                                        movieRatingSpinner.setSelection(4);
                                                        ratingsSeekbar.setProgress(4);
                                                        movieRatingIcons.getChildAt(4).animate().rotation(360).setDuration(500).start();
                                                        break;
                                                }
                                                alertDialog2.cancel();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Picasso.get().load(response.getString("Poster")).into(posterImageView);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error

                                    }
                                });

                        requestQueue.add(jsonObjectRequest);
                    }
                });
            }
        });

        // Access the RequestQueue through your singleton class.
        //        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
