package com.matthewtimmons.upcomingeventsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {
    public static String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static String CURRENT_USER_OBJECT = "CURRENT_USER_OBJECT";
    String displayName, profilePhotoURL;
    List<String> friends, gamesOwnedByGameId, moviesSeenByMovieId;
    Map<String, Object> interestLevels;
    Map<String, Object> myFavorites;
    Map<String, Object> pendingFriendRequests;

    public User() {}

    // ---------------------------- Parcelable methods (start) ---------------------------- //

    protected User(Parcel in) {
        interestLevels = new HashMap<>();
        myFavorites = new HashMap<>();
        pendingFriendRequests = new HashMap<>();

        displayName = in.readString();
        profilePhotoURL = in.readString();
        friends = in.createStringArrayList();
        gamesOwnedByGameId = in.createStringArrayList();
        moviesSeenByMovieId = in.createStringArrayList();
        in.readMap(interestLevels, ClassLoader.getSystemClassLoader());
        in.readMap(myFavorites, ClassLoader.getSystemClassLoader());
        in.readMap(pendingFriendRequests, ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(profilePhotoURL);
        dest.writeStringList(friends);
        dest.writeStringList(gamesOwnedByGameId);
        dest.writeStringList(moviesSeenByMovieId);
        dest.writeMap(interestLevels);
        dest.writeMap(myFavorites);
        dest.writeMap(pendingFriendRequests);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // ---------------------------- Parcelable methods (end) ---------------------------- //

    public static Map<String, Object> getBlankUserValues() {
        Map<String, Object> allData = new HashMap<>();
        HashMap<String, Object> emptyHashMapStringObject= new HashMap<>();
        ArrayList<String> emptyArrayList = new ArrayList<>();

        allData.put("displayName", "");
        allData.put("profilePhotoURL", "");
        allData.put("friends", emptyArrayList);
        allData.put("gamesOwnedByGameId", emptyArrayList);
        allData.put("moviesSeenByMovieId", emptyArrayList);
        allData.put("favorites", emptyHashMapStringObject);
        allData.put("interestLevels", emptyHashMapStringObject);
        allData.put("pendingFriendRequests", emptyHashMapStringObject);
        return allData;
    }

    public static String getCurrentUserId() {
        return CURRENT_USER_ID;
    }

    public static void setCurrentUserId(String currentUserId) {
        CURRENT_USER_ID = currentUserId;
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

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getGamesOwnedByGameId() {
        return gamesOwnedByGameId;
    }

    public void setGamesOwnedByGameId(List<String> gamesOwnedByGameId) {
        this.gamesOwnedByGameId = gamesOwnedByGameId;
    }

    public List<String> getMoviesSeenByMovieId() {
        return moviesSeenByMovieId;
    }

    public void setMoviesSeenByMovieId(List<String> moviesSeenByMovieId) {
        this.moviesSeenByMovieId = moviesSeenByMovieId;
    }

    public Map<String, Object> getInterestLevels() {
        return interestLevels;
    }

    public void setInterestLevels(Map<String, Object> interestLevels) {
        this.interestLevels = interestLevels;
    }

    public Map<String, Object> getMyFavorites() {
        return myFavorites;
    }

    public void setMyFavorites(Map<String, Object> myFavorites) {
        this.myFavorites = myFavorites;
    }

    public Map<String, Object> getPendingFriendRequests() {
        return pendingFriendRequests;
    }

    public void setPendingFriendRequests(Map<String, Object> pendingFriendRequests) {
        this.pendingFriendRequests = pendingFriendRequests;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }
}
