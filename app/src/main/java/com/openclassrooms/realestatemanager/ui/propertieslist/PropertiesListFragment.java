package com.openclassrooms.realestatemanager.ui.propertieslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.ui.addproperty.AddPropertyFragment;
import com.openclassrooms.realestatemanager.ui.loansimulator.LoanSimulatorFragment;
import com.openclassrooms.realestatemanager.ui.modifyproperty.ModifyPropertyFragment;
import com.openclassrooms.realestatemanager.ui.propertiesmap.PropertiesMapFragment;
import com.openclassrooms.realestatemanager.ui.propertiesmap.UserViewModel;
import com.openclassrooms.realestatemanager.ui.propertiesmap.UserViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.propertysearch.PropertySearchFragment;
import com.openclassrooms.realestatemanager.ui.propertysearch.PropertySearchViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.PropertySearchViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class PropertiesListFragment extends Fragment implements PropertyListCallback {

    private FragmentPropertiesListBinding binding;
    private RecyclerView mRecyclerView;
    private PropertyViewModel mPropertyViewModel;
    private PropertySearchViewModel mPropertySearchViewModel;
    private PropertiesListAdapter mListPropertiesAdapter;
    private List<Property> propertySearchResults;
    private List<Property> propertyList;
    private Long propertyId;
    private Menu mMenu;

    public PropertiesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertiesListBinding.inflate(inflater, container, false);
        initRecyclerView();
        configureViewModels();
        fetchProperties();
        initMediasLiveData();
        initSearchResults();
        setButtonLeaveSearchListener();
        setFabClickListener();
        mPropertyViewModel.getAllMediasContentProvider(requireContext().getContentResolver());
        initMenu();
        initDrawer();
        return binding.getRoot();
    }

    private void initDrawer() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarPropertyList);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbarToolbarPropertyList, R.string.nav_open, R.string.nav_close);

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initRecyclerView() {
        mRecyclerView = binding.recyclerViewPropertiesList;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mListPropertiesAdapter = new PropertiesListAdapter(new ArrayList<>(), this);
        mRecyclerView.setAdapter(mListPropertiesAdapter);
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mPropertySearchViewModel = new ViewModelProvider(requireActivity(), PropertySearchViewModelFactory.getInstance()).get(PropertySearchViewModel.class);
    }

    private void fetchProperties() {
        mPropertyViewModel.getPropertiesLiveData().observe(getViewLifecycleOwner(), list -> {
            propertyList = list;
        });
        mPropertyViewModel.getPropertiesContentProvider(requireContext().getContentResolver());
    }

    private void initSearchResults() {
        mPropertySearchViewModel.getPropertySearchResultsLiveData().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                propertySearchResults = list;
                mPropertyViewModel.getAllMediasContentProvider(requireContext().getContentResolver());
                mPropertySearchViewModel.getPropertySearchResultsLiveData().postValue(null);
            }
        });
    }

    private void setButtonLeaveSearchListener() {
        binding.buttonLeaveSearchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                propertySearchResults = null;
                mPropertyViewModel.getAllMediasContentProvider(requireContext().getContentResolver());
                binding.buttonLeaveSearchMode.setVisibility(View.GONE);
            }
        });
    }

    private void initMediasLiveData() {
        mPropertyViewModel.getAllMediasLiveData().observe(getViewLifecycleOwner(), medias -> {
            int visibility;
            if (propertySearchResults != null) {
                binding.buttonLeaveSearchMode.setVisibility(View.VISIBLE);
                if (propertySearchResults.isEmpty()) {
                    visibility = View.VISIBLE;
                } else {
                    visibility = View.INVISIBLE;
                }
                mListPropertiesAdapter.setData(mPropertyViewModel.assemblePropertyAndMedia(propertySearchResults, medias));
            } else {
                binding.buttonLeaveSearchMode.setVisibility(View.GONE);
                if (propertyList == null || propertyList.isEmpty()) {
                    visibility = View.VISIBLE;
                } else {
                    visibility = View.INVISIBLE;
                }
                mListPropertiesAdapter.setData(mPropertyViewModel.assemblePropertyAndMedia(propertyList, medias));
            }
            binding.textViewEmptyList.setVisibility(visibility);

        });
    }


    private void setFabClickListener() {
        binding.fabAddProperty.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new AddPropertyFragment(), "AddProperty")
                    .addToBackStack("AddProperty")
                    .commit();
        });
    }

    private void initMenu() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_propertylist, menu);
                mMenu = menu;
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menuItem_search) {
                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frameLayout_fragmentContainer, new PropertySearchFragment(), "PropertySearch")
                            .addToBackStack("PropertySearch")
                            .commit();
                }
                if (menuItem.getItemId() == R.id.menuItem_edit && propertyId != null) {
                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frameLayout_fragmentContainer, ModifyPropertyFragment.newInstance(propertyId), "ModifyProperty")
                            .addToBackStack("ModifyProperty")
                            .commit();
                }
                return true;
            }
        }, getViewLifecycleOwner());
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuItem_globalMap) {
                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frameLayout_fragmentContainer, new PropertiesMapFragment(), "PropertiesMap")
                            .addToBackStack("PropertiesMap")
                            .commit();
                }
                if (item.getItemId() == R.id.menuItem_loanSimulator) {
                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frameLayout_fragmentContainer, new LoanSimulatorFragment(), "LoanSimulator")
                            .addToBackStack("LoanSimulator")
                            .commit();
                }
                return true;
            }
        });
    }

    @Override
    public void propertiesListAdapterCallback(Property property) {
        PropertyDetailsFragment propertyDetailsFragment = new PropertyDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("PropertyId", property.getId());
        propertyDetailsFragment.setArguments(args);
        boolean isTablet = requireContext().getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            propertyId = property.getId();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_tablet, propertyDetailsFragment, "PropertyDetails")
                    .commit();
        } else {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout_fragmentContainer, propertyDetailsFragment, "PropertyDetails")
                    .addToBackStack("PropertyDetails")
                    .commit();
        }
    }
}