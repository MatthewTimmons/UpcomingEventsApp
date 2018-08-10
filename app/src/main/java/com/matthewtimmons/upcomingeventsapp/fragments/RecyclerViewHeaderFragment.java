package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matthewtimmons.upcomingeventsapp.R;

public class RecyclerViewHeaderFragment extends Fragment {
    private static final String CURRENT_OBJECT_ID = "CURRENT_OBJECT_ID";
    private static final String RESOURCE_ID = "RESOURCE_ID";
    private static final String FIRST_COLUMN_NAME = "FIRST_COLUMN_NAME";
    private static final String SECOND_COLUMN_NAME = "SECOND_COLUMN_NAME";
    private static final String THIRD_COLUMN_NAME = "THIRD_COLUMN_NAME";
    RecyclerView recyclerView;
    TextView firstColumnNameTextView;
    TextView secondColumnNameTextView;
    TextView thirdColumnNameTextView;
    String eventId;
    Integer resourceId;
    String firstColumnName;
    String secondColumnName;
    String thirdColumnName;

    // Header only
    public static RecyclerViewHeaderFragment newInstance(String currentUser, String firstColumnName) {
        RecyclerViewHeaderFragment recyclerViewHeaderFragment = new RecyclerViewHeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_OBJECT_ID, currentUser);
        bundle.putString(FIRST_COLUMN_NAME, firstColumnName);
        bundle.putString(SECOND_COLUMN_NAME, "");
        bundle.putString(THIRD_COLUMN_NAME, "");
        recyclerViewHeaderFragment.setArguments(bundle);
        return recyclerViewHeaderFragment;
    }

    public static RecyclerViewHeaderFragment newInstance(String currentUser, String firstColumnName, String secondColumnName, String thirdColumnName) {
        RecyclerViewHeaderFragment recyclerViewHeaderFragment = new RecyclerViewHeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_OBJECT_ID, currentUser);
        bundle.putString(FIRST_COLUMN_NAME, firstColumnName);
        bundle.putString(SECOND_COLUMN_NAME, secondColumnName);
        bundle.putString(THIRD_COLUMN_NAME, thirdColumnName);
        recyclerViewHeaderFragment.setArguments(bundle);
        return recyclerViewHeaderFragment;
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
        firstColumnName = getArguments().getString(FIRST_COLUMN_NAME);
        secondColumnName = getArguments().getString(SECOND_COLUMN_NAME);
        thirdColumnName = getArguments().getString(THIRD_COLUMN_NAME);
        firstColumnNameTextView = view.findViewById(R.id.first_column_name);
        secondColumnNameTextView = view.findViewById(R.id.plus_icon);
        thirdColumnNameTextView = view.findViewById(R.id.third_column_name);

        if (!firstColumnName.equals("")) {
            firstColumnNameTextView.setText(firstColumnName);
            firstColumnNameTextView.setVisibility(View.VISIBLE);
        }

        if (!secondColumnName.equals("")) {
            secondColumnNameTextView.setText(secondColumnName);
            secondColumnNameTextView.setVisibility(View.VISIBLE);
        }

        if (!thirdColumnName.equals("")) {
            thirdColumnNameTextView.setText(thirdColumnName);
            thirdColumnNameTextView.setVisibility(View.VISIBLE);
        }
    }
}
