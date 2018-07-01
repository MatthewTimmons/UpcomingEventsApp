package com.matthewtimmons.upcomingeventsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matthewtimmons.upcomingeventsapp.models.Concert;
import com.matthewtimmons.upcomingeventsapp.views.ConcertListAdapter;

public class ConcertsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ConcertListAdapter concertAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_concerts, container, false);

        recyclerView = v.findViewById(R.id.concerts_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        concertAdapter = new ConcertListAdapter(Concert.getPlaceholderConcerts());
        recyclerView.setAdapter(concertAdapter);

        return v;
    }


}
