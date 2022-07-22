package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;

public class PropertyDetailsFragment extends Fragment {

    private FragmentPropertyDetailsBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private Property mProperty;

    public PropertyDetailsFragment(Property property) {
        mProperty = property;
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
        initUI();
        return binding.getRoot();
    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
    }

    private void initUI() {
        binding.textViewDescription.setText(mProperty.getDescription());
        binding.textViewSurface.setText(String.valueOf(mProperty.getSurface()));
        binding.textViewRoomsCount.setText(String.valueOf(mProperty.getRooms_count()));
        binding.textViewLocation.setText(mProperty.getAddress());
    }
}