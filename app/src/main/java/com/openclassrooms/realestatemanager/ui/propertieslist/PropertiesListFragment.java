package com.openclassrooms.realestatemanager.ui.propertieslist;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.ui.addproperty.AddPropertyFragment;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PropertiesListFragment extends Fragment implements PropertyListCallback {

    private FragmentPropertiesListBinding binding;
    private RecyclerView mRecyclerView;
    private PropertyViewModel mPropertyViewModel;
    private PropertiesListAdapter mListPropertiesAdapter;
    private PropertyListCallback mCallback;
    private UserViewModel mUserViewModel;

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
        initNetworkStatus();
        fetchProperties();
        return binding.getRoot();
    }


    private void initNetworkStatus() {
        mUserViewModel.getConnectionStatus().observe(getViewLifecycleOwner(), this::onNetworkStatusChange);
    }

    private void onNetworkStatusChange(boolean isConnected) {
        if (isConnected) {
            setClickListener(true);
            binding.textViewInternetNoConnection.setVisibility(View.GONE);
            binding.fabAddProperty.setClickable(true);
            binding.fabAddProperty.setEnabled(true);
            binding.fabAddProperty.setFocusable(true);
        } else {
            setClickListener(false);
            binding.textViewInternetNoConnection.setVisibility(View.VISIBLE);
            binding.fabAddProperty.setClickable(false);
            binding.fabAddProperty.setEnabled(false);
            binding.fabAddProperty.setFocusable(false);
        }
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
        mUserViewModel = new ViewModelProvider(requireActivity(), UserViewModelFactory.getInstance(requireContext())).get(UserViewModel.class);
    }

    private void fetchProperties() {
        mPropertyViewModel.getPropertiesLiveData().observe(getViewLifecycleOwner(), this::fetchMedias);
        mPropertyViewModel.getProperties();
    }

    private void fetchMedias(List<Property> properties) {
        mPropertyViewModel.getAllMediasLiveData().observe(getViewLifecycleOwner(), medias -> {
            int visibility;
            if (properties == null || properties.isEmpty()) {
                visibility = View.VISIBLE;
            } else {
                visibility = View.INVISIBLE;
            }
            binding.textViewEmptyList.setVisibility(visibility);
            mListPropertiesAdapter.setData(mPropertyViewModel.assemblePropertyAndMedia(properties, medias));
        });
        mPropertyViewModel.getAllMedias();
    }

    private void setClickListener(boolean enabled) {
        if (enabled) {
            binding.fabAddProperty.setOnClickListener(v -> {
                if (Utils.isInternetAvailable(requireContext())) {
                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frameLayout_fragmentContainer, new AddPropertyFragment(), "AddProperty")
                            .addToBackStack("AddProperty")
                            .commit();
                } else {
                    Snackbar.make(requireView(), "No connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.fabAddProperty.setOnClickListener(null);
        }
    }

    @Override
    public void propertiesListAdapterCallback(Property property) {
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, new PropertyDetailsFragment(property), "PropertyDetails")
                .addToBackStack("PropertyDetails")
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        onNetworkStatusChange(Utils.isInternetAvailable(requireContext()));
    }
}