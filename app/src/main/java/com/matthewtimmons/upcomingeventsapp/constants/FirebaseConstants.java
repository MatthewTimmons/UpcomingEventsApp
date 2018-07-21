package com.matthewtimmons.upcomingeventsapp.constants;

public class FirebaseConstants {
    // Collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CONCERTS = "concerts";
    public static final String COLLECTION_GAMES = "games";
    public static final String COLLECTION_MOVIES = "movies";

    // User keys
    public static final String KEY_MOVIES_SEEN = "moviesSeenByMovieId";
    public static final String KEY_GAMES_OWNED = "gamesOwnedByGameId";
    public static final String KEY_INTEREST_LEVELS_USER = "interestLevels";

    // Generic Event Keys
    public static final String KEY_IMAGE_URL = "imageUrl";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DATE = "date";

    // Event-type specific Keys
    public static final String KEY_CONCERT_LOCATION = "concertLocation";
    public static final String KEY_CONCERT_BANDS_ARRAY = "concertBandsArray";

    public static final String KEY_MOVIE_RATING = "movieRating";
    public static final String KEY_MOVIE_GENRE = "movieGenre";
}
