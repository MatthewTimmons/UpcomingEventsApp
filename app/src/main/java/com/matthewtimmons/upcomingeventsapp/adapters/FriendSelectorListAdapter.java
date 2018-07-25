package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.matthewtimmons.upcomingeventsapp.activities.ProfileViewActivity;

import java.util.ArrayList;
import java.util.List;

public class FriendSelectorListAdapter extends RecyclerView.Adapter<FriendSelectorListAdapter.FriendSelectorViewHolder> {
    List<DocumentSnapshot> friends;
    List<String> friendsChecked;
    boolean includeCheckboxes;
    boolean friendsClickable;
    String numberOfSharedGames = "0";

    public FriendSelectorListAdapter(List<DocumentSnapshot> friends, ArrayList<String> friendsChecked, boolean includeCheckboxes, boolean friendsClickable) {
        this.friends = friends;
        this.friendsChecked = friendsChecked;
        this.includeCheckboxes = includeCheckboxes;
        this.friendsClickable = friendsClickable;
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

        friendSelectorViewHolder.friendUsername.setText(userDocumentSnapshot.getString("displayName"));

        friendSelectorViewHolder.numberOfSharedGames.setText("Number of shared games");
        friendSelectorViewHolder.numberOfSharedGames.setVisibility(View.GONE);

        if (includeCheckboxes) {
            friendSelectorViewHolder.friendCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (friendsChecked.size() <= 1) {
                            friendsChecked.add(userDocumentSnapshot.getId());
                        } else {
                            Toast.makeText(friendSelectorViewHolder.itemView.getContext(), "Cannot select more than two people", Toast.LENGTH_SHORT).show();
                            compoundButton.setChecked(false);
                            friendsChecked.remove(userDocumentSnapshot.getId());
                        }
                    } else if (!b) {
                            friendsChecked.remove(userDocumentSnapshot.getId());
                    }
                }
            });
        } else if (!includeCheckboxes) {
            friendSelectorViewHolder.friendCheckbox.setVisibility(View.GONE);
        }

        if (friendsClickable) {
            friendSelectorViewHolder.friendUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context =friendSelectorViewHolder.friendUsername.getContext();
                    Intent intent = new Intent(context, ProfileViewActivity.class);
                    intent.putExtra("CURRENT_USER", userDocumentSnapshot.getId());
                    context.startActivity(intent);
                }
            });
        }
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
