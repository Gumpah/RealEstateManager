package com.openclassrooms.realestatemanager.ui.viewmodels;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.jraska.livedata.TestObserver;
import com.openclassrooms.realestatemanager.data.PlacesStreams;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.PlaceType;
import com.openclassrooms.realestatemanager.data.model.remote.Geometry;
import com.openclassrooms.realestatemanager.data.model.remote.Location;
import com.openclassrooms.realestatemanager.data.model.remote.PlaceAPI;
import com.openclassrooms.realestatemanager.data.model.remote.PlacesNearbyResult;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

@RunWith(MockitoJUnitRunner.class)
public class PlacesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    PlacesViewModel mPlacesViewModel;
    @Mock
    PlacesStreams mPlacesStreams;
    @Mock
    PlacesViewModel spyPlacesViewModel;

    @Before
    public void setUp() throws Exception {
        mPlacesViewModel = new PlacesViewModel(mPlacesStreams);
        spyPlacesViewModel = spy(mPlacesViewModel);
    }

    @Test
    public void placeAPIToPlace() {
        String type = "myType";
        String id = "abc";
        String name = "name";
        double latitude = 1.1;
        double longitude = 2.2;
        String address = "123 my address";
        PlaceAPI placeAPI = new PlaceAPI();
        placeAPI.setPlaceId(id);
        placeAPI.setName(name);
        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        geometry.setLocation(location);
        placeAPI.setGeometry(geometry);
        placeAPI.setVicinity(address);

        Place placeExpected = new Place(id, name, latitude, longitude, address, type);
        Place placeActual = mPlacesViewModel.placeAPIToPlace(placeAPI, type);

        assertEquals(placeExpected, placeActual);
    }

    @Test
    public void placeAPIListToPlaceList() {
        String type = "myType";
        String id = "abc";
        String name = "name";
        double latitude = 1.1;
        double longitude = 2.2;
        String address = "123 my address";
        PlaceAPI placeAPI = new PlaceAPI();
        placeAPI.setPlaceId(id);
        placeAPI.setName(name);
        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        geometry.setLocation(location);
        placeAPI.setGeometry(geometry);
        placeAPI.setVicinity(address);
        Place place = new Place(id, name, latitude, longitude, address, type);

        ArrayList<PlaceAPI> placeAPIS = new ArrayList<>();
        placeAPIS.add(placeAPI);
        placeAPIS.add(placeAPI);
        ArrayList<String> placeTypes = new ArrayList<>();
        placeTypes.add(type);
        placeTypes.add(type);

        ArrayList<Place> placesExpected = new ArrayList<>();
        placesExpected.add(place);
        placesExpected.add(place);
        ArrayList<Place> placesActual = mPlacesViewModel.placeAPIListToPlaceList(placeAPIS, placeTypes);

        assertEquals(placesExpected, placesActual);
    }

    @Test
    public void fetchPlaces() throws InterruptedException {
        String type = "school";
        String id = "abc";
        String name = "name";
        double latitude = 1.1;
        double longitude = 2.2;
        String address = "123 my address";
        PlaceAPI placeAPI = new PlaceAPI();
        placeAPI.setPlaceId(id);
        placeAPI.setName(name);
        Geometry geometry = new Geometry();
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        geometry.setLocation(location);
        placeAPI.setGeometry(geometry);
        placeAPI.setVicinity(address);
        placeAPI.setTypes(Collections.singletonList(type));
        List<PlaceAPI> placeAPIS = Arrays.asList(placeAPI, placeAPI);
        PlacesNearbyResult placesNearbyResult = new PlacesNearbyResult();
        placesNearbyResult.setResults(placeAPIS);
        ArrayList<PlacesNearbyResult> placesNearbyResults = new ArrayList<>();
        placesNearbyResults.add(placesNearbyResult);
        ArrayList<String> placeTypes = new ArrayList<>();
        placeTypes.add(type);

        ArrayList<Place> placesExpected = new ArrayList<>();
        Place place = new Place(id, name, latitude, longitude, address, type);
        placesExpected.add(place);
        placesExpected.add(place);

        when(mPlacesStreams.streamFetchNearbyPlaces("", "")).thenReturn(Observable.just(placesNearbyResults));

        spyPlacesViewModel.fetchPlaces("", "");

        verify(spyPlacesViewModel, times(2)).getFirstCommonStringIn2Lists(PlaceType.types, placeTypes);

        TestObserver.test(spyPlacesViewModel.getPlacesMutableLiveData())
                .awaitValue()
                .assertValue(placesExpected);
    }

    @Test
    public void getFirstCommonStringIn2Lists() {
        String typeExpected = "TypeAbc";
        List<String> possibleTypes = Arrays.asList("zgdsfg", "dsgsadh", "sdfgcqnd", "ap^fd", typeExpected, "mùfdkkz");
        List<String> types = Arrays.asList("mcpka", typeExpected, "ajpjx", "nywien,a", "ajnbx_é", "manhx");

        String typeActual = mPlacesViewModel.getFirstCommonStringIn2Lists(possibleTypes, types);
        assertEquals(typeExpected, typeActual);

        possibleTypes = Arrays.asList("zgdsfg", "dsgsadh", "sdfgcqnd", "ap^fd", "mùfdkkz");
        typeActual = mPlacesViewModel.getFirstCommonStringIn2Lists(possibleTypes, types);

        assertNull(typeActual);
    }

    @Mock
    AutocompleteSupportFragment autocompleteSupportFragment;
    @Captor
    ArgumentCaptor<PlaceSelectionListener> placeSelectionListenerCaptor;

    @Test
    public void autocompleteRequest() throws InterruptedException {
        String id = "abc";
        String name = "name";
        double latitude = 1.1;
        double longitude = 2.2;
        String address = "123 my address";
        com.google.android.libraries.places.api.model.Place placeAPI = com.google.android.libraries.places.api.model.Place
                .builder()
                .setId(id)
                .setName(name)
                .setLatLng(new LatLng(latitude, longitude))
                .setAddress(address)
                .build();

        mPlacesViewModel.autocompleteRequest(autocompleteSupportFragment);
        verify(autocompleteSupportFragment).setOnPlaceSelectedListener(placeSelectionListenerCaptor.capture());
        PlaceSelectionListener placeSelectionListener = placeSelectionListenerCaptor.getValue();
        placeSelectionListener.onPlaceSelected(placeAPI);

        Place placeExpected = new Place(id, name, latitude, longitude, address, null);
        TestObserver.test(mPlacesViewModel.getPropertyPlaceMutableLiveData())
                .awaitValue()
                .assertValue(placeExpected);
    }
}