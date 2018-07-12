package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.matthewtimmons.upcomingeventsapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendInfoListAdapter extends RecyclerView.Adapter<FriendInfoListAdapter.FriendInfoViewHolder> {

    List<DocumentSnapshot> filteredResults;
    DocumentSnapshot thisEvent;
    String eventType;
    String eventId;

    public FriendInfoListAdapter(List<DocumentSnapshot> filteredResults, String eventType, String eventId) {
        this.filteredResults = filteredResults;
        this.eventType = eventType;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public FriendInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_event_information, viewGroup, false);


        return new FriendInfoViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendInfoViewHolder friendInfoViewHolder, int i) {
        DocumentSnapshot userDocumentSnapshot = filteredResults.get(i);
        friendInfoViewHolder.friendUsername.setText(userDocumentSnapshot.getString("username"));
        friendInfoViewHolder.friendInterestLevel.setText((Integer) userDocumentSnapshot.get(FieldPath.of("interestLevels", eventType, eventId)));

        // Check checkbox if movie has been seen
        if (eventType.equals("movies")) {
            ArrayList<String> moviesSeen = (ArrayList<String>) userDocumentSnapshot.get("moviesSeenByMovieId");
            if (moviesSeen.contains(eventId)) {
                friendInfoViewHolder.friendCheckbox.setImageResource(R.drawable.ic_movies_blue);
            }
        }
    }

    @Override
    public int getItemCount() {
        return filteredResults.size();
    }

    public class FriendInfoViewHolder extends RecyclerView.ViewHolder {
        TextView friendUsername;
        TextView friendInterestLevel;
        ImageView friendCheckbox;


        public FriendInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            friendUsername = itemView.findViewById(R.id.friend_user_name);
            friendInterestLevel = itemView.findViewById(R.id.friend_interest_level);
            friendCheckbox = itemView.findViewById(R.id.friend_checkbox);
        }
    }
}
