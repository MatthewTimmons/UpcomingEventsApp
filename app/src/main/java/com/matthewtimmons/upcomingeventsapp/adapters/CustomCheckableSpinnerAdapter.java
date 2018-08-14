package com.matthewtimmons.upcomingeventsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.matthewtimmons.upcomingeventsapp.R;
import com.matthewtimmons.upcomingeventsapp.manager.Firestore;

import java.util.ArrayList;
import java.util.List;

public class CustomCheckableSpinnerAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    List<String> listData;
    ArrayList<String> allCurrentlyOwnedConsoles;
    String currentUserId, gameId;

    private static final long DELAY_SEEKBAR_HINT_MS = 1000L;
    Handler checkmarksHandler = new Handler();

    public CustomCheckableSpinnerAdapter(List<String> listData, Activity activity, String currentUserId, String gameId, ArrayList<String> allCurrentlyOwnedConsoles) {
        this.listData = listData;
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentUserId = currentUserId;
        this.gameId = gameId;
        if (allCurrentlyOwnedConsoles != null) {
            this.allCurrentlyOwnedConsoles = allCurrentlyOwnedConsoles;
        } else {
            this.allCurrentlyOwnedConsoles = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item_checkable_item, null);
        }
        TextView spinnerItemName = view.findViewById(R.id.item_string);
        spinnerItemName.setText(listData.get(position));

        if (!allCurrentlyOwnedConsoles.isEmpty() && position == 0) {
            ((CheckBox) view.findViewById(R.id.checkbox)).setChecked(true);
        }
        return view;
    }

    //TODO Fix the checkbox functionality
    @Override
    public View getDropDownView(int position, View convertView, final ViewGroup parent) {
        final View view = super.getDropDownView(position, convertView, parent);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        final String currentConsoleName = ((TextView) view.findViewById(R.id.item_string)).getText().toString();

        if (allCurrentlyOwnedConsoles.contains(currentConsoleName)) {
            checkBox.setOnCheckedChangeListener (null);
            checkBox.setChecked (true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                final String consoleName = ((TextView) view.findViewById(R.id.item_string)).getText().toString();
                if (!consoleName.equals("Owned")) {
                    if (b) {
                        if (allCurrentlyOwnedConsoles.isEmpty()) {
                            allCurrentlyOwnedConsoles.add("Owned");
                        }
                        allCurrentlyOwnedConsoles.add(consoleName);
                    } else {
                        allCurrentlyOwnedConsoles.remove(consoleName);
                        if (allCurrentlyOwnedConsoles.size() == 1) {
                            allCurrentlyOwnedConsoles.remove("Owned");
                        }
                    }
                } else {
                    if (b) {
                        allCurrentlyOwnedConsoles.add(consoleName);
                    } else {
                        allCurrentlyOwnedConsoles = new ArrayList<>();
                    }
                }
                checkmarksHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!allCurrentlyOwnedConsoles.isEmpty()) {
                            Firestore.updateFirestoreDocument("users/" + currentUserId,
                                    "gamesOwned." + gameId,
                                    allCurrentlyOwnedConsoles);
                        } else {
                            Firestore.updateFirestoreDocument("users/" + currentUserId,
                                    "gamesOwned." + gameId,
                                    FieldValue.delete());
                        }
                    }
                }, DELAY_SEEKBAR_HINT_MS);
            }
        });
        return view;
    }
}
