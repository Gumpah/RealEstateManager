package com.openclassrooms.realestatemanager.data.model;

import android.net.Uri;

import com.openclassrooms.realestatemanager.data.model.entities.Property;

import java.util.Objects;

public class PropertyAndImage {

    Property property;
    Uri uri;

    public PropertyAndImage(Property property, Uri uri) {
        this.property = property;
        this.uri = uri;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAndImage that = (PropertyAndImage) o;
        return Objects.equals(property, that.property) && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, uri);
    }
}
