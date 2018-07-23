package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.FriendsListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewholder> {
    List<DocumentSnapshot> friendDocumentSnapshots;

    public FriendListAdapter(List<DocumentSnapshot> friendDocumentSnapshots) {
        this.friendDocumentSnapshots = friendDocumentSnapshots;
    }

    @NonNull
    @Override
    public FriendListViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_friend_icon_square, viewGroup, false);
        return new FriendListViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.FriendListViewholder friendListViewholder, int i) {
        DocumentSnapshot currentFriendDocumentSnapshot = friendDocumentSnapshots.get(i);

        friendListViewholder.displayName.setText(currentFriendDocumentSnapshot.getString("displayName"));
        Picasso.get().load(currentFriendDocumentSnapshot.getString("profilePhotoURL")).error(R.drawable.ic_default_profile_photo).into(friendListViewholder.profilePhoto);
    }

    @Override
    public int getItemCount() {
        return friendDocumentSnapshots.size();
    }

    public class FriendListViewholder extends RecyclerView.ViewHolder {
        ImageView profilePhoto;
        TextView displayName;

        public FriendListViewholder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_photo);
            displayName = itemView.findViewById(R.id.display_name);
        }
    }
}
