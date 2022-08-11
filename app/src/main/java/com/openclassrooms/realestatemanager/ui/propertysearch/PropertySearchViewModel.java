package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.content.ContentResolver;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLngBounds;
import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.provider.MyContentProvider;

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

    public Property getPropertyById(ContentResolver contentResolver, long propertyId) { return mPropertyRepository.getPropertyByIdContentProvider(contentResolver, propertyId); }

    public MutableLiveData<List<Property>> getPropertySearchResultsLiveData() { return propertySearchResults; }

    public List<Property> getPropertiesByPropertyTypeContentProvider(ContentResolver contentResolver, String propertyType) {
        return mPropertyRepository.getPropertiesByPropertyTypeContentProvider(contentResolver, propertyType);
    }

    public List<Property> getPropertiesByPriceRangeContentProvider(ContentResolver contentResolver, Integer priceMin, Integer priceMax) {
        return mPropertyRepository.getPropertiesByPriceRangeContentProvider(contentResolver, priceMin, priceMax);
    }

    public List<Property> getPropertiesByPriceMinContentProvider(ContentResolver contentResolver, Integer priceMin) {
        return mPropertyRepository.getPropertiesByPriceMinContentProvider(contentResolver, priceMin);
    }

    public List<Property> getPropertiesBySurfaceRangeContentProvider(ContentResolver contentResolver, int surfaceMin, int surfaceMax) {
        return mPropertyRepository.getPropertiesBySurfaceRangeContentProvider(contentResolver, surfaceMin, surfaceMax);
    }

    public List<Property> getPropertiesBySurfaceMinContentProvider(ContentResolver contentResolver, int surfaceMin) {
        return mPropertyRepository.getPropertiesBySurfaceMinContentProvider(contentResolver, surfaceMin);
    }

    public List<Property> getPropertiesByRoomsRangeContentProvider(ContentResolver contentResolver, int roomsMin, int roomsMax) {
        return mPropertyRepository.getPropertiesByRoomsRangeContentProvider(contentResolver, roomsMin, roomsMax);
    }

    public List<Property> getPropertiesByRoomsMinContentProvider(ContentResolver contentResolver, int roomsMin) {
        return mPropertyRepository.getPropertiesByRoomsMinContentProvider(contentResolver, roomsMin);
    }

    public List<Property> getPropertiesByBathroomsMinContentProvider(ContentResolver contentResolver, int bathroomsMin) {
        return mPropertyRepository.getPropertiesByBathroomsMinContentProvider(contentResolver, bathroomsMin);
    }

    public List<Property> getPropertiesByBathroomsRangeContentProvider(ContentResolver contentResolver, int bathroomsMin, int bathroomsMax) {
        return mPropertyRepository.getPropertiesByBathroomsRangeContentProvider(contentResolver, bathroomsMin, bathroomsMax);
    }

    public List<Property> getPropertiesByBedroomsMinContentProvider(ContentResolver contentResolver, int bedroomsMin) {
        return mPropertyRepository.getPropertiesByBedroomsMinContentProvider(contentResolver, bedroomsMin);
    }

    public List<Property> getPropertiesByBedroomsRangeContentProvider(ContentResolver contentResolver, int bedroomsMin, int bedroomsMax) {
        return mPropertyRepository.getPropertiesByBedroomsRangeContentProvider(contentResolver, bedroomsMin, bedroomsMax);
    }

    public List<Property> getPropertiesInRadiusContentProvider(ContentResolver contentResolver, Double lat1, Double lng1, Double lat2, Double lng2) {
        return mPropertyRepository.getPropertiesInRadiusContentProvider(contentResolver, lat1, lng1, lat2, lng2);
    }

    public ArrayList<Long> getPropertiesIdsForAPlaceTypeContentProvider(ContentResolver contentResolver, String placeType) {
        return mPropertyRepository.getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, placeType);
    }

    public List<Property> getPropertiesByMarketEntryDateRangeContentProvider(ContentResolver contentResolver, long marketEntryMin, long marketEntryMax) {
        return mPropertyRepository.getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, marketEntryMin, marketEntryMax);
    }

    public List<Property> getPropertiesByMarketEntryDateMinContentProvider(ContentResolver contentResolver, long marketEntryMin) {
        return mPropertyRepository.getPropertiesByMarketEntryDateMinContentProvider(contentResolver, marketEntryMin);
    }

    public List<Property> getPropertiesBySoldDateRangeContentProvider(ContentResolver contentResolver, long soldMin, long soldMax) {
        return mPropertyRepository.getPropertiesBySoldDateRangeContentProvider(contentResolver, soldMin, soldMax);
    }

    public List<Property> getPropertiesBySoldDateMinContentProvider(ContentResolver contentResolver, long soldMin) {
        return mPropertyRepository.getPropertiesBySoldDateMinContentProvider(contentResolver, soldMin);
    }

    public List<Media> getMediasByPropertyIdCountRangeContentProvider(ContentResolver contentResolver, int mediaCountMin, int mediaCountMax) {
        return mPropertyRepository.getMediasByPropertyIdCountRangeContentProvider(contentResolver, mediaCountMin, mediaCountMax);
    }

    public List<Media> getMediasByPropertyIdCountMinContentProvider(ContentResolver contentResolver, int mediaCountMin) {
        return mPropertyRepository.getMediasByPropertyIdCountMinContentProvider(contentResolver, mediaCountMin);
    }

    public void searchProperty(ContentResolver contentResolver, String propertyType, Integer priceMin, Integer priceMax, Integer surfaceMin, Integer surfaceMax, Integer roomsMin, Integer roomsMax, Integer bathroomsMin, Integer bathroomsMax, Integer bedroomsMin, Integer bedroomsMax, LatLngBounds bounds, ArrayList<String> placesTypes, Long marketEntryDateMin, Long marketEntryDateMax, Long soldDateMin, Long soldDateMax, Integer mediaCountMin, Integer mediaCountMax) {
        mExecutor.execute(() -> {
            ArrayList<ArrayList<Long>> arrayOfIdsArrays = new ArrayList<>();
            if (propertyType != null) {
                ArrayList<Long> propertiesIds = getIdListFromPropertyList(getPropertiesByPropertyTypeContentProvider(contentResolver, propertyType));
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (priceMin != null || priceMax != null) {
                ArrayList<Long> propertiesIds;
                if (priceMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceRangeContentProvider(contentResolver, 0, priceMax));
                } else if (priceMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceMinContentProvider(contentResolver, priceMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceRangeContentProvider(contentResolver, priceMin, priceMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (surfaceMin != null || surfaceMax != null) {
                ArrayList<Long> propertiesIds;
                if (surfaceMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceRangeContentProvider(contentResolver, 0, surfaceMax));
                } else if (surfaceMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceMinContentProvider(contentResolver, surfaceMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceRangeContentProvider(contentResolver, surfaceMin, surfaceMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (roomsMin != null || roomsMax != null) {
                ArrayList<Long> propertiesIds;
                if (roomsMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsRangeContentProvider(contentResolver, 0, roomsMax));
                } else if (roomsMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsMinContentProvider(contentResolver, roomsMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsRangeContentProvider(contentResolver, roomsMin, roomsMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (bathroomsMin != null || bathroomsMax != null) {
                ArrayList<Long> propertiesIds;
                if (bathroomsMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsRangeContentProvider(contentResolver, 0, bathroomsMax));
                } else if (bathroomsMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsMinContentProvider(contentResolver, bathroomsMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsRangeContentProvider(contentResolver, bathroomsMin, bathroomsMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (bedroomsMin != null || bedroomsMax != null) {
                ArrayList<Long> propertiesIds;
                if (bedroomsMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsRangeContentProvider(contentResolver, 0, bedroomsMax));
                } else if (bedroomsMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsMinContentProvider(contentResolver, bedroomsMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsRangeContentProvider(contentResolver, bedroomsMin, bedroomsMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (bounds != null) {
                ArrayList<Long> propertiesIds = getIdListFromPropertyList(getPropertiesInRadiusContentProvider(contentResolver, bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude));
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (placesTypes != null && !placesTypes.isEmpty()) {
                for (String type : placesTypes) {
                    arrayOfIdsArrays.add(getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, type));
                }
            }
            if (marketEntryDateMin != null || marketEntryDateMax != null) {
                ArrayList<Long> propertiesIds;
                if (marketEntryDateMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, 0, marketEntryDateMax));
                } else if (marketEntryDateMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateMinContentProvider(contentResolver, marketEntryDateMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, marketEntryDateMin, marketEntryDateMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (soldDateMin != null || soldDateMax != null) {
                ArrayList<Long> propertiesIds;
                if (soldDateMin == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateRangeContentProvider(contentResolver, 0, soldDateMax));
                } else if (soldDateMax == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateMinContentProvider(contentResolver, soldDateMin));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateRangeContentProvider(contentResolver, soldDateMin, soldDateMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (mediaCountMin != null || mediaCountMax != null) {
                ArrayList<Long> propertiesIds;
                if (mediaCountMin == null) {
                    propertiesIds = getPropertyIdListFromMediaList(getMediasByPropertyIdCountRangeContentProvider(contentResolver, 0, mediaCountMax));
                } else if (mediaCountMax == null) {
                    propertiesIds = getPropertyIdListFromMediaList(getMediasByPropertyIdCountMinContentProvider(contentResolver, mediaCountMin));
                } else {
                    propertiesIds = getPropertyIdListFromMediaList(getMediasByPropertyIdCountRangeContentProvider(contentResolver, mediaCountMin, mediaCountMax));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }

            ArrayList<Long> resultIds = getCommonIdsFromIdLists(arrayOfIdsArrays);
            ArrayList<Property> resultProperties = new ArrayList<>();
            for (Long id : resultIds) {
                resultProperties.add(getPropertyById(contentResolver, id));
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

    private ArrayList<Long> getPropertyIdListFromMediaList(List<Media> medias) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Media media : medias) {
            if (!ids.contains(media.getPropertyId())) ids.add(media.getPropertyId());
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
