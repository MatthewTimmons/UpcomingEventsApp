package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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
import com.google.android.gms.flags.impl.DataUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.internal.StringUtil;

public class ForesightActivity extends AppCompatActivity {
    String moviePosterUrl;
    TextView welcomeTextView;
    TextView getSuggestionsTextView;
    Button dismissMessageButton;
    EditText movieTitleEditText;
    EditText movieGenreEditText;
    EditText movieRatingEditText;
    EditText movieReleaseDateEditText;
    Button addToMyMoviesButton;
    Button addToAllMoviesButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foresight);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_welcome, null);
        welcomeTextView = findViewById(R.id.welcome_text_view);
        getSuggestionsTextView = findViewById(R.id.get_suggestions_button);
        dismissMessageButton = dialogView.findViewById(R.id.dismiss_dialog_button);
        movieTitleEditText = findViewById(R.id.movie_title_text_view);
        movieGenreEditText = findViewById(R.id.movie_genre_text_view);
        movieRatingEditText = findViewById(R.id.movie_rating_text_view);
        movieReleaseDateEditText = findViewById(R.id.movie_release_date_text_view);
        addToMyMoviesButton = findViewById(R.id.add_to_my_movies_button);
        addToAllMoviesButton = findViewById(R.id.add_to_all_movies_button);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        dismissMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

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
                    movieData.put("imageUrl", moviePosterUrl);
                    movieData.put("movieGenre", movieGenreEditText.getText().toString());
                    movieData.put("movieRating", movieRatingEditText.getText().toString());
                    movieData.put("title", movieTitleEditText.getText().toString());
                    movieData.put("isCustomEvent", true);
                    Firestore.collection("movies").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                task.getResult().getReference().collection("movies").add(movieData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(ForesightActivity.this, "Movie has been added to your list of movies.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Firestore.collection("movies").document(currentUserId).collection("movies").add(movieData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(ForesightActivity.this, "Movie has been added to your list of movies.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
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
                    movieData.put("imageUrl", "https://i.pinimg.com/originals/cf/15/09/cf1509164702e231902c98f65fb35371.jpg");
                    movieData.put("movieGenre", movieGenreEditText.getText().toString());
                    movieData.put("movieRating", movieRatingEditText.getText().toString());
                    movieData.put("title", movieTitleEditText.getText().toString());
                    Firestore.collection("movies").document("Suggested Additions").collection("movies").add(movieData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(ForesightActivity.this, "Movie has been recommended for global adoption.", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                    public void onClick(View view) {
                        RequestQueue requestQueue = Volley.newRequestQueue(ForesightActivity.this);

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
                                                Toast.makeText(ForesightActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
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
                                            Picasso.get().load(response.getString("Poster")).into((ImageView) findViewById(R.id.poster_image_view));
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
