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
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.manager.DevHelper;

import java.util.ArrayList;


public class SharedGamesFragment extends Fragment {
    public static final CollectionReference allGamesCollectionReference = Firestore.collection(FirebaseConstants.COLLECTION_GAMES);
    public static final CollectionReference allUsersCollectionReference = Firestore.collection(FirebaseConstants.COLLECTION_USERS);
    private static final String KEY_FIRST_USER_ID = "KEY_FIRST_USER_ID";
    private static final String KEY_SECOND_USER_ID = "KEY_SECOND_USER_ID";
    RecyclerView recyclerView;
    String firstUserId, secondUserId;

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
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstUserId = getArguments().getString(KEY_FIRST_USER_ID);
        secondUserId = getArguments().getString(KEY_SECOND_USER_ID);

        recyclerView = view.findViewById(R.id.events_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<DocumentSnapshot> allSharedGameDocumentSnapshots = new ArrayList<>();
        final ArrayList<String> allSharedGames = new ArrayList<>();

        final DocumentReference currentUserDocRef = allUsersCollectionReference.document(firstUserId);
        final DocumentReference friendsDocRef = allUsersCollectionReference.document(secondUserId);

        currentUserDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final ArrayList<String> listOfGames1 = (ArrayList<String>) task.getResult().get(FirebaseConstants.KEY_GAMES_OWNED);
                friendsDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> listOfGames2 = (ArrayList<String>) task.getResult().get(FirebaseConstants.KEY_GAMES_OWNED);
                        allSharedGames.addAll(DevHelper.fetchListOfMatchingItems(listOfGames1, listOfGames2));

                        allGamesCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                ArrayList<DocumentSnapshot> allGameDocumentSnapshots = (ArrayList<DocumentSnapshot>) task.getResult().getDocuments();
                                for (DocumentSnapshot game : allGameDocumentSnapshots) {
                                    if (allSharedGames.contains(game.getId())) {
                                        allSharedGameDocumentSnapshots.add(game);
                                    }
                                }
                                if (allSharedGameDocumentSnapshots.size() > 0) {
                                    EventListAdapter gameListAdapter = new EventListAdapter(allSharedGameDocumentSnapshots);
                                    recyclerView.setAdapter(gameListAdapter);
                                } else {
                                    view.findViewById(R.id.no_results_message_text_view).setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}