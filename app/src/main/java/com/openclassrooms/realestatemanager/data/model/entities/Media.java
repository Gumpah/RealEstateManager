package com.openclassrooms.realestatemanager.data.model.entities;

import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @Ignore
    public Media() {
    }

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

    public void setId(long id) {
        this.id = id;
    }

    public void setMedia_uri(String media_uri) {
        this.media_uri = media_uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Media fromCursor(Cursor cursor) {
        final Media media = new Media();
        media.setId(cursor.getLong(cursor.getColumnIndexOrThrow("media_id")));
        media.setPropertyId(cursor.getLong(cursor.getColumnIndexOrThrow("propertyId")));
        media.setMedia_uri(cursor.getString(cursor.getColumnIndexOrThrow(("media_uri"))));
        media.setName(cursor.getString((cursor.getColumnIndexOrThrow("name"))));
        return media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return id == media.id && propertyId == media.propertyId && Objects.equals(media_uri, media.media_uri) && Objects.equals(name, media.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, propertyId, media_uri, name);
    }
}
