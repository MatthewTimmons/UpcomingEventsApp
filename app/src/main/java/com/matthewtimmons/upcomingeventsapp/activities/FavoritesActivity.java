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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.FirestoreManager;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    EventListAdapter eventListAdapter;
    RecyclerView recyclerView;
    DocumentSnapshot currentUser;
    List<DocumentSnapshot> allUsers;
    List<DocumentSnapshot> allFavoriteMovieDocumentSnapshots;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //TODO: Come back and make sure this works for all types of events, not just movies
        FirebaseFirestore.getInstance().collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allMovies = task.getResult().getDocuments();
                FirebaseFirestore.getInstance().collection("users").document("Matt").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<DocumentSnapshot> finalListOfFavorites = new ArrayList<>();
                        ArrayList<String> listOfFavorites = (ArrayList<String>) task.getResult().get(FieldPath.of("myFavorites", "movies"));
                    for (DocumentSnapshot movie : allMovies) {
                       if (listOfFavorites.contains(movie.getId())) {
                           finalListOfFavorites.add(movie);
                       }
                    }
                    eventListAdapter = new EventListAdapter(finalListOfFavorites, FirebaseConstants.COLLECTION_MOVIES);
                    recyclerView.setAdapter(eventListAdapter);
                    }
                });
            }
        });
    }
}