package com.openclassrooms.realestatemanager.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.List;

@Dao
public interface PropertyPlaceDao {

    @Query("SELECT * FROM PropertyPlace")
    List<PropertyPlace> getPropertyPlaces();

    @Query("SELECT * FROM PropertyPlace WHERE propertyPlace_id = :propertyPlaceId")
    LiveData<PropertyPlace> getPropertyPlaceById(long propertyPlaceId);

    @Query("SELECT * FROM PropertyPlace WHERE property_id = :propertyId")
    List<PropertyPlace> getPropertyPlacesByPropertyId(long propertyId);

    @Insert
    long insertPropertyPlace(PropertyPlace propertyPlace);

    @Insert
    void insertMultiplePropertyPlaces(List<PropertyPlace> places);

    @Update
    void updatePropertyPlace(PropertyPlace propertyPlace);

    @Query("DELETE FROM PropertyPlace WHERE propertyPlace_id = :propertyPlaceId")
    void deletePropertyPlace(long propertyPlaceId);
}
