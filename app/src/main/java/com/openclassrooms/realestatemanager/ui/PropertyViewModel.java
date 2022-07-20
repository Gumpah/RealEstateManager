package com.openclassrooms.realestatemanager.ui;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.PropertyRepository;

public class PropertyViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;

    public PropertyViewModel(PropertyRepository propertyRepository) {
        mPropertyRepository = propertyRepository;
    }
}
