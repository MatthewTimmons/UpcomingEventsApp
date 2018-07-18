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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendInfoListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;

import java.util.ArrayList;
import java.util.List;

public class FriendInfoFragment extends Fragment {
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private static final String KEY_EVENT_ID = "keyEventId";
    private static final String CURRENT_USER = "Matt";
    FriendInfoListAdapter friendsListAdapter;
    RecyclerView recyclerView;
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
        eventId = getArguments().getString(KEY_EVENT_ID);
        eventType = getArguments().getString(KEY_EVENT_TYPE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                List<DocumentSnapshot> friends = getFilteredUsersList(allUsers);
                friendsListAdapter = new FriendInfoListAdapter(friends, eventType, eventId);
                recyclerView.setAdapter(friendsListAdapter);
            }
        });

        if (eventType.equals(FirebaseConstants.COLLECTION_MOVIES)) {
            view.findViewById(R.id.friend_checkbox_column).setVisibility(View.VISIBLE);
        }
    }

    private List<DocumentSnapshot> getFilteredUsersList(List<DocumentSnapshot> allUsers) {
        List<DocumentSnapshot> friends = new ArrayList<>();
        for (DocumentSnapshot user : allUsers) {
            if (!user.getId().equals(CURRENT_USER)) {
                friends.add(user);
            }
        }
        return friends;
    }
}
