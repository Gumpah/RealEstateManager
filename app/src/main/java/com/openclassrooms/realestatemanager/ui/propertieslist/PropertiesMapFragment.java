package com.openclassrooms.realestatemanager.ui.propertieslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesMapBinding;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PropertiesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private FragmentPropertiesMapBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private ArrayList<Property> mProperties;
    private GoogleMap mMap;

    public PropertiesMapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertiesMapBinding.inflate(inflater, container, false);
        configureViewModels();
        setToolbar();
        initMap();
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
    }

    private void setToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarPropertiesMap);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        binding.toolbarToolbarPropertiesMap.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView_propertiesMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void getProperties() {
        mPropertyViewModel.getPropertiesLiveData().observe(getViewLifecycleOwner(), list -> {
            mProperties = new ArrayList<>(list);
            displayPropertiesMarkers(list);
        });
        mPropertyViewModel.getPropertiesContentProvider(requireContext().getContentResolver());
    }

    private void displayPropertiesMarkers(List<Property> properties) {
        for (Property property : properties) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(property.getLatitude(), property.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(Utils.getTypeInUserLanguage(requireContext(), property.getProperty_type()));
            Marker marker = mMap.addMarker(markerOptions);
            if (marker != null) marker.setTag(property.getId());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        mMap.setOnInfoWindowClickListener(this);
        getProperties();
        //CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(17);
        //mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if (marker.getTag() != null) {
            System.out.println("Marker call");
            Property property = mPropertyViewModel.getPropertyInListFromId(mProperties, Long.parseLong(String.valueOf(marker.getTag())));
            if (property != null) {
                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frameLayout_fragmentContainer, new PropertyDetailsFragment(property), "PropertyDetails")
                        .addToBackStack("PropertyDetails")
                        .commit();
            }
        }
    }
}
