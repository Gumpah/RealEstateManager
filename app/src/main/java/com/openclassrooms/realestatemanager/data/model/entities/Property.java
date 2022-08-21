package com.openclassrooms.realestatemanager.data.model.entities;

import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Property {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id", index = true)
    public long id = 0;
    String property_type;
    Double price;
    Integer surface;
    Integer rooms_count;
    Integer bathrooms_count;
    Integer bedrooms_count;
    String description;
    String address;
    Double latitude;
    Double longitude;
    PropertyStatus status;
    Long market_entry;
    Long sold;
    String agent;

    @Ignore
    public Property() {
    }

    public Property(String property_type, Double price,  Integer surface, Integer rooms_count, Integer bathrooms_count, Integer bedrooms_count, String description, String address, Double latitude, Double longitude, PropertyStatus status, Long market_entry, String agent) {
        this.property_type = property_type;
        this.price = price;
        this.surface = surface;
        this.rooms_count = rooms_count;
        this.bathrooms_count = bathrooms_count;
        this.bedrooms_count = bedrooms_count;
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

    public Integer getSurface() {
        return surface;
    }

    public Integer getRooms_count() {
        return rooms_count;
    }

    public Integer getBathrooms_count() {
        return bathrooms_count;
    }

    public Integer getBedrooms_count() {
        return bedrooms_count;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public Long getMarket_entry() {
        return market_entry;
    }

    public Long getSold() {
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

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public void setRooms_count(Integer rooms_count) {
        this.rooms_count = rooms_count;
    }

    public void setBathrooms_count(Integer bathrooms_count) {
        this.bathrooms_count = bathrooms_count;
    }

    public void setBedrooms_count(Integer bedrooms_count) {
        this.bedrooms_count = bedrooms_count;
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

    public void setMarket_entry(long market_entry) {
        this.market_entry = market_entry;
    }

    public void setSold(long sold) {
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
        property.setBathrooms_count(cursor.getInt(cursor.getColumnIndexOrThrow("bathrooms_count")));
        property.setBedrooms_count(cursor.getInt(cursor.getColumnIndexOrThrow("bedrooms_count")));
        property.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
        property.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        property.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        property.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
        property.setStatus(PropertyStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))));
        property.setMarket_entry(cursor.getLong(cursor.getColumnIndexOrThrow("market_entry")));
        property.setSold(cursor.getLong(cursor.getColumnIndexOrThrow("sold")));
        property.setAgent(cursor.getString(cursor.getColumnIndexOrThrow("agent")));
        return property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return id == property.id && Objects.equals(surface, property.surface) && Objects.equals(rooms_count, property.rooms_count) && Objects.equals(bathrooms_count, property.bathrooms_count) && Objects.equals(bedrooms_count, property.bedrooms_count) && Double.compare(property.latitude, latitude) == 0 && Double.compare(property.longitude, longitude) == 0 && Objects.equals(market_entry, property.market_entry) && Objects.equals(sold, property.sold) && Objects.equals(property_type, property.property_type) && Objects.equals(price, property.price) && Objects.equals(description, property.description) && Objects.equals(address, property.address) && status == property.status && Objects.equals(agent, property.agent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, property_type, price, surface, rooms_count, bathrooms_count, bedrooms_count, description, address, latitude, longitude, status, market_entry, sold, agent);
    }
}
