package com.openclassrooms.realestatemanager.ui.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.PropertyAndImage;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private Executor mExecutor;
    private MutableLiveData<List<Property>> mProperties;
    private MutableLiveData<List<Media>> mAllMedias;
    private MutableLiveData<List<Media>> mMedias;
    private MutableLiveData<List<Place>> mPlaces;

    public PropertyViewModel(PropertyRepository propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
        mProperties = new MutableLiveData<>();
        mMedias = new MutableLiveData<>();
        mAllMedias = new MutableLiveData<>();
        mPlaces = new MutableLiveData<>();
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

    public LiveData<List<Media>> getAllMediasLiveData() { return mAllMedias; }

    public void getMediasByPropertyId(long propertyId) {
        mExecutor.execute(() -> {
            mMedias.postValue(mPropertyRepository.getMediasByPropertyId(propertyId));
        });
    }

    public void getAllMedias() {
        mExecutor.execute(() -> {
            mAllMedias.postValue(mPropertyRepository.getAllMedias());
        });
    }

    public List<PropertyAndImage> assemblePropertyAndMedia(List<Property> properties, List<Media> medias) {
        List<PropertyAndImage> propertyAndImageList = new ArrayList<>();
        if (properties != null) {
            for (Property property : properties) {
                if (medias == null) {
                    propertyAndImageList.add(new PropertyAndImage(property, null));
                } else {
                    propertyAndImageList.add(new PropertyAndImage(property, getFirstImageOfProperty(property.getId(), medias)));
                }
            }
        }
        return propertyAndImageList;
    }

    public Uri getFirstImageOfProperty(long propertyId, List<Media> medias) {
        for (Media media : medias) {
            if (media.getPropertyId() == propertyId) return Uri.parse(media.getMedia_uri());
        }
        return null;
    }

    public void insertPropertyAndMediasAndPlaces(Property property, ArrayList<Media> medias, ArrayList<Place> places) {
        if (property == null) return;
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
            if (places != null && !places.isEmpty()) {
                System.out.println("Test1");
                for (Place place : places) {
                    mPropertyRepository.insertPlace(place);
                    mPropertyRepository.insertPropertyPlace(new PropertyPlace(place.getId(), propertyId));
                }
            }
        });
    }

    public LiveData<List<Place>> getPlacesLiveData() {
        return mPlaces;
    }

    public void getPlacesByPropertyId(long propertyId) {
        mExecutor.execute(() -> {
            ArrayList<Place> places = new ArrayList<>();
            List<PropertyPlace> propertyPlaces = mPropertyRepository.getPropertyPlacesByPropertyId(propertyId);
            for (PropertyPlace propertyPlace : propertyPlaces) {
                Place place = mPropertyRepository.getPlaceByPlaceId(propertyPlace.getPlace_id());
                places.add(place);
            }
            mPlaces.postValue(places);
        });
    }
}
