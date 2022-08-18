package com.openclassrooms.realestatemanager.data.model;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Objects;

public class SearchCriteria {

    String propertyType;
    Integer priceMin;
    Integer priceMax;
    Integer surfaceMin;
    Integer surfaceMax;
    Integer roomsMin;
    Integer roomsMax;
    Integer bathroomsMin;
    Integer bathroomsMax;
    Integer bedroomsMin;
    Integer bedroomsMax;
    LatLngBounds bounds;
    ArrayList<String> placesTypes;
    Long marketEntryDateMin;
    Long marketEntryDateMax;
    Long soldDateMin;
    Long soldDateMax;
    Integer mediaCountMin;
    Integer mediaCountMax;

    public SearchCriteria() {
    }

    public SearchCriteria(String propertyType, Integer priceMin, Integer priceMax, Integer surfaceMin, Integer surfaceMax, Integer roomsMin, Integer roomsMax, Integer bathroomsMin, Integer bathroomsMax, Integer bedroomsMin, Integer bedroomsMax, LatLngBounds bounds, ArrayList<String> placesTypes, Long marketEntryDateMin, Long marketEntryDateMax, Long soldDateMin, Long soldDateMax, Integer mediaCountMin, Integer mediaCountMax) {
        this.propertyType = propertyType;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.surfaceMin = surfaceMin;
        this.surfaceMax = surfaceMax;
        this.roomsMin = roomsMin;
        this.roomsMax = roomsMax;
        this.bathroomsMin = bathroomsMin;
        this.bathroomsMax = bathroomsMax;
        this.bedroomsMin = bedroomsMin;
        this.bedroomsMax = bedroomsMax;
        this.bounds = bounds;
        this.placesTypes = placesTypes;
        this.marketEntryDateMin = marketEntryDateMin;
        this.marketEntryDateMax = marketEntryDateMax;
        this.soldDateMin = soldDateMin;
        this.soldDateMax = soldDateMax;
        this.mediaCountMin = mediaCountMin;
        this.mediaCountMax = mediaCountMax;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public Integer getPriceMin() {
        return priceMin;
    }

    public Integer getPriceMax() {
        return priceMax;
    }

    public Integer getSurfaceMin() {
        return surfaceMin;
    }

    public Integer getSurfaceMax() {
        return surfaceMax;
    }

    public Integer getRoomsMin() {
        return roomsMin;
    }

    public Integer getRoomsMax() {
        return roomsMax;
    }

    public Integer getBathroomsMin() {
        return bathroomsMin;
    }

    public Integer getBathroomsMax() {
        return bathroomsMax;
    }

    public Integer getBedroomsMin() {
        return bedroomsMin;
    }

    public Integer getBedroomsMax() {
        return bedroomsMax;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public ArrayList<String> getPlacesTypes() {
        return placesTypes;
    }

    public Long getMarketEntryDateMin() {
        return marketEntryDateMin;
    }

    public Long getMarketEntryDateMax() {
        return marketEntryDateMax;
    }

    public Long getSoldDateMin() {
        return soldDateMin;
    }

    public Long getSoldDateMax() {
        return soldDateMax;
    }

    public Integer getMediaCountMin() {
        return mediaCountMin;
    }

    public Integer getMediaCountMax() {
        return mediaCountMax;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setPriceMin(Integer priceMin) {
        this.priceMin = priceMin;
    }

    public void setPriceMax(Integer priceMax) {
        this.priceMax = priceMax;
    }

    public void setSurfaceMin(Integer surfaceMin) {
        this.surfaceMin = surfaceMin;
    }

    public void setSurfaceMax(Integer surfaceMax) {
        this.surfaceMax = surfaceMax;
    }

    public void setRoomsMin(Integer roomsMin) {
        this.roomsMin = roomsMin;
    }

    public void setRoomsMax(Integer roomsMax) {
        this.roomsMax = roomsMax;
    }

    public void setBathroomsMin(Integer bathroomsMin) {
        this.bathroomsMin = bathroomsMin;
    }

    public void setBathroomsMax(Integer bathroomsMax) {
        this.bathroomsMax = bathroomsMax;
    }

    public void setBedroomsMin(Integer bedroomsMin) {
        this.bedroomsMin = bedroomsMin;
    }

    public void setBedroomsMax(Integer bedroomsMax) {
        this.bedroomsMax = bedroomsMax;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public void setPlacesTypes(ArrayList<String> placesTypes) {
        this.placesTypes = placesTypes;
    }

    public void setMarketEntryDateMin(Long marketEntryDateMin) {
        this.marketEntryDateMin = marketEntryDateMin;
    }

    public void setMarketEntryDateMax(Long marketEntryDateMax) {
        this.marketEntryDateMax = marketEntryDateMax;
    }

    public void setSoldDateMin(Long soldDateMin) {
        this.soldDateMin = soldDateMin;
    }

    public void setSoldDateMax(Long soldDateMax) {
        this.soldDateMax = soldDateMax;
    }

    public void setMediaCountMin(Integer mediaCountMin) {
        this.mediaCountMin = mediaCountMin;
    }

    public void setMediaCountMax(Integer mediaCountMax) {
        this.mediaCountMax = mediaCountMax;
    }

    public boolean isAtLeastOneOfCriteriaNonNull() {
        return (propertyType != null ||
                priceMin != null ||
                priceMax != null ||
                surfaceMin != null ||
                surfaceMax != null ||
                roomsMin != null ||
                roomsMax != null ||
                bathroomsMin != null ||
                bathroomsMax != null ||
                bedroomsMin != null ||
                bedroomsMax != null ||
                bounds != null ||
                (placesTypes != null && !placesTypes.isEmpty()) ||
                marketEntryDateMin != null ||
                marketEntryDateMax != null ||
                soldDateMin != null ||
                soldDateMax != null ||
                mediaCountMin != null ||
                mediaCountMax != null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchCriteria that = (SearchCriteria) o;
        return Objects.equals(propertyType, that.propertyType) && Objects.equals(priceMin, that.priceMin) && Objects.equals(priceMax, that.priceMax) && Objects.equals(surfaceMin, that.surfaceMin) && Objects.equals(surfaceMax, that.surfaceMax) && Objects.equals(roomsMin, that.roomsMin) && Objects.equals(roomsMax, that.roomsMax) && Objects.equals(bathroomsMin, that.bathroomsMin) && Objects.equals(bathroomsMax, that.bathroomsMax) && Objects.equals(bedroomsMin, that.bedroomsMin) && Objects.equals(bedroomsMax, that.bedroomsMax) && Objects.equals(bounds, that.bounds) && Objects.equals(placesTypes, that.placesTypes) && Objects.equals(marketEntryDateMin, that.marketEntryDateMin) && Objects.equals(marketEntryDateMax, that.marketEntryDateMax) && Objects.equals(soldDateMin, that.soldDateMin) && Objects.equals(soldDateMax, that.soldDateMax) && Objects.equals(mediaCountMin, that.mediaCountMin) && Objects.equals(mediaCountMax, that.mediaCountMax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyType, priceMin, priceMax, surfaceMin, surfaceMax, roomsMin, roomsMax, bathroomsMin, bathroomsMax, bedroomsMin, bedroomsMax, bounds, placesTypes, marketEntryDateMin, marketEntryDateMax, soldDateMin, soldDateMax, mediaCountMin, mediaCountMax);
    }
}
