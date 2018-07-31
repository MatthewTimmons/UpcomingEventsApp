package com.matthewtimmons.upcomingeventsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.activities.ProfileViewActivity;
import com.matthewtimmons.upcomingeventsapp.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersListAdapterSquare extends RecyclerView.Adapter<UsersListAdapterSquare.FriendListViewholder> {
    List<DocumentSnapshot> friendDocumentSnapshots;

    public UsersListAdapterSquare(List<DocumentSnapshot> friendDocumentSnapshots) {
        this.friendDocumentSnapshots = friendDocumentSnapshots;
    }

    @NonNull
    @Override
    public FriendListViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_friend_icon_square, viewGroup, false);
        return new FriendListViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersListAdapterSquare.FriendListViewholder friendListViewholder, int i) {
        final DocumentSnapshot currentFriendDocumentSnapshot = friendDocumentSnapshots.get(i);
        User user = new User(currentFriendDocumentSnapshot);

        // Set image and displayname
        friendListViewholder.displayName.setText(user.getDisplayName());
        Picasso.get().load(user.getProfilePhotoURL()).error(R.drawable.ic_default_profile_photo).into(friendListViewholder.profilePhoto);

        friendListViewholder.friendSquareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = friendListViewholder.friendSquareIcon.getContext();
                Intent intent = new Intent(context, ProfileViewActivity.class);
                intent.putExtra(User.CURRENT_USER_ID, currentFriendDocumentSnapshot.getString("displayName"));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendDocumentSnapshots.size();
    }

    public class FriendListViewholder extends RecyclerView.ViewHolder {
        ConstraintLayout friendSquareIcon;
        ImageView profilePhoto;
        TextView displayName;

        public FriendListViewholder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_photo);
            displayName = itemView.findViewById(R.id.display_name);
            friendSquareIcon = itemView.findViewById(R.id.friend_square_icon);
        }
    }
}
