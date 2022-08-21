package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.StaticMapService;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;
import com.openclassrooms.realestatemanager.ui.modifyproperty.ModifyPropertyFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;

public class PropertyDetailsFragment extends Fragment implements DisplayMediaCallback {

    private FragmentPropertyDetailsBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private Property mProperty;
    private PropertyDetailsPagerAdapter mPropertyDetailsPagerAdapter;
    private Toolbar toolbar;
    private boolean isTablet;

    public PropertyDetailsFragment() {
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
        isTablet = requireContext().getResources().getBoolean(R.bool.isTablet);
        configureViewModel();
        initViewPager();
        setMediasListener();
        initPropertyByIdListener();
        setMapButtonClickListener();
        initData();
        getPropertyById();
        initToolbar();
        return binding.getRoot();
    }

    private void initToolbar() {
        if (isTablet) {
            toolbar = requireActivity().findViewById(R.id.toolbar_toolbarPropertyList);
            toolbar.getMenu().removeItem(R.id.menuItem_edit);
        } else {
            toolbar = binding.toolbarToolbarPropertyDetails;
            setToolbar();
            initMenu();
        }
        if (toolbar != null) toolbar.inflateMenu(R.menu.menu_propertydetails);
    }

    private void setToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarPropertyDetails);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        binding.toolbarToolbarPropertyDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void initMenu() {
        if (!isTablet && binding.toolbarToolbarPropertyDetails != null) {
            binding.toolbarToolbarPropertyDetails.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menuItem_edit) {
                        requireActivity().getSupportFragmentManager().beginTransaction().
                                replace(R.id.frameLayout_fragmentContainer, ModifyPropertyFragment.newInstance(mProperty.getId()), "PropertySearch")
                                .addToBackStack("PropertySearch")
                                .commit();
                    }
                    return true;
                }
            });
        }
    }

    private void getPropertyById() {
        Bundle args = getArguments();
        if (args != null && args.getLong("PropertyId", 0) != 0) {
            mPropertyViewModel.getPropertyByIdContentProvider(requireActivity().getContentResolver(), args.getLong("PropertyId", 0));
        }
    }

    private void initPropertyByIdListener() {
        mPropertyViewModel.getPropertyByIdLiveData().observe(getViewLifecycleOwner(), property -> {
            mProperty = property;
            initData();
        });
    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
    }

    private void initData() {
        if (mProperty != null) {
            initUIData();
            initStaticMap();
            mPropertyViewModel.getMediasByPropertyIdContentProvider(requireContext().getContentResolver(), mProperty.getId());
        }
    }

    private void initUIData() {
        if (mProperty.getDescription().isEmpty()) {
            binding.textViewDescription.setText(R.string.noDescription);
        } else {
            binding.textViewDescription.setText(mProperty.getDescription());
        }
        if (mProperty.getSurface() != -1) {
            binding.textViewSurface.setText(String.valueOf(mProperty.getSurface()));
        } else {
            binding.textViewSurface.setText("-");
        }
        if (mProperty.getRooms_count() != -1) {
            binding.textViewRoomsCount.setText(String.valueOf(mProperty.getRooms_count()));
        } else {
            binding.textViewRoomsCount.setText("-");
        }
        if (mProperty.getBathrooms_count() != -1) {
            binding.textViewBathroomsCount.setText(String.valueOf(mProperty.getBathrooms_count()));
        } else {
            binding.textViewBathroomsCount.setText("-");
        }
        if (mProperty.getBedrooms_count() != -1) {
            binding.textViewBedroomsCount.setText(String.valueOf(mProperty.getBedrooms_count()));
        } else {
            binding.textViewBedroomsCount.setText("-");
        }
        binding.textViewLocation.setText(mProperty.getAddress());
    }

    private void setMediasListener() {
        mPropertyViewModel.getMediasByPropertyIdLiveData().observe(getViewLifecycleOwner(), medias -> {
            mPropertyDetailsPagerAdapter.setData(medias);
        });
    }

    private void initViewPager() {
        mPropertyDetailsPagerAdapter = new PropertyDetailsPagerAdapter(requireContext(), new ArrayList<>(), this);
        binding.viewPagerMedias.setAdapter(mPropertyDetailsPagerAdapter);
        binding.dotsIndicator.attachTo(binding.viewPagerMedias);
    }

    @Override
    public void displayMediaCallback(String uriString) {
        MediaDisplayFragment mediaDisplayFragment = new MediaDisplayFragment();
        Bundle args = new Bundle();
        args.putString("Uri", uriString);
        mediaDisplayFragment.setArguments(args);
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, mediaDisplayFragment, "MediaDisplay")
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
                if(mProperty != null) {
                    showFragmentMap();
                }
            }
        });
    }

    private void showFragmentMap() {
        PropertyDetailsMapFragment propertyDetailsMapFragment = new PropertyDetailsMapFragment();
        Bundle args = new Bundle();
        args.putLong("PropertyId", mProperty.getId());
        propertyDetailsMapFragment.setArguments(args);
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, propertyDetailsMapFragment, "PropertyDetailsMap")
                .addToBackStack("PropertyDetailsMap")
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isTablet) {
            toolbar.getMenu().removeItem(R.id.menuItem_edit);
        }
    }
}