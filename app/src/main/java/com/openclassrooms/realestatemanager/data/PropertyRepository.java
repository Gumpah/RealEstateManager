package com.openclassrooms.realestatemanager.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PlaceDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyPlaceDao;
import com.openclassrooms.realestatemanager.data.daos.SearchDao;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;
import com.openclassrooms.realestatemanager.data.provider.MediaContentProvider;
import com.openclassrooms.realestatemanager.data.provider.PlaceContentProvider;
import com.openclassrooms.realestatemanager.data.provider.PropertyContentProvider;
import com.openclassrooms.realestatemanager.data.provider.PropertyPlaceContentProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyRepository {

    private PropertyDao mPropertyDao;
    private MediaDao mMediaDao;
    private PlaceDao mPlaceDao;
    private PropertyPlaceDao mPropertyPlaceDao;
    private SearchDao mSearchDao;

    public PropertyRepository(PropertyDao propertyDao, MediaDao mediaDao, PlaceDao placeDao, PropertyPlaceDao propertyPlaceDao, SearchDao searchDao) {
        mPropertyDao = propertyDao;
        mMediaDao = mediaDao;
        mPlaceDao = placeDao;
        mPropertyPlaceDao = propertyPlaceDao;
        mSearchDao = searchDao;
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

    //ContentProvider
    public Cursor getPropertiesContentProvider(ContentResolver contentResolver) {
        String[] arguments = {"getProperties"};
        return contentResolver.query(PropertyContentProvider.URI_PROPERTY, null, null, arguments, null);
    }

    public Cursor getPropertyByIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getPropertyById"};
        return contentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI_PROPERTY, propertyId), null, null, arguments, null);
    }

    public Cursor getMediasByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getMediasByPropertyId"};
        return contentResolver.query(ContentUris.withAppendedId(MediaContentProvider.URI_MEDIA, propertyId), null, null, arguments, null);
    }

    public Cursor getAllMediasContentProvider(ContentResolver contentResolver) {
        String[] arguments = {"getAllMedias"};
        return contentResolver.query(MediaContentProvider.URI_MEDIA, null, null, arguments, null);
    }

    public Cursor getPropertyPlacesByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getPropertyPlacesByPropertyId"};
        return contentResolver.query(ContentUris.withAppendedId(PropertyPlaceContentProvider.URI_PROPERTY_PLACE, propertyId), null, null, arguments, null);
    }

    public Cursor getPlaceByPlaceIdContentProvider(ContentResolver contentResolver, String placeId) {
        String[] arguments = {"getPlaceById", placeId};
        return contentResolver.query(PlaceContentProvider.URI_PLACE, null, null, arguments, null);
    }

    public List<Property> getPropertiesByPropertyType(String propertyType) {
        return mSearchDao.getPropertiesByPropertyType(propertyType);
    }

    public List<Property> getPropertiesByPriceRange(Integer priceMin, Integer priceMax) {
        return mSearchDao.getPropertiesByPriceRange(priceMin, priceMax);
    }

    public List<Property> getPropertiesBySurfaceRange(int surfaceMin, int surfaceMax) {
        return mSearchDao.getPropertiesBySurfaceRange(surfaceMin, surfaceMax);
    }

    public List<Property> getPropertiesByRoomsRange(int roomsMin, int roomsMax) {
        return mSearchDao.getPropertiesByRoomsRange(roomsMin, roomsMax);
    }

    public List<Property> getPropertiesInRadius(Double lat1, Double lng1, Double lat2, Double lng2) {
        return mSearchDao.getPropertiesInRadius(lat1, lng1, lat2, lng2);
    }

}
