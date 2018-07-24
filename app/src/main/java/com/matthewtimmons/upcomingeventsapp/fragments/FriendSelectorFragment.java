package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendSelectorListAdapter;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;

import java.util.ArrayList;
import java.util.List;

public class FriendSelectorFragment extends Fragment {
    private static final String CURRENT_USER = "CURRENT_USER";
    private static final String FRIENDS_CHECKED = "FRIENDS_CHECKED";
    private static final String INCLUDE_CHECKMARKS = "INCLUDE_CHECKMARKS";
    private static final String USERS_CLICKABLE = "USERS_CLICKABLE";
    FriendSelectorListAdapter friendSelectorListAdapter;
    RecyclerView recyclerView;
    String currentUser;
    DocumentReference currentUserDocRef;
    ArrayList<String> friendsChecked;
    boolean includeCheckmarks;
    boolean usersClickable;

    public static FriendSelectorFragment newInstance(ArrayList<String> friendsChecked, String currentUser, boolean includeCheckmarks, boolean usersClickable) {
        FriendSelectorFragment friendSelectorFragment = new FriendSelectorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_USER, currentUser);
        bundle.putStringArrayList(FRIENDS_CHECKED, friendsChecked);
        bundle.putBoolean(INCLUDE_CHECKMARKS, includeCheckmarks);
        bundle.putBoolean(USERS_CLICKABLE, usersClickable);
        friendSelectorFragment.setArguments(bundle);
        return friendSelectorFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_friends_interest, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = getArguments().getString(CURRENT_USER);
        friendsChecked = getArguments().getStringArrayList(FRIENDS_CHECKED);
        includeCheckmarks = getArguments().getBoolean(INCLUDE_CHECKMARKS);
        usersClickable = getArguments().getBoolean(USERS_CLICKABLE);

        currentUserDocRef = FirebaseFirestore.getInstance().collection("users").document(currentUser);

        view.findViewById(R.id.second_column_name).setVisibility(View.INVISIBLE);

        recyclerView = view.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> allFriendIds = (ArrayList<String>) task.getResult().get("friends");
                        ArrayList<DocumentSnapshot> friendDocumentSnapshots = UserHelper.fetchFilteredUsersList(allUsers, allFriendIds, currentUser, includeCheckmarks);



    //                List<DocumentSnapshot> allFriends = UserHelper.fetchFilteredUsersList(allUsers);

                    friendSelectorListAdapter = new FriendSelectorListAdapter(friendDocumentSnapshots, friendsChecked, includeCheckmarks, usersClickable);
                    recyclerView.setAdapter(friendSelectorListAdapter);

                    }
                });
            }
        });
    }
}