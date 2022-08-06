package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.StaticMapService;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

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
        initStaticMap();
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
        mPropertyViewModel.getMediasByPropertyIdLiveData().observe(getViewLifecycleOwner(), medias -> {
            mPropertyDetailsPagerAdapter.setData(medias);
        });
        // OLD-REQUEST mPropertyViewModel.getMediasByPropertyId(mProperty.getId());
        mPropertyViewModel.getMediasByPropertyIdContentProvider(requireContext().getContentResolver(), mProperty.getId());
    }

    private void initViewPager() {
        mPropertyDetailsPagerAdapter = new PropertyDetailsPagerAdapter(requireContext(), new ArrayList<>(), this);
        binding.viewPagerMedias.setAdapter(mPropertyDetailsPagerAdapter);
        binding.dotsIndicator.attachTo(binding.viewPagerMedias);
    }

    @Override
    public void displayMediaCallback(String uriString) {
        Uri uri = Uri.parse(uriString);
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, new MediaDisplayFragment(uri), "MediaDisplay")
                .addToBackStack("MediaDisplay")
                .commit();
    }

    private void initStaticMap() {
        Glide.with(binding.getRoot())
                .load(StaticMapService.getStaticMap(BuildConfig.MAPS_API_KEY, Utils.createLocationString(mProperty.getLatitude(), mProperty.getLongitude())))
                .into(binding.imageViewStaticMap);
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
                replace(R.id.frameLayout_fragmentContainer, new PropertyDetailsMapFragment(mProperty), "PropertyDetailsMap")
                .addToBackStack("PropertyDetailsMap")
                .commit();
    }

}