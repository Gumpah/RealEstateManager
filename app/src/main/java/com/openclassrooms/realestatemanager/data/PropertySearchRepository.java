package com.openclassrooms.realestatemanager.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;

import com.openclassrooms.realestatemanager.data.daos.SearchDao;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;
import com.openclassrooms.realestatemanager.data.provider.MyContentProvider;

import java.util.ArrayList;
import java.util.List;

public class PropertySearchRepository {

    public PropertySearchRepository() {
    }

    public Property getPropertyByIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getPropertyById"};
        return cursorToProperty(contentResolver.query(ContentUris.withAppendedId(MyContentProvider.URI_PROPERTY, propertyId), null, null, arguments, null));
    }

    public List<Property> getPropertiesByPropertyTypeContentProvider(ContentResolver contentResolver, String propertyType) {
        String[] arguments = {"getPropertiesByPropertyType", propertyType};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByPriceRangeContentProvider(ContentResolver contentResolver, Integer priceMin, Integer priceMax) {
        String[] arguments = {"getPropertiesByPriceRange", String.valueOf(priceMin), String.valueOf(priceMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByPriceMinContentProvider(ContentResolver contentResolver, Integer priceMin) {
        String[] arguments = {"getPropertiesByPriceMin", String.valueOf(priceMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesBySurfaceRangeContentProvider(ContentResolver contentResolver, int surfaceMin, int surfaceMax) {
        String[] arguments = {"getPropertiesBySurfaceRange", String.valueOf(surfaceMin), String.valueOf(surfaceMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesBySurfaceMinContentProvider(ContentResolver contentResolver, int surfaceMin) {
        String[] arguments = {"getPropertiesBySurfaceMin", String.valueOf(surfaceMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByRoomsRangeContentProvider(ContentResolver contentResolver, int roomsMin, int roomsMax) {
        String[] arguments = {"getPropertiesByRoomsRange", String.valueOf(roomsMin), String.valueOf(roomsMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByRoomsMinContentProvider(ContentResolver contentResolver, int roomsMin) {
        String[] arguments = {"getPropertiesByRoomsMin", String.valueOf(roomsMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByBathroomsRangeContentProvider(ContentResolver contentResolver, int bathroomsMin, int bathroomsMax) {
        String[] arguments = {"getPropertiesByBathroomsRange", String.valueOf(bathroomsMin), String.valueOf(bathroomsMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByBathroomsMinContentProvider(ContentResolver contentResolver, int bathroomsMin) {
        String[] arguments = {"getPropertiesByBathroomsMin", String.valueOf(bathroomsMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByBedroomsRangeContentProvider(ContentResolver contentResolver, int bedroomsMin, int bedroomsMax) {
        String[] arguments = {"getPropertiesByBedroomsRange", String.valueOf(bedroomsMin), String.valueOf(bedroomsMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByBedroomsMinContentProvider(ContentResolver contentResolver, int bedroomsMin) {
        String[] arguments = {"getPropertiesByBedroomsMin", String.valueOf(bedroomsMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesInRadiusContentProvider(ContentResolver contentResolver, Double lat1, Double lng1, Double lat2, Double lng2) {
        String[] arguments = {"getPropertiesInRadius", String.valueOf(lat1), String.valueOf(lng1), String.valueOf(lat2), String.valueOf(lng2)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public ArrayList<Long> getPropertiesIdsForAPlaceTypeContentProvider(ContentResolver contentResolver, String placeType) {
        ArrayList<Long> propertyIds = new ArrayList<>();
        String[] arguments = {"getPlacesByType", placeType};
        for (Place place : cursorToPlaceList(contentResolver.query(MyContentProvider.URI_PLACE, null, null, arguments, null))) {
            String[] arguments2 = {"getPropertyPlacesByPlaceId", place.getId()};
            List<PropertyPlace> propertyPlaces = cursorToPropertyPlaceList(contentResolver.query(MyContentProvider.URI_PROPERTY_PLACE, null, null, arguments2, null));
            for (PropertyPlace propertyPlace : propertyPlaces) {
                Long propertyId = propertyPlace.getProperty_id();
                if (!propertyIds.contains(propertyId)) propertyIds.add(propertyId);
            }
        }
        return propertyIds;
    }

    public List<Property> getPropertiesByMarketEntryDateRangeContentProvider(ContentResolver contentResolver, long marketEntryMin, long marketEntryMax) {
        String[] arguments = {"getPropertiesByMarketEntryDateRange", String.valueOf(marketEntryMin), String.valueOf(marketEntryMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesByMarketEntryDateMinContentProvider(ContentResolver contentResolver, long marketEntryMin) {
        String[] arguments = {"getPropertiesByMarketEntryDateMin", String.valueOf(marketEntryMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesBySoldDateRangeContentProvider(ContentResolver contentResolver, long soldMin, long soldMax) {
        String[] arguments = {"getPropertiesBySoldDateRange", String.valueOf(soldMin), String.valueOf(soldMax)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Property> getPropertiesBySoldDateMinContentProvider(ContentResolver contentResolver, long soldMin) {
        String[] arguments = {"getPropertiesBySoldDateMin", String.valueOf(soldMin)};
        return cursorToPropertyList(contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null));
    }

    public List<Media> getMediasByPropertyIdCountRangeContentProvider(ContentResolver contentResolver, int mediaCountMin, int mediaCountMax) {
        String[] arguments = {"getMediasByPropertyIdCountRange", String.valueOf(mediaCountMin), String.valueOf(mediaCountMax)};
        return cursorToMediaList(contentResolver.query(MyContentProvider.URI_MEDIA, null, null, arguments, null));
    }

    public List<Media> getMediasByPropertyIdCountMinContentProvider(ContentResolver contentResolver, int mediaCountMin) {
        String[] arguments = {"getMediasByPropertyIdCountMin", String.valueOf(mediaCountMin)};
        return cursorToMediaList(contentResolver.query(MyContentProvider.URI_MEDIA, null, null, arguments, null));
    }

    private Property cursorToProperty(Cursor cursor) {
        Property property = new Property();
        if (cursor.moveToFirst()){ property = Property.fromCursor(cursor); }
        cursor.close();
        return property;
    }

    private List<Property> cursorToPropertyList(Cursor cursor) {
        ArrayList<Property> properties = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                properties.add(Property.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return properties;
    }

    private List<Place> cursorToPlaceList(Cursor cursor) {
        ArrayList<Place> properties = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                properties.add(Place.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return properties;
    }

    private List<PropertyPlace> cursorToPropertyPlaceList(Cursor cursor) {
        ArrayList<PropertyPlace> properties = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                properties.add(PropertyPlace.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return properties;
    }

    private List<Media> cursorToMediaList(Cursor cursor) {
        ArrayList<Media> medias = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                medias.add(Media.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medias;
    }
}