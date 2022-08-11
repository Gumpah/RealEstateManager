package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.databinding.PropertyDetailsImageItemBinding;

import java.util.List;

public class PropertyDetailsPagerAdapter extends PagerAdapter {

    // Context object
    Context mContext;

    // Array of images
    List<Media> mMedias;

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    DisplayMediaCallback mCallback;

    public PropertyDetailsPagerAdapter(Context context, List<Media> medias, DisplayMediaCallback callback) {
        mContext = context;
        mMedias = medias;
        mCallback = callback;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
    }

    public void setData(List<Media> medias) {
        mMedias = medias;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMedias.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PropertyDetailsImageItemBinding binding = PropertyDetailsImageItemBinding.inflate(mLayoutInflater, container, false);
        Media media = mMedias.get(position);
        Glide.with(binding.getRoot())
                .load(media.getMedia_uri())
                .centerCrop()
                .into(binding.imageViewMedia);
        binding.textViewMediaTitle.setText(media.getName());
        container.addView(binding.getRoot());
        setClickListener(binding.constraintLayoutContainer, media.getMedia_uri());
        return binding.getRoot();
    }

    private void setClickListener(ConstraintLayout constraintLayoutContainer, String mediaUri) {
        constraintLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.displayMediaCallback(mediaUri);
            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
