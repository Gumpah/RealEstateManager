package com.openclassrooms.realestatemanager.data;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.model.Media;
import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {

    private PropertyDao mPropertyDao;

    private MediaDao mMediaDao;

    public PropertyRepository(PropertyDao propertyDao, MediaDao mediaDao) {
        mPropertyDao = propertyDao;
        mMediaDao = mediaDao;
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
}
