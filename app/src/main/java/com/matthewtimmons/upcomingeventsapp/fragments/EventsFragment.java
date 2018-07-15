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
import com.matthewtimmons.upcomingeventsapp.constants.EventConstants;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.adapters.EventListAdapter;

import java.util.List;

public class EventsFragment extends Fragment {
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private static final String KEY_EVENT_DATE = "keyEventDate";
    private String eventType;
    private String eventDate;
    RecyclerView recyclerView;
    EventListAdapter eventListAdapter;

    public static EventsFragment newInstance(String eventType, String keyEventDate) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EVENT_TYPE, eventType);
        args.putString(KEY_EVENT_DATE, keyEventDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_events, container, false);
        eventType = getArguments().getString(KEY_EVENT_TYPE);
        eventDate = getArguments().getString(KEY_EVENT_DATE);

        switch (eventType) {
            case FirebaseConstants.KEY_CONCERTS:
                v.findViewById(R.id.events_recycler_view).setBackgroundResource(R.drawable.music_notes);
                break;
            case FirebaseConstants.KEY_GAMES:
                v.findViewById(R.id.events_recycler_view).setBackgroundResource(R.drawable.vaporwave);
                break;
            case FirebaseConstants.KEY_MOVIES:
                v.findViewById(R.id.events_recycler_view).setBackgroundResource(R.drawable.popcorn);
                break;
        }

        Query eventQuery = FirebaseFirestore.getInstance().collection(eventType).orderBy(eventDate);
        eventQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> eventDocumentSnapshots = task.getResult().getDocuments();
                recyclerView = v.findViewById(R.id.events_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                eventListAdapter = new EventListAdapter(eventDocumentSnapshots, eventType);
                recyclerView.setAdapter(eventListAdapter);
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
