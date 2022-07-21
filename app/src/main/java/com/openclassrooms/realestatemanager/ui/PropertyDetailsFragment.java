package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;

public class PropertyDetailsFragment extends Fragment {

    private FragmentPropertyDetailsBinding binding;
    private PropertyViewModel mPropertyViewModel;

    public PropertyDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false);
        configureViewModel();
        return binding.getRoot();
    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
    }
}