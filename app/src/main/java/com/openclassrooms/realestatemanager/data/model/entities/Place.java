package com.openclassrooms.realestatemanager.data.model.entities;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Place {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "place_id", index = true)
    public String id; //not generated automatically, given by API
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    //add type


    public Place() {
    }

    public Place(String id, String name, double latitude, double longitude, String address) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Place fromCursor(Cursor cursor) {
        final Place place = new Place();
        place.setId(cursor.getString(cursor.getColumnIndexOrThrow("place_id")));
        place.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        place.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        place.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
        place.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        return place;
    }
}
