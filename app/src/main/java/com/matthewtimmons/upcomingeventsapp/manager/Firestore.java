package com.matthewtimmons.upcomingeventsapp.manager;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Firestore {

    public static CollectionReference collection(String collectionId) {
        return FirebaseFirestore.getInstance().collection(collectionId);
    }

    public static DocumentSnapshot refreshThisDocument(String documentId) {
        return FirebaseFirestore.getInstance().collection("movies").document(documentId).get().getResult();
    }

}
