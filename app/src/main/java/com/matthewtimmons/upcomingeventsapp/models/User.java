package com.matthewtimmons.upcomingeventsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {
    public static String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static String CURRENT_USER_OBJECT = "CURRENT_USER_OBJECT";

    String displayName, profilePhotoURL, userId;
    List<String> friends, moviesSeenByMovieId;
    Map<String, Object> interestLevels, gamesOwned, myFavorites, pendingFriendRequests;

    // interestLevels = interestLevels > Event Type > Event ID > Interest Level Value
    // gamesOwned = gamesOwned > Game ID > List of Strings representing what consoles that game is owned for
    // myFavorites = myFavorites > Event Type > List of Event ID's
    // pendingFriendRequests = pendingFriendRequests > User ID > boolean seen or not seen

    public User() {}

    public User(DocumentSnapshot currentUserDocumentSnapshot) {
        this.userId = currentUserDocumentSnapshot.getId();
        this.displayName = currentUserDocumentSnapshot.getString("displayName");
        this.profilePhotoURL = currentUserDocumentSnapshot.getString("profilePhotoURL");
        this.friends = (List<String>) currentUserDocumentSnapshot.get("friends");
        this.gamesOwned = (Map<String, Object>) currentUserDocumentSnapshot.get("gamesOwned");
        this.moviesSeenByMovieId = (List<String>) currentUserDocumentSnapshot.get("moviesSeenByMovieId");
        this.interestLevels = (Map<String, Object>) currentUserDocumentSnapshot.get("interestLevels");
        this.myFavorites = (Map<String, Object>) currentUserDocumentSnapshot.get("myFavorites");
        this.pendingFriendRequests = (Map<String, Object>) currentUserDocumentSnapshot.get("pendingFriendRequests");
    }

    // ---------------------------- Parcelable methods (start) ---------------------------- //

    protected User(Parcel in) {
        gamesOwned = new HashMap<>();
        interestLevels = new HashMap<>();
        myFavorites = new HashMap<>();
        pendingFriendRequests = new HashMap<>();

        displayName = in.readString();
        profilePhotoURL = in.readString();
        friends = in.createStringArrayList();
        moviesSeenByMovieId = in.createStringArrayList();
        in.readMap(gamesOwned, ClassLoader.getSystemClassLoader());
        in.readMap(interestLevels, ClassLoader.getSystemClassLoader());
        in.readMap(myFavorites, ClassLoader.getSystemClassLoader());
        in.readMap(pendingFriendRequests, ClassLoader.getSystemClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(profilePhotoURL);
        dest.writeStringList(friends);
        dest.writeStringList(moviesSeenByMovieId);
        dest.writeMap(gamesOwned);
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
        HashMap<String, Object> concertsGamesMoviesHashMapStringObject = new HashMap<>();
        HashMap<String, Object> concertsGamesMoviesHashMapObject = new HashMap<>();
        ArrayList<String> emptyArrayList = new ArrayList<>();

        concertsGamesMoviesHashMapStringObject.put("concerts", emptyArrayList);
        concertsGamesMoviesHashMapStringObject.put("games", emptyArrayList);
        concertsGamesMoviesHashMapStringObject.put("movies", emptyArrayList);

        concertsGamesMoviesHashMapObject.put("concerts", emptyHashMapStringObject);
        concertsGamesMoviesHashMapObject.put("games", emptyHashMapStringObject);
        concertsGamesMoviesHashMapObject.put("movies", emptyHashMapStringObject);

        allData.put("displayName", "");
        allData.put("profilePhotoURL", "https://localmarketingplus.ca/wp-content/uploads/2015/02/blue-head.jpg");
        allData.put("friends", emptyArrayList);
        allData.put("gamesOwned", emptyArrayList);
        allData.put("moviesSeenByMovieId", emptyArrayList);
        allData.put("myFavorites", concertsGamesMoviesHashMapStringObject);
        allData.put("interestLevels", concertsGamesMoviesHashMapObject);
        allData.put("pendingFriendRequests", emptyHashMapStringObject);
        return allData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Map<String, Object> getGamesOwned() {
        return gamesOwned;
    }

    public void setGamesOwned(Map<String, Object> gamesOwned) {
        this.gamesOwned = gamesOwned;
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
