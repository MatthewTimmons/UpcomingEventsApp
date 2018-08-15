package com.matthewtimmons.upcomingeventsapp.models;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CurrentUserSingleton {
    private static CurrentUserSingleton currentUserSingleton;
    public static User currentUserObject;

    private CurrentUserSingleton(final String currentUserId){
        FirebaseFirestore.getInstance().document("users/" + currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUserObject = new User(documentSnapshot);
            }
        });
    }

    public static CurrentUserSingleton getInstance(String currentUserId) {
        if (currentUserSingleton == null) {
            currentUserSingleton = new CurrentUserSingleton(currentUserId);
        }
        return currentUserSingleton;
    }

    public static void clearCurrentUserSingleton() {
        currentUserSingleton = null;
    }
}