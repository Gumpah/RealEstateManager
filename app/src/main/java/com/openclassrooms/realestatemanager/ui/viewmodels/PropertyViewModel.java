package com.openclassrooms.realestatemanager.ui.viewmodels;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.PropertyAndImage;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.ArrayList;
import java.util.Date;
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

    public LiveData<List<Media>> getMediasByPropertyIdLiveData() { return mMedias; }

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

    public List<Property> getPropertiesByPropertyType(String propertyType) {
        return mPropertyRepository.getPropertiesByPropertyType(propertyType);
    }

    public List<Property> getPropertiesByPriceRange(Integer priceMin, Integer priceMax) {
        return mPropertyRepository.getPropertiesByPriceRange(priceMin, priceMax);
    }

    public List<Property> getPropertiesBySurfaceRange(int surfaceMin, int surfaceMax) {
        return mPropertyRepository.getPropertiesBySurfaceRange(surfaceMin, surfaceMax);
    }

    public List<Property> getPropertiesByRoomsRange(int roomsMin, int roomsMax) {
        return mPropertyRepository.getPropertiesByRoomsRange(roomsMin, roomsMax);
    }

    public List<Property> getPropertiesInRadius(Double lat1, Double lng1, Double lat2, Double lng2) {
        return mPropertyRepository.getPropertiesInRadius(lat1, lng1, lat2, lng2);
    }

    public void searchProperty(String propertyType, Integer priceMin, Integer priceMax, Integer surfaceMin, Integer surfaceMax, Integer roomsMin, Integer roomsMax, LatLngBounds bounds, Date marketEntryDateMin, Date marketEntryDateMax, Date soldDateMin, Date soldDateMax) {
        mExecutor.execute(() -> {
            if (propertyType != null) {
                ArrayList<Long> propertiesIds = new ArrayList<>();
                for (Property property : getPropertiesByPropertyType(propertyType)) {
                    propertiesIds.add(property.getId());
                }
            }
            if (priceMin != null || priceMax != null) {
                ArrayList<Long> propertiesIds = new ArrayList<>();
                for (Property property : getPropertiesByPriceRange(priceMin, priceMax)) {
                    propertiesIds.add(property.getId());
                }
            }
            if (surfaceMin != null || surfaceMax != null) {
                ArrayList<Long> propertiesIds = new ArrayList<>();
                for (Property property : getPropertiesBySurfaceRange(surfaceMin, surfaceMax)) {
                    propertiesIds.add(property.getId());
                }
            }
            if (roomsMin != null || roomsMax != null) {
                ArrayList<Long> propertiesIds = new ArrayList<>();
                for (Property property : getPropertiesByRoomsRange(roomsMin, roomsMax)) {
                    propertiesIds.add(property.getId());
                }
            }
            if (bounds != null) {
                ArrayList<Long> propertiesIds = new ArrayList<>();
                for (Property property : getPropertiesInRadius(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude)) {
                    System.out.println("SearchTest > " + property.getAddress());
                    propertiesIds.add(property.getId());
                }
            }
            if (marketEntryDateMin != null || marketEntryDateMax != null) {

            }
            if (soldDateMin != null || soldDateMax != null) {

            }
        });
        //creating list of property ids for each request
        //check common elements in all lists
        //create property list from id list
    }
}
