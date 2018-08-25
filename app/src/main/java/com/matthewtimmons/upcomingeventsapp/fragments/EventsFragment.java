package com.matthewtimmons.upcomingeventsapp.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.ProfileViewActivity;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.manager.DevHelper;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class EventsFragment extends Fragment {
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private String eventType;
    RecyclerView recyclerView;
    EventListAdapter eventListAdapter;

    public static EventsFragment newInstance(String eventType) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EVENT_TYPE, eventType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_events, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventType = getArguments().getString(KEY_EVENT_TYPE);

        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query eventQuery = FirebaseFirestore.getInstance().collection(eventType).orderBy(FirebaseConstants.KEY_DATE);
        eventQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> eventDocumentSnapshots = task.getResult().getDocuments();
                recyclerView = view.findViewById(R.id.events_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                // This is the section I messed with, and it technically works, but it does not allow the user to click on other people's events
                // Need to go into the adapter and add a rule that the file path for custom events is //movies/userID/movies/movieID
                if (UserManager.getInstance().getCurrentUser() == null) {
                    Firestore.collection("users").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User currentUser = documentSnapshot.toObject(User.class);
                            getEventsAndSetView(currentUserId, currentUser);
                        }
                    });
                } else {
                    getEventsAndSetView(currentUserId, UserManager.getInstance().getCurrentUser());
                }
            }
        });
    }

    void getEventsAndSetView(String currentUserId, User currentUser) {
        final List<DocumentSnapshot> allEvents = new ArrayList<>();
        List<Query> listOfQueries = new ArrayList<>();
        Query globalEvents = Firestore.collection(eventType).whereEqualTo("eventCreator", "global");
        Query currentUserCustomEvents = Firestore.collection(eventType).whereEqualTo("eventCreator", currentUserId);

        listOfQueries.add(globalEvents);
        listOfQueries.add(currentUserCustomEvents);

        for (String friendId : currentUser.getFriends()) {
            listOfQueries.add(Firestore.collection(eventType).whereEqualTo("eventCreator", friendId));
        }

        for (Query query : listOfQueries) {
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    allEvents.addAll(documentSnapshots.getDocuments());
                    eventListAdapter = new EventListAdapter(DevHelper.sortByDate(allEvents));
                    recyclerView.setAdapter(eventListAdapter);
                }
            });
        }
    }
}
