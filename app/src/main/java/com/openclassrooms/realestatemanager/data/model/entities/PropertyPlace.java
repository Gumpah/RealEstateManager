package com.openclassrooms.realestatemanager.data.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("place_id"), @Index("property_id")}, foreignKeys = {@ForeignKey(entity = Property.class,
        parentColumns = "property_id",
        childColumns = "property_id"), @ForeignKey(entity = Place.class,
        parentColumns = "place_id",
        childColumns = "place_id")})
public class PropertyPlace {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "propertyPlace_id", index = true)
    public long id = 0;
    public String place_id;
    public long property_id;

    public PropertyPlace(String place_id, long property_id) {
        this.place_id = place_id;
        this.property_id = property_id;
    }

    public long getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public long getProperty_id() {
        return property_id;
    }
}
