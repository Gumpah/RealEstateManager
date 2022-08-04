package com.openclassrooms.realestatemanager.data.model;

import android.net.Uri;

import com.openclassrooms.realestatemanager.data.model.entities.Property;

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
}
