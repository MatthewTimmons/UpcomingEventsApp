package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.adapters.GameListAdapter;

import java.util.List;

public class GamesFragment extends Fragment {
    RecyclerView recyclerView;
    GameListAdapter gameAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_games, container, false);

        Query gamesQuery = FirebaseFirestore.getInstance().collection("games").orderBy("gameReleaseDate");

        gamesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> listOfAllGameDocumentSnapshots = task.getResult().getDocuments();
                gameAdapter = new GameListAdapter(listOfAllGameDocumentSnapshots);
                recyclerView = v.findViewById(R.id.games_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(gameAdapter);
            }
        });


        return v;
    }
}
