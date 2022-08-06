package com.openclassrooms.realestatemanager.data.model.entities;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Property {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id", index = true)
    public long id = 0;
    String property_type;
    Double price;
    int surface;
    int rooms_count;
    String description;
    String address;
    double latitude;
    double longitude;
    PropertyStatus status;
    String market_entry;
    public String sold;
    String agent;

    public Property() {
    }

    public Property(String property_type, Double price, int surface, int rooms_count, String description, String address, double latitude, double longitude, PropertyStatus status, String market_entry, String agent) {
        this.property_type = property_type;
        this.price = price;
        this.surface = surface;
        this.rooms_count = rooms_count;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.market_entry = market_entry;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public void setRooms_count(int rooms_count) {
        this.rooms_count = rooms_count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public void setMarket_entry(String market_entry) {
        this.market_entry = market_entry;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public static Property fromCursor(Cursor cursor) {
        final Property property = new Property();
        property.setId(cursor.getLong(cursor.getColumnIndexOrThrow("property_id")));
        property.setProperty_type(cursor.getString(cursor.getColumnIndexOrThrow("property_type")));
        property.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
        property.setSurface(cursor.getInt(cursor.getColumnIndexOrThrow("surface")));
        property.setRooms_count(cursor.getInt(cursor.getColumnIndexOrThrow("rooms_count")));
        property.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        property.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        property.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        property.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
        property.setStatus(PropertyStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))));
        property.setMarket_entry(cursor.getString(cursor.getColumnIndexOrThrow("market_entry")));
        property.setSold(cursor.getString(cursor.getColumnIndexOrThrow("sold")));
        property.setAgent(cursor.getString(cursor.getColumnIndexOrThrow("agent")));
        return property;
    }
}
