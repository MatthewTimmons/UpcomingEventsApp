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
import com.google.firebase.firestore.QuerySnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.adapters.FriendInfoListAdapter;

import java.util.List;

public class FriendInfoFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FriendInfoListAdapter friendInfoListAdapter;
    List<DocumentSnapshot> filteredResults;
    String eventId;
    String username;
    DocumentSnapshot event;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_event_information, container, false);

        Task<QuerySnapshot> querySnapshotTask = FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                QuerySnapshot results = task.getResult();

                for (DocumentSnapshot document : results) {
                    if (!document.getId().equals("Matt")) {
                        filteredResults.add(document);
                    }
                }

            }
        });

        Task<QuerySnapshot> querySnapshotTask2 = FirebaseFirestore.getInstance().collection("movies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    if (document.getId().equals(eventId)) {
                        event = document;
                    }
                }
            }
        });

        recyclerView = v.findViewById(R.id.friends_event_information);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        friendInfoListAdapter = new FriendInfoListAdapter(filteredResults, "movies", "Aquaman");
        recyclerView.setAdapter(friendInfoListAdapter);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
