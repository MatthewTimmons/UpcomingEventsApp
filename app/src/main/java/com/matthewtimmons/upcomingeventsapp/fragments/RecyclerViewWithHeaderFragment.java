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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.RecyclerViewWithHeaderListAdapter;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewWithHeaderFragment extends Fragment {
    private static final String KEY_EVENT_TYPE = "keyEventType";
    private static final String KEY_CURRENT_USER_ID = "keyEventId";
    private static final String CURRENT_USER = "Matt";
    DocumentReference currentUserReference = FirebaseFirestore.getInstance().collection(FirebaseConstants.COLLECTION_USERS).document(CURRENT_USER);
    RecyclerView recyclerView;
    TextView firstColumnName;
    TextView secondColumnName;
    TextView thirdColumnName;
    String eventId;

    public static RecyclerViewWithHeaderFragment newInstance(String currentUser) {
        RecyclerViewWithHeaderFragment recyclerViewWithHeaderFragment = new RecyclerViewWithHeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CURRENT_USER_ID, currentUser);
        recyclerViewWithHeaderFragment.setArguments(bundle);
        return recyclerViewWithHeaderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_friends_interest, container, false);
        firstColumnName = v.findViewById(R.id.first_column_name);
        secondColumnName = v.findViewById(R.id.second_column_name);
        thirdColumnName = v.findViewById(R.id.third_column_name);
        eventId = getArguments().getString(KEY_CURRENT_USER_ID);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = view.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        UserHelper.setFavoritesRecyclerViewAdapter(currentUserReference, recyclerView, true);




        firstColumnName.setText("Favorite Events:");
        secondColumnName.setVisibility(View.GONE);
        thirdColumnName.setVisibility(View.GONE);

    }
}
