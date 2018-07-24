package com.matthewtimmons.upcomingeventsapp.manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.firebase.ui.auth.ui.email.CheckEmailFragment.TAG;

public class Firestore {
    public static CollectionReference collection(String collectionId) {
        return FirebaseFirestore.getInstance().collection(collectionId);
    }

    public static DocumentSnapshot refreshThisDocument(String documentId) {
        return FirebaseFirestore.getInstance().collection("movies").document(documentId).get().getResult();
    }

    public static void copyAllDataToOneObjectInOtherDocument(final DocumentReference fromPath, final DocumentReference toPath) {
        // How to call this method:
//        DocumentReference fromPath = Firestore.collection("users").document("fromUserName");
//        DocumentReference toPath = Firestore.collection("users").document("NewUserId");
//        copyAllDataToOneObjectInOtherDocument(fromPath, toPath);

        fromPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final Map<String, Object> appUserData = new HashMap<>();
                            appUserData.put("allAppData", task.getResult().getData());
                    final DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        toPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Map<String, Object> val = task.getResult().getData();
                                val.putAll(appUserData);
                                toPath.set(val);
                            }
                        });
                    }
                }
            }
        });
    }

}
