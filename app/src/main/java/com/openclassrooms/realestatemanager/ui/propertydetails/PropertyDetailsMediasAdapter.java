package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.data.model.Media;
import com.openclassrooms.realestatemanager.databinding.PropertyDetailsImageItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PropertyDetailsMediasAdapter extends RecyclerView.Adapter<PropertyDetailsMediasAdapter.PropertyDetailsMediasViewHolder> {

    public List<Media> mMediaList;

    public PropertyDetailsMediasAdapter(ArrayList<Media> list) {
        mMediaList = list;
    }

    @NonNull
    @Override
    public PropertyDetailsMediasAdapter.PropertyDetailsMediasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PropertyDetailsMediasAdapter.PropertyDetailsMediasViewHolder(PropertyDetailsImageItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyDetailsMediasAdapter.PropertyDetailsMediasViewHolder holder, int position) {
        holder.bind(mMediaList.get(position));
        holder.setClickListener();
    }

    @Override
    public int getItemCount() {
        if (mMediaList == null) {
            return 0;
        } else {
            return mMediaList.size();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Media> list) {
        mMediaList = list;
        notifyDataSetChanged();
    }

    public static class PropertyDetailsMediasViewHolder extends RecyclerView.ViewHolder {

        private PropertyDetailsImageItemBinding binding;

        public PropertyDetailsMediasViewHolder(PropertyDetailsImageItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

        void bind(Media media) {
            Glide.with(binding.getRoot())
                    .load(media.getMedia_uri())
                    .centerCrop()
                    .into(binding.imageViewMedia);
            binding.textViewMediaTitle.setText(media.getName());
        }

        void setClickListener() {
            binding.constraintLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
