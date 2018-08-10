package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.matthewtimmons.upcomingeventsapp.R;

public class ListViewHeaderFragment extends Fragment {
    private static final String CURRENT_OBJECT_ID = "CURRENT_OBJECT_ID";
    private static final String FIRST_COLUMN_NAME = "FIRST_COLUMN_NAME";
    TextView firstColumnNameTextView;
    String eventId;
    String firstColumnName;

    // Header only
    public static com.matthewtimmons.upcomingeventsapp.fragments.ListViewHeaderFragment newInstance(String currentUser, String firstColumnName) {
        com.matthewtimmons.upcomingeventsapp.fragments.ListViewHeaderFragment listViewHeaderFragment = new com.matthewtimmons.upcomingeventsapp.fragments.ListViewHeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_OBJECT_ID, currentUser);
        bundle.putString(FIRST_COLUMN_NAME, firstColumnName);
        listViewHeaderFragment.setArguments(bundle);
        return listViewHeaderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_list_view_header, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventId = getArguments().getString(CURRENT_OBJECT_ID);
        firstColumnName = getArguments().getString(FIRST_COLUMN_NAME);
        firstColumnNameTextView = view.findViewById(R.id.first_column_name);

        firstColumnNameTextView.setText(firstColumnName);
        firstColumnNameTextView.setVisibility(View.VISIBLE);

    }
}
