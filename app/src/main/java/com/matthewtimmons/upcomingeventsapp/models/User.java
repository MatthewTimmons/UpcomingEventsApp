package com.matthewtimmons.upcomingeventsapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    String displayName, profilePhotoURL;
    List<String> friends, gamesOwnedById, moviesSeenByMovieId;
    Map<String, Object> interestLevels, myFavorites;

    public static Map<String, Object> getBlankUserValues() {
        Map<String, Object> emptyHashMapStringObject = new HashMap<>();
        Map<String, Object> myFavoritesObject= new HashMap<>();
        Map<String, Integer> emptyHashMapStringInteger = new HashMap<>();
        List<String> emptyArrayList = new ArrayList<>();
        emptyHashMapStringObject.put("concerts", emptyHashMapStringInteger);
        emptyHashMapStringObject.put("games", emptyHashMapStringInteger);
        emptyHashMapStringObject.put("movies", emptyHashMapStringInteger);
        myFavoritesObject.put("concerts", emptyArrayList);
        myFavoritesObject.put("games", emptyArrayList);
        myFavoritesObject.put("movies", emptyArrayList);

        Map<String, Object> blankUser = new HashMap<>();
        blankUser.put("displayName", null);
        blankUser.put("friends", emptyArrayList);
        blankUser.put("gamesOwnedByGameId", emptyArrayList);
        blankUser.put("interestLevels", emptyHashMapStringObject);
        blankUser.put("moviesSeenByMovieId", emptyArrayList);
        blankUser.put("myFavorites", myFavoritesObject);
        blankUser.put("profilePhotoURL", null);

        return blankUser;
    }

    public User() {

    }

    public User(String displayName, String profilePhotoURL, ArrayList<String> friends, ArrayList<String> gamesOwnedById, ArrayList<String> moviesSeenByMovieId, HashMap<String, Object> interestLevels, HashMap<String, Object> myFavorites) {
        this.displayName = displayName;
        this.profilePhotoURL = profilePhotoURL;
        this.friends = friends;
        this.gamesOwnedById = gamesOwnedById;
        this.moviesSeenByMovieId = moviesSeenByMovieId;
        this.interestLevels = interestLevels;
        this.myFavorites = myFavorites;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public List<String> getGamesOwnedById() {
        return gamesOwnedById;
    }

    public void setGamesOwnedById(ArrayList<String> gamesOwnedById) {
        this.gamesOwnedById = gamesOwnedById;
    }

    public List<String> getMoviesSeenByMovieId() {
        return moviesSeenByMovieId;
    }

    public void setMoviesSeenByMovieId(ArrayList<String> moviesSeenByMovieId) {
        this.moviesSeenByMovieId = moviesSeenByMovieId;
    }

    public Map<String, Object> getInterestLevels() {
        return interestLevels;
    }

    public void setInterestLevels(HashMap<String, Object> interestLevels) {
        this.interestLevels = interestLevels;
    }

    public Map<String, Object> getMyFavorites() {
        return myFavorites;
    }

    public void setMyFavorites(HashMap<String, Object> myFavorites) {
        this.myFavorites = myFavorites;
    }
}
