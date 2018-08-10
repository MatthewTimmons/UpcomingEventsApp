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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendInfoListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.DevHelper;

import java.util.ArrayList;
import java.util.List;

public class FriendInfoFragment extends Fragment {
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private static final String KEY_EVENT_ID = "keyEventId";
    Fragment friendsHeader;
    FriendInfoListAdapter friendsListAdapter;
    RecyclerView recyclerView;
    String currentUserId;
    String eventId;
    String eventType;

    public static FriendInfoFragment newInstance(String eventType, String eventId) {
        FriendInfoFragment friendInfoFragment = new FriendInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_TYPE, eventType);
        bundle.putString(KEY_EVENT_ID, eventId);
        friendInfoFragment.setArguments(bundle);
        return friendInfoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_friends_interest, container, false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventId = getArguments().getString(KEY_EVENT_ID);
        eventType = getArguments().getString(KEY_EVENT_TYPE);

        if (eventType.equals(FirebaseConstants.COLLECTION_MOVIES)) {
            friendsHeader = RecyclerViewHeaderFragment.newInstance(currentUserId, "Friends", "Interest Level", "Seen");
        } else if (eventType.equals(FirebaseConstants.COLLECTION_GAMES)) {
            friendsHeader = RecyclerViewHeaderFragment.newInstance(currentUserId, "Friends", "Interest Level", "Owned");
        } else {
            friendsHeader = RecyclerViewHeaderFragment.newInstance(currentUserId, "Friends", "Interest Level", "");
        }
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.column_names_constraint_layout, friendsHeader).commit();

        recyclerView = view.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          ArrayList<String> allFriendIds = (ArrayList<String>) task.getResult().get("friends");
                          List<DocumentSnapshot> friends = DevHelper.fetchFilteredUsersList(allUsers, allFriendIds);

                          friendsListAdapter = new FriendInfoListAdapter(friends, eventType, eventId);
                          recyclerView.setAdapter(friendsListAdapter);
                    }
                });
            }
        });




    }
}
