package com.openclassrooms.realestatemanager.data.model;

import androidx.room.ColumnInfo;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    @ColumnInfo(name = "place_id", index = true)
    public String id; //not generated automatically, given by API
    private String name;
    private LatLng position;
    private String address;
    //add type
}
