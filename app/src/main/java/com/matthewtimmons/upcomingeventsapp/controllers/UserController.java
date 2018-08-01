package com.matthewtimmons.upcomingeventsapp.controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    public static void getUser(String id, final UserController.GetUserListener userListener) {
        Firestore.collection(FirebaseConstants.COLLECTION_USERS).document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                User user = documentSnapshot.toObject(User.class);
                userListener.onUserRetrieved(user);
            }
        });
    }

    public interface GetUserListener {
        void onUserRetrieved(User user);
    }

    public static DocumentReference getUserReference(String userId) {
        return FirebaseFirestore.getInstance().document(FirebaseConstants.COLLECTION_USERS + "/" + userId);
    }

    public static void updateDisplayName(String userId, String updatedDisplayName) {
        Firestore.collection("users").document(userId).update("displayName", updatedDisplayName);
    }

    public static void updateAuthDisplayName(String userId, String updatedDisplayName) {
        Firestore.collection("usersAuth").document(userId).update("displayName", updatedDisplayName);
    }

    public static void updateFirebaseUserDisplayName(FirebaseUser currentUser, String updatedDisplayName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(updatedDisplayName).build();
        currentUser.updateProfile(profileUpdates);
    }
}
