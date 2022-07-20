package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;

public class AddPropertyFragment extends Fragment {

    public AddPropertyFragment() {
        // Required empty public constructor
    }

    public static AddPropertyFragment newInstance(String param1, String param2) {
        AddPropertyFragment fragment = new AddPropertyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_property, container, false);
    }
}