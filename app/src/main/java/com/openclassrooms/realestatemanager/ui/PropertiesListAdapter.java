package com.openclassrooms.realestatemanager.ui;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PropertiesListAdapter extends RecyclerView.Adapter<PropertiesListAdapter.PropertiesListViewHolder> {

    @NonNull
    @Override
    public PropertiesListAdapter.PropertiesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PropertiesListAdapter.PropertiesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class PropertiesListViewHolder extends RecyclerView.ViewHolder {

        public PropertiesListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
