package com.openclassrooms.realestatemanager.data.daos;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.entities.Media;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM Media")
    List<Media> getMedias();

    @Query("SELECT * FROM Media")
    Cursor getMediasCursor();

    @Query("SELECT * FROM Media WHERE media_id = :mediaId")
    Media getMediaById(long mediaId);

    @Query("SELECT * FROM Media WHERE media_id = :mediaId")
    Cursor getMediaByIdCursor(long mediaId);

    @Query("SELECT * FROM Media WHERE propertyId = :propertyId")
    List<Media> getMediasByPropertyId(long propertyId);

    @Query("SELECT * FROM Media WHERE propertyId = :propertyId")
    Cursor getMediasByPropertyIdCursor(long propertyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedia(Media media);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleMedias(List<Media> medias);

    @Update
    void updateMedia(Media media);

    @Query("DELETE FROM Media WHERE media_id = :mediaId")
    void deleteMedia(long mediaId);
}
