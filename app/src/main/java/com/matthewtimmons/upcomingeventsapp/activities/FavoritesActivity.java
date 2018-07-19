package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {
    EventListAdapter eventListAdapter;
    RecyclerView recyclerView;

    private DocumentReference currentUserReference = FirebaseFirestore.getInstance().document("users/Matt");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
                                        eventListAdapter = new EventListAdapter(allFavoriteDocumentSnapshots);
                                        recyclerView.setAdapter(eventListAdapter);
                                    }
                                });
                            }
                        });
                    }
                });

            }

            HashMap<String, String> fetchSharedValues(Map<String, Object> allDataForUser, String eventType) {
                HashMap<String, String> allEventValues = new HashMap<>();
                ArrayList<String> allEvents = (ArrayList<String>) allDataForUser.get(eventType);
                for (String id : allEvents) {
                    allEventValues.put(id, eventType);
                }
                return allEventValues;
            }
        });
    }
}