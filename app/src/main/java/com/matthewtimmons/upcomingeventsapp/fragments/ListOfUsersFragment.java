package com.matthewtimmons.upcomingeventsapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.controllers.UserController;
import com.matthewtimmons.upcomingeventsapp.manager.UserHelper;
import com.matthewtimmons.upcomingeventsapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class ListOfUsersFragment extends Fragment {
    private static final String FRIENDS_CHECKED = "FRIENDS_CHECKED";
    private static final String INCLUDE_CHECKMARKS = "INCLUDE_CHECKMARKS";
    private static final String FLAG = "FLAG";
    public static String columnTitle = "Friends";
    private static Bundle bundle;
    RecyclerView recyclerView;
    String currentUserId;
    DocumentReference currentUserDocRef;
    ArrayList<String> friendsChecked;
    String flag;
    boolean includeCheckmarks;
    boolean square;

    public static ListOfUsersFragment initializeBundleWithSharedValues(String currentUserId) {
        bundle = new Bundle();
        bundle.putString(User.CURRENT_USER_ID, currentUserId);
        return new ListOfUsersFragment();
    }

    public static ListOfUsersFragment newInstance(String currentUserId) {
        ListOfUsersFragment listOfUsersFragment = initializeBundleWithSharedValues(currentUserId);
        bundle.putBoolean(INCLUDE_CHECKMARKS, false);
        listOfUsersFragment.setArguments(bundle);
        return listOfUsersFragment;
    }

    public static ListOfUsersFragment newInstance(String currentUserId, String flag) {
        ListOfUsersFragment listOfUsersFragment = initializeBundleWithSharedValues(currentUserId);
        bundle.putString(FLAG, flag);
        listOfUsersFragment.setArguments(bundle);
        return listOfUsersFragment;
    }

    public static ListOfUsersFragment newInstance(String currentUserId, ArrayList<String> friendsChecked) {
        ListOfUsersFragment listOfUsersFragment = initializeBundleWithSharedValues(currentUserId);
        bundle.putString(FLAG, "checkable");
        bundle.putStringArrayList(FRIENDS_CHECKED, friendsChecked);
        bundle.putBoolean(INCLUDE_CHECKMARKS, true);
        listOfUsersFragment.setArguments(bundle);
        return listOfUsersFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUserId = getArguments().getString(User.CURRENT_USER_ID);
        includeCheckmarks = getArguments().getBoolean(INCLUDE_CHECKMARKS);
        friendsChecked = getArguments().getStringArrayList(FRIENDS_CHECKED);
        flag = getArguments().getString(FLAG);
        square = false;

        currentUserDocRef = UserController.getUserReference(currentUserId);

        // Set users list header
        if (flag != null && flag.equals("pendingFriendRequests")) { columnTitle = "Pending Friend Requests"; }
        else { columnTitle = "Friends"; }

        if ((flag == null) || (!flag.equals("pendingFriendRequests") && !flag.equals("Grid"))) {
            Fragment listOfUsersHeader = RecyclerViewHeaderFragment.newInstance(currentUserId, columnTitle);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.list_of_users_header, listOfUsersHeader).commit();
        }

        // Set users list recycler view and layout manager
        recyclerView = view.findViewById(R.id.list_of_users_recycler_view);
        if (flag == null || !flag.equals("Grid")) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (flag.equals("Grid")){
            int numberOfColumns = UserHelper.calculateNumberOfColumns(getContext(), 200);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
            square = true;
        }

        // Set list adapter
        if (flag == null || flag.equals("Grid")) {
            UserHelper.setUsersListAdapter(recyclerView, "friends", currentUserId, false, square);
        } else if (flag.equals("pendingFriendRequests")) {
            UserHelper.setPendingRequestsListAdapter(recyclerView, currentUserId);
        } else if (flag.equals("checkable")) {
            UserHelper.setCheckableUsersListAdapter(recyclerView, "friends", currentUserId, true, friendsChecked);
        }
    }
}
