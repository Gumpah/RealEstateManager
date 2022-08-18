package com.openclassrooms.realestatemanager.ui.propertysearch;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import android.content.ContentResolver;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jraska.livedata.TestObserver;
import com.openclassrooms.realestatemanager.data.PropertySearchRepository;
import com.openclassrooms.realestatemanager.data.model.SearchCriteria;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.utils.Utils;

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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(MockitoJUnitRunner.class)
public class PropertySearchViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    Property property;
    Media media;

    PropertySearchViewModel mPropertySearchViewModel;
    @Mock
    PropertySearchViewModel spyPropertySearchViewModel;
    @Mock
    PropertySearchRepository mPropertySearchRepository;
    @Mock
    Executor executor;
    @Mock
    ContentResolver contentResolver;
    @Captor
    ArgumentCaptor<Runnable> runnable;

    @Before
    public void setUp() throws Exception {
        property = new Property(
                "type",
                5d,
                4,
                3,
                2,
                1,
                "description",
                "address",
                1.1,
                2.2,
                PropertyStatus.AVAILABLE,
                1660125600000L,
                "Tom");
        property.setId(1);
        media = new Media();
        media.setPropertyId(property.getId());

        mPropertySearchViewModel = new PropertySearchViewModel(mPropertySearchRepository, executor);
        spyPropertySearchViewModel = spy(mPropertySearchViewModel);

        List<Property> properties = Collections.singletonList(property);
        List<Media> medias = Collections.singletonList(media);
        ArrayList<Long> propertyArrayList = new ArrayList<>();
        propertyArrayList.add(property.getId());

        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByPropertyTypeContentProvider(eq(contentResolver), anyString());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByPriceRangeContentProvider(eq(contentResolver), anyInt(), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByPriceMinContentProvider(eq(contentResolver), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesBySurfaceRangeContentProvider(eq(contentResolver), anyInt(), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesBySurfaceMinContentProvider(eq(contentResolver), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByRoomsRangeContentProvider(eq(contentResolver), anyInt(), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByRoomsMinContentProvider(eq(contentResolver), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByBathroomsRangeContentProvider(eq(contentResolver), anyInt(), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByBathroomsMinContentProvider(eq(contentResolver), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByBedroomsRangeContentProvider(eq(contentResolver), anyInt(), anyInt());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByBedroomsMinContentProvider(eq(contentResolver), anyInt());
        doReturn(propertyArrayList).when(spyPropertySearchViewModel).getPropertiesIdsForAPlaceTypeContentProvider(eq(contentResolver), anyString());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesInRadiusContentProvider(eq(contentResolver), anyDouble(), anyDouble(), anyDouble(), anyDouble());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByMarketEntryDateRangeContentProvider(eq(contentResolver), anyLong(), anyLong());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesByMarketEntryDateMinContentProvider(eq(contentResolver), anyLong());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesBySoldDateRangeContentProvider(eq(contentResolver), anyLong(), anyLong());
        doReturn(properties).when(spyPropertySearchViewModel).getPropertiesBySoldDateMinContentProvider(eq(contentResolver), anyLong());
        doReturn(medias).when(spyPropertySearchViewModel).getMediasByPropertyIdCountRangeContentProvider(eq(contentResolver), anyInt(), anyInt());
        doReturn(medias).when(spyPropertySearchViewModel).getMediasByPropertyIdCountMinContentProvider(eq(contentResolver), anyInt());
    }

    @Test
    public void getPropertyById() {
        long propertyId = 1;
        mPropertySearchViewModel.getPropertyById(contentResolver, propertyId);

        verify(mPropertySearchRepository).getPropertyByIdContentProvider(contentResolver, propertyId);
    }

    @Test
    public void getPropertiesByPropertyTypeContentProvider() {
        String type = "myType";
        mPropertySearchViewModel.getPropertiesByPropertyTypeContentProvider(contentResolver, type);

        verify(mPropertySearchRepository).getPropertiesByPropertyTypeContentProvider(contentResolver, type);
    }

    @Test
    public void getPropertiesByPriceRangeContentProvider() {
        int priceMin = 1;
        int priceMax = 2;
        mPropertySearchViewModel.getPropertiesByPriceRangeContentProvider(contentResolver, priceMin, priceMax);

        verify(mPropertySearchRepository).getPropertiesByPriceRangeContentProvider(contentResolver, priceMin, priceMax);
    }

    @Test
    public void getPropertiesByPriceMinContentProvider() {
        int priceMin = 1;
        mPropertySearchViewModel.getPropertiesByPriceMinContentProvider(contentResolver, priceMin);

        verify(mPropertySearchRepository).getPropertiesByPriceMinContentProvider(contentResolver, priceMin);
    }

    @Test
    public void getPropertiesBySurfaceRangeContentProvider() {
        int surfaceMin = 1;
        int surfaceMax = 2;
        mPropertySearchViewModel.getPropertiesBySurfaceRangeContentProvider(contentResolver, surfaceMin, surfaceMax);

        verify(mPropertySearchRepository).getPropertiesBySurfaceRangeContentProvider(contentResolver, surfaceMin, surfaceMax);
    }

    @Test
    public void getPropertiesBySurfaceMinContentProvider() {
        int surfaceMin = 1;
        mPropertySearchViewModel.getPropertiesBySurfaceMinContentProvider(contentResolver, surfaceMin);

        verify(mPropertySearchRepository).getPropertiesBySurfaceMinContentProvider(contentResolver, surfaceMin);
    }

    @Test
    public void getPropertiesByRoomsRangeContentProvider() {
        int roomsMin = 1;
        int roomsMax = 2;
        mPropertySearchViewModel.getPropertiesByRoomsRangeContentProvider(contentResolver, roomsMin, roomsMax);

        verify(mPropertySearchRepository).getPropertiesByRoomsRangeContentProvider(contentResolver, roomsMin, roomsMax);
    }

    @Test
    public void getPropertiesByRoomsMinContentProvider() {
        int roomsMin = 1;
        mPropertySearchViewModel.getPropertiesByRoomsMinContentProvider(contentResolver, roomsMin);

        verify(mPropertySearchRepository).getPropertiesByRoomsMinContentProvider(contentResolver, roomsMin);
    }

    @Test
    public void getPropertiesByBathroomsRangeContentProvider() {
        int bathroomsMin = 1;
        int bathroomsMax = 2;
        mPropertySearchViewModel.getPropertiesByBathroomsRangeContentProvider(contentResolver, bathroomsMin, bathroomsMax);

        verify(mPropertySearchRepository).getPropertiesByBathroomsRangeContentProvider(contentResolver, bathroomsMin, bathroomsMax);
    }

    @Test
    public void getPropertiesByBathroomsMinContentProvider() {
        int bathroomsMin = 1;
        mPropertySearchViewModel.getPropertiesByBathroomsMinContentProvider(contentResolver, bathroomsMin);

        verify(mPropertySearchRepository).getPropertiesByBathroomsMinContentProvider(contentResolver, bathroomsMin);
    }

    @Test
    public void getPropertiesByBedroomsRangeContentProvider() {
        int bedroomsMin = 1;
        int bedroomsMax = 2;
        mPropertySearchViewModel.getPropertiesByBedroomsRangeContentProvider(contentResolver, bedroomsMin, bedroomsMax);

        verify(mPropertySearchRepository).getPropertiesByBedroomsRangeContentProvider(contentResolver, bedroomsMin, bedroomsMax);
    }

    @Test
    public void getPropertiesByBedroomsMinContentProvider() {
        int bedroomsMin = 1;
        mPropertySearchViewModel.getPropertiesByBedroomsMinContentProvider(contentResolver, bedroomsMin);

        verify(mPropertySearchRepository).getPropertiesByBedroomsMinContentProvider(contentResolver, bedroomsMin);
    }

    @Test
    public void getPropertiesInRadiusContentProvider() {
        double lat1 = 1.1;
        double lng1 = 2.2;
        double lat2 = 3.3;
        double lng2 = 4.4;
        mPropertySearchViewModel.getPropertiesInRadiusContentProvider(contentResolver, lat1, lng1, lat2, lng2);

        verify(mPropertySearchRepository).getPropertiesInRadiusContentProvider(contentResolver, lat1, lng1, lat2, lng2);
    }

    @Test
    public void getPropertiesIdsForAPlaceTypeContentProvider() {
        String placeType = "myPlaceType";
        mPropertySearchViewModel.getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, placeType);

        verify(mPropertySearchRepository).getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, placeType);
    }

    @Test
    public void getPropertiesByMarketEntryDateRangeContentProvider() {
        long marketEntryDateMin = 1;
        long marketEntryDateMax = 2;
        mPropertySearchViewModel.getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, marketEntryDateMin, marketEntryDateMax);

        verify(mPropertySearchRepository).getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, marketEntryDateMin, marketEntryDateMax);
    }

    @Test
    public void getPropertiesByMarketEntryDateMinContentProvider() {
        long marketEntryDateMin = 1;
        mPropertySearchViewModel.getPropertiesByMarketEntryDateMinContentProvider(contentResolver, marketEntryDateMin);

        verify(mPropertySearchRepository).getPropertiesByMarketEntryDateMinContentProvider(contentResolver, marketEntryDateMin);
    }

    @Test
    public void getPropertiesBySoldDateRangeContentProvider() {
        long soldDateMin = 1;
        long soldDateMax = 2;
        mPropertySearchViewModel.getPropertiesBySoldDateRangeContentProvider(contentResolver, soldDateMin, soldDateMax);

        verify(mPropertySearchRepository).getPropertiesBySoldDateRangeContentProvider(contentResolver, soldDateMin, soldDateMax);
    }

    @Test
    public void getPropertiesBySoldDateMinContentProvider() {
        long soldDateMin = 1;
        mPropertySearchViewModel.getPropertiesBySoldDateMinContentProvider(contentResolver, soldDateMin);

        verify(mPropertySearchRepository).getPropertiesBySoldDateMinContentProvider(contentResolver, soldDateMin);
    }

    @Test
    public void getMediasByPropertyIdCountRangeContentProvider() {
        int mediaCountMin = 1;
        int mediaCountMax = 2;
        mPropertySearchViewModel.getMediasByPropertyIdCountRangeContentProvider(contentResolver, mediaCountMin, mediaCountMax);

        verify(mPropertySearchRepository).getMediasByPropertyIdCountRangeContentProvider(contentResolver, mediaCountMin, mediaCountMax);
    }

    @Test
    public void getMediasByPropertyIdCountMinContentProvider() {
        int mediaCountMin = 1;
        mPropertySearchViewModel.getMediasByPropertyIdCountMinContentProvider(contentResolver, mediaCountMin);

        verify(mPropertySearchRepository).getMediasByPropertyIdCountMinContentProvider(contentResolver, mediaCountMin);
    }

    @Captor
    ArgumentCaptor<SearchCriteria> searchCriteriaCaptor;

    @Test
    public void searchDataProcess() {
        Place place = new Place("1", "a", 1.1, 2.2, "abc", "type");
        int radius = 100;

        LatLng center = new LatLng(place.getLatitude(), place.getLongitude());
        LatLngBounds b = Utils.generateBounds(center, radius);

        String type = "myType";
        int priceMin = 1;
        int priceMax = 2;
        int surfaceMin = 1;
        int surfaceMax = 2;
        int roomsMin = 1;
        int roomsMax = 2;
        int bathroomsMin = 1;
        int bathroomsMax = 2;
        int bedroomsMin = 1;
        int bedroomsMax = 2;
        String placeType = "myPlaceType";
        ArrayList<String> placeTypes = new ArrayList<>();
        placeTypes.add(placeType);
        long marketEntryDateMin = 39600000;
        long marketEntryDateMax = 39600000;
        long soldDateMin = 39600000;
        long soldDateMax = 39600000;
        int mediaCountMin = 1;
        int mediaCountMax = 2;

        CharSequence priceMinChar = String.valueOf(priceMin);
        CharSequence priceMaxChar = String.valueOf(priceMax);
        CharSequence surfaceMinChar = String.valueOf(surfaceMin);
        CharSequence surfaceMaxChar = String.valueOf(surfaceMax);
        CharSequence roomsMinChar = String.valueOf(roomsMin);
        CharSequence roomsMaxChar = String.valueOf(roomsMax);
        CharSequence bathroomsMinChar = String.valueOf(bathroomsMin);
        CharSequence bathroomsMaxChar = String.valueOf(bathroomsMax);
        CharSequence bedroomsMinChar = String.valueOf(bedroomsMin);
        CharSequence bedroomsMaxChar = String.valueOf(bedroomsMax);
        CharSequence radiusChar = String.valueOf(radius);
        Date marketEntryMin = Utils.convertLongToDate(marketEntryDateMin);
        Date marketEntryMax = Utils.convertLongToDate(marketEntryDateMax);
        Date soldMin = Utils.convertLongToDate(soldDateMin);
        Date soldMax = Utils.convertLongToDate(soldDateMax);
        CharSequence mediaCountMinChar = String.valueOf(mediaCountMin);
        CharSequence mediaCountMaxChar = String.valueOf(mediaCountMax);

        SearchCriteria searchCriteriaExpected = new SearchCriteria(type, priceMin, priceMax, surfaceMin, surfaceMax, roomsMin, roomsMax, bathroomsMin, bathroomsMax, bedroomsMin, bedroomsMax, b, placeTypes, marketEntryDateMin, marketEntryDateMax, soldDateMin, soldDateMax, mediaCountMin, mediaCountMax);

        spyPropertySearchViewModel.searchDataProcess(contentResolver, type, priceMinChar, priceMaxChar, surfaceMinChar, surfaceMaxChar, roomsMinChar, roomsMaxChar, bathroomsMinChar, bathroomsMaxChar, bedroomsMinChar, bedroomsMaxChar, place, radiusChar, placeTypes, marketEntryMin, marketEntryMax, soldMin, soldMax, mediaCountMinChar, mediaCountMaxChar);

        verify(spyPropertySearchViewModel).searchProperty(eq(contentResolver), searchCriteriaCaptor.capture());

        SearchCriteria searchCriteriaActual = searchCriteriaCaptor.getValue();

        assertEquals(searchCriteriaExpected, searchCriteriaActual);


    }

    @Test
    public void isCharSequenceNotNullOrEmpty() {
        CharSequence charSequence = null;

        boolean booleanExpected = false;
        boolean booleanActual = mPropertySearchViewModel.isCharSequenceNotNullOrEmpty(charSequence);

        assertEquals(booleanExpected, booleanActual);

        charSequence = "";

        booleanActual = mPropertySearchViewModel.isCharSequenceNotNullOrEmpty(charSequence);

        assertEquals(booleanExpected, booleanActual);

        charSequence = "a";

        booleanExpected = true;
        booleanActual = mPropertySearchViewModel.isCharSequenceNotNullOrEmpty(charSequence);

        assertEquals(booleanExpected, booleanActual);
    }

    @Test
    public void searchProperty() throws InterruptedException {
        LatLngBounds b = new LatLngBounds(new LatLng(1.1, 2.2), new LatLng(3.3, 4.4));
        String type = "myType";
        int priceMin = 1;
        int priceMax = 2;
        int surfaceMin = 1;
        int surfaceMax = 2;
        int roomsMin = 1;
        int roomsMax = 2;
        int bathroomsMin = 1;
        int bathroomsMax = 2;
        int bedroomsMin = 1;
        int bedroomsMax = 2;
        String placeType = "myPlaceType";
        ArrayList<String> placeTypes = new ArrayList<>();
        placeTypes.add(placeType);
        long marketEntryDateMin = 1;
        long marketEntryDateMax = 2;
        long soldDateMin = 1;
        long soldDateMax = 2;
        int mediaCountMin = 1;
        int mediaCountMax = 2;

        SearchCriteria searchCriteria = new SearchCriteria(type, priceMin, priceMax, surfaceMin, surfaceMax, roomsMin, roomsMax, bathroomsMin, bathroomsMax, bedroomsMin, bedroomsMax, b, placeTypes, marketEntryDateMin, marketEntryDateMax, soldDateMin, soldDateMax, mediaCountMin, mediaCountMax);

        doReturn(property).when(spyPropertySearchViewModel).getPropertyById(contentResolver, property.getId());

        spyPropertySearchViewModel.searchProperty(contentResolver, searchCriteria);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        List<Property> propertiesExpected = Collections.singletonList(property);

        TestObserver.test(spyPropertySearchViewModel.getPropertySearchResultsLiveData())
                .awaitValue()
                .assertValue(propertiesExpected);
    }

    @Test
    public void searchPropertyMin() throws InterruptedException {
        LatLngBounds b = new LatLngBounds(new LatLng(1.1, 2.2), new LatLng(3.3, 4.4));
        String type = "myType";
        int priceMin = 1;
        Integer priceMax = null;
        int surfaceMin = 1;
        Integer surfaceMax = null;
        int roomsMin = 1;
        Integer roomsMax = null;
        int bathroomsMin = 1;
        Integer bathroomsMax = null;
        int bedroomsMin = 1;
        Integer bedroomsMax = null;
        String placeType = "myPlaceType";
        ArrayList<String> placeTypes = new ArrayList<>();
        placeTypes.add(placeType);
        long marketEntryDateMin = 1;
        Long marketEntryDateMax = null;
        long soldDateMin = 1;
        Long soldDateMax = null;
        int mediaCountMin = 1;
        Integer mediaCountMax = null;

        SearchCriteria searchCriteria = new SearchCriteria(type, priceMin, priceMax, surfaceMin, surfaceMax, roomsMin, roomsMax, bathroomsMin, bathroomsMax, bedroomsMin, bedroomsMax, b, placeTypes, marketEntryDateMin, marketEntryDateMax, soldDateMin, soldDateMax, mediaCountMin, mediaCountMax);

        doReturn(property).when(spyPropertySearchViewModel).getPropertyById(contentResolver, property.getId());

        spyPropertySearchViewModel.searchProperty(contentResolver, searchCriteria);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        List<Property> propertiesExpected = Collections.singletonList(property);

        TestObserver.test(spyPropertySearchViewModel.getPropertySearchResultsLiveData())
                .awaitValue()
                .assertValue(propertiesExpected);
    }

    @Test
    public void getIdListFromPropertyList() {
        List<Property> properties = Arrays.asList(property, property);

        ArrayList<Long> idsExpected = new ArrayList<>();
        idsExpected.add(property.getId());
        ArrayList<Long> idsActual = mPropertySearchViewModel.getIdListFromPropertyList(properties);

        assertEquals(idsExpected, idsActual);
    }

    @Test
    public void getPropertyIdListFromMediaList() {
        List<Media> medias = Arrays.asList(media, media);

        ArrayList<Long> idsExpected = new ArrayList<>();
        idsExpected.add(media.getPropertyId());
        ArrayList<Long> idsActual = mPropertySearchViewModel.getPropertyIdListFromMediaList(medias);

        assertEquals(idsExpected, idsActual);
    }

    @Test
    public void getCommonIdsFromIdLists() {
        ArrayList<Long> ids1 = new ArrayList<>(Arrays.asList(1L,2L,3L,4L,5L));
        ArrayList<Long> ids2 = new ArrayList<>(Arrays.asList(2L,3L,4L,5L));
        ArrayList<Long> ids3 = new ArrayList<>(Arrays.asList(1L,2L,5L));
        ArrayList<Long> ids4 = new ArrayList<>(Arrays.asList(1L,2L,3L,4L,5L));
        ArrayList<ArrayList<Long>> arraysOfIds = new ArrayList<>();
        arraysOfIds.add(ids1);
        arraysOfIds.add(ids2);
        arraysOfIds.add(ids3);
        arraysOfIds.add(ids4);

        ArrayList<Long> idsExpected = new ArrayList<>(Arrays.asList(2L,5L));
        ArrayList<Long> idsActual = mPropertySearchViewModel.getCommonIdsFromIdLists(arraysOfIds);

        assertEquals(idsExpected, idsActual);
    }
}