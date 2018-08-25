package com.matthewtimmons.upcomingeventsapp.manager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.security.auth.callback.Callback;

public class FirestoreRun implements Runnable {
    private Handler handler;
    DocumentReference documentToRetrieve;

    @Override
    public void run() {
        documentToRetrieve.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                List<Query> listOfQueries = new ArrayList<>();
                for (String friendId : new ArrayList<String>()) {
                    Query val = Firestore.collection("movies").whereEqualTo("user", friendId);
                    listOfQueries.add(val);
                }

                final List<DocumentSnapshot> allDocumentSnapshots = new ArrayList<>();
                for (Query query : listOfQueries) {
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            allDocumentSnapshots.addAll(documentSnapshots.getDocuments());
                        }
                    });
                }


            }
        });
    }
}