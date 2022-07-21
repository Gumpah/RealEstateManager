package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;

import java.util.ArrayList;
import java.util.List;

public class PropertiesListFragment extends Fragment {

    private FragmentPropertiesListBinding binding;
    private RecyclerView mRecyclerView;
    private PropertyViewModel mPropertyViewModel;
    private PropertiesListAdapter mListPropertiesAdapter;
    private ClickCallback mCallback;

    public PropertiesListFragment(ClickCallback callback) {
        mCallback = callback;
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
        mListPropertiesAdapter = new PropertiesListAdapter(new ArrayList<>());
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
        binding.fabAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.myClickCallback("propertiesList");
            }
        });
    }
}