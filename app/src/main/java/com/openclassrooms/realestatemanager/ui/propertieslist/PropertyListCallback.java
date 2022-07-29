package com.openclassrooms.realestatemanager.ui.propertieslist;

import com.openclassrooms.realestatemanager.data.model.entities.Property;

public interface PropertyListCallback {
    void propertiesListAdapterCallback(Property property);
}