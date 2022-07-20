package com.openclassrooms.realestatemanager.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getProperties();

    @Query("SELECT * FROM Property WHERE property_id = :propertyId")
    LiveData<Property> getPropertyById(long propertyId);

    @Insert
    void insertProperty(Property property);

    @Update
    void updateProperty(Property property);

    @Query("DELETE FROM Property WHERE property_id = :propertyId")
    void deleteProperty(long propertyId);

}