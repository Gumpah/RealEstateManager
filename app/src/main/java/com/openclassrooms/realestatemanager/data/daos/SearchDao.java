package com.openclassrooms.realestatemanager.data.daos;

import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.List;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM Property WHERE property_type = :propertyType")
    List<Property> getPropertiesByPropertyType(String propertyType);

    @Query("SELECT * FROM Property WHERE price BETWEEN :priceMin and :priceMax")
    List<Property> getPropertiesByPriceRange(int priceMin, int priceMax);

    @Query("SELECT * FROM Property WHERE price >= :priceMin")
    List<Property> getPropertiesByPriceMin(int priceMin);

    @Query("SELECT * FROM Property WHERE surface BETWEEN :surfaceMin and :surfaceMax")
    List<Property> getPropertiesBySurfaceRange(int surfaceMin, int surfaceMax);

    @Query("SELECT * FROM Property WHERE surface >= :surfaceMin")
    List<Property> getPropertiesBySurfaceMin(int surfaceMin);

    @Query("SELECT * FROM Property WHERE rooms_count BETWEEN :roomsMin and :roomsMax")
    List<Property> getPropertiesByRoomsRange(int roomsMin, int roomsMax);

    @Query("SELECT * FROM Property WHERE rooms_count >= :roomsMin")
    List<Property> getPropertiesByRoomsMin(int roomsMin);

    @Query("SELECT * FROM Property WHERE bathrooms_count BETWEEN :bathroomsMin and :bathroomsMax")
    List<Property> getPropertiesByBathroomsRange(int bathroomsMin, int bathroomsMax);

    @Query("SELECT * FROM Property WHERE bathrooms_count >= :bathroomsMin")
    List<Property> getPropertiesByBathroomsMin(int bathroomsMin);

    @Query("SELECT * FROM Property WHERE bedrooms_count BETWEEN :bedroomsMin and :bedroomsMax")
    List<Property> getPropertiesByBedroomsRange(int bedroomsMin, int bedroomsMax);

    @Query("SELECT * FROM Property WHERE bedrooms_count >= :bedroomsMin")
    List<Property> getPropertiesByBedroomsMin(int bedroomsMin);

    @Query("SELECT * FROM Property WHERE (latitude BETWEEN :lat1 and :lat2) AND (longitude BETWEEN :lng1 and :lng2)")
    List<Property> getPropertiesInRadius(Double lat1, Double lng1, Double lat2, Double lng2);

    @Query("SELECT * FROM PLACE WHERE type = :placeType")
    List<Place> getPlacesByType(String placeType);

    @Query("SELECT * FROM PropertyPlace WHERE place_id = :placeId")
    List<PropertyPlace> getPropertyPlacesByPlaceId(String placeId);

    @Query("SELECT * FROM PROPERTY WHERE market_entry BETWEEN :marketEntryMin and :marketEntryMax")
    List<Property> getPropertiesByMarketEntryDateRange(long marketEntryMin, long marketEntryMax);

    @Query("SELECT * FROM PROPERTY WHERE market_entry >= :marketEntryMin")
    List<Property> getPropertiesByMarketEntryDateMin(long marketEntryMin);

    @Query("SELECT * FROM PROPERTY WHERE sold BETWEEN :soldMin and :soldMax")
    List<Property> getPropertiesBySoldDateRange(long soldMin, long soldMax);

    @Query("SELECT * FROM PROPERTY WHERE sold >= :soldMin")
    List<Property> getPropertiesBySoldDateMin(long soldMin);
}
