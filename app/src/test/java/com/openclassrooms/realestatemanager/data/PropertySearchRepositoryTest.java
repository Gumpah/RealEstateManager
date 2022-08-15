package com.openclassrooms.realestatemanager.data;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContentResolver;
import android.database.Cursor;

import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.data.provider.MyContentProvider;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PropertySearchRepositoryTest {

    private PropertySearchRepository mPropertySearchRepository;

    @Mock
    Cursor cursor;
    @Mock
    ContentResolver contentResolver;
    @Captor
    ArgumentCaptor<String[]> argumentCaptorStringList;
    @Captor
    ArgumentCaptor<String[]> argumentCaptorStringList2;
    @Mock
    PropertySearchRepository spyPropertySearchRepository;

    private static MockedStatic<Property> propertyClass;
    private static MockedStatic<Media> mediaClass;
    private static MockedStatic<PropertyPlace> propertyPlaceClass;
    private static MockedStatic<Place> placeClass;

    @Before
    public void setUp() throws Exception {
        mPropertySearchRepository = new PropertySearchRepository();
        spyPropertySearchRepository = spy(mPropertySearchRepository);

    }

    @BeforeClass
    public static void init() {
        propertyClass = mockStatic(Property.class);
        mediaClass = mockStatic(Media.class);
        propertyPlaceClass = mockStatic(PropertyPlace.class);
        placeClass = mockStatic(Place.class);
    }

    @AfterClass
    public static void close() {
        propertyClass.close();
        mediaClass.close();
        propertyPlaceClass.close();
        placeClass.close();
    }

    @Test
    public void getPropertyByIdContentProvider() {
        String[] expectedArguments = {"getPropertyById"};
        long propertyId = 6;

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertyByIdContentProvider(contentResolver, propertyId);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToProperty(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByPropertyTypeContentProvider() {
        String propertyType = "myType";
        String[] expectedArguments = {"getPropertiesByPropertyType", propertyType};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByPropertyTypeContentProvider(contentResolver, propertyType);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByPriceRangeContentProvider() {
        int priceMin = 5;
        int priceMax = 6;
        String[] expectedArguments = {"getPropertiesByPriceRange", String.valueOf(priceMin), String.valueOf(priceMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByPriceRangeContentProvider(contentResolver, priceMin, priceMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }
    @Test
    public void getPropertiesByPriceMinContentProvider() {
        int priceMin = 5;
        String[] expectedArguments = {"getPropertiesByPriceMin", String.valueOf(priceMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByPriceMinContentProvider(contentResolver, priceMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesBySurfaceRangeContentProvider() {
        int surfaceMin = 5;
        int surfaceMax = 6;
        String[] expectedArguments = {"getPropertiesBySurfaceRange", String.valueOf(surfaceMin), String.valueOf(surfaceMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesBySurfaceRangeContentProvider(contentResolver, surfaceMin, surfaceMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesBySurfaceMinContentProvider() {
        int surfaceMin = 5;
        String[] expectedArguments = {"getPropertiesBySurfaceMin", String.valueOf(surfaceMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesBySurfaceMinContentProvider(contentResolver, surfaceMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByRoomsRangeContentProvider() {
        int roomsMin = 5;
        int roomsMax = 6;
        String[] expectedArguments = {"getPropertiesByRoomsRange", String.valueOf(roomsMin), String.valueOf(roomsMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByRoomsRangeContentProvider(contentResolver, roomsMin, roomsMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByRoomsMinContentProvider() {
        int roomsMin = 5;
        String[] expectedArguments = {"getPropertiesByRoomsMin", String.valueOf(roomsMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByRoomsMinContentProvider(contentResolver, roomsMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByBathroomsRangeContentProvider() {
        int bathroomsMin = 5;
        int bathroomsMax = 6;
        String[] expectedArguments = {"getPropertiesByBathroomsRange", String.valueOf(bathroomsMin), String.valueOf(bathroomsMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByBathroomsRangeContentProvider(contentResolver, bathroomsMin, bathroomsMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByBathroomsMinContentProvider() {
        int bathroomsMin = 5;
        String[] expectedArguments = {"getPropertiesByBathroomsMin", String.valueOf(bathroomsMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByBathroomsMinContentProvider(contentResolver, bathroomsMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByBedroomsRangeContentProvider() {
        int bedroomsMin = 5;
        int bedroomsMax = 6;
        String[] expectedArguments = {"getPropertiesByBedroomsRange", String.valueOf(bedroomsMin), String.valueOf(bedroomsMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByBedroomsRangeContentProvider(contentResolver, bedroomsMin, bedroomsMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByBedroomsMinContentProvider() {
        int bedroomsMin = 5;
        String[] expectedArguments = {"getPropertiesByBedroomsMin", String.valueOf(bedroomsMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByBedroomsMinContentProvider(contentResolver, bedroomsMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesInRadiusContentProvider() {
        double lat1 = 2;
        double lng1 = 3;
        double lat2 = 5;
        double lng2 = 6;
        String[] expectedArguments = {"getPropertiesInRadius", String.valueOf(lat1), String.valueOf(lng1), String.valueOf(lat2), String.valueOf(lng2)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesInRadiusContentProvider(contentResolver, lat1, lng1, lat2, lng2);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesIdsForAPlaceTypeContentProvider() {
        String placeType = "myPlaceType";
        Place place = new Place("abc", "name", 1.1, 2.2, "address", "myPlaceType");
        List<Place> places = Collections.singletonList(place);
        PropertyPlace propertyPlace = new PropertyPlace("abc", 2);
        List<PropertyPlace> propertyPlaces = Collections.singletonList(propertyPlace);
        ArrayList<Long> propertyIdsExpected = new ArrayList<>();
        propertyIdsExpected.add(propertyPlace.getProperty_id());
        String[] expectedArguments1 = {"getPlacesByType", placeType};
        String[] expectedArguments2 = {"getPropertyPlacesByPlaceId", place.getId()};


        when(contentResolver.query(any(), eq(null), eq(null), eq(expectedArguments1), eq(null))).thenReturn(cursor);
        when(contentResolver.query(any(), eq(null), eq(null), eq(expectedArguments2), eq(null))).thenReturn(cursor);

        doReturn(places).when(spyPropertySearchRepository).cursorToPlaceList(cursor);
        doReturn(propertyPlaces).when(spyPropertySearchRepository).cursorToPropertyPlaceList(cursor);

        ArrayList<Long> propertyIdsActual = spyPropertySearchRepository.getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, placeType);

        assertEquals(propertyIdsExpected, propertyIdsActual);
    }

    @Test
    public void getPropertiesByMarketEntryDateRangeContentProvider() {
        long marketEntryMin = 1;
        long marketEntryMax = 2;
        String[] expectedArguments = {"getPropertiesByMarketEntryDateRange", String.valueOf(marketEntryMin), String.valueOf(marketEntryMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, marketEntryMin, marketEntryMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesByMarketEntryDateMinContentProvider() {
        long marketEntryMin = 1;
        String[] expectedArguments = {"getPropertiesByMarketEntryDateMin", String.valueOf(marketEntryMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesByMarketEntryDateMinContentProvider(contentResolver, marketEntryMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesBySoldDateRangeContentProvider() {
        long soldMin = 1;
        long soldMax = 2;
        String[] expectedArguments = {"getPropertiesBySoldDateRange", String.valueOf(soldMin), String.valueOf(soldMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesBySoldDateRangeContentProvider(contentResolver, soldMin, soldMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getPropertiesBySoldDateMinContentProvider() {
        long soldMin = 1;
        String[] expectedArguments = {"getPropertiesBySoldDateMin", String.valueOf(soldMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getPropertiesBySoldDateMinContentProvider(contentResolver, soldMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToPropertyList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getMediasByPropertyIdCountRangeContentProvider() {
        int mediaCountMin = 5;
        int mediaCountMax = 6;
        String[] expectedArguments = {"getMediasByPropertyIdCountRange", String.valueOf(mediaCountMin), String.valueOf(mediaCountMax)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getMediasByPropertyIdCountRangeContentProvider(contentResolver, mediaCountMin, mediaCountMax);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToMediaList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void getMediasByPropertyIdCountMinContentProvider() {
        int mediaCountMin = 5;
        String[] expectedArguments = {"getMediasByPropertyIdCountMin", String.valueOf(mediaCountMin)};

        when(contentResolver.query(any(), eq(null), eq(null), argumentCaptorStringList.capture(), eq(null))).thenReturn(cursor);
        spyPropertySearchRepository.getMediasByPropertyIdCountMinContentProvider(contentResolver, mediaCountMin);
        String[] actualArguments = argumentCaptorStringList.getValue();
        verify(spyPropertySearchRepository).cursorToMediaList(cursor);

        assertArrayEquals(expectedArguments, actualArguments);
    }

    @Test
    public void cursorToProperty() {
        Property propertyExpected = new Property(
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
        propertyExpected.setId(1);
        propertyExpected.setSold(1660298400000L);

        propertyClass.when(() -> Property.fromCursor(cursor))
                .thenReturn(propertyExpected);
        when(cursor.moveToFirst()).thenReturn(true);

        Property propertyActual = mPropertySearchRepository.cursorToProperty(cursor);

        assertEquals(propertyExpected, propertyActual);
    }

    @Test
    public void cursorToPropertyList() {
        Property property = new Property(
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
        property.setSold(1660298400000L);

        propertyClass.when(() -> Property.fromCursor(cursor))
                .thenReturn(property)
                .thenReturn(property);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);

        List<Property> propertyListExpected = Arrays.asList(property, property);
        List<Property> propertyListActual = mPropertySearchRepository.cursorToPropertyList(cursor);

        assertEquals(propertyListExpected, propertyListActual);
    }

    @Test
    public void cursorToPlaceList() {
        Place place = new Place("1", "name", 1.1, 2.2, "address", "type");

        placeClass.when(() -> Place.fromCursor(cursor))
                .thenReturn(place)
                .thenReturn(place);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);

        List<Place> placeListExpected = Arrays.asList(place, place);
        List<Place> placeListActual = mPropertySearchRepository.cursorToPlaceList(cursor);

        assertEquals(placeListExpected, placeListActual);
    }

    @Test
    public void cursorToPropertyPlaceList() {

        PropertyPlace propertyPlace = new PropertyPlace("1", 2);
        propertyPlace.setId(3);

        propertyPlaceClass.when(() -> PropertyPlace.fromCursor(cursor))
                .thenReturn(propertyPlace)
                .thenReturn(propertyPlace);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);

        List<PropertyPlace> propertyPlaceListExpected = Arrays.asList(propertyPlace, propertyPlace);
        List<PropertyPlace> propertyPlaceListActual = mPropertySearchRepository.cursorToPropertyPlaceList(cursor);

        assertEquals(propertyPlaceListExpected, propertyPlaceListActual);
    }

    @Test
    public void cursorToMediaList() {

        Media media = new Media("mediaUri", "name");
        media.setId(1);
        media.setPropertyId(2);

        mediaClass.when(() -> Media.fromCursor(cursor))
                .thenReturn(media)
                .thenReturn(media);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);

        List<Media> mediaListExpected = Arrays.asList(media, media);
        List<Media> mediaListActual = mPropertySearchRepository.cursorToMediaList(cursor);

        assertEquals(mediaListExpected, mediaListActual);
    }
}