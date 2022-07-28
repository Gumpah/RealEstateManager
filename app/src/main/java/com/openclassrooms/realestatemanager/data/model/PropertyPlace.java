package com.openclassrooms.realestatemanager.data.model;

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
    public long id;
    public String place_id;
    public long property_id;
}
