package com.matthewtimmons.upcomingeventsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matthewtimmons.upcomingeventsapp.models.Game;
import com.matthewtimmons.upcomingeventsapp.views.GameListAdapter;

public class GamesFragment extends Fragment {
    RecyclerView recyclerView;
    GameListAdapter gameAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_games, container, false);

        recyclerView = v.findViewById(R.id.games_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        gameAdapter = new GameListAdapter(Game.getPlaceholderGames());
        recyclerView.setAdapter(gameAdapter);

        return v;
    }
}
