package com.openclassrooms.realestatemanager.data;

import com.openclassrooms.realestatemanager.data.daos.MediaDao;
import com.openclassrooms.realestatemanager.data.daos.PlaceDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyDao;
import com.openclassrooms.realestatemanager.data.daos.PropertyPlaceDao;
import com.openclassrooms.realestatemanager.data.model.entities.Property;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

    @Before
    public void setUp() throws Exception {
        mPropertyRepository = new PropertyRepository(propertyDao, mediaDao, placeDao, propertyPlaceDao);
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
}