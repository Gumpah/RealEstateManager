package com.openclassrooms.realestatemanager.data;

import com.openclassrooms.realestatemanager.data.model.remote.PlacesNearbyResult;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlacesStreams {

    public ArrayList<PlacesNearbyResult> listRepeatGlobal;

    private boolean isNextPageTokenNull() {
        return (listRepeatGlobal.get(listRepeatGlobal.size() - 1).getNextPageToken() == null
        || listRepeatGlobal.get(listRepeatGlobal.size() - 1).getNextPageToken().isEmpty());
    }

    public Observable<PlacesNearbyResult> streamFetchNearbyPlacesOld(String mapApiKey, String location) {
        PlacesService placesService = PlacesService.retrofitGsonConverter.create(PlacesService.class);
        return placesService.getNearbyPlaces(mapApiKey, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    private Observable<ArrayList<PlacesNearbyResult>> streamFetchNearbyPlacesNextPageRepeat(String mapApiKey, ArrayList<PlacesNearbyResult> list) {
        listRepeatGlobal = new ArrayList<>(list);
        return streamFetchNearbyPlacesNextPage(mapApiKey, list)
                .delay(2500, TimeUnit.MILLISECONDS)
                .takeUntil(result -> (isNextPageTokenNull()))
                .flatMap(r -> streamFetchNearbyPlacesNextPage(mapApiKey, listRepeatGlobal))
                .repeat()
                .takeUntil(result -> (isNextPageTokenNull()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    private Observable<ArrayList<PlacesNearbyResult>> streamFetchNearbyPlacesNextPage(String mapApiKey, ArrayList<PlacesNearbyResult> list) {
        PlacesService placesService = PlacesService.retrofitGsonConverter.create(PlacesService.class);
        if (isNextPageTokenNull()) {
            return Observable.just(listRepeatGlobal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(10, TimeUnit.SECONDS);
        } else {
            return placesService.getNearbyPlacesNextPage(mapApiKey, listRepeatGlobal.get(listRepeatGlobal.size() - 1).getNextPageToken())
                    .map(placesNearbyResult -> {
                        listRepeatGlobal.add(placesNearbyResult);
                        return listRepeatGlobal;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(10, TimeUnit.SECONDS);
        }
    }

    public Observable<ArrayList<PlacesNearbyResult>> streamFetchNearbyPlaces(String mapApiKey, String location) {
        ArrayList<PlacesNearbyResult> list = new ArrayList<>();
        return streamFetchNearbyPlacesOld(mapApiKey, location)
                .map(placesNearbyResult -> {
                    list.add(placesNearbyResult);
                    return list;
                })
                .delay(2500, TimeUnit.MILLISECONDS)
                .flatMap(l -> streamFetchNearbyPlacesNextPageRepeat(mapApiKey, l));
    }
}
