package com.openclassrooms.realestatemanager.ui.viewmodels;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.jraska.livedata.TestObserver;
import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.PropertyAndImage;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyPlace;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.utils.FileManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class PropertyViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    PropertyViewModel mPropertyViewModel;
    @Mock
    PropertyRepository mPropertyRepository;
    @Mock
    Executor executor;
    @Captor
    ArgumentCaptor<Runnable> runnable;
    @Mock
    PropertyViewModel spyPropertyViewModel;
    @Mock
    ContentResolver contentResolver;
    @Mock
    Cursor cursor;

    private static MockedStatic<Property> propertyClass;
    private static MockedStatic<Media> mediaClass;
    private static MockedStatic<PropertyPlace> propertyPlaceClass;
    private static MockedStatic<Place> placeClass;
    private static MockedStatic<Uri> uriClass;
    private static MockedStatic<FileManager> fileManagerClass;

    @Before
    public void setUp() throws Exception {
        mPropertyViewModel = new PropertyViewModel(mPropertyRepository, executor);
        spyPropertyViewModel = spy(mPropertyViewModel);
    }

    @BeforeClass
    public static void init() {
        propertyClass = mockStatic(Property.class);
        mediaClass = mockStatic(Media.class);
        propertyPlaceClass = mockStatic(PropertyPlace.class);
        placeClass = mockStatic(Place.class);
        uriClass = mockStatic(Uri.class);
        fileManagerClass = mockStatic(FileManager.class);
    }

    @AfterClass
    public static void close() {
        propertyClass.close();
        mediaClass.close();
        propertyPlaceClass.close();
        placeClass.close();
        uriClass.close();
        fileManagerClass.close();
    }

    @Test
    public void getPropertyInListFromId() {
        long propertyOtherId = 2;
        long propertyExpectedId = 1;
        Property propertyOther = new Property();
        propertyOther.setId(propertyOtherId);
        Property propertyExpected = new Property();
        propertyExpected.setId(propertyExpectedId);

        ArrayList<Property> properties = new ArrayList<>();
        properties.add(propertyOther);
        properties.add(propertyExpected);

        Property propertyActual = mPropertyViewModel.getPropertyInListFromId(properties, propertyExpectedId);

        assertEquals(propertyExpected, propertyActual);
    }

    @Test
    public void updateProperty() throws InterruptedException {
        Property property = new Property();
        property.setId(1);
        mPropertyViewModel.updateProperty(property);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        verify(mPropertyRepository).updateProperty(property);
    }

    @Mock
    Uri uri1;
    @Mock
    Uri uri2;


    @Test
    public void assemblePropertyAndMedia() {
        long property1Id = 1;
        long property2Id = 2;
        Property property1 = new Property();
        property1.setId(property1Id);
        Property property2 = new Property();
        property2.setId(property2Id);
        Media media1 = new Media("uri1", "name1");
        Media media2 = new Media("uri2", "name2");
        List<PropertyAndImage> propertyAndImagesExpected = Arrays.asList(new PropertyAndImage(property1, uri1), new PropertyAndImage(property2, uri2));
        List<Property> properties = Arrays.asList(property1, property2);
        List<Media> medias = Arrays.asList(media1, media2);

        when(spyPropertyViewModel.getFirstImageOfProperty(property1.getId(), medias)).thenReturn(uri1);
        when(spyPropertyViewModel.getFirstImageOfProperty(property2.getId(), medias)).thenReturn(uri2);

        List<PropertyAndImage> propertyAndImagesActual = spyPropertyViewModel.assemblePropertyAndMedia(properties, medias);

        assertFalse(propertyAndImagesActual.isEmpty());
        assertEquals(propertyAndImagesExpected, propertyAndImagesActual);
    }

    @Test
    public void getFirstImageOfProperty() {
        long property1Id = 1;
        long property2Id = 2;
        Media media1 = new Media("uri1", "name1");
        media1.setPropertyId(property1Id);
        Media media2 = new Media("uri2", "name2");
        media2.setPropertyId(property2Id);
        List<Media> medias = Arrays.asList(media1, media2);
        Uri uriExpected = uri1;

        uriClass.when(() -> Uri.parse(media2.getMedia_uri())).thenReturn(uri1);
        Uri uriActual = mPropertyViewModel.getFirstImageOfProperty(property2Id, medias);
        uriClass.verify(() -> Uri.parse(media2.getMedia_uri()));

        assertEquals(uriExpected, uriActual);
    }

    @Captor
    ArgumentCaptor<PropertyPlace> propertyPlaceArgumentCaptor;
    @Captor
    ArgumentCaptor<ArrayList<Media>> mediasArgumentCaptor;
    @Captor
    ArgumentCaptor<Property> propertyArgumentCaptor;

    @Test
    public void insertPropertyAndMediasAndPlaces() throws InterruptedException {
        long propertyIdExpected = 5;
        Property propertyExpected = new Property();
        propertyExpected.setProperty_type("myType");
        Media media1 = new Media("uri1", "name1");
        Media media2 = new Media("uri2", "name2");
        ArrayList<Media> medias = new ArrayList<>();
        medias.add(media1);
        medias.add(media2);
        Place place1 = new Place("abc", "name", 1.1, 2.2, "address", "myPlaceType");
        Place place2 = new Place("abcd", "name2", 1.1, 2.2, "address", "myPlaceType");
        ArrayList<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);

        media1.setPropertyId(propertyIdExpected);
        media2.setPropertyId(propertyIdExpected);
        ArrayList<Media> mediasExpected = new ArrayList<>();
        mediasExpected.add(media1);
        mediasExpected.add(media2);
        PropertyPlace propertyPlace1Expected = new PropertyPlace(place1.getId(), propertyIdExpected);
        PropertyPlace propertyPlace2Expected = new PropertyPlace(place2.getId(), propertyIdExpected);

        when(mPropertyRepository.insertProperty(propertyExpected)).thenReturn(propertyIdExpected);

        mPropertyViewModel.insertPropertyAndMediasAndPlaces(propertyExpected, medias, places);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();
        verify(mPropertyRepository).insertProperty(propertyArgumentCaptor.capture());
        verify(mPropertyRepository).insertMultipleMedias(mediasArgumentCaptor.capture());
        verify(mPropertyRepository, times(2)).insertPropertyPlace(propertyPlaceArgumentCaptor.capture());

        Property propertyActual = propertyArgumentCaptor.getValue();
        assertEquals(propertyExpected, propertyActual);

        ArrayList<Media> mediasActual = mediasArgumentCaptor.getValue();
        assertEquals(mediasExpected, mediasActual);

        List<PropertyPlace> propertyPlacesArguments = propertyPlaceArgumentCaptor.getAllValues();
        PropertyPlace propertyPlace1Actual = propertyPlacesArguments.get(0);
        PropertyPlace propertyPlace2Actual = propertyPlacesArguments.get(1);
        assertEquals(propertyPlace1Expected, propertyPlace1Actual);
        assertEquals(propertyPlace2Expected, propertyPlace2Actual);

        LiveData<Long> propertyIdLiveData = mPropertyViewModel.getPropertyCreatedIdLiveData();

        TestObserver.test(propertyIdLiveData)
                .awaitValue()
                .assertValue(propertyIdExpected);
    }

    @Test
    public void insertMultipleMedias() {
        long propertyId = 6;
        Media media1 = new Media("uri1", "name1");
        Media media2 = new Media("uri2", "name2");
        ArrayList<Media> medias = new ArrayList<>();
        medias.add(media1);
        medias.add(media2);
        Media media3 = new Media("uri1", "name1");
        Media media4 = new Media("uri2", "name2");
        media3.setPropertyId(propertyId);
        media4.setPropertyId(propertyId);
        ArrayList<Media> mediasExpected = new ArrayList<>();
        mediasExpected.add(media3);
        mediasExpected.add(media4);

        mPropertyViewModel.insertMultipleMedias(medias, propertyId);

        verify(mPropertyRepository).insertMultipleMedias(mediasArgumentCaptor.capture());

        ArrayList<Media> mediasActual = mediasArgumentCaptor.getValue();

        assertEquals(mediasExpected, mediasActual);
    }

    @Test
    public void getPropertiesContentProvider() throws InterruptedException {
        Property property1 = new Property();
        property1.setId(1);
        Property property2 = new Property();
        property2.setId(2);
        List<Property> propertiesExpected = Arrays.asList(property1, property2);

        when(mPropertyRepository.getPropertiesContentProvider(contentResolver)).thenReturn(cursor);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);
        propertyClass.when(() -> Property.fromCursor(cursor))
                .thenReturn(property1)
                .thenReturn(property2);

        mPropertyViewModel.getPropertiesContentProvider(contentResolver);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        TestObserver.test(mPropertyViewModel.getPropertiesLiveData())
                .awaitValue()
                .assertValue(propertiesExpected);
    }

    @Test
    public void getPropertyByIdContentProvider() throws InterruptedException {
        long propertyId = 6;
        Property propertyExpected = new Property();
        propertyExpected.setId(propertyId);
        when(mPropertyRepository.getPropertyByIdContentProvider(contentResolver, propertyId)).thenReturn(propertyExpected);
        mPropertyViewModel.getPropertyByIdContentProvider(contentResolver, propertyId);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        TestObserver.test(mPropertyViewModel.getPropertyByIdLiveData())
                .awaitValue()
                .assertValue(propertyExpected);
    }

    @Test
    public void getMediasByPropertyIdContentProvider() throws InterruptedException {
        long mediaId = 7;
        Media media1 = new Media();
        Media media2 = new Media();
        media1.setId(mediaId);
        media2.setId(mediaId);
        List<Media> mediasExpected = Arrays.asList(media1, media2);
        when(mPropertyRepository.getMediasByPropertyIdContentProvider(contentResolver, mediaId)).thenReturn(mediasExpected);
        mPropertyViewModel.getMediasByPropertyIdContentProvider(contentResolver, mediaId);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        TestObserver.test(mPropertyViewModel.getMediasByPropertyIdLiveData())
                .awaitValue()
                .assertValue(mediasExpected);
    }

    @Test
    public void getAllMediasContentProvider() throws InterruptedException {
        long mediaId1 = 7;
        long mediaId2 = 8;
        Media media1 = new Media();
        Media media2 = new Media();
        media1.setId(mediaId1);
        media2.setId(mediaId2);
        List<Media> mediasExpected = Arrays.asList(media1, media2);

        when(mPropertyRepository.getAllMediasContentProvider(contentResolver)).thenReturn(cursor);
        mediaClass.when(() -> Media.fromCursor(cursor))
                .thenReturn(media1)
                .thenReturn(media2);
        when(cursor.moveToFirst())
                .thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);

        mPropertyViewModel.getAllMediasContentProvider(contentResolver);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        TestObserver.test(mPropertyViewModel.getAllMediasLiveData())
                .awaitValue()
                .assertValue(mediasExpected);
    }

    @Test
    public void getPlacesByPropertyIdContentProvider() throws InterruptedException {
        long propertyId = 7;
        String placeId1 = "a";
        String placeId2 = "b";
        PropertyPlace propertyPlace1 = new PropertyPlace(placeId1, propertyId);
        PropertyPlace propertyPlace2 = new PropertyPlace(placeId2, propertyId);
        Place place1 = new Place();
        Place place2 = new Place();
        place1.setId(placeId1);
        place2.setId(placeId2);
        List<Place> placesExpected = Arrays.asList(place1, place2);

        when(mPropertyRepository.getPropertyPlacesByPropertyIdContentProvider(contentResolver, propertyId)).thenReturn(cursor);
        when(cursor.moveToFirst())
                .thenReturn(true);
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(false);
        propertyPlaceClass.when(() -> PropertyPlace.fromCursor(cursor))
                .thenReturn(propertyPlace1)
                .thenReturn(propertyPlace2);
        doReturn(place1).when(spyPropertyViewModel).getPlaceByPlaceIdContentProvider(contentResolver, propertyPlace1.getPlace_id());
        doReturn(place2).when(spyPropertyViewModel).getPlaceByPlaceIdContentProvider(contentResolver, propertyPlace2.getPlace_id());

        spyPropertyViewModel.getPlacesByPropertyIdContentProvider(contentResolver, propertyId);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        TestObserver.test(spyPropertyViewModel.getPlacesLiveData())
                .awaitValue()
                .assertValue(placesExpected);
    }

    @Test
    public void getPlaceByPlaceIdContentProvider() {
        String placeId = "abc";
        Place placeExpected = new Place();
        placeExpected.setId(placeId);

        when(mPropertyRepository.getPlaceByPlaceIdContentProvider(contentResolver, placeId)).thenReturn(cursor);
        when(cursor.moveToFirst())
                .thenReturn(true);
        placeClass.when(() -> Place.fromCursor(cursor))
                .thenReturn(placeExpected);

        Place placeActual = mPropertyViewModel.getPlaceByPlaceIdContentProvider(contentResolver, placeId);

        assertEquals(placeExpected, placeActual);
    }

    @Test
    public void updatePropertyAndContent() throws InterruptedException {
        long propertyId = 6;
        Property property = new Property();
        property.setId(propertyId);
        Media media = new Media("uri1", "name1");
        ArrayList<Media> medias = new ArrayList<>();
        medias.add(media);

        spyPropertyViewModel.updatePropertyAndContent(contentResolver, property, medias);

        verify(executor).execute(runnable.capture());
        Runnable capturedRunnable = runnable.getValue();
        capturedRunnable.run();

        verify(spyPropertyViewModel).updateProperty(property);
        verify(spyPropertyViewModel).deleteMediasFromPropertyId(contentResolver, propertyId);
        verify(spyPropertyViewModel).insertMultipleMedias(medias, propertyId);

        TestObserver.test(spyPropertyViewModel.getPropertyUpdateLiveData())
                .awaitValue()
                .assertValue(true);
    }

    @Test
    public void deleteMediasFromPropertyId() {
        long propertyId = 7;
        long mediaId = 8;
        Media media = new Media("uri1", "name1");
        media.setId(mediaId);
        ArrayList<Media> medias = new ArrayList<>();
        medias.add(media);

        when(mPropertyRepository.getMediasByPropertyIdContentProvider(contentResolver, propertyId)).thenReturn(medias);
        uriClass.when(() -> Uri.parse(media.getMedia_uri()))
                .thenReturn(uri1);

        mPropertyViewModel.deleteMediasFromPropertyId(contentResolver, propertyId);

        fileManagerClass.verify(() -> FileManager.deleteFileFromUri(uri1));
        verify(mPropertyRepository).deleteMediaByMediaId(media.getId());
    }
}