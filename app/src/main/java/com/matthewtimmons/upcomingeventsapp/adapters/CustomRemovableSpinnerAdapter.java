package com.matthewtimmons.upcomingeventsapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtimmons.upcomingeventsapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomRemovableSpinnerAdapter extends RecyclerView.Adapter<CustomRemovableSpinnerAdapter.CustomRemovableViewHolder> {
    ArrayList<String> bandNames;
    String bandName;

    public CustomRemovableSpinnerAdapter(ArrayList<String> bandNames) {
        this.bandNames = bandNames;
    }

    @NonNull
    @Override
    public CustomRemovableViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_item_removable_item, viewGroup, false);
        return new CustomRemovableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomRemovableViewHolder customRemovableViewHolder, final int i) {
        bandName = bandNames.get(i);
        customRemovableViewHolder.bandNameTextView.setText(bandName);

        customRemovableViewHolder.moveItemUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > 0) {
                    String toMove = bandNames.get(i);
                    bandNames.set(i, bandNames.get(i - 1));
                    bandNames.set(i - 1, toMove);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(view.getContext(), "Item is already at the top of the list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        customRemovableViewHolder.moveItemDownImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i < bandNames.size() - 1) {
                    String toMove = bandNames.get(i);
                    bandNames.set(i, bandNames.get(i + 1));
                    bandNames.set(i + 1, toMove);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(view.getContext(), "Item is already at the bottom of the list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        customRemovableViewHolder.deleteItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bandNames.remove(i);
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "\"" + bandName + "\" has been removed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bandNames.size();
    }

    public class CustomRemovableViewHolder extends RecyclerView.ViewHolder {
        TextView bandNameTextView;
        ImageView moveItemUpImageView;
        ImageView moveItemDownImageView;
        ImageView deleteItemImageView;

        public CustomRemovableViewHolder(@NonNull View itemView) {
            super(itemView);
            bandNameTextView = itemView.findViewById(R.id.band_name);
            moveItemUpImageView = itemView.findViewById(R.id.up_arrow_icon_image_view);
            moveItemDownImageView = itemView.findViewById(R.id.down_arrow_icon_image_view);
            deleteItemImageView = itemView.findViewById(R.id.remove_icon_image_view);
        }
    }
}
