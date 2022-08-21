package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.FragmentMediaDisplayBinding;

public class MediaDisplayFragment extends Fragment {

    private FragmentMediaDisplayBinding binding;

    public MediaDisplayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMediaDisplayBinding.inflate(inflater, container, false);
        setToolbar();
        getUriFromBundleArgs();
        return binding.getRoot();
    }

    private void setToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarMediaDisplay);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        binding.toolbarToolbarMediaDisplay.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void getUriFromBundleArgs() {
        Bundle args = getArguments();
        if (args != null && args.getString("Uri") != null) {
            setImage(Uri.parse(args.getString("Uri")));
        }
    }

    private void setImage(Uri uri) {
        Glide.with(binding.getRoot())
                .load(uri)
                .fitCenter()
                .into(binding.imageViewMedia);
    }
}