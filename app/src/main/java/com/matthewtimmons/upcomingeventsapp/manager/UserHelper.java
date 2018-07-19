package com.matthewtimmons.upcomingeventsapp.manager;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserHelper {
    //TODO: Implement method to get the current user
    public static final String CURRENT_USER = "Matt";



    public static List<DocumentSnapshot> getFilteredUsersList(List<DocumentSnapshot> allUsers) {
        List<DocumentSnapshot> friends = new ArrayList<>();
        for (DocumentSnapshot user : allUsers) {
            if (!user.getId().equals(CURRENT_USER)) {
                friends.add(user);
            }
        }
        return friends;
    }
}
