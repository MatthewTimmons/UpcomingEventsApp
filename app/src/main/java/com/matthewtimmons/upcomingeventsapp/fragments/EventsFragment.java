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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.MoviesFirestoreRecyclerAdapter;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;
import com.matthewtimmons.upcomingeventsapp.models.Movie;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.matthewtimmons.upcomingeventsapp.models.UserManager;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private String eventType;
    RecyclerView recyclerView;
    EventListAdapter eventListAdapter;
    MoviesFirestoreRecyclerAdapter moviesFirestoreRecyclerAdapter;

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
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventType = getArguments().getString(KEY_EVENT_TYPE);

        recyclerView = view.findViewById(R.id.events_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

    void getEventsAndSetView(final String currentUserId, User currentUser) {
//        final List<DocumentSnapshot> allEvents = new ArrayList<>();
//        Query query = FirebaseFirestore.getInstance().collection("movies")
//                .whereGreaterThan("date", "09/05/2018")
//                .orderBy("date", Query.Direction.ASCENDING);

//        ListenerRegistration listenerRegistration = Firestore.collection(eventType).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                if ()
//            }
//        });

//        switch (eventType) {
//            case "concerts":
////                FirestoreRecyclerOptions<Concert> options = new FirestoreRecyclerOptions.Builder<Concert>()
////                        .setQuery(query, Concert.class)
////                        .build();
//
//                break;
//            case "games":
////                FirestoreRecyclerOptions<Game> options = new FirestoreRecyclerOptions.Builder<Game>()
////                        .setQuery(query, Game.class)
////                        .build();
//                break;
//        }



        //----------------------------------------Test----------------------------------------//
//        FirestoreRecyclerOptions<Movie> options = new FirestoreRecyclerOptions.Builder<Movie>()
//                .setQuery(query, new SnapshotParser<Movie>() {
//                    @NonNull
//                    @Override
//                    public Movie parseSnapshot(@NonNull DocumentSnapshot snapshot) {
//                        return new Movie(snapshot);
//                    }
//                }).build();
//
//        moviesFirestoreRecyclerAdapter = new MoviesFirestoreRecyclerAdapter(options, currentUserId);
//        recyclerView.setAdapter(moviesFirestoreRecyclerAdapter);
//
//        if (moviesFirestoreRecyclerAdapter != null) {
//            moviesFirestoreRecyclerAdapter.startListening();
//        }
        //----------------------------------------End Test----------------------------------------//

        final List<String> acceptableEventCreators = currentUser.getFriends();
        acceptableEventCreators.add("global");
        acceptableEventCreators.add(currentUserId);

        final List<DocumentSnapshot> allVisibleEvents = new ArrayList<>();
        FirebaseFirestore.getInstance().collection(eventType).orderBy("date", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> allEvents = task.getResult().getDocuments();

                for (DocumentSnapshot document : allEvents) {
                    String eventCreator = document.getString("eventCreator");
                    if (acceptableEventCreators.contains(eventCreator)) {
                        allVisibleEvents.add(document);
                    }
                }

                eventListAdapter = new EventListAdapter(allVisibleEvents);
                recyclerView.setAdapter(eventListAdapter);
            }
        });




        //----------------------------------------Test----------------------------------------//

//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        Query allMovies = FirebaseFirestore.getInstance().collection("movies");
//        Query secondQuery = rootRef...
//
//        Task firstTask = allMovies.get();
//        Task secondTask = secondQuery.get();
//
//        Task combinedTask = Tasks.whenAllSuccess(firstTask, secondTask).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
//            @Override
//            public void onSuccess(List<Object> list) {
//                //Do what you need to do with your list
//            }
//        });


        //----------------------------------------End Test----------------------------------------//





        // PREVIOUS METHOD OF QUERYING DATABASE //
//        List<Query> listOfQueries = new ArrayList<>();
//        Query globalEvents = Firestore.collection(eventType).whereEqualTo("eventCreator", "global");
//        Query currentUserCustomEvents = Firestore.collection(eventType).whereEqualTo("eventCreator", currentUserId);
//
//        listOfQueries.add(globalEvents);
//        listOfQueries.add(currentUserCustomEvents);
//
//        for (String friendId : currentUser.getFriends()) {
//            listOfQueries.add(Firestore.collection(eventType).whereEqualTo("eventCreator", friendId));
//        }
//
//        for (Query query : listOfQueries) {
//            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot documentSnapshots) {
//                    allEvents.addAll(documentSnapshots.getDocuments());
//                    eventListAdapter = new EventListAdapter(DevHelper.sortByDate(allEvents));
//                    recyclerView.setAdapter(eventListAdapter);
//                }
//            });
//        }


        // This snapshot listener does not work. It causes the movies to display two of each movie
//        for (Query query : listOfQueries) {
//            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                    for (DocumentChange dc: documentSnapshots.getDocumentChanges()) {
//                        switch (dc.getType()) {
//                            case ADDED:
//                                if (!allEvents.contains(dc.getDocument())) {
//                                    allEvents.add(dc.getDocument());
//                                }
//                        }
//                    }
//                }
//            });
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (moviesFirestoreRecyclerAdapter != null) {
            moviesFirestoreRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        moviesFirestoreRecyclerAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        moviesFirestoreRecyclerAdapter.stopListening();
    }
}
