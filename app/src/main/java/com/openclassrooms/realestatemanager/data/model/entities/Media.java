package com.openclassrooms.realestatemanager.data.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index("propertyId"), foreignKeys = @ForeignKey(entity = Property.class,
        parentColumns = "property_id",
        childColumns = "propertyId"))
public class Media {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_id", index = true)
    public long id = 0;
    long propertyId;
    String media_uri;
    String name;

    public Media(String media_uri, String name) {
        this.media_uri = media_uri;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public String getMedia_uri() {
        return media_uri;
    }

    public String getName() {
        return name;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
