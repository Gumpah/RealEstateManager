package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.Media;
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.data.model.PropertyStatus;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class AddPropertyFragment extends Fragment {

    private FragmentAddPropertyBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private ClickCallback mClickCallback;

    public AddPropertyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPropertyBinding.inflate(inflater, container, false);
        configureViewModel();
        setClickListener();
        return binding.getRoot();
    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
    }

    private void setClickListener() {
        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void onSubmit() {
        String address = "";
        if (binding.textInputLayoutAddress.getEditText() != null) {
            address = binding.textInputLayoutAddress.getEditText().getText().toString();
        }
        String agent = "";
        if (binding.textInputLayoutAgent.getEditText() != null) {
            agent = binding.textInputLayoutAgent.getEditText().getText().toString();
        }
        String description = "";
        if (binding.textInputLayoutDescription.getEditText() != null) {
            description = binding.textInputLayoutDescription.getEditText().getText().toString();
        }
        String marketEntryDate = "";
        if (binding.textInputLayoutMarketEntryDate.getEditText() != null) {
            marketEntryDate = binding.textInputLayoutMarketEntryDate.getEditText().getText().toString();
        }
        String price = "";
        if (binding.textInputLayoutPrice.getEditText() != null) {
            price = binding.textInputLayoutPrice.getEditText().getText().toString();
        }
        String propertyType = "";
        if (binding.textInputLayoutPropertyType.getEditText() != null) {
            propertyType = binding.textInputLayoutPropertyType.getEditText().getText().toString();
        }
        String roomsCount = "";
        if (binding.textInputLayoutRoomsCount.getEditText() != null) {
            roomsCount = binding.textInputLayoutRoomsCount.getEditText().getText().toString();
        }
        String surface = "";
        if (binding.textInputLayoutSurface.getEditText() != null) {
            surface = binding.textInputLayoutSurface.getEditText().getText().toString();
        }

        if (address.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (agent.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (description.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (marketEntryDate.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (price.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (propertyType.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (roomsCount.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (surface.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        Property property = new Property(propertyType, Double.valueOf(price), Integer.parseInt(surface), Integer.parseInt(roomsCount), description, address, PropertyStatus.AVAILABLE, marketEntryDate, agent);
        System.out.println("Test1" + property.getId());
        ArrayList<Media> medias = new ArrayList<>();
        medias.add(new Media("test1", "chambre"));
        medias.add(new Media("test2", "salon"));
        mPropertyViewModel.insertPropertyAndMedias(property, medias);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}