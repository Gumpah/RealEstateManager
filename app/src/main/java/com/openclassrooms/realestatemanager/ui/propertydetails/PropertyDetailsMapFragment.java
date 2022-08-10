package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsMapBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

import java.util.List;

public class PropertyDetailsMapFragment extends Fragment implements OnMapReadyCallback {
    
    private FragmentPropertyDetailsMapBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private Property mProperty;
    private GoogleMap mMap;

    public PropertyDetailsMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertyDetailsMapBinding.inflate(inflater, container, false);
        configureViewModels();
        initMap();
        initPropertyByIdListener();
        setToolbar();
        Bundle args = getArguments();
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
    }

    private void getPropertyById() {
        Bundle args = getArguments();
        if (args != null && args.getLong("PropertyId", 0) != 0) {
            System.out.println("Id is: ");
            mPropertyViewModel.getPropertyByIdContentProvider(requireActivity().getContentResolver(), args.getLong("PropertyId", 0));
        }
    }

    private void initPropertyByIdListener() {
        mPropertyViewModel.getPropertyByIdLiveData().observe(getViewLifecycleOwner(), property -> {
            mProperty = property;
            displayPropertyMarker();
            getPropertyPlaces();
            cameraZoom();
        });
    }

    private void setToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarPropertyDetailsMap);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        binding.toolbarToolbarPropertyDetailsMap.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView_propertyDetailsMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void getPropertyPlaces() {
        mPropertyViewModel.getPlacesLiveData().observe(getViewLifecycleOwner(), this::displayPlacesMarkers);
        // OLD-REQUEST mPropertyViewModel.getPlacesByPropertyId(mProperty.getId());
        mPropertyViewModel.getPlacesByPropertyIdContentProvider(requireContext().getContentResolver(), mProperty.getId());
    }

    private void displayPropertyMarker() {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(mProperty.getLatitude(), mProperty.getLongitude()))
                .title("Property");
        Marker marker = mMap.addMarker(markerOptions);
        if (marker != null) {
            marker.showInfoWindow();
        }
    }

    private void displayPlacesMarkers(List<Place> places) {
        for (Place place : places) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(place.getLatitude(), place.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(place.getName());
            mMap.addMarker(markerOptions);
        }
    }

    private void cameraZoom() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mProperty.getLatitude(), mProperty.getLongitude()), 17);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        System.out.println("Map ready");
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        getPropertyById();
    }


}
