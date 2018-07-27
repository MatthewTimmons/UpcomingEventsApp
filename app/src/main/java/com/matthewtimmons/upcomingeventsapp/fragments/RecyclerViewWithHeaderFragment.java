package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;

public class RecyclerViewWithHeaderFragment extends Fragment {
    private static final String CURRENT_OBJECT_ID = "CURRENT_OBJECT_ID";
    TextView firstColumnName;
    TextView secondColumnName;
    TextView thirdColumnName;
    String eventId;

    public static RecyclerViewWithHeaderFragment newInstance(String currentUser) {
        RecyclerViewWithHeaderFragment recyclerViewWithHeaderFragment = new RecyclerViewWithHeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_OBJECT_ID, currentUser);
        recyclerViewWithHeaderFragment.setArguments(bundle);
        return recyclerViewWithHeaderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_recycler_view_header, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventId = getArguments().getString(CURRENT_OBJECT_ID);
        firstColumnName = view.findViewById(R.id.first_column_name);
        secondColumnName = view.findViewById(R.id.second_column_name);
        thirdColumnName = view.findViewById(R.id.third_column_name);


        firstColumnName.setText("Favorite Events:");
        secondColumnName.setVisibility(View.GONE);
        thirdColumnName.setVisibility(View.GONE);

    }
}
