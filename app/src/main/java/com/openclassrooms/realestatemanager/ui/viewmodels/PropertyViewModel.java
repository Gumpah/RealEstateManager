package com.openclassrooms.realestatemanager.ui.viewmodels;

import android.content.ContentResolver;
import android.database.Cursor;
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
    private MutableLiveData<Property> mProperty;
    private MutableLiveData<Long> mPropertyCreatedId;

    public PropertyViewModel(PropertyRepository propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
        mProperties = new MutableLiveData<>();
        mMedias = new MutableLiveData<>();
        mAllMedias = new MutableLiveData<>();
        mPlaces = new MutableLiveData<>();
        mProperty = new MutableLiveData<>();
        mPropertyCreatedId = new MutableLiveData<>();
    }

    public LiveData<List<Property>> getPropertiesLiveData() { return mProperties; }

    public LiveData<Long> getPropertyCreatedIdLiveData() { return mPropertyCreatedId; }

    public LiveData<Property> getPropertyByIdLiveData() {
        return mProperty;
    }

    public Property getPropertyInListFromId(List<Property> properties, long id){
        for (Property property : properties) {
            if (property.getId() == id) return property;
        }
        return null;
    }

    public void insertProperty(Property property) { mExecutor.execute(() -> {
        mPropertyRepository.insertProperty(property);
    });  }

    public void updateProperty(Property property) { mExecutor.execute(() -> {
        mPropertyRepository.updateProperty(property);
    });  }

    public void deleteProperty(long propertyId) { mExecutor.execute(() -> {
        mPropertyRepository.deleteProperty(propertyId);
    });  }

    public LiveData<List<Media>> getMediasByPropertyIdLiveData() { return mMedias; }

    public LiveData<List<Media>> getAllMediasLiveData() { return mAllMedias; }

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
                for (Place place : places) {
                    mPropertyRepository.insertPlace(place);
                    mPropertyRepository.insertPropertyPlace(new PropertyPlace(place.getId(), propertyId));
                }
            }
            mPropertyCreatedId.postValue(propertyId);
        });
    }

    public LiveData<List<Place>> getPlacesLiveData() {
        return mPlaces;
    }

    public void getPropertiesContentProvider(ContentResolver contentResolver) {
        mExecutor.execute(() -> {
            Cursor cursor = mPropertyRepository.getPropertiesContentProvider(contentResolver);
            ArrayList<Property> properties = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    properties.add(Property.fromCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
            mProperties.postValue(properties);
        });
    }

    public void getPropertyByIdContentProvider(ContentResolver contentResolver, long propertyId) {
        mExecutor.execute(() -> {
            mProperty.postValue(mPropertyRepository.getPropertyByIdContentProvider(contentResolver, propertyId));
        });
    }

    public void getMediasByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        mExecutor.execute(() -> {
            Cursor cursor = mPropertyRepository.getMediasByPropertyIdContentProvider(contentResolver, propertyId);
            ArrayList<Media> medias = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    medias.add(Media.fromCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
            mMedias.postValue(medias);
        });
    }

    public void getAllMediasContentProvider(ContentResolver contentResolver) {
        mExecutor.execute(() -> {
            Cursor cursor = mPropertyRepository.getAllMediasContentProvider(contentResolver);
            ArrayList<Media> medias = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    medias.add(Media.fromCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
            mAllMedias.postValue(medias);
        });
    }

    public void getPlacesByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        mExecutor.execute(() -> {
            Cursor cursor = mPropertyRepository.getPropertyPlacesByPropertyIdContentProvider(contentResolver, propertyId);
            ArrayList<Place> places = new ArrayList<>();
            if (cursor.moveToFirst()){
                do {
                    PropertyPlace propertyPlace = PropertyPlace.fromCursor(cursor);
                    Place place = getPlaceByPlaceIdContentProvider(contentResolver, propertyPlace.getPlace_id());
                    places.add(place);
                } while (cursor.moveToNext());
            }
            cursor.close();
            mPlaces.postValue(places);
        });
    }

    public Place getPlaceByPlaceIdContentProvider(ContentResolver contentResolver, String placeId) {
        Cursor cursor = mPropertyRepository.getPlaceByPlaceIdContentProvider(contentResolver, placeId);
        Place place = new Place();
        if (cursor.moveToFirst()){ place = Place.fromCursor(cursor); }
        cursor.close();
        return place;
    }
}
