package com.openclassrooms.realestatemanager.data.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.List;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM Property WHERE property_type = :propertyType")
    List<Property> getPropertiesByPropertyType(String propertyType);

    @Query("SELECT * FROM Property WHERE price BETWEEN :priceMin and :priceMax")
    List<Property> getPropertiesByPriceRange(int priceMin, int priceMax);

    @Query("SELECT * FROM Property WHERE surface BETWEEN :surfaceMin and :surfaceMax")
    List<Property> getPropertiesBySurfaceRange(int surfaceMin, int surfaceMax);

    @Query("SELECT * FROM Property WHERE rooms_count BETWEEN :roomsMin and :roomsMax")
    List<Property> getPropertiesByRoomsRange(int roomsMin, int roomsMax);

    @Query("SELECT * FROM Property WHERE (latitude BETWEEN :lat1 and :lat2) AND (longitude BETWEEN :lng1 and :lng2)")
    List<Property> getPropertiesInRadius(Double lat1, Double lng1, Double lat2, Double lng2);
}
