package com.openclassrooms.realestatemanager.data.model.entities;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("place_id"), @Index("property_id")}, foreignKeys = {@ForeignKey(entity = Property.class,
        parentColumns = "property_id",
        childColumns = "property_id"), @ForeignKey(entity = Place.class,
        parentColumns = "place_id",
        childColumns = "place_id")})
public class PropertyPlace {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "propertyPlace_id", index = true)
    public long id = 0;
    public String place_id;
    public long property_id;

    public PropertyPlace() {
    }

    public PropertyPlace(String place_id, long property_id) {
        this.place_id = place_id;
        this.property_id = property_id;
    }

    public long getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public long getProperty_id() {
        return property_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setProperty_id(long property_id) {
        this.property_id = property_id;
    }

    public static PropertyPlace fromCursor(Cursor cursor) {
        final PropertyPlace propertyPlace = new PropertyPlace();
        propertyPlace.setId(cursor.getLong(cursor.getColumnIndexOrThrow("propertyPlace_id")));
        propertyPlace.setPlace_id(cursor.getString(cursor.getColumnIndexOrThrow("place_id")));
        propertyPlace.setProperty_id(cursor.getLong(cursor.getColumnIndexOrThrow("property_id")));
        return propertyPlace;
    }
}
