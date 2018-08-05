package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
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
import com.matthewtimmons.upcomingeventsapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import io.opencensus.internal.StringUtil;

public class ForesightActivity extends AppCompatActivity {
    String[] formattedSearchTerms;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foresight);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_welcome, null);
        final TextView welcomeTextView = findViewById(R.id.welcome_text_view);
        final TextView getSuggestionsTextView = findViewById(R.id.get_suggestions_button);
        Button dismissMessageButton = dialogView.findViewById(R.id.dismiss_dialog_button);
        final EditText movieTitleEditText = findViewById(R.id.movie_title_text_view);
        final EditText movieGenreEditText = findViewById(R.id.movie_genre_text_view);
        final EditText movieRatingEditText = findViewById(R.id.movie_rating_text_view);
        final EditText movieReleaseDateEditText = findViewById(R.id.movie_release_date_text_view);



        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        dismissMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
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
                EditText searchYear = dialogView2.findViewById(R.id.search_year_edit_text);
                Button searchButton = dialogView2.findViewById(R.id.search_button);


                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestQueue requestQueue = Volley.newRequestQueue(ForesightActivity.this);
//                        String titleSearchTerms = "t=" + searchTitle.getText().toString();
                        String titleSearchTerms = "t=" + searchTitle.getText().toString();
//                        String yearSearchTerms = "y=" + movieReleaseDateEditText.getText().toString();

//                formattedSearchTerms = TextUtils.split(searchTerms, " ");
//                searchTerms = TextUtils.join("20%", formattedSearchTerms);
                        String url = "http://www.omdbapi.com/?" + titleSearchTerms  + "&apikey=33c658ff";
                        //        String url = "http://www.omdbapi.com/?i=tt3896198&apikey=33c658ff";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            movieTitleEditText.setText(response.getString("Title"));
                                            movieGenreEditText.setText(response.getString("Genre"));
                                            movieRatingEditText.setText(response.getString("Rated"));
                                            movieReleaseDateEditText.setText(response.getString("Released"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            Picasso.get().load(response.getString("Poster")).into((ImageView) findViewById(R.id.poster_image_view));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

//                                Toast.makeText(ForesightActivity.this, "Successful yay!", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        //                        Toast.makeText(ForesightActivity.this, "Failed the thing", Toast.LENGTH_SHORT).show();
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
