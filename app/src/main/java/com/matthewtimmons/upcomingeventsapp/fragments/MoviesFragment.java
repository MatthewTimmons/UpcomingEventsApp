package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;

import java.util.List;

public class MoviesFragment extends Fragment {
    RecyclerView recyclerView;
    EventListAdapter movieListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_events, container, false);

        // Get list of movies
        Query query = FirebaseFirestore.getInstance().collection(FirebaseConstants.KEY_MOVIES).orderBy(FirebaseConstants.KEY_MOVIE_DATE);
        final Task<QuerySnapshot> querySnapshotTask = query.get();
        querySnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> listOfMoviesDocuments = queryDocumentSnapshots.getDocuments();
                movieListAdapter = new EventListAdapter(listOfMoviesDocuments, EventConstants.EVENT_TYPE_MOVIE);
                recyclerView = v.findViewById(R.id.events_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(movieListAdapter);
            }
        });
        return v;
    }
}
