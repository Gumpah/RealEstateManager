package com.openclassrooms.realestatemanager.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.model.Media;
import com.openclassrooms.realestatemanager.data.model.Property;

@androidx.room.Database(entities = {Property.class, Media.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

        private final static String databaseName = "MyDatabase.db";

        private static volatile Database INSTANCE;

        public abstract PropertyDao propertyDao();
        public abstract MediaDao mediaDao();

        public static Database getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (Database.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        Database.class, databaseName)
                                .build();
                    }
                }
            }
            return INSTANCE;
        }
}