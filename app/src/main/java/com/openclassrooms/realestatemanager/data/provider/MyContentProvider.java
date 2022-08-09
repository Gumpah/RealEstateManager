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
