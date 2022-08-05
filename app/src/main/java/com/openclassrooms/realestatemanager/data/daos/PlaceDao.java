package com.openclassrooms.realestatemanager.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.entities.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM Place")
    List<Place> getPlaces();

    @Query("SELECT * FROM Place WHERE place_id = :placeId")
    Place getPlaceById(String placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(Place place);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultiplePlaces(List<Place> places);

    @Update
    void updatePlace(Place place);

    @Query("DELETE FROM Place WHERE place_id = :placeId")
    void deletePlace(long placeId);
}
