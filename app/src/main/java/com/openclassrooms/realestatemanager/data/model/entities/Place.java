package com.openclassrooms.realestatemanager.data.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Place {

    @ColumnInfo(name = "place_id", index = true)
    public String id; //not generated automatically, given by API
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    //add type

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
}
