package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.PlacesStreams;
import com.openclassrooms.realestatemanager.data.model.placenearby.PlaceAPI;
import com.openclassrooms.realestatemanager.data.model.placenearby.PlacesNearbyResult;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class PlacesViewModel extends ViewModel {

    private Disposable disposable;
    private PlacesStreams mPlacesStreams;

    public PlacesViewModel(PlacesStreams placesStreams) {
        mPlacesStreams = placesStreams;
    }

    public void NearbyPlaceToPlace(PlacesNearbyResult placesNearbyResult) {

    }

    public void fetchPlaces(String apiKey, String location) {
        disposable = mPlacesStreams.streamFetchNearbyPlaces(apiKey, location).subscribeWith(new DisposableObserver<PlacesNearbyResult>() {
            @Override
            public void onNext(PlacesNearbyResult placesNearbyResult) {
                for (PlaceAPI place : placesNearbyResult.getResults()) {
                    System.out.println("Test " + place.getName());
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
