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

    public PropertyRepository(PropertyDao propertyDao, MediaDao mediaDao, PlaceDao placeDao, PropertyPlaceDao propertyPlaceDao) {
        mPropertyDao = propertyDao;
        mMediaDao = mediaDao;
        mPlaceDao = placeDao;
        mPropertyPlaceDao = propertyPlaceDao;
    }

    public long insertProperty(Property property) { return mPropertyDao.insertProperty(property); }

    public void updateProperty(Property property) { mPropertyDao.updateProperty(property); }

    public void deleteProperty(long propertyId) { mPropertyDao.deleteProperty(propertyId);}

    public void insertMultipleMedias(List<Media> medias) { mMediaDao.insertMultipleMedias(medias); }

    public void deleteMediaByMediaId(long id) {
        mMediaDao.deleteMedia(id);
    }

    public void insertPlace(Place place) { mPlaceDao.insertPlace(place); }

    public void insertPropertyPlace(PropertyPlace propertyPlace) { mPropertyPlaceDao.insertPropertyPlace(propertyPlace); }

    public Cursor getPropertiesContentProvider(ContentResolver contentResolver) {
        String[] arguments = {"getProperties"};
        return contentResolver.query(MyContentProvider.URI_PROPERTY, null, null, arguments, null);
    }

    public Property getPropertyByIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getPropertyById"};
        return cursorToProperty(contentResolver.query(ContentUris.withAppendedId(MyContentProvider.URI_PROPERTY, propertyId), null, null, arguments, null));
    }

    public List<Media> getMediasByPropertyIdContentProvider(ContentResolver contentResolver, long propertyId) {
        String[] arguments = {"getMediasByPropertyId"};
        return cursorToMediaList(contentResolver.query(ContentUris.withAppendedId(MyContentProvider.URI_MEDIA, propertyId), null, null, arguments, null));
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

    private Property cursorToProperty(Cursor cursor) {
        Property property = new Property();
        if (cursor.moveToFirst()){ property = Property.fromCursor(cursor); }
        cursor.close();
        return property;
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
