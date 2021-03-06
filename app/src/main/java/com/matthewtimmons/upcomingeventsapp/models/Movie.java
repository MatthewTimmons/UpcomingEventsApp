package com.matthewtimmons.upcomingeventsapp.models;

import android.content.res.Resources;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie extends Event implements Serializable {
    private String rating;
    private String genre;

    public Movie(DocumentSnapshot documentSnapshot) {
        super(documentSnapshot);
        this.rating = documentSnapshot.getString("rating");
        this.genre = documentSnapshot.getString("genre");
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getFormattedRating(Resources res) {
        if (!rating.equals("Rating Pending")) return res.getString(R.string.formatted_rating, rating);
        else return rating;
    }

//
//    public static List<Movie> getPlaceholderMovies() {
//        List<Movie> movies = new ArrayList<>();
//        movies.add(new Movie("The Predator", "R", "Horror, action", "9/14/2018", "https://ewedit.files.wordpress.com/2017/10/predator-app.jpg", 0));
//        movies.add(new Movie("Venom", "PG-13", "Action", "10/05/2018", "data:image/jpeg;base64,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", 1));
//        movies.add(new Movie("How the Grinch Stole Christmas", "PG", "Family", "11/09/2018", "https://www.bleedingcool.com/wp-content/uploads/2017/12/the-grinch-poster.jpg", 2));
//        movies.add(new Movie("Fantastic Beasts: The Crimes of Grindelwald", "PG-13", "Adventure", "11/16/2018", "http://www.the-leaky-cauldron.org/wp-content/uploads/assets/crimesofgrindelwaldlogo.jpg", 3));
//        movies.add(new Movie("Aquaman", "PG-13", "Action", "12/21/2018", "https://i.pinimg.com/originals/cf/15/09/cf1509164702e231902c98f65fb35371.jpg", 4));
//        return movies;
//    }
}
