package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.openclassrooms.realestatemanager.data.PlacesStreams;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.remote.PlaceAPI;
import com.openclassrooms.realestatemanager.data.model.remote.PlacesNearbyResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class PlacesViewModel extends ViewModel {

    private Disposable disposable;
    private PlacesStreams mPlacesStreams;
    private MutableLiveData<ArrayList<Place>> mPlacesList;
    private MutableLiveData<Place> mPropertyPlace;

    public PlacesViewModel(PlacesStreams placesStreams) {
        mPlacesStreams = placesStreams;
        mPlacesList = new MutableLiveData<>();
        mPropertyPlace = new MutableLiveData<>();
    }

    public Place placeAPIToPlace(PlaceAPI placeAPI) {
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
            placeList.add(placeAPIToPlace(placeAPI));
        }
        return placeList;
    }

    public void fetchPlaces(String apiKey, String location) {
        ArrayList<PlaceAPI> places = new ArrayList<>();
        ArrayList<String> possibleTypes = new ArrayList<>(Arrays.asList(
                "park",
                "school",
                "store",
                "penthouse",
                "university",
                "subway_station",
                "train_station",
                "bus_station",
                "supermarket",
                "movie_theater"));
        disposable = mPlacesStreams.streamFetchNearbyPlaces(apiKey, location).subscribeWith(new DisposableObserver<PlacesNearbyResult>() {
            @Override
            public void onNext(PlacesNearbyResult placesNearbyResult) {
                places.clear();
                for (PlaceAPI placeAPI : placesNearbyResult.getResults()) {
                    System.out.println("Test " + placeAPI.getName());
                    places.add(placeAPI);
                    /*
                    if (!Collections.disjoint(placeAPI.getTypes(), possibleTypes)) {
                        System.out.println("MyTest " + placeAPI.getName());
                        places.add(placeAPI);
                    }
                     */
                }
                mPlacesList.postValue(placeAPIListToPlaceList(places));
                System.out.println("Should be first");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                System.out.println("Should be second");
            }
        });
    }

    public MutableLiveData<ArrayList<Place>> getPlacesMutableLiveData() {
        return mPlacesList;
    }

    public MutableLiveData<Place> getPropertyPlaceMutableLiveData() {
        return mPropertyPlace;
    }

    public void autocompleteRequest(AutocompleteSupportFragment autocompleteFragment) {
        autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.NAME,
                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                com.google.android.libraries.places.api.model.Place.Field.ADDRESS));
        autocompleteFragment.setCountry("FR");
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place placeAPI) {
                if (placeAPI.getLatLng() != null) {
                    Place place = new Place(
                            placeAPI.getId(),
                            placeAPI.getName(),
                            placeAPI.getLatLng().latitude,
                            placeAPI.getLatLng().longitude,
                            placeAPI.getAddress());
                    mPropertyPlace.postValue(place);
                }
            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}
