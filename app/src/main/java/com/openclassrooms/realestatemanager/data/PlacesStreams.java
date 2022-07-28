package com.openclassrooms.realestatemanager.data;

import com.openclassrooms.realestatemanager.data.model.placenearby.PlacesNearbyResult;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlacesStreams {
    public Observable<PlacesNearbyResult> streamFetchNearbyPlaces(String mapApiKey, String location) {
        PlacesService placesService = PlacesService.retrofitGsonConverter.create(PlacesService.class);
        return placesService.getNearbyPlaces(mapApiKey, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
