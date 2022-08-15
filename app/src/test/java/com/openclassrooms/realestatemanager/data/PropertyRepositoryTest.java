package com.openclassrooms.realestatemanager.data;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PlaceDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyPlaceDao;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContentResolver;
import android.database.Cursor;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PropertyRepositoryTest {

    PropertyRepository mPropertyRepository;

    @Mock
    PropertyDao propertyDao;
    @Mock
    MediaDao mediaDao;
    @Mock
    PlaceDao placeDao;
    @Mock
    PropertyPlaceDao propertyPlaceDao;
    @Mock
    ContentResolver contentResolver;
    @Mock
    PropertyRepository spyPropertyRepository;
    @Mock
    Cursor cursor;

    private static MockedStatic<Property> propertyClass;
    private static MockedStatic<Media> mediaClass;

    @Before
    public void setUp() throws Exception {
        mPropertyRepository = new PropertyRepository(propertyDao, mediaDao, placeDao, propertyPlaceDao);
        spyPropertyRepository = spy(mPropertyRepository);
    }

    @BeforeClass
    public static void init() {
        propertyClass = mockStatic(Property.class);
        mediaClass = mockStatic(Media.class);
    }

    @AfterClass
    public static void close() {
        propertyClass.close();
        mediaClass.close();
    }

    @Test
    public void insertProperty() {
        Property property = new Property();
        property.setId(1);
        mPropertyRepository.insertProperty(property);

        verify(propertyDao).insertProperty(property);
    }

    @Test
    public void updateProperty() {
        Property property = new Property();
        property.setId(1);
        mPropertyRepository.updateProperty(property);

        verify(propertyDao).updateProperty(property);
    }

    @Test
    public void deleteProperty() {
        long propertyId = 1;
        mPropertyRepository.deleteProperty(propertyId);

        verify(propertyDao).deleteProperty(propertyId);
    }

    @Test
    public void insertMultipleMedias() {
        Media media = new Media("mediaUri", "name");
        List<Media> medias = Collections.singletonList(media);
        mPropertyRepository.insertMultipleMedias(medias);

        verify(mediaDao).insertMultipleMedias(medias);
    }

    @Test
    public void deleteMediaByMediaId() {
        long mediaId = 1;
        mPropertyRepository.deleteMediaByMediaId(mediaId);

        verify(mediaDao).deleteMedia(mediaId);
    }

    @Test
    public void insertPlace() {
        Place place = new Place("abc", "name", 1.1, 2.2, "address", "myPlaceType");
        mPropertyRepository.insertPlace(place);

        verify(placeDao).insertPlace(place);
    }

    @Test
    public void insertPropertyPlace() {
        PropertyPlace propertyPlace = new PropertyPlace("abc", 2);
        mPropertyRepository.insertPropertyPlace(propertyPlace);

        verify(propertyPlaceDao).insertPropertyPlace(propertyPlace);
    }

    @Test
    public void getPropertiesContentProvider() {
        String[] expectedArguments = {"getProperties"};

        mPropertyRepository.getPropertiesContentProvider(contentResolver);

        verify(contentResolver).query(any(), eq(null), eq(null), eq(expectedArguments), eq(null));
    }

    @Test
    public void getPropertyByIdContentProvider() {
        long propertyId = 1;
        String[] expectedArguments = {"getPropertyById"};
        when(contentResolver.query(any(), eq(null), eq(null), eq(expectedArguments), eq(null))).thenReturn(cursor);
        spyPropertyRepository.getPropertyByIdContentProvider(contentResolver, propertyId);

        verify(contentResolver).query(any(), eq(null), eq(null), eq(expectedArguments), eq(null));
        verify(spyPropertyRepository).cursorToProperty(cursor);
    }

    @Test
    public void getMediasByPropertyIdContentProvider() {
        long propertyId = 1;
        String[] expectedArguments = {"getMediasByPropertyId"};
        when(contentResolver.query(any(), eq(null), eq(null), eq(expectedArguments), eq(null))).thenReturn(cursor);
        spyPropertyRepository.getMediasByPropertyIdContentProvider(contentResolver, propertyId);

        verify(contentResolver).query(any(), eq(null), eq(null), eq(expectedArguments), eq(null));
        verify(spyPropertyRepository).cursorToMediaList(cursor);
    }

    @Test
    public void getAllMediasContentProvider() {
        String[] expectedArguments = {"getAllMedias"};

        mPropertyRepository.getAllMediasContentProvider(contentResolver);

        verify(contentResolver).query(any(), eq(null), eq(null), eq(expectedArguments), eq(null));
    }

    @Test
    public void getPropertyPlacesByPropertyIdContentProvider() {
        long propertyId = 1;
        String[] expectedArguments = {"getPropertyPlacesByPropertyId"};
        mPropertyRepository.getPropertyPlacesByPropertyIdContentProvider(contentResolver, propertyId);

        verify(contentResolver).query(any(), eq(null), eq(null), eq(expectedArguments), eq(null));
    }

    @Test
    public void getPlaceByPlaceIdContentProvider() {
        String placeId = "abc";
        String[] expectedArguments = {"getPlaceById", placeId};
        mPropertyRepository.getPlaceByPlaceIdContentProvider(contentResolver, placeId);

        verify(contentResolver).query(any(), eq(null), eq(null), eq(expectedArguments), eq(null));
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

        Property propertyActual = mPropertyRepository.cursorToProperty(cursor);

        assertEquals(propertyExpected, propertyActual);
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
        List<Media> mediaListActual = mPropertyRepository.cursorToMediaList(cursor);

        assertEquals(mediaListExpected, mediaListActual);
    }
}