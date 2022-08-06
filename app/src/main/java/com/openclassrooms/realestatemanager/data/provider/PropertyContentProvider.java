package com.openclassrooms.realestatemanager.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.data.Database;
import com.openclassrooms.realestatemanager.data.model.entities.Property;

public class PropertyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.data.provider";

    public static final String TABLE_NAME = Property.class.getSimpleName();

    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if (getContext() != null) {
            if (strings1 != null && strings1[0] != null && strings1[0].equals("getPropertyByIdCursor")) {
                long propertyId = ContentUris.parseId(uri);
                final Cursor cursor = Database.getDatabase(getContext()).propertyDao().getPropertyByIdCursor(propertyId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } else {
                final Cursor cursor = Database.getDatabase(getContext()).propertyDao().getPropertiesCursor();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
        }
        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
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
