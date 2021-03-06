package com.matthewtimmons.upcomingeventsapp.controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.models.Movie;

public class GamesController {

    public static void getGame(String filePath, final GetGameListener gameListener) {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_GAMES).document(filePath)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Game game = new Game(documentSnapshot);
                gameListener.onGameRetrieved(game);
            }
        });
    }

    public interface GetGameListener {
        void onGameRetrieved(Game game);
    }
}
