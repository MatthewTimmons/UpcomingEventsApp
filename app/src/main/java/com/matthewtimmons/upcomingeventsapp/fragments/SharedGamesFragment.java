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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendSelectorListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;

import java.util.ArrayList;
import java.util.List;


public class SharedGamesFragment extends Fragment {
    DocumentReference currentUser = FirebaseFirestore.getInstance().collection("users").document("Matt");
    private static final String KEY_FIRST_USER_ID = "keyFirstUserId";
    private static final String KEY_SECOND_USER_ID = "keySecondUserId";
    private static final String CURRENT_USER = "Matt";
    FriendSelectorListAdapter friendSelectorListAdapter;
    RecyclerView recyclerView;
    String firstUserId;
    String secondUserId;

    public static SharedGamesFragment newInstance(String firstUserId, String secondUserId) {
        SharedGamesFragment friendSelectorFragment = new SharedGamesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FIRST_USER_ID, firstUserId);
        bundle.putString(KEY_SECOND_USER_ID, secondUserId);
        friendSelectorFragment.setArguments(bundle);
        return friendSelectorFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_events, container, false);
        firstUserId = getArguments().getString(KEY_FIRST_USER_ID);
        secondUserId = getArguments().getString(KEY_SECOND_USER_ID);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.events_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                //TODO
                final ArrayList<String> allSharedGames = new ArrayList<>();
                final CollectionReference allUsers = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS);
                final DocumentReference currentUserDocRef = allUsers.document(firstUserId);
                final DocumentReference friendsDocRef = allUsers.document(secondUserId);


                currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final ArrayList<String> currentUsersGames = (ArrayList<String>) task.getResult().get("gamesOwnedByGameId");
                        friendsDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                ArrayList<String> friendsGames = (ArrayList<String>) task.getResult().get("gamesOwnedByGameId");

                                for (String game : currentUsersGames) {
                                    if (friendsGames.contains(game)) {
                                        allSharedGames.add(game);
                                    }
                                }

                                final List<DocumentSnapshot> allFavoriteDocumentSnapshots = new ArrayList<>();
                                final CollectionReference collectionReferenceGames = FirebaseFirestore.getInstance().collection("games");

                                collectionReferenceGames.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List<DocumentSnapshot> allGames = task.getResult().getDocuments();
                                        for (DocumentSnapshot game : allGames) {
                                            if (allSharedGames.contains(game.getId())) {
                                                allFavoriteDocumentSnapshots.add(game);
                                            }
                                        }
                                        EventListAdapter gameListAdapter = new EventListAdapter(allFavoriteDocumentSnapshots);
                                        recyclerView.setAdapter(gameListAdapter);
                                    }
                                });
                            }
                        });
                    }
                });
            }
}