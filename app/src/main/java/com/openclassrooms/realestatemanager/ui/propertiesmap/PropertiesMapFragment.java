package com.openclassrooms.realestatemanager.ui.propertiesmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import pub.devrel.easypermissions.AppSettingsDialog;

public class PropertiesMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private FragmentPropertiesMapBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private UserViewModel mUserViewModel;
    private ArrayList<Property> mProperties;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public PropertiesMapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertiesMapBinding.inflate(inflater, container, false);
        configureViewModels();
        setToolbar();
        initMap();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity(), UserViewModelFactory.getInstance(requireContext())).get(UserViewModel.class);
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

    private void checkLocationPermission() {
        boolean status = Utils.getLocationPermissionStatus(requireContext());
        if (!status) {
            locationPermissionRequest.launch(PERMISSIONS);
        } else {
            mUserViewModel.getLocationPermissionStatus().postValue(true);
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationPermissionStatusListener() {
        mUserViewModel.getLocationPermissionStatus().observe(getViewLifecycleOwner(), value -> {
            if (value && Utils.getLocationPermissionStatus(requireContext())) {
                if (!mMap.isMyLocationEnabled()) {
                    mMap.setMyLocationEnabled(true);
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLng, 14);
                        mMap.animateCamera(cameraUpdate);
                    }
                });
            }
        } );
    }

    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                        if (isGranted.containsValue(true)) {
                            mUserViewModel.getLocationPermissionStatus().postValue(true);
                        } else if (isGranted.containsValue(false)){
                            mUserViewModel.getLocationPermissionStatus().postValue(false);
                            showMissingPermissionError();
                        }
                    }
            );


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
        initLocationPermissionStatusListener();
        checkLocationPermission();
        getProperties();
    }

    private void showMissingPermissionError() {
        new AppSettingsDialog.Builder(requireActivity())
                .setThemeResId(R.style.AlertDialog)
                .setTitle(R.string.location_permission_denied_dialog_title)
                .setRationale(R.string.location_permission_denied_dialog_message)
                .build()
                .show();
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if (marker.getTag() != null) {
            Property property = mPropertyViewModel.getPropertyInListFromId(mProperties, Long.parseLong(String.valueOf(marker.getTag())));
            if (property != null) {
                PropertyDetailsFragment propertyDetailsFragment = new PropertyDetailsFragment();
                Bundle args = new Bundle();
                args.putLong("PropertyId", property.getId());
                propertyDetailsFragment.setArguments(args);
                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frameLayout_fragmentContainer, propertyDetailsFragment, "PropertyDetails")
                        .addToBackStack("PropertyDetails")
                        .commit();
            }
        }
    }
}
