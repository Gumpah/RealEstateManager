package com.openclassrooms.realestatemanager.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.data.model.BitmapAndString;
import com.openclassrooms.realestatemanager.databinding.AddPropertyImageItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PropertyMediasAdapter extends RecyclerView.Adapter<PropertyMediasAdapter.AddPropertyMediasViewHolder> {

    private List<BitmapAndString> mBitmapList;
    private AddAndModifyPropertyCallback mCallback;

    public PropertyMediasAdapter(ArrayList<BitmapAndString> list, AddAndModifyPropertyCallback callback) {
        mBitmapList = list;
        mCallback = callback;
    }

    @NonNull
    @Override
    public PropertyMediasAdapter.AddPropertyMediasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PropertyMediasAdapter.AddPropertyMediasViewHolder(AddPropertyImageItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyMediasAdapter.AddPropertyMediasViewHolder holder, int position) {
        holder.bind(mBitmapList.get(position));
        holder.setClickListener(mCallback, position);
    }

    @Override
    public int getItemCount() {
        if (mBitmapList == null) {
            return 0;
        } else {
            return mBitmapList.size();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BitmapAndString> list) {
        mBitmapList = list;
        notifyDataSetChanged();
    }

    public static class AddPropertyMediasViewHolder extends RecyclerView.ViewHolder {

        private AddPropertyImageItemBinding binding;

        public AddPropertyMediasViewHolder(AddPropertyImageItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

        void bind(BitmapAndString bitmapAndString) {
            Glide.with(binding.getRoot())
                    .load(bitmapAndString.getMedia())
                    .centerCrop()
                    .into(binding.imageViewPropertyImage);
            binding.textViewImageName.setText(bitmapAndString.getMedia_description());
        }

        void setClickListener(AddAndModifyPropertyCallback callback, int position) {
            binding.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.removeMedia(position);
                }
            });
        }
    }
}
