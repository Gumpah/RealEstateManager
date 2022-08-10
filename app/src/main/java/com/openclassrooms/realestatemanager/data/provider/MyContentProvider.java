package com.openclassrooms.realestatemanager.data.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.data.Database;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;

public class MyContentProvider extends android.content.ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.data.provider";

    public static final String MEDIA_TABLE_NAME = Media.class.getSimpleName();
    public static final Uri URI_MEDIA = Uri.parse("content://" + AUTHORITY + "/" + MEDIA_TABLE_NAME);

    public static final String PLACE_TABLE_NAME = Place.class.getSimpleName();
    public static final Uri URI_PLACE = Uri.parse("content://" + AUTHORITY + "/" + PLACE_TABLE_NAME);

    public static final String PROPERTY_TABLE_NAME = Property.class.getSimpleName();
    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + PROPERTY_TABLE_NAME);

    public static final String PROPERTY_PLACE_TABLE_NAME = PropertyPlace.class.getSimpleName();
    public static final Uri URI_PROPERTY_PLACE = Uri.parse("content://" + AUTHORITY + "/" + PROPERTY_PLACE_TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if (getContext() != null) {
            if (strings1 != null && strings1[0] != null && strings1[0].equals("getMediasByPropertyId")) {
                long propertyId = ContentUris.parseId(uri);
                final Cursor cursor = Database.getDatabase(getContext()).mediaDao().getMediasByPropertyIdCursor(propertyId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getAllMedias")) {
                final Cursor cursor = Database.getDatabase(getContext()).mediaDao().getMediasCursor();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getMediaById")) {
                long mediaId = ContentUris.parseId(uri);
                final Cursor cursor = Database.getDatabase(getContext()).mediaDao().getMediaByIdCursor(mediaId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPlaceById") && strings1[1] != null && !strings1[1].isEmpty()) {
                String placeId = strings1[1];
                final Cursor cursor = Database.getDatabase(getContext()).placeDao().getPlaceByIdCursor(placeId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertyById")) {
                long propertyId = ContentUris.parseId(uri);
                final Cursor cursor = Database.getDatabase(getContext()).propertyDao().getPropertyByIdCursor(propertyId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getProperties")) {
                final Cursor cursor = Database.getDatabase(getContext()).propertyDao().getPropertiesCursor();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertyPlacesByPropertyId")) {
                long propertyId = ContentUris.parseId(uri);
                final Cursor cursor = Database.getDatabase(getContext()).propertyPlaceDao().getPropertyPlacesByPropertyIdCursor(propertyId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
                // --- SEARCH ---
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByPropertyType") && strings1[1] != null && !strings1[1].isEmpty()) {
                String propertyType = strings1[1];
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByPropertyTypeCursor(propertyType);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByPriceRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                int priceMin = Integer.parseInt(strings1[1]);
                int priceMax = Integer.parseInt(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByPriceRangeCursor(priceMin, priceMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByPriceMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                int priceMin = Integer.parseInt(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByPriceMinCursor(priceMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesBySurfaceRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                int surfaceMin = Integer.parseInt(strings1[1]);
                int surfaceMax = Integer.parseInt(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesBySurfaceRangeCursor(surfaceMin, surfaceMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesBySurfaceMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                int surfaceMin = Integer.parseInt(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesBySurfaceMinCursor(surfaceMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByRoomsRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                int roomsMin = Integer.parseInt(strings1[1]);
                int roomsMax = Integer.parseInt(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByRoomsRangeCursor(roomsMin, roomsMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByRoomsMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                int roomsMin = Integer.parseInt(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByRoomsMinCursor(roomsMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByBathroomsRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                int bathroomsMin = Integer.parseInt(strings1[1]);
                int bathroomsMax = Integer.parseInt(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByBathroomsRangeCursor(bathroomsMin, bathroomsMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByBathroomsMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                int bathroomsMin = Integer.parseInt(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByBathroomsMinCursor(bathroomsMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByBedroomsRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                int bedroomsMin = Integer.parseInt(strings1[1]);
                int bedroomsMax = Integer.parseInt(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByBedroomsRangeCursor(bedroomsMin, bedroomsMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByBedroomsMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                int bedroomsMin = Integer.parseInt(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByBedroomsMinCursor(bedroomsMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesInRadius")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()
                    && strings1[3] != null && !strings1[3].isEmpty()
                    && strings1[4] != null && !strings1[4].isEmpty()) {
                double lat1 = Double.parseDouble(strings1[1]);
                double lng1 = Double.parseDouble(strings1[2]);
                double lat2 = Double.parseDouble(strings1[3]);
                double lng2 = Double.parseDouble(strings1[4]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesInRadiusCursor(lat1, lng1, lat2, lng2);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPlacesByType")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                String placeType = strings1[1];
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPlacesByTypeCursor(placeType);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertyPlacesByPlaceId")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                String placeId = strings1[1];
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertyPlacesByPlaceIdCursor(placeId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByMarketEntryDateRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                long marketEntryMin = Long.parseLong(strings1[1]);
                long marketEntryMax = Long.parseLong(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByMarketEntryDateRangeCursor(marketEntryMin, marketEntryMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesByMarketEntryDateMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                long marketEntryMin = Long.parseLong(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesByMarketEntryDateMinCursor(marketEntryMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesBySoldDateRange")
                    && strings1[1] != null && !strings1[1].isEmpty()
                    && strings1[2] != null && !strings1[2].isEmpty()) {
                long soldMin = Long.parseLong(strings1[1]);
                long soldMax = Long.parseLong(strings1[2]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesBySoldDateRangeCursor(soldMin, soldMax);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertiesBySoldDateMin")
                    && strings1[1] != null && !strings1[1].isEmpty()) {
                long soldMin = Long.parseLong(strings1[1]);
                final Cursor cursor = Database.getDatabase(getContext()).searchDao().getPropertiesBySoldDateMinCursor(soldMin);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
        }
        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + MEDIA_TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
