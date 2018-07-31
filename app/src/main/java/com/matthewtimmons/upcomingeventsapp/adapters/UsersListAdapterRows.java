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
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapterRows extends RecyclerView.Adapter<UsersListAdapterRows.FriendSelectorViewHolder> {
    List<DocumentSnapshot> friends;
    List<String> friendsChecked;
    boolean includeCheckboxes;
    String numberOfSharedGames = "0";

    public UsersListAdapterRows(List<DocumentSnapshot> friends) {
        this.friends = friends;
        this.includeCheckboxes = false;
    }

    public UsersListAdapterRows(List<DocumentSnapshot> friends, ArrayList<String> friendsChecked) {
        this.friends = friends;
        this.friendsChecked = friendsChecked;
        this.includeCheckboxes = true;
    }

    @NonNull
    @Override
    public UsersListAdapterRows.FriendSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_friend_information, viewGroup, false);
        return new FriendSelectorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersListAdapterRows.FriendSelectorViewHolder friendSelectorViewHolder, int i) {
        final DocumentSnapshot userDocumentSnapshot = friends.get(i);
        final User user = new User(userDocumentSnapshot);

        // Set users displayname
        friendSelectorViewHolder.friendUsername.setText(user.getDisplayName());

        if (includeCheckboxes) {
            // Set rules for checkboxes
            friendSelectorViewHolder.friendCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (friendsChecked.size() <= 1) {
                            friendsChecked.add(user.getId());
                        } else {
                            Toast.makeText(friendSelectorViewHolder.itemView.getContext(), "Cannot select more than two people", Toast.LENGTH_SHORT).show();
                            compoundButton.setChecked(false);
                            friendsChecked.remove(user.getId());
                        }
                    } else if (!b) {
                            friendsChecked.remove(user.getId());
                    }
                }
            });
        } else {
            // Make checkbox invisible and make friends clickable
            friendSelectorViewHolder.friendCheckbox.setVisibility(View.GONE);
            friendSelectorViewHolder.friendUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context =friendSelectorViewHolder.friendUsername.getContext();
                    Intent intent = new Intent(context, ProfileViewActivity.class);
                    intent.putExtra(User.CURRENT_USER_ID, user.getId());
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
        CheckBox friendCheckbox;

        public FriendSelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            friendUsername = itemView.findViewById(R.id.friend_user_name);
            friendCheckbox = itemView.findViewById(R.id.event_type_icon);
        }
    }
}
