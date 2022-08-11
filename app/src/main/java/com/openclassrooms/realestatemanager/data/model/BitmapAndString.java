package com.openclassrooms.realestatemanager.data.model;

import android.graphics.Bitmap;

public class BitmapAndString {

    Bitmap media;
    String media_description;

    public BitmapAndString(Bitmap media, String media_description) {
        this.media = media;
        this.media_description = media_description;
    }

    public Bitmap getMedia() {
        return media;
    }

    public void setMedia(Bitmap media) {
        this.media = media;
    }

    public String getMedia_description() {
        return media_description;
    }

    public void setMedia_description(String media_description) {
        this.media_description = media_description;
    }
}
