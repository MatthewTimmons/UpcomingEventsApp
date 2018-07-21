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
    DocumentReference currentUser = FirebaseFirestore.getInstance().collection("users").document("Matt");
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private static final String KEY_EVENT_ID = "keyEventId";
    private static final String CURRENT_USER = "Matt";
    FriendSelectorListAdapter friendSelectorListAdapter;
    RecyclerView recyclerView;

    public static FriendSelectorFragment newInstance() {
        FriendSelectorFragment friendSelectorFragment = new FriendSelectorFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(KEY_EVENT_ID, eventId);
        friendSelectorFragment.setArguments(bundle);
        return friendSelectorFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_friends_interest, container, false);
//        eventId = getArguments().getString(KEY_EVENT_ID);
//        eventType = getArguments().getString(KEY_EVENT_TYPE);
        v.findViewById(R.id.second_column_name).setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> allFriendIds = (ArrayList<String>) task.getResult().get("friends");
                        ArrayList<DocumentSnapshot> friendDocumentSnapshots = UserHelper.fetchFilteredUsersList(allUsers, allFriendIds, CURRENT_USER);



    //                List<DocumentSnapshot> allFriends = UserHelper.fetchFilteredUsersList(allUsers);

                    friendSelectorListAdapter = new FriendSelectorListAdapter(friendDocumentSnapshots);
                    recyclerView.setAdapter(friendSelectorListAdapter);

                    }
                });
            }
        });
    }
}