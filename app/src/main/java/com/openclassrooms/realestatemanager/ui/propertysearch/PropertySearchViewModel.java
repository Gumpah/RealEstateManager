package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.content.ContentResolver;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.entities.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertySearchViewModel extends ViewModel {

    private PropertyRepository mPropertyRepository;
    private Executor mExecutor;
    private MutableLiveData<List<Property>> propertySearchResults;

    public PropertySearchViewModel(PropertyRepository propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
        propertySearchResults = new MutableLiveData<>();
    }

    public Property getPropertyById(long propertyId) { return mPropertyRepository.getPropertyById(propertyId); }
    public MutableLiveData<List<Property>> getPropertySearchResultsLiveData() { return propertySearchResults; }

    public List<Property> getPropertiesByPropertyType(String propertyType) {
        return mPropertyRepository.getPropertiesByPropertyType(propertyType);
    }

    public List<Property> getPropertiesByPriceRange(Integer priceMin, Integer priceMax) {
        return mPropertyRepository.getPropertiesByPriceRange(priceMin, priceMax);
    }

    public List<Property> getPropertiesByPriceMin(Integer priceMin) {
        return mPropertyRepository.getPropertiesByPriceMin(priceMin);
    }

    public List<Property> getPropertiesBySurfaceRange(int surfaceMin, int surfaceMax) {
        return mPropertyRepository.getPropertiesBySurfaceRange(surfaceMin, surfaceMax);
    }

    public List<Property> getPropertiesBySurfaceMin(int surfaceMin) {
        return mPropertyRepository.getPropertiesBySurfaceMin(surfaceMin);
    }

    public List<Property> getPropertiesByRoomsRange(int roomsMin, int roomsMax) {
        return mPropertyRepository.getPropertiesByRoomsRange(roomsMin, roomsMax);
    }

    public List<Property> getPropertiesByRoomsMin(int roomsMin) {
        return mPropertyRepository.getPropertiesByRoomsMin(roomsMin);
    }

    public List<Property> getPropertiesByBathroomsMin(int bathroomsMin) {
        return mPropertyRepository.getPropertiesByBathroomsMin(bathroomsMin);
    }

    public List<Property> getPropertiesByBathroomsRange(int bathroomsMin, int bathroomsMax) {
        return mPropertyRepository.getPropertiesByBathroomsRange(bathroomsMin, bathroomsMax);
    }

    public List<Property> getPropertiesByBedroomsMin(int bedroomsMin) {
        return mPropertyRepository.getPropertiesByBedroomsMin(bedroomsMin);
    }

    public List<Property> getPropertiesByBedroomsRange(int bedroomsMin, int bedroomsMax) {
        return mPropertyRepository.getPropertiesByBedroomsRange(bedroomsMin, bedroomsMax);
    }

    public List<Property> getPropertiesInRadius(Double lat1, Double lng1, Double lat2, Double lng2) {
        return mPropertyRepository.getPropertiesInRadius(lat1, lng1, lat2, lng2);
    }

    public ArrayList<Long> getPropertiesIdsForAPlaceType(String placeType) {
        return mPropertyRepository.getPropertiesIdsForAPlaceType(placeType);
    }

    public List<Property> getPropertiesByMarketEntryDateRange(long marketEntryMin, long marketEntryMax) {
        return mPropertyRepository.getPropertiesByMarketEntryDateRange(marketEntryMin, marketEntryMax);
    }

    public List<Property> getPropertiesByMarketEntryDateMin(long marketEntryMin) {
        return mPropertyRepository.getPropertiesByMarketEntryDateMin(marketEntryMin);
    }

    public List<Property> getPropertiesBySoldDateRange(long soldMin, long soldMax) {
        return mPropertyRepository.getPropertiesBySoldDateRange(soldMin, soldMax);
    }

    public List<Property> getPropertiesBySoldDateMin(long soldMin) {
        return mPropertyRepository.getPropertiesBySoldDateMin(soldMin);
    }

    public void searchProperty(ContentResolver contentResolver, String propertyType, Integer priceMin, Integer priceMax, Integer surfaceMin, Integer surfaceMax, Integer roomsMin, Integer roomsMax, Integer bathroomsMin, Integer bathroomsMax, Integer bedroomsMin, Integer bedroomsMax, LatLngBounds bounds, ArrayList<String> placesTypes, Long marketEntryDateMin, Long marketEntryDateMax, Long soldDateMin, Long soldDateMax) {
        mExecutor.execute(() -> {
            ArrayList<ArrayList<Long>> arrayOfIdsArrays = new ArrayList<>();
            if (propertyType != null) {
                ArrayList<Long> propertiesIds = getIdListFromPropertyList(getPropertiesByPropertyType(propertyType));
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (priceMin != null || priceMax != null) {
                ArrayList<Long> propertiesIds;
                if (priceMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceRange(0, priceMax));
                } else if (priceMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceMin(priceMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceRange(priceMin, priceMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (surfaceMin != null || surfaceMax != null) {
                ArrayList<Long> propertiesIds;
                if (surfaceMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceRange(0, surfaceMax));
                } else if (surfaceMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceMin(surfaceMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceRange(surfaceMin, surfaceMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (roomsMin != null || roomsMax != null) {
                ArrayList<Long> propertiesIds;
                if (roomsMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsRange(0, roomsMax));
                } else if (roomsMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsMin(roomsMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsRange(roomsMin, roomsMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (bathroomsMin != null || bathroomsMax != null) {
                ArrayList<Long> propertiesIds;
                if (bathroomsMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsRange(0, bathroomsMax));
                } else if (bathroomsMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsMin(bathroomsMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsRange(bathroomsMin, bathroomsMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (bedroomsMin != null || bedroomsMax != null) {
                ArrayList<Long> propertiesIds;
                if (bedroomsMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsRange(0, bedroomsMax));
                } else if (bedroomsMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsMin(bedroomsMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsRange(bedroomsMin, bedroomsMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (bounds != null) {
                ArrayList<Long> propertiesIds = getIdListFromPropertyList(getPropertiesInRadius(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude));
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (placesTypes != null && !placesTypes.isEmpty()) {
                for (String type : placesTypes) {
                    arrayOfIdsArrays.add(getPropertiesIdsForAPlaceType(type));
                }
            }
            if (marketEntryDateMin != null || marketEntryDateMax != null) {
                ArrayList<Long> propertiesIds;
                if (marketEntryDateMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateRange(0, marketEntryDateMax));
                } else if (marketEntryDateMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateMin(marketEntryDateMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateRange(marketEntryDateMin, marketEntryDateMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (soldDateMin != null || soldDateMax != null) {
                ArrayList<Long> propertiesIds;
                if (soldDateMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateRange(0, soldDateMax));
                } else if (soldDateMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateMin(soldDateMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateRange(soldDateMin, soldDateMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }

            ArrayList<Long> resultIds = getCommonIdsFromIdLists(arrayOfIdsArrays);
            ArrayList<Property> resultProperties = new ArrayList<>();
            for (Long id : resultIds) {
                resultProperties.add(getPropertyById(id));
            }
            propertySearchResults.postValue(resultProperties);
        });
    }

    private ArrayList<Long> getIdListFromPropertyList(List<Property> properties) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Property property : properties) {
            ids.add(property.getId());
        }
        return ids;
    }

    private ArrayList<Long> getCommonIdsFromIdLists(ArrayList<ArrayList<Long>> arrayOfIdsArrays) {
        int criteriaCount = arrayOfIdsArrays.size();
        ArrayList<Long> idsInCommon = new ArrayList<>();
        ArrayList<Long> idsAllNoDuplicates = new ArrayList<>();
        ArrayList<Long> idsAll = new ArrayList<>();
        for (ArrayList<Long> ids : arrayOfIdsArrays) {
            idsAll.addAll(ids);
            for (Long id : ids) {
                if (!idsAllNoDuplicates.contains(id)) idsAllNoDuplicates.add(id);
            }
        }
        for (Long id : idsAllNoDuplicates) {
            if (Collections.frequency(idsAll, id) == criteriaCount) {
                idsInCommon.add(id);
            }
        }
        return idsInCommon;
    }
}
