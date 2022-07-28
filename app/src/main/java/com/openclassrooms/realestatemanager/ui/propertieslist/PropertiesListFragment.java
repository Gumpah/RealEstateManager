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
import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.ui.addproperty.AddPropertyFragment;
import com.openclassrooms.realestatemanager.ui.callbacks.ClickCallback;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

import java.util.ArrayList;

public class PropertiesListFragment extends Fragment implements ClickCallback {

    private FragmentPropertiesListBinding binding;
    private RecyclerView mRecyclerView;
    private PropertyViewModel mPropertyViewModel;
    private PropertiesListAdapter mListPropertiesAdapter;
    private ClickCallback mCallback;

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
        setData();
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

    private void setData() {
        mPropertyViewModel.getPropertiesLiveData().observe(getViewLifecycleOwner(), list -> {
            mListPropertiesAdapter.setData(list);
        });
        mPropertyViewModel.getProperties();
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