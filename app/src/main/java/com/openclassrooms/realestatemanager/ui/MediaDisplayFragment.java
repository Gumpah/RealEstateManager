package com.openclassrooms.realestatemanager.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMediaDisplayBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;

public class MediaDisplayFragment extends Fragment {

    private Uri mUri;
    private FragmentMediaDisplayBinding binding;

    public MediaDisplayFragment(Uri uri) {
        mUri = uri;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMediaDisplayBinding.inflate(inflater, container, false);
        setImage();
        return binding.getRoot();
    }

    private void setImage() {
        Glide.with(binding.getRoot())
                .load(mUri)
                .fitCenter()
                .into(binding.imageViewMedia);
    }
}