package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;

import java.util.ArrayList;
import java.util.List;

public class FriendSelectorListAdapter extends RecyclerView.Adapter<FriendSelectorListAdapter.FriendSelectorViewHolder> {
    List<DocumentSnapshot> friends;
    List<String> friendsChecked;
    String numberOfSharedGames = "0";

    public FriendSelectorListAdapter(List<DocumentSnapshot> friends, ArrayList<String> friendsChecked) {
        this.friends = friends;
        this.friendsChecked = friendsChecked;
    }

    @NonNull
    @Override
    public FriendSelectorListAdapter.FriendSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_friend_information, viewGroup, false);
        return new FriendSelectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendSelectorListAdapter.FriendSelectorViewHolder friendSelectorViewHolder, int i) {
        final DocumentSnapshot userDocumentSnapshot = friends.get(i);

        friendSelectorViewHolder.friendUsername.setText(userDocumentSnapshot.getId());

        friendSelectorViewHolder.numberOfSharedGames.setText("Number of shared games");
        friendSelectorViewHolder.numberOfSharedGames.setVisibility(View.GONE);

        friendSelectorViewHolder.friendCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (friendsChecked.size() > 1) {
                    Toast.makeText(friendSelectorViewHolder.itemView.getContext(), "Cannot select more than two people", Toast.LENGTH_LONG).show();
                    compoundButton.setChecked(false);
                    friendsChecked.remove(userDocumentSnapshot.getId());
                } else if (friendsChecked.size() <= 1){
                    if (b) {
                        friendsChecked.add(userDocumentSnapshot.getId());
                    } else if (!b) {
                        friendsChecked.remove(userDocumentSnapshot.getId());
                    }
                }
            }
        });
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
