package com.openclassrooms.realestatemanager.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Property {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    public long id = 0;
    String property_type;
    Double price;
    int surface;
    int rooms_count;
    String description;
    String address;
    PropertyStatus status;
    String market_entry;
    public String sold;
    String agent;

    public Property(String property_type, Double price, int surface, int rooms_count, String description, String address, PropertyStatus status, String market_entry, String agent) {
        this.property_type = property_type;
        this.price = price;
        this.surface = surface;
        this.rooms_count = rooms_count;
        this.description = description;
        this.address = address;
        this.status = status;
        this.market_entry = market_entry;
        this.sold = sold;
        this.agent = agent;
    }

    public long getId() {
        return id;
    }

    public String getProperty_type() {
        return property_type;
    }

    public Double getPrice() {
        return price;
    }

    public int getSurface() {
        return surface;
    }

    public int getRooms_count() {
        return rooms_count;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public String getMarket_entry() {
        return market_entry;
    }

    public String getSold() {
        return sold;
    }

    public String getAgent() {
        return agent;
    }
}
