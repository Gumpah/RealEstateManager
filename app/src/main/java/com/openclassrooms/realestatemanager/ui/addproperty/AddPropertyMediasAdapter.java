package com.openclassrooms.realestatemanager.ui.addproperty;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.AddPropertyImageItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AddPropertyMediasAdapter extends RecyclerView.Adapter<AddPropertyMediasAdapter.AddPropertyMediasViewHolder> {

    private List<Bitmap> mBitmapList;
    private AddPropertyCallback mCallback;

    public AddPropertyMediasAdapter(ArrayList<Bitmap> list, AddPropertyCallback callback) {
        mBitmapList = list;
        mCallback = callback;
    }

    @NonNull
    @Override
    public AddPropertyMediasAdapter.AddPropertyMediasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AddPropertyMediasAdapter.AddPropertyMediasViewHolder(AddPropertyImageItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddPropertyMediasAdapter.AddPropertyMediasViewHolder holder, int position) {
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
    public void setData(List<Bitmap> list) {
        mBitmapList = list;
        notifyDataSetChanged();
    }

    public static class AddPropertyMediasViewHolder extends RecyclerView.ViewHolder {

        private AddPropertyImageItemBinding binding;

        public AddPropertyMediasViewHolder(AddPropertyImageItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

        void bind(Bitmap bitmap) {
            Glide.with(binding.getRoot())
                    .load(bitmap)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.imageViewPropertyImage);
        }

        void setClickListener(AddPropertyCallback callback, int position) {
            binding.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.removeMedia(position);
                }
            });
        }
    }
}
