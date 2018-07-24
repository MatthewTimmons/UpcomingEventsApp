package com.matthewtimmons.upcomingeventsapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class FriendListGridAdapter extends BaseAdapter {
    List<DocumentSnapshot> friends;

    public FriendListGridAdapter(List<DocumentSnapshot> friends) {
        this.friends = friends;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
