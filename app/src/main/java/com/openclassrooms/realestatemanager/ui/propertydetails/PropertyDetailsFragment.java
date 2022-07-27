package com.openclassrooms.realestatemanager.ui.propertydetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.data.model.Property;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyDetailsBinding;
import com.openclassrooms.realestatemanager.ui.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.PropertyViewModelFactory;

import java.util.ArrayList;

public class PropertyDetailsFragment extends Fragment {

    private FragmentPropertyDetailsBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private Property mProperty;
    private RecyclerView mRecyclerView;
    private PropertyDetailsMediasAdapter mPropertyDetailsMediasAdapter;

    public PropertyDetailsFragment(Property property) {
        mProperty = property;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertyDetailsBinding.inflate(inflater, container, false);
        configureViewModel();
        initUI();
        initRecyclerView();
        initMedias();
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
        mPropertyViewModel.getMediasByPropertyId().observe(getViewLifecycleOwner(), medias -> {
            mPropertyDetailsMediasAdapter.setData(medias);
        });
        mPropertyViewModel.getMediasByPropertyId(mProperty.getId());
    }

    private void initRecyclerView() {
        mRecyclerView = binding.recyclerViewMedias;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.HORIZONTAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mPropertyDetailsMediasAdapter = new PropertyDetailsMediasAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mPropertyDetailsMediasAdapter);
    }
}