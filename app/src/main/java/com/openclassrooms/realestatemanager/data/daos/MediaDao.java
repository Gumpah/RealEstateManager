package com.openclassrooms.realestatemanager.data.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.Media;
import com.openclassrooms.realestatemanager.data.model.Property;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM Media")
    List<Media> getMedias();

    @Query("SELECT * FROM Media WHERE media_id = :mediaId")
    LiveData<Media> getMediaById(long mediaId);

    @Query("SELECT * FROM Media WHERE propertyId = :propertyId")
    List<Media> getMediasByPropertyId(long propertyId);

    @Insert
    void insertMedia(Media media);

    @Insert
    void insertMultipleMedias(List<Media> medias);

    @Update
    void updateMedia(Media media);

    @Query("DELETE FROM Media WHERE media_id = :mediaId")
    void deleteMedia(long mediaId);
}
