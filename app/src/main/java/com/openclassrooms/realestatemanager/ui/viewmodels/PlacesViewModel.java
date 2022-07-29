package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.PlacesStreams;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.remote.PlaceAPI;
import com.openclassrooms.realestatemanager.data.model.remote.PlacesNearbyResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class PlacesViewModel extends ViewModel {

    private Disposable disposable;
    private PlacesStreams mPlacesStreams;
    private MutableLiveData<List<Place>> mPlacesList;

    public PlacesViewModel(PlacesStreams placesStreams) {
        mPlacesStreams = placesStreams;
        mPlacesList = new MutableLiveData<>();
    }

    public Place nearbyPlaceToPlace(PlaceAPI placeAPI) {
        String id = placeAPI.getPlaceId();
        String name = placeAPI.getName();
        double latitude = placeAPI.getGeometry().getLocation().getLat();
        double longitude = placeAPI.getGeometry().getLocation().getLng();
        String address = placeAPI.getVicinity();
        return new Place(id, name, latitude, longitude, address);
    }

    public ArrayList<Place> placeAPIListToPlaceList(ArrayList<PlaceAPI> placeAPIList) {
        ArrayList<Place> placeList = new ArrayList<>();
        for (PlaceAPI placeAPI : placeAPIList) {
            placeList.add(nearbyPlaceToPlace(placeAPI));
        }
        return placeList;
    }

    public void fetchPlaces(String apiKey, String location) {
        ArrayList<PlaceAPI> places = new ArrayList<>();
        disposable = mPlacesStreams.streamFetchNearbyPlaces(apiKey, location).subscribeWith(new DisposableObserver<PlacesNearbyResult>() {
            @Override
            public void onNext(PlacesNearbyResult placesNearbyResult) {
                places.clear();
                places.addAll(placesNearbyResult.getResults());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                mPlacesList.postValue(placeAPIListToPlaceList(places));
            }
        });
    }

    public MutableLiveData<List<Place>> getPlacesMutableLiveData() {
        return mPlacesList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}
