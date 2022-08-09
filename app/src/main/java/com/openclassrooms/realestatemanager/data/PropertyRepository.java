package com.openclassrooms.realestatemanager.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PlaceDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyPlaceDao;
import com.openclassrooms.realestatemanager.data.daos.SearchDao;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;
import com.openclassrooms.realestatemanager.data.provider.MyContentProvider;

import java.util.ArrayList;
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

    public Property getPropertyById(long propertyId) { return mPropertyDao.getPropertyById(propertyId); }

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
        return contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null);
    }

    public Cursor getPropertyByIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getPropertyById"};
        return contentResolver.query(ContentUris.withAppendedId(MyContentProvider.URI_PROPERTY, propertyId), null, null, arguments, null);
    }

    public Cursor getMediasByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getMediasByPropertyId"};
        return contentResolver.query(ContentUris.withAppendedId(MyContentProvider.URI_MEDIA, propertyId), null, null, arguments, null);
    }

    public Cursor getAllMediasContentProvider(ContentResolver contentResolver) {
        String[] arguments = {"getAllMedias"};
        return contentResolver.query(MyContentProvider.URI_MEDIA, null, null, arguments, null);
    }

    public Cursor getPropertyPlacesByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getPropertyPlacesByPropertyId"};
        return contentResolver.query(ContentUris.withAppendedId(MyContentProvider.URI_PROPERTY_PLACE, propertyId), null, null, arguments, null);
    }

    public Cursor getPlaceByPlaceIdContentProvider(ContentResolver contentResolver, String placeId) {
        String[] arguments = {"getPlaceById", placeId};
        return contentResolver.query(MyContentProvider.URI_PLACE, null, null, arguments, null);
    }

    public List<Property> getPropertiesByPropertyType(String propertyType) {
        return mSearchDao.getPropertiesByPropertyType(propertyType);
    }

    public List<Property> getPropertiesByPriceRange(Integer priceMin, Integer priceMax) {
        return mSearchDao.getPropertiesByPriceRange(priceMin, priceMax);
    }

    public List<Property> getPropertiesByPriceMin(Integer priceMin) {
        return mSearchDao.getPropertiesByPriceMin(priceMin);
    }

    public List<Property> getPropertiesBySurfaceRange(int surfaceMin, int surfaceMax) {
        return mSearchDao.getPropertiesBySurfaceRange(surfaceMin, surfaceMax);
    }

    public List<Property> getPropertiesBySurfaceMin(int surfaceMin) {
        return mSearchDao.getPropertiesBySurfaceMin(surfaceMin);
    }

    public List<Property> getPropertiesByRoomsRange(int roomsMin, int roomsMax) {
        return mSearchDao.getPropertiesByRoomsRange(roomsMin, roomsMax);
    }

    public List<Property> getPropertiesByRoomsMin(int roomsMin) {
        return mSearchDao.getPropertiesByRoomsMin(roomsMin);
    }

    public List<Property> getPropertiesByBathroomsRange(int bathroomsMin, int bathroomsMax) {
        return mSearchDao.getPropertiesByBathroomsRange(bathroomsMin, bathroomsMax);
    }

    public List<Property> getPropertiesByBathroomsMin(int bathroomsMin) {
        return mSearchDao.getPropertiesByBathroomsMin(bathroomsMin);
    }

    public List<Property> getPropertiesByBedroomsRange(int bedroomsMin, int bedroomsMax) {
        return mSearchDao.getPropertiesByBedroomsRange(bedroomsMin, bedroomsMax);
    }

    public List<Property> getPropertiesByBedroomsMin(int bedroomsMin) {
        return mSearchDao.getPropertiesByBedroomsMin(bedroomsMin);
    }

    public List<Property> getPropertiesInRadius(Double lat1, Double lng1, Double lat2, Double lng2) {
        return mSearchDao.getPropertiesInRadius(lat1, lng1, lat2, lng2);
    }

    public ArrayList<Long> getPropertiesIdsForAPlaceType(String placeType) {
        ArrayList<Long> propertyIds = new ArrayList<>();
        for (Place place : mSearchDao.getPlacesByType(placeType)) {
            List<PropertyPlace> propertyPlaces = mSearchDao.getPropertyPlacesByPlaceId(place.getId());
            for (PropertyPlace propertyPlace : propertyPlaces) {
                Long propertyId = propertyPlace.getProperty_id();
                if (!propertyIds.contains(propertyId)) propertyIds.add(propertyId);
            }
        }
        return propertyIds;
    }

    public List<Property> getPropertiesByMarketEntryDateRange(long marketEntryMin, long marketEntryMax) {
        return mSearchDao.getPropertiesByMarketEntryDateRange(marketEntryMin, marketEntryMax);
    }

    public List<Property> getPropertiesByMarketEntryDateMin(long marketEntryMin) {
        return mSearchDao.getPropertiesByMarketEntryDateMin(marketEntryMin);
    }

    public List<Property> getPropertiesBySoldDateRange(long soldMin, long soldMax) {
        return mSearchDao.getPropertiesBySoldDateRange(soldMin, soldMax);
    }

    public List<Property> getPropertiesBySoldDateMin(long soldMin) {
        return mSearchDao.getPropertiesBySoldDateMin(soldMin);
    }
}
