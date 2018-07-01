package com.matthewtimmons.upcomingeventsapp.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    public static List<Movie> getPlaceholderMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("How the Grinch Stole Christmas", "PG", "Family", "11/09/2018", "grinchimage.com"));
        movies.add(new Movie("How the Grinch Stole Christmas", "PG", "Family", "11/09/2018", "grinchimage.com"));
        movies.add(new Movie("How the Grinch Stole Christmas", "PG", "Family", "11/09/2018", "grinchimage.com"));
        movies.add(new Movie("How the Grinch Stole Christmas", "PG", "Family", "11/09/2018", "grinchimage.com"));
        movies.add(new Movie("How the Grinch Stole Christmas", "PG", "Family", "11/09/2018", "grinchimage.com"));
        return movies;
    }

    String movieTitle;
    String movieRating;
    String movieGenre;
    String movieReleaseDate;
    String movieImageUrl;

    public Movie(String movieTitle, String movieRating, String movieGenre, String movieReleaseDate, String movieImageUrl) {
        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
        this.movieGenre = movieGenre;
        this.movieReleaseDate = movieReleaseDate;
        this.movieImageUrl = movieImageUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }
}
