package com.matthewtimmons.upcomingeventsapp.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    public static String CURRENT_USER_ID = "CURRENT_USER_ID";

    String id, displayName, profilePhotoURL;
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

    public User(FirebaseUser firebaseUser) {
        DocumentReference userDocumentReference = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
        userDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot val = task.getResult();
                User user = new User(val);
            }
        });
    }

    public User(DocumentSnapshot userDocumentSnapshot) {
        this.id = userDocumentSnapshot.getId();
        this.displayName = userDocumentSnapshot.getString("displayName");
        this.profilePhotoURL = userDocumentSnapshot.getString("profilePhotoURL");
        this.friends = (List<String>) userDocumentSnapshot.get("friends");
        this.gamesOwnedById = (List<String>) userDocumentSnapshot.get("gamesOwnedByGameId");
        this.moviesSeenByMovieId = (List<String>) userDocumentSnapshot.get("moviesSeenByMovieId");
        this.interestLevels = (Map<String, Object>) userDocumentSnapshot.get("interestLevels");
        this.myFavorites = (Map<String, Object>) userDocumentSnapshot.get("myFavorites");
    }


    public User(String id, String displayName, String profilePhotoURL, ArrayList<String> friends, ArrayList<String> gamesOwnedById, ArrayList<String> moviesSeenByMovieId, HashMap<String, Object> interestLevels, HashMap<String, Object> myFavorites) {
        this.id = id;
        this.displayName = displayName;
        this.profilePhotoURL = profilePhotoURL;
        this.friends = friends;
        this.gamesOwnedById = gamesOwnedById;
        this.moviesSeenByMovieId = moviesSeenByMovieId;
        this.interestLevels = interestLevels;
        this.myFavorites = myFavorites;
    }

//    public static String getInterestLevelInEvent(DocumentSnapshot userDocmuentSnapshot, String eventType, String eventId) {
//        userDocmuentSnapshot.getString(FieldPath.of(FirebaseConstants.KEY_INTEREST_LEVELS_USER, eventType, eventId).toString());
//    }

    public static DocumentReference getUserReference(String userId) {
        return FirebaseFirestore.getInstance().document(FirebaseConstants.COLLECTION_USERS + "/" + userId);
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getCurrentUserId(FirebaseAuth firebaseAuth) {
        return firebaseAuth.getCurrentUser().getUid();
    }

    public static String getCurrentUserDisplayName(FirebaseAuth firebaseAuth) {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    public static void updateDisplayName(String userId, String updatedDisplayName) {
        Firestore.collection("users").document(userId).update("displayName", updatedDisplayName);
    }

    public static void updateAuthDisplayName(String userId, String updatedDisplayName) {
        Firestore.collection("usersAuth").document(userId).update("displayName", updatedDisplayName);
    }

    public static void updateFirebaseUserDisplayName(FirebaseUser currentUser, String updatedDisplayName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(updatedDisplayName).build();
        currentUser.updateProfile(profileUpdates);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
