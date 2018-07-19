package com.matthewtimmons.upcomingeventsapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedGamesActivity extends AppCompatActivity {
    EventListAdapter eventListAdapter;
    RecyclerView recyclerView;

    private DocumentReference currentUserReference = FirebaseFirestore.getInstance().document("users/Matt");

    public static final String currentUserId = "Matt";
    public static final String friendUserId = "Ryan";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //TODO
        final ArrayList<String> allSharedGames = new ArrayList<>();
        final CollectionReference allUsers = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS);
        final DocumentReference currentUserDocRef = allUsers.document(currentUserId);
        final DocumentReference friendsDocRef = allUsers.document(friendUserId);


        currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final ArrayList<String> currentUsersGames = (ArrayList<String>) task.getResult().get("gamesOwnedByGameId");
                friendsDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> friendsGames = (ArrayList<String>) task.getResult().get("gamesOwnedByGameId");

                        for (String game : currentUsersGames) {
                            if (friendsGames.contains(game)) {
                                allSharedGames.add(game);
                            }
                        }

                        final List<DocumentSnapshot> allFavoriteDocumentSnapshots = new ArrayList<>();
                        final CollectionReference collectionReferenceGames = FirebaseFirestore.getInstance().collection("games");

                        collectionReferenceGames.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<DocumentSnapshot> allGames = task.getResult().getDocuments();
                                for (DocumentSnapshot game : allGames) {
                                    if (allSharedGames.contains(game.getId())) {
                                        allFavoriteDocumentSnapshots.add(game);
                                    }
                                }
                                EventListAdapter gameListAdapter = new EventListAdapter(allFavoriteDocumentSnapshots);
                                recyclerView.setAdapter(gameListAdapter);
                            }
                        });
                    }
                });
            }
        });
    }
}