package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.AddEventsActivity;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddMovieFragment extends Fragment{
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    String currentUserId, moviePosterUrl;
    TextView welcomeTextView, getSuggestionsTextView;
    ImageView posterImageView;
    EditText movieTitleEditText, movieGenreEditText, movieRatingEditText, movieReleaseDateEditText;
    Button addToMyMoviesButton, addToAllMoviesButton;

    public static AddMovieFragment newInstance(String currentUserId) {
        AddMovieFragment foresightMoviesFragment = new AddMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_USER_ID, currentUserId);
        foresightMoviesFragment.setArguments(bundle);
        return foresightMoviesFragment;
    }

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
        movieTitleEditText = view.findViewById(R.id.movie_title_text_view);
        movieGenreEditText = view.findViewById(R.id.movie_genre_text_view);
        movieRatingEditText = view.findViewById(R.id.movie_rating_text_view);
        movieReleaseDateEditText = view.findViewById(R.id.movie_release_date_text_view);
        addToMyMoviesButton = getActivity().findViewById(R.id.add_to_my_movies_button);
        addToAllMoviesButton = getActivity().findViewById(R.id.add_to_all_movies_button);

        currentUserId = getArguments().getString(CURRENT_USER_ID);

        getSuggestionsTextView.setVisibility(View.VISIBLE);
        addToMyMoviesButton.setText("Add to my movies");
        Picasso.get().load(R.drawable.ic_movies_blue).into(posterImageView);

        addToMyMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movieTitleEditText.getText().toString().equals("") &&
                        !movieGenreEditText.getText().toString().equals("") &&
                        !movieRatingEditText.getText().toString().equals("") &&
                        !movieReleaseDateEditText.getText().toString().equals("")) {
                    final Map<String, Object> movieData = new HashMap<>();
                    movieData.put("date", movieReleaseDateEditText.getText().toString());
                    movieData.put("eventType", "movies");
                    movieData.put("genre", movieGenreEditText.getText().toString());
                    movieData.put("rating", movieRatingEditText.getText().toString());
                    movieData.put("title", movieTitleEditText.getText().toString());
                    movieData.put("isCustomEvent", true);
                    if (moviePosterUrl != null && !moviePosterUrl.equals("")) {
                        movieData.put("imageUrl", moviePosterUrl);
                    } else if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
                        movieData.put("imageUrl", AddEventsActivity.moviePosterUrl);
                    } else {
                        movieData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
                        Toast.makeText(getContext(), "No movie poster detected", Toast.LENGTH_SHORT).show();
                    }
                    Firestore.collection("users").document(currentUserId).collection("movies").add(movieData);
                    Toast.makeText(getContext(), "Movie has been added to your list of movies.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addToAllMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movieTitleEditText.getText().toString().equals("") &&
                        !movieGenreEditText.getText().toString().equals("") &&
                        !movieRatingEditText.getText().toString().equals("") &&
                        !movieReleaseDateEditText.getText().toString().equals("")) {
                    final Map<String, Object> movieData = new HashMap<>();
                    movieData.put("date", movieReleaseDateEditText.getText().toString());
                    movieData.put("eventType", "movies");
                    movieData.put("genre", movieGenreEditText.getText().toString());
                    movieData.put("rating", movieRatingEditText.getText().toString());
                    movieData.put("title", movieTitleEditText.getText().toString());
                    movieData.put("isCustomEvent", true);
                    if (AddEventsActivity.moviePosterUrl != null && !AddEventsActivity.moviePosterUrl.equals("")) {
                        movieData.put("imageUrl", AddEventsActivity.moviePosterUrl);
                    } else if (moviePosterUrl != null && !moviePosterUrl.equals("")) {
                        movieData.put("imageUrl", moviePosterUrl);
                    } else {
                        movieData.put("imageUrl", "https://thewindowsclub-thewindowsclubco.netdna-ssl.com/wp-content/uploads/2018/06/Broken-image-icon-in-Chrome.gif");
                        Toast.makeText(getContext(), "No movie poster detected", Toast.LENGTH_SHORT).show();
                    }
                    Firestore.collection("usersAuth").document("Suggested Additions").collection("movies").add(movieData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(getContext(), "Movie has been recommended for global adoption.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "All fields must be entered", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                                                movieRatingEditText.setText(response.getString("Rated"));
                                                movieReleaseDateEditText.setText(response.getString("Released"));
                                                moviePosterUrl = response.getString("Poster");
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
