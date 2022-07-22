package com.openclassrooms.realestatemanager.ui;

import com.openclassrooms.realestatemanager.data.model.Property;

public interface ClickCallback {
    void propertiesListAdapterCallback(Property property);
}