package com.matthewtimmons.upcomingeventsapp.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.RecyclerViewWithHeaderListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.fragments.SharedGamesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHelper {
    //TODO: Implement method to get the current user
    public static final String CURRENT_USER = "Matt";

    public static void setFriendsListAdapter(final RecyclerView recyclerView, final String currentUser) {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> allFriendIds = (ArrayList<String>) task.getResult().get("friends");
                        List<DocumentSnapshot> friends = UserHelper.fetchFilteredUsersList(allUsers, allFriendIds);

                        FriendListAdapter recyclerAdapter = new FriendListAdapter(friends);
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                });
            }
        });
    }

    public static int calculateNumberOfColumns(Context context, int itemWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / itemWidth);
    }

    public static String convertArrayOfStringsToString (ArrayList<String> arrayOfStrings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayOfStrings.size() -1; i++) {
            stringBuilder.append(arrayOfStrings.get(i) + ", ");
        }
        stringBuilder.append(arrayOfStrings.get(arrayOfStrings.size() -1));
        return stringBuilder.toString();
    }

    public static ArrayList<DocumentSnapshot> fetchFilteredUsersList(List<DocumentSnapshot> allUsers, ArrayList<String> namesToSearchFor) {
        ArrayList<DocumentSnapshot> friends = new ArrayList<>();
        for (DocumentSnapshot user : allUsers) {
            if (namesToSearchFor.contains(user.getId())) {
                friends.add(user);
            }
        }
        return friends;
    }

    public static ArrayList<DocumentSnapshot> fetchFilteredUsersList(List<DocumentSnapshot> allUsers, ArrayList<String> namesToSearchFor, String currentUser, boolean includeCurrentUser) {
        ArrayList<DocumentSnapshot> friends = new ArrayList<>();
        if (includeCurrentUser) { namesToSearchFor.add(currentUser); }
        for (DocumentSnapshot user : allUsers) {
            if (namesToSearchFor.contains(user.getId())) {
                friends.add(user);
            }
        }
        return friends;
    }

    public static ArrayList<String> fetchListOfOwnedGames(DocumentReference userDocRef) {
        final ArrayList<String> listOfOwnedGames = new ArrayList<>();
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> listOfGames = (ArrayList<String>) task.getResult().get(FirebaseConstants.KEY_GAMES_OWNED);

                listOfOwnedGames.addAll(listOfGames);
            }
        });
        return listOfOwnedGames;
    }

    public static ArrayList<String> fetchListOfMatchingItems(ArrayList<String> firstUser, ArrayList<String> secondUser) {
        final ArrayList<String> listOfSharedGames = new ArrayList<>();
        for (String item : firstUser) {
            if (secondUser.contains(item)) {
                listOfSharedGames.add(item);
            }
        }
        return listOfSharedGames;
    }

    public static HashMap<String, String> fetchSharedValues(Map<String, Object> allDataForUser, String eventType) {
        HashMap<String, String> allEventValues = new HashMap<>();
        ArrayList<String> allEvents = (ArrayList<String>) allDataForUser.get(eventType);
        for (String id : allEvents) {
            allEventValues.put(id, eventType);
        }
        return allEventValues;
    }

    public static void setFavoritesRecyclerViewAdapter(DocumentReference currentUserReference, final RecyclerView recyclerView, final boolean collapsed) {
        currentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final HashMap<String, Object> allEventsMap = new HashMap<>();
                Map<String, Object> allDataForUser = (Map<String, Object>) task.getResult().get("myFavorites");

                allEventsMap.putAll(fetchSharedValues(allDataForUser, "concerts"));
                allEventsMap.putAll(fetchSharedValues(allDataForUser, "games"));
                allEventsMap.putAll(fetchSharedValues(allDataForUser, "movies"));

                //Temp
                final List<DocumentSnapshot> allDocumentSnapshots = new ArrayList<>();
                final List<DocumentSnapshot> allFavoriteDocumentSnapshots = new ArrayList<>();

                final CollectionReference collectionReferenceConcerts = FirebaseFirestore.getInstance().collection("concerts");
                final CollectionReference collectionReferenceGames = FirebaseFirestore.getInstance().collection("games");
                final CollectionReference collectionReferenceMovies = FirebaseFirestore.getInstance().collection("movies");

                collectionReferenceConcerts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        allDocumentSnapshots.addAll(task.getResult().getDocuments());
                        collectionReferenceGames.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                allDocumentSnapshots.addAll(task.getResult().getDocuments());
                                collectionReferenceMovies.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        allDocumentSnapshots.addAll(task.getResult().getDocuments());

                                        // Extract only items that are favorited by the user
                                        for (DocumentSnapshot documentSnapshot : allDocumentSnapshots) {
                                            if (allEventsMap.keySet().contains(documentSnapshot.getId())) {
                                                allFavoriteDocumentSnapshots.add(documentSnapshot);
                                            }
                                        }
                                        if (collapsed) {
                                            RecyclerViewWithHeaderListAdapter recyclerViewWithHeaderListAdapter =
                                            new RecyclerViewWithHeaderListAdapter(allFavoriteDocumentSnapshots);
                                            StringBuilder stringBuilder = new StringBuilder();
                                            for (DocumentSnapshot doc : allFavoriteDocumentSnapshots) {
                                                stringBuilder.append(doc.getString("title"));
                                            }
                                            Toast.makeText(recyclerView.getContext(), stringBuilder, Toast.LENGTH_SHORT).show();
                                            recyclerView.setAdapter(recyclerViewWithHeaderListAdapter);
                                        } else {
                                            EventListAdapter eventListAdapter = new EventListAdapter(allFavoriteDocumentSnapshots);
                                            recyclerView.setAdapter(eventListAdapter);
                                        }


                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }


}
