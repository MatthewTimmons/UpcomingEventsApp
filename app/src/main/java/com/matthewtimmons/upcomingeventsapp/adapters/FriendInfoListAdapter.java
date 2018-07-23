package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FieldPath;
import com.matthewtimmons.upcomingeventsapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.constants.FirebaseConstants;

import java.util.ArrayList;
import java.util.List;

public class FriendInfoListAdapter extends RecyclerView.Adapter<FriendInfoListAdapter.FriendInfoViewHolder> {
    List<DocumentSnapshot> friends;
    String eventType;
    String eventId;
    String friendsInterestLevel = "0";

    public FriendInfoListAdapter(List<DocumentSnapshot> friends, String eventType, String eventId) {
        this.friends = friends;
        this.eventType = eventType;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public FriendInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_friend_event_information, viewGroup, false);
        return new FriendInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendInfoViewHolder friendInfoViewHolder, int i) {
        DocumentSnapshot userDocumentSnapshot = friends.get(i);

        try {
            friendsInterestLevel = (userDocumentSnapshot.get(FieldPath.of(FirebaseConstants.KEY_INTEREST_LEVELS_USER, eventType, eventId))).toString();
            friendInfoViewHolder.friendInterestLevel.setText(friendsInterestLevel);
        } catch (NullPointerException e) {
            friendsInterestLevel = "-";
            friendInfoViewHolder.friendInterestLevel.setTextSize(24);
            friendInfoViewHolder.friendInterestLevel.setText(friendsInterestLevel);
        }
        friendInfoViewHolder.friendUsername.setText(userDocumentSnapshot.getId());

        // Check checkbox if movie has been seen
        if (eventType.equals(FirebaseConstants.COLLECTION_MOVIES)) {
            ArrayList<String> moviesSeen = (ArrayList<String>) userDocumentSnapshot.get(FirebaseConstants.KEY_MOVIES_SEEN);
            if (moviesSeen.contains(eventId)) {
                friendInfoViewHolder.friendCheckbox.setVisibility(View.VISIBLE);
                friendInfoViewHolder.friendCheckbox.setImageResource(R.drawable.ic_checked_checkbox);
            } else {
                friendInfoViewHolder.friendCheckbox.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendInfoViewHolder extends RecyclerView.ViewHolder {
        TextView friendUsername;
        TextView friendInterestLevel;
        ImageView friendCheckbox;


        public FriendInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            friendUsername = itemView.findViewById(R.id.friend_user_name);
            friendInterestLevel = itemView.findViewById(R.id.favorite_star_icon);
            friendCheckbox = itemView.findViewById(R.id.event_type_icon);
        }
    }
}
