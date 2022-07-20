package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;

public class PropertyDetailsFragment extends Fragment {

    public PropertyDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_property_details, container, false);
    }

    /*
    public static PropertyDetailsFragment newInstance(String param1, String param2) {
        PropertyDetailsFragment fragment = new PropertyDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
     */
}