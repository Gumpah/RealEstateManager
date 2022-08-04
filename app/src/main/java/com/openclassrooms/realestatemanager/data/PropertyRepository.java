package com.openclassrooms.realestatemanager.data;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PlaceDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyPlaceDao;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.List;

public class PropertyRepository {

    private PropertyDao mPropertyDao;
    private MediaDao mMediaDao;
    private PlaceDao mPlaceDao;
    private PropertyPlaceDao mPropertyPlaceDao;

    public PropertyRepository(PropertyDao propertyDao, MediaDao mediaDao, PlaceDao placeDao, PropertyPlaceDao propertyPlaceDao) {
        mPropertyDao = propertyDao;
        mMediaDao = mediaDao;
        mPlaceDao = placeDao;
        mPropertyPlaceDao = propertyPlaceDao;
    }

    public List<Property> getProperties() { return mPropertyDao.getProperties(); }

    public LiveData<Property> getPropertyById(long propertyId) { return mPropertyDao.getPropertyById(propertyId); }

    public long insertProperty(Property property) { return mPropertyDao.insertProperty(property); }

    public void updateProperty(Property property) { mPropertyDao.updateProperty(property); }

    public void deleteProperty(long propertyId) { mPropertyDao.deleteProperty(propertyId);}

    public void insertMedia(Media media) { mMediaDao.insertMedia(media); }

    public void insertMultipleMedias(List<Media> medias) { mMediaDao.insertMultipleMedias(medias); }

    public List<Media> getMediasByPropertyId(long propertyId) {
        return mMediaDao.getMediasByPropertyId(propertyId);
    }

    public List<Media> getAllMedias() { return mMediaDao.getMedias(); }

    public void insertPlace(Place place) { mPlaceDao.insertPlace(place); }

    public void insertMultiplePlaces(List<Place> places) { mPlaceDao.insertMultiplePlaces(places); }

    public void insertPropertyPlace(PropertyPlace propertyPlace) { mPropertyPlaceDao.insertPropertyPlace(propertyPlace); }

    public void insertMultiplePropertyPlaces(List<PropertyPlace> propertyPlaces) {
        mPropertyPlaceDao.insertMultiplePropertyPlaces(propertyPlaces);
    }

    public List<PropertyPlace> getPropertyPlacesByPropertyId(long propertyId) {
        return mPropertyPlaceDao.getPropertyPlacesByPropertyId(propertyId);
    }

    public Place getPlaceByPlaceId(String placeId) {
        return mPlaceDao.getPlaceById(placeId);
    }
}
