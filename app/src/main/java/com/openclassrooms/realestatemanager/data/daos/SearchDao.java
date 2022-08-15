package com.openclassrooms.realestatemanager.data.daos;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

import java.util.List;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM Property WHERE property_type = :propertyType")
    Cursor getPropertiesByPropertyTypeCursor(String propertyType);

    @Query("SELECT * FROM Property WHERE price BETWEEN :priceMin and :priceMax")
    Cursor getPropertiesByPriceRangeCursor(int priceMin, int priceMax);

    @Query("SELECT * FROM Property WHERE price >= :priceMin")
    Cursor getPropertiesByPriceMinCursor(int priceMin);

    @Query("SELECT * FROM Property WHERE surface BETWEEN :surfaceMin and :surfaceMax")
    Cursor getPropertiesBySurfaceRangeCursor(int surfaceMin, int surfaceMax);

    @Query("SELECT * FROM Property WHERE surface >= :surfaceMin")
    Cursor getPropertiesBySurfaceMinCursor(int surfaceMin);

    @Query("SELECT * FROM Property WHERE rooms_count BETWEEN :roomsMin and :roomsMax")
    Cursor getPropertiesByRoomsRangeCursor(int roomsMin, int roomsMax);

    @Query("SELECT * FROM Property WHERE rooms_count >= :roomsMin")
    Cursor getPropertiesByRoomsMinCursor(int roomsMin);

    @Query("SELECT * FROM Property WHERE bathrooms_count BETWEEN :bathroomsMin and :bathroomsMax")
    Cursor getPropertiesByBathroomsRangeCursor(int bathroomsMin, int bathroomsMax);

    @Query("SELECT * FROM Property WHERE bathrooms_count >= :bathroomsMin")
    Cursor getPropertiesByBathroomsMinCursor(int bathroomsMin);

    @Query("SELECT * FROM Property WHERE bedrooms_count BETWEEN :bedroomsMin and :bedroomsMax")
    Cursor getPropertiesByBedroomsRangeCursor(int bedroomsMin, int bedroomsMax);

    @Query("SELECT * FROM Property WHERE bedrooms_count >= :bedroomsMin")
    Cursor getPropertiesByBedroomsMinCursor(int bedroomsMin);

    @Query("SELECT * FROM Property WHERE (latitude BETWEEN :lat1 and :lat2) AND (longitude BETWEEN :lng1 and :lng2)")
    Cursor getPropertiesInRadiusCursor(Double lat1, Double lng1, Double lat2, Double lng2);

    @Query("SELECT * FROM PLACE WHERE type = :placeType")
    Cursor getPlacesByTypeCursor(String placeType);

    @Query("SELECT * FROM PropertyPlace WHERE place_id = :placeId")
    Cursor getPropertyPlacesByPlaceIdCursor(String placeId);

    @Query("SELECT * FROM PROPERTY WHERE market_entry BETWEEN :marketEntryMin and :marketEntryMax")
    Cursor getPropertiesByMarketEntryDateRangeCursor(long marketEntryMin, long marketEntryMax);

    @Query("SELECT * FROM PROPERTY WHERE market_entry >= :marketEntryMin")
    Cursor getPropertiesByMarketEntryDateMinCursor(long marketEntryMin);

    @Query("SELECT * FROM PROPERTY WHERE sold BETWEEN :soldMin and :soldMax")
    Cursor getPropertiesBySoldDateRangeCursor(long soldMin, long soldMax);

    @Query("SELECT * FROM PROPERTY WHERE sold >= :soldMin")
    Cursor getPropertiesBySoldDateMinCursor(long soldMin);

    @Query("SELECT * FROM MEDIA GROUP BY propertyId having COUNT(*) BETWEEN :mediaCountMin and :mediaCountMax")
    Cursor getMediasByPropertyIdCountRangeCursor(int mediaCountMin, int mediaCountMax);

    @Query("SELECT * FROM MEDIA GROUP BY propertyId having COUNT(*) >= :mediaCountMin")
    Cursor getMediasByPropertyIdCountMinCursor(int mediaCountMin);
}
