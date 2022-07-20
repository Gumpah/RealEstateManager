package com.openclassrooms.realestatemanager.data;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.List;

public class PropertyRepository {

    private PropertyDao mPropertyDao;

    public PropertyRepository(PropertyDao propertyDao) {
        mPropertyDao = propertyDao;
    }

    public LiveData<List<Property>> getProperties() { return mPropertyDao.getProperties(); }

    public LiveData<Property> getPropertyById(long propertyId) { return mPropertyDao.getPropertyById(propertyId); }

    public void insertProperty(Property property) { mPropertyDao.insertProperty(property); }

    public void updateProperty(Property property) { mPropertyDao.updateProperty(property); }

    public void deleteProperty(long propertyId) { mPropertyDao.deleteProperty(propertyId);}
}
