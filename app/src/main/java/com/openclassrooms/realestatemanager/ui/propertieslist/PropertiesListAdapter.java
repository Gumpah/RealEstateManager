package com.openclassrooms.realestatemanager.ui.propertieslist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.data.model.PropertyAndImage;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.PropertyListItemBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PropertiesListAdapter extends RecyclerView.Adapter<PropertiesListAdapter.PropertiesListViewHolder> {

    public List<PropertyAndImage> mProperties;
    private PropertyListCallback mCallback;

    public PropertiesListAdapter(ArrayList<PropertyAndImage> list, PropertyListCallback callback) {
        mProperties = list;
        mCallback = callback;
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
        holder.setClickListener(mCallback, mProperties.get(position).getProperty());
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
    public void setData(List<PropertyAndImage> list) {
        mProperties = list;
        notifyDataSetChanged();
    }

    public static class PropertiesListViewHolder extends RecyclerView.ViewHolder {

        private PropertyListItemBinding binding;

        public PropertiesListViewHolder(PropertyListItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

        void bind(PropertyAndImage propertyAndImage) {
            binding.textViewLocation.setText(propertyAndImage.getProperty().getAddress());
            binding.textViewPrice.setText(String.valueOf(propertyAndImage.getProperty().getPrice()));
            binding.textViewPropertyType.setText(Utils.getTypeInUserLanguage(binding.getRoot().getContext(), propertyAndImage.getProperty().getProperty_type()));
            Glide.with(binding.getRoot())
                    .load(propertyAndImage.getUri())
                    .centerCrop()
                    .into(binding.imageViewPropertyImage);
        }

        void setClickListener(PropertyListCallback callback, Property property) {
            binding.constraintLayoutContainerClickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.propertiesListAdapterCallback(property);
                }
            });
        }
    }
}
