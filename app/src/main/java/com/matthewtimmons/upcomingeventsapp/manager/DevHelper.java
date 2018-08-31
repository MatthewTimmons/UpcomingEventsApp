package com.matthewtimmons.upcomingeventsapp.manager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.UsersListAdapterRows;
import com.matthewtimmons.upcomingeventsapp.adapters.CollapsedEventListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.UsersListAdapterSquare;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DevHelper {

    public static List<DocumentSnapshot> sortByDate(List<DocumentSnapshot> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(new Comparator<DocumentSnapshot>() {
                @Override
                public int compare(DocumentSnapshot documentSnapshot, DocumentSnapshot t1) {
                    String date1AsString = documentSnapshot.getString("date");
                    String date2AsString = t1.getString("date");
                    String reorderedDate1 = date1AsString.substring(6, 10) + date1AsString.substring(0, 2) + date1AsString.substring(3, 5);
                    String reorderedDate2 = date2AsString.substring(6, 10) + date2AsString.substring(0, 2) + date2AsString.substring(3, 5);

                    return reorderedDate1.compareTo(reorderedDate2);
                }
            });
        }
        return list;
    }

    public static void setUsersListAdapter(final RecyclerView recyclerView, final String firestoreBranchName, final String currentUserId, final boolean includeCurrentUser, final boolean square) {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        RecyclerView.Adapter recyclerAdapter;
                        ArrayList<String> allSearchResults = (ArrayList<String>) task.getResult().get(firestoreBranchName);
                        List<DocumentSnapshot> searchResultsDocumentSnapshots = DevHelper.fetchFilteredUsersList(allUsers, allSearchResults, currentUserId, includeCurrentUser);

                        if (!square) {
                            recyclerAdapter = new UsersListAdapterRows(searchResultsDocumentSnapshots);
                        } else {
                            recyclerAdapter = new UsersListAdapterSquare(searchResultsDocumentSnapshots);
                        }
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                });
            }
        });
    }

    public static void setPendingRequestsListAdapter(final RecyclerView recyclerView, final String currentUserId) {
        Firestore.collection(FirebaseConstants.COLLECTION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                Firestore.collection(FirebaseConstants.COLLECTION_USERS).document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> allSearchResults = new ArrayList<String>();
                        HashMap<String, Boolean> pendingFriendRequests = (HashMap<String, Boolean>) task.getResult().get("pendingFriendRequests");
                        allSearchResults.addAll(pendingFriendRequests.keySet());
                        List<DocumentSnapshot> searchResultsDocumentSnapshots = DevHelper.fetchFilteredUsersList(allUsers, allSearchResults, currentUserId, false);

                        // Update all pending requests to say they have been shown
                        for (Map.Entry<String, Boolean> entry : pendingFriendRequests.entrySet()) {
                            pendingFriendRequests.put(entry.getKey(), true);
                        }
                        Firestore.collection("users").document(currentUserId).update("pendingFriendRequests", pendingFriendRequests);

                        UsersListAdapterRows recyclerAdapter = new UsersListAdapterRows(searchResultsDocumentSnapshots);
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                });
            }
        });
    }

    public static void setCheckableUsersListAdapter(final RecyclerView recyclerView, final String firestoreBranchName, final String currentUserId, final boolean includeCurrentUser, final ArrayList<String> friendsChecked) {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> allSearchResults = (ArrayList<String>) task.getResult().get(firestoreBranchName);
                        List<DocumentSnapshot> searchResultsDocumentSnapshots = DevHelper.fetchFilteredUsersList(allUsers, allSearchResults, currentUserId, includeCurrentUser);

                        UsersListAdapterRows recyclerAdapter = new UsersListAdapterRows(searchResultsDocumentSnapshots, friendsChecked);
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

    public static ArrayList<String> fetchListOfMatchingItems(Set<String> firstUser, Set<String> secondUser) {
        final ArrayList<String> listOfSharedGames = new ArrayList<>();
        for (String item : firstUser) {
            if (secondUser.contains(item)) {
                listOfSharedGames.add(item);
            }
        }
        return listOfSharedGames;
    }

    public static HashMap<String, String> unwrapContainer(Map<String, Object> allDataForUser, String eventType) {
        HashMap<String, String> allEventValues = new HashMap<>();
        ArrayList<String> allEvents = (ArrayList<String>) allDataForUser.get(eventType);
        for (String id : allEvents) {
            allEventValues.put(id, eventType);
        }
        return allEventValues;
    }

    public static void setFavoritesRecyclerViewAdapter(User user, final RecyclerView recyclerView, final boolean collapsed) {
        final HashMap<String, Object> allFavoriteEventsMap = new HashMap<>();
        Map<String, Object> allFavoriteEvents = user.getMyFavorites();

        allFavoriteEventsMap.putAll(unwrapContainer(allFavoriteEvents, "concerts"));
        allFavoriteEventsMap.putAll(unwrapContainer(allFavoriteEvents, "games"));
        allFavoriteEventsMap.putAll(unwrapContainer(allFavoriteEvents, "movies"));

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
                                    if (allFavoriteEventsMap.keySet().contains(documentSnapshot.getId())) {
                                        allFavoriteDocumentSnapshots.add(documentSnapshot);
                                    }
                                }
                                if (collapsed) {
                                    CollapsedEventListAdapter collapsedEventListAdapter =
                                    new CollapsedEventListAdapter(allFavoriteDocumentSnapshots);
                                    recyclerView.setAdapter(collapsedEventListAdapter);
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
}
