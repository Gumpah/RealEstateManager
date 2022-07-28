package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.Media;
import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private Executor mExecutor;
    private MutableLiveData<List<Property>> mProperties;
    private MutableLiveData<List<Media>> mMedias;

    public PropertyViewModel(PropertyRepository propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
        mProperties = new MutableLiveData<>();
        mMedias = new MutableLiveData<>();
    }

    public LiveData<List<Property>> getPropertiesLiveData() { return mProperties; }

    public void getProperties() { mExecutor.execute(() -> {
        mProperties.postValue(mPropertyRepository.getProperties());
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

    public void insertPropertyAndMedias(Property property, ArrayList<Media> medias) {
        mExecutor.execute(() -> {
            long propertyId = mPropertyRepository.insertProperty(property);
            if (medias != null && !medias.isEmpty()) {
                ArrayList<Media> mediaList = new ArrayList<>();
                for (Media media : medias) {
                    media.setPropertyId(propertyId);
                    mediaList.add(media);
                }
                mPropertyRepository.insertMultipleMedias(mediaList);
            }
        });
    }

    public LiveData<List<Media>> getMediasByPropertyId() { return mMedias; }

    public void getMediasByPropertyId(long propertyId) {
        mExecutor.execute(() -> {
            mMedias.postValue(mPropertyRepository.getMediasByPropertyId(propertyId));
        });
    }
}
