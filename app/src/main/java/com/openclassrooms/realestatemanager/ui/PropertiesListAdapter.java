package com.openclassrooms.realestatemanager.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.PropertyListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PropertiesListAdapter extends RecyclerView.Adapter<PropertiesListAdapter.PropertiesListViewHolder> {

    public List<Property> mProperties;
    private ClickCallback mCallback;

    public PropertiesListAdapter(ArrayList<Property> list) {
        mProperties = list;
    }

    @NonNull
    @Override
    public PropertiesListAdapter.PropertiesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PropertiesListViewHolder(PropertyListItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertiesListAdapter.PropertiesListViewHolder holder, int position) {
        holder.bind(mProperties.get(position));
    }

    @Override
    public int getItemCount() {
        if (mProperties == null) {
            return 0;
        } else {
            return mProperties.size();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Property> list) {
        mProperties = list;
        notifyDataSetChanged();
    }

    public static class PropertiesListViewHolder extends RecyclerView.ViewHolder {

        private PropertyListItemBinding binding;

        public PropertiesListViewHolder(PropertyListItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

        void bind(Property property) {
            binding.textViewLocation.setText(property.getAddress());
            binding.textViewPrice.setText(String.valueOf(property.getPrice()));
            binding.textViewPropertyType.setText(property.getProperty_type());
        }
    }
}
