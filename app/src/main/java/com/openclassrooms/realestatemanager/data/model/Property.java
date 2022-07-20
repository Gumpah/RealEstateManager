package com.openclassrooms.realestatemanager.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class Property {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    long id;
    String property_type;
    Double price;
    int rooms_count;
    String description;
    ArrayList<String> medias_uri_list;
    String address;
    PropertyStatus status;
    Date market_entry;
    Date sold;
    String agent;
}
