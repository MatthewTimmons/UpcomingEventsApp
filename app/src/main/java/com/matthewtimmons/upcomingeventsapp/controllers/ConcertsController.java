package com.matthewtimmons.upcomingeventsapp.controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.models.Concert;

public class ConcertsController {

    public static void getConcert(String id, final ConcertsController.GetConcertListener concertListener) {
        FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_CONCERTS).document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Concert concert = new Concert(documentSnapshot);
                concertListener.onConcertRetrieved(concert);
            }
        });
    }

    public interface GetConcertListener {
        void onConcertRetrieved(Concert concert);
    }

}
