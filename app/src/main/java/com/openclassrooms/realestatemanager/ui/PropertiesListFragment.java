package com.openclassrooms.realestatemanager.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;

public class PropertiesListFragment extends Fragment {

    private FragmentPropertiesListBinding binding;
    private RecyclerView mRecyclerView;

    public PropertiesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertiesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void initRecyclerView() {
        mRecyclerView = binding.recyclerViewPropertiesList;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        PropertiesListAdapter mListPropertiesAdapter = new PropertiesListAdapter();
        mRecyclerView.setAdapter(mListPropertiesAdapter);
    }

    /*
    public static PropertiesListFragment newInstance(String param1, String param2) {
        PropertiesListFragment fragment = new PropertiesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
     */
}