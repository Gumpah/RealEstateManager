package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.openclassrooms.realestatemanager.data.PlacesStreams;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.PlaceType;
import com.openclassrooms.realestatemanager.data.model.remote.PlaceAPI;
import com.openclassrooms.realestatemanager.data.model.remote.PlacesNearbyResult;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Place placeAPIToPlace(PlaceAPI placeAPI, String type) {
        String id = placeAPI.getPlaceId();
        String name = placeAPI.getName();
        double latitude = placeAPI.getGeometry().getLocation().getLat();
        double longitude = placeAPI.getGeometry().getLocation().getLng();
        String address = placeAPI.getVicinity();
        return new Place(id, name, latitude, longitude, address, type);
    }

    public ArrayList<Place> placeAPIListToPlaceList(ArrayList<PlaceAPI> placeAPIList, ArrayList<String> placesTypes) {
        ArrayList<Place> placeList = new ArrayList<>();
        int i = 0;
        for (PlaceAPI placeAPI : placeAPIList) {
            placeList.add(placeAPIToPlace(placeAPI, placesTypes.get(i)));
            i++;
        }
        return placeList;
    }

    public void fetchPlacesOld(String apiKey, String location) {
        ArrayList<PlaceAPI> places = new ArrayList<>();
        ArrayList<String> placesTypes = new ArrayList<>();
        ArrayList<String> possibleTypes = PlaceType.types;
        disposable = mPlacesStreams.streamFetchNearbyPlacesOld(apiKey, location).subscribeWith(new DisposableObserver<PlacesNearbyResult>() {
            @Override
            public void onNext(PlacesNearbyResult placesNearbyResult) {
                places.clear();
                for (PlaceAPI placeAPI : placesNearbyResult.getResults()) {
                    String commonType = getFirstCommonStringIn2Lists(possibleTypes, placeAPI.getTypes());
                    if (commonType != null) {
                        System.out.println("Test " + placeAPI.getName());
                        places.add(placeAPI);
                        placesTypes.add(commonType);
                    }
                }
                mPlacesList.postValue(placeAPIListToPlaceList(places, placesTypes));
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void fetchPlaces(String apiKey, String location) {
        System.out.println("fetchCalled");
        ArrayList<PlacesNearbyResult> mPlacesNearbyResults = new ArrayList<>();
        ArrayList<PlaceAPI> places = new ArrayList<>();
        ArrayList<String> placesTypes = new ArrayList<>();
        ArrayList<String> possibleTypes = PlaceType.types;
        disposable = mPlacesStreams.streamFetchNearbyPlaces(apiKey, location).subscribeWith(new DisposableObserver<ArrayList<PlacesNearbyResult>>() {
            @Override
            public void onNext(ArrayList<PlacesNearbyResult> placesNearbyResults) {
                System.out.println("fetchCalledNext");
                places.clear();
                mPlacesNearbyResults.clear();
                mPlacesNearbyResults.addAll(placesNearbyResults);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("fetchCalledError" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("fetchCalledComplete " + mPlacesNearbyResults.size());
                for (PlacesNearbyResult placesNearbyResult : mPlacesNearbyResults) {
                    System.out.println("CCC");
                    for (PlaceAPI placeAPI : placesNearbyResult.getResults()) {
                        System.out.println("Test " + placeAPI.getName());
                        String commonType = getFirstCommonStringIn2Lists(possibleTypes, placeAPI.getTypes());
                        if (commonType != null) {
                            System.out.println("Common Type" + placeAPI.getName() + " " + commonType);
                            places.add(placeAPI);
                            placesTypes.add(commonType);
                        }
                    }
                }
                mPlacesList.postValue(placeAPIListToPlaceList(places, placesTypes));
            }
        });
    }


    public String getFirstCommonStringIn2Lists(List<String> possibleTypes, List<String> placesTypes) {
        for (String placeType : possibleTypes) {
            for (String possibleType : placesTypes) {
                if (placeType.equals(possibleType)) return possibleType;
            }
        }
        return null;
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
                if (placeAPI.getLatLng() != null && placeAPI.getId() != null) {
                    Place place = new Place(
                            placeAPI.getId(),
                            placeAPI.getName(),
                            placeAPI.getLatLng().latitude,
                            placeAPI.getLatLng().longitude,
                            placeAPI.getAddress(),
                            null);
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
