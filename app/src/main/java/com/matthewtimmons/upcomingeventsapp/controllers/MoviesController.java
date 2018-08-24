package com.matthewtimmons.upcomingeventsapp.controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.models.Movie;

public class MoviesController {

    public static void getMovie(String filePath, final GetMovieListener movieListener) {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_MOVIES).document(filePath)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Movie movie = new Movie(documentSnapshot);
                movieListener.onMovieRetrieved(movie);
            }
        });
    }

    public interface GetMovieListener {
        void onMovieRetrieved(Movie movie);
    }
}
