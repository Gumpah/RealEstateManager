package com.openclassrooms.realestatemanager.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private Executor mExecutor;
    private LiveData<List<Property>> mProperties;

    public PropertyViewModel(PropertyRepository propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
        mProperties = new MutableLiveData<>();
    }

    public LiveData<List<Property>> getPropertiesLiveData() { return mProperties; }

    public void getProperties() { mExecutor.execute(() -> {
        mProperties = mPropertyRepository.getProperties();
    });  }

    public LiveData<Property> getPropertyById(long propertyId) { return mPropertyRepository.getPropertyById(propertyId); }

    public void insertProperty(Property property) { mExecutor.execute(() -> {
        mPropertyRepository.insertProperty(property);
    });  }

    public void updateProperty(Property property) { mExecutor.execute(() -> {
        mPropertyRepository.updateProperty(property);
    });  }

    public void deleteProperty(long propertyId) { mExecutor.execute(() -> {
        mPropertyRepository.deleteProperty(propertyId);
    });  }
}
