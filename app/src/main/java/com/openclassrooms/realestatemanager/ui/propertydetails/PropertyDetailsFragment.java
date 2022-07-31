package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

import java.util.ArrayList;

public class PropertyDetailsFragment extends Fragment implements DisplayMediaCallback {

    private FragmentPropertyDetailsBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private Property mProperty;
    private PropertyDetailsPagerAdapter mPropertyDetailsPagerAdapter;

    public PropertyDetailsFragment(Property property) {
        mProperty = property;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false);
        configureViewModel();
        initUI();
        initViewPager();
        initMedias();
        setMapButtonClickListener();
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

    private void initMedias() {
        mPropertyViewModel.getMediasByPropertyId().observe(getViewLifecycleOwner(), medias -> {
            mPropertyDetailsPagerAdapter.setData(medias);
        });
        mPropertyViewModel.getMediasByPropertyId(mProperty.getId());
    }

    private void initViewPager() {
        mPropertyDetailsPagerAdapter = new PropertyDetailsPagerAdapter(requireContext(), new ArrayList<>(), this);
        binding.viewPagerMedias.setAdapter(mPropertyDetailsPagerAdapter);
    }

    @Override
    public void displayMediaCallback(String uriString) {
        Uri uri = Uri.parse(uriString);
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, new MediaDisplayFragment(uri), "MediaDisplay")
                .addToBackStack("MediaDisplay")
                .commit();
    }

    private void setMapButtonClickListener() {
        binding.buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentMap();
            }
        });
    }

    private void showFragmentMap() {
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, new PropertyDetailsMapFragment(), "PropertyDetailsMap")
                .addToBackStack("PropertyDetailsMap")
                .commit();
    }

}