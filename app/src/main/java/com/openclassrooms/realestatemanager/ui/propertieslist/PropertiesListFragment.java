package com.openclassrooms.realestatemanager.ui.propertieslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.ui.addproperty.AddPropertyFragment;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class PropertiesListFragment extends Fragment implements PropertyListCallback {

    private FragmentPropertiesListBinding binding;
    private RecyclerView mRecyclerView;
    private PropertyViewModel mPropertyViewModel;
    private PropertiesListAdapter mListPropertiesAdapter;
    private PropertyListCallback mCallback;

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
        configureViewModel();
        fetchProperties();
        setClickListener();
        return binding.getRoot();
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

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
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

    private void setClickListener() {
        binding.fabAddProperty.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new AddPropertyFragment(), "AddProperty")
                    .addToBackStack("AddProperty")
                    .commit();
        });
    }

    @Override
    public void propertiesListAdapterCallback(Property property) {
        requireActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, new PropertyDetailsFragment(property), "PropertyDetails")
                .addToBackStack("PropertyDetails")
                .commit();
    }
}