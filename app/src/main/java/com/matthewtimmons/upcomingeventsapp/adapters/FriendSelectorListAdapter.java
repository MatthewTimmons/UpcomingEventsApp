package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;

import java.util.List;

public class FriendSelectorListAdapter extends RecyclerView.Adapter<FriendSelectorListAdapter.FriendSelectorViewHolder> {
    List<DocumentSnapshot> friends;

    public FriendSelectorListAdapter(List<DocumentSnapshot> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendSelectorListAdapter.FriendSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_friend_information, viewGroup, false);
        return new FriendSelectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendSelectorListAdapter.FriendSelectorViewHolder friendSelectorViewHolder, int i) {
        DocumentSnapshot userDocumentSnapshot = friends.get(i);

        friendSelectorViewHolder.friendUsername.setText(userDocumentSnapshot.getId());

        String numberOfSharedGames = "0";
        friendSelectorViewHolder.numberOfSharedGames.setText("Number of shared games");
        friendSelectorViewHolder.numberOfSharedGames.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendSelectorViewHolder extends RecyclerView.ViewHolder {
        TextView friendUsername;
        TextView numberOfSharedGames;
        CheckBox friendCheckbox;


        public FriendSelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            friendUsername = itemView.findViewById(R.id.friend_user_name);
            numberOfSharedGames = itemView.findViewById(R.id.number_of_shared_games);
            friendCheckbox = itemView.findViewById(R.id.event_type_icon);
        }
    }
}
