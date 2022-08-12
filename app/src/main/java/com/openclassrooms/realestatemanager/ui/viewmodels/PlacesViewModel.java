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
    private MutableLiveData<Boolean> autocompleteRequestError;
    private MutableLiveData<Boolean> fetchNearbyPlacesError;

    public PlacesViewModel(PlacesStreams placesStreams) {
        mPlacesStreams = placesStreams;
        mPlacesList = new MutableLiveData<>();
        mPropertyPlace = new MutableLiveData<>();
        autocompleteRequestError = new MutableLiveData<>();
        fetchNearbyPlacesError = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getAutocompleteRequestError() {
        return autocompleteRequestError;
    }

    public MutableLiveData<Boolean> getFetchNearbyPlacesError() {
        return fetchNearbyPlacesError;
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

    public void fetchPlaces(String apiKey, String location) {
        ArrayList<PlacesNearbyResult> mPlacesNearbyResults = new ArrayList<>();
        ArrayList<PlaceAPI> places = new ArrayList<>();
        ArrayList<String> placesTypes = new ArrayList<>();
        ArrayList<String> possibleTypes = PlaceType.types;
        disposable = mPlacesStreams.streamFetchNearbyPlaces(apiKey, location).subscribeWith(new DisposableObserver<ArrayList<PlacesNearbyResult>>() {
            @Override
            public void onNext(ArrayList<PlacesNearbyResult> placesNearbyResults) {
                places.clear();
                mPlacesNearbyResults.clear();
                mPlacesNearbyResults.addAll(placesNearbyResults);
            }

            @Override
            public void onError(Throwable e) {
                fetchNearbyPlacesError.postValue(true);
            }

            @Override
            public void onComplete() {
                for (PlacesNearbyResult placesNearbyResult : mPlacesNearbyResults) {
                    for (PlaceAPI placeAPI : placesNearbyResult.getResults()) {
                        String commonType = getFirstCommonStringIn2Lists(possibleTypes, placeAPI.getTypes());
                        if (commonType != null) {
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
                autocompleteRequestError.postValue(true);
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
