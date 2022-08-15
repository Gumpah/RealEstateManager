package com.openclassrooms.realestatemanager.data.daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.List;

@Dao
public interface PropertyPlaceDao {

    @Query("SELECT * FROM PropertyPlace WHERE property_id = :propertyId")
    Cursor getPropertyPlacesByPropertyIdCursor(long propertyId);

    @Insert
    long insertPropertyPlace(PropertyPlace propertyPlace);
}
