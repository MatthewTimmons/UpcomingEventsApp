package com.matthewtimmons.upcomingeventsapp.models;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserManager {
    private static UserManager userManager;
    private User currentUser;

    private UserManager() {}

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public void setCurrentUser() {
        FirebaseFirestore.getInstance().document("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = new User(documentSnapshot);
            }
        });
    }

    public void clearCurrentUser() {
        userManager = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getUserId();
        }
        return null;
    }
}