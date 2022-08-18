package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.content.ContentResolver;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.data.PropertySearchRepository;
import com.openclassrooms.realestatemanager.data.model.SearchCriteria;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class PropertySearchViewModel extends ViewModel {

    private PropertySearchRepository mPropertySearchRepository;
    private Executor mExecutor;
    private MutableLiveData<List<Property>> propertySearchResults;
    private MutableLiveData<String> mPropertySearchError;

    public PropertySearchViewModel(PropertySearchRepository propertySearchRepository, Executor executor) {
        mPropertySearchRepository = propertySearchRepository;
        mExecutor = executor;
        propertySearchResults = new MutableLiveData<>();
        mPropertySearchError = new MutableLiveData<>();
    }

    public MutableLiveData<List<Property>> getPropertySearchResultsLiveData() { return propertySearchResults; }


    public LiveData<String> getPropertySearchError() {
        return mPropertySearchError;
    }

    public Property getPropertyById(ContentResolver contentResolver, long propertyId) { return mPropertySearchRepository.getPropertyByIdContentProvider(contentResolver, propertyId); }

    public List<Property> getPropertiesByPropertyTypeContentProvider(ContentResolver contentResolver, String propertyType) {
        return mPropertySearchRepository.getPropertiesByPropertyTypeContentProvider(contentResolver, propertyType);
    }

    public List<Property> getPropertiesByPriceRangeContentProvider(ContentResolver contentResolver, Integer priceMin, Integer priceMax) {
        return mPropertySearchRepository.getPropertiesByPriceRangeContentProvider(contentResolver, priceMin, priceMax);
    }

    public List<Property> getPropertiesByPriceMinContentProvider(ContentResolver contentResolver, Integer priceMin) {
        return mPropertySearchRepository.getPropertiesByPriceMinContentProvider(contentResolver, priceMin);
    }

    public List<Property> getPropertiesBySurfaceRangeContentProvider(ContentResolver contentResolver, int surfaceMin, int surfaceMax) {
        return mPropertySearchRepository.getPropertiesBySurfaceRangeContentProvider(contentResolver, surfaceMin, surfaceMax);
    }

    public List<Property> getPropertiesBySurfaceMinContentProvider(ContentResolver contentResolver, int surfaceMin) {
        return mPropertySearchRepository.getPropertiesBySurfaceMinContentProvider(contentResolver, surfaceMin);
    }

    public List<Property> getPropertiesByRoomsRangeContentProvider(ContentResolver contentResolver, int roomsMin, int roomsMax) {
        return mPropertySearchRepository.getPropertiesByRoomsRangeContentProvider(contentResolver, roomsMin, roomsMax);
    }

    public List<Property> getPropertiesByRoomsMinContentProvider(ContentResolver contentResolver, int roomsMin) {
        return mPropertySearchRepository.getPropertiesByRoomsMinContentProvider(contentResolver, roomsMin);
    }

    public List<Property> getPropertiesByBathroomsRangeContentProvider(ContentResolver contentResolver, int bathroomsMin, int bathroomsMax) {
        return mPropertySearchRepository.getPropertiesByBathroomsRangeContentProvider(contentResolver, bathroomsMin, bathroomsMax);
    }

    public List<Property> getPropertiesByBathroomsMinContentProvider(ContentResolver contentResolver, int bathroomsMin) {
        return mPropertySearchRepository.getPropertiesByBathroomsMinContentProvider(contentResolver, bathroomsMin);
    }

    public List<Property> getPropertiesByBedroomsRangeContentProvider(ContentResolver contentResolver, int bedroomsMin, int bedroomsMax) {
        return mPropertySearchRepository.getPropertiesByBedroomsRangeContentProvider(contentResolver, bedroomsMin, bedroomsMax);
    }

    public List<Property> getPropertiesByBedroomsMinContentProvider(ContentResolver contentResolver, int bedroomsMin) {
        return mPropertySearchRepository.getPropertiesByBedroomsMinContentProvider(contentResolver, bedroomsMin);
    }

    public List<Property> getPropertiesInRadiusContentProvider(ContentResolver contentResolver, Double lat1, Double lng1, Double lat2, Double lng2) {
        return mPropertySearchRepository.getPropertiesInRadiusContentProvider(contentResolver, lat1, lng1, lat2, lng2);
    }

    public ArrayList<Long> getPropertiesIdsForAPlaceTypeContentProvider(ContentResolver contentResolver, String placeType) {
        return mPropertySearchRepository.getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, placeType);
    }

    public List<Property> getPropertiesByMarketEntryDateRangeContentProvider(ContentResolver contentResolver, long marketEntryMin, long marketEntryMax) {
        return mPropertySearchRepository.getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, marketEntryMin, marketEntryMax);
    }

    public List<Property> getPropertiesByMarketEntryDateMinContentProvider(ContentResolver contentResolver, long marketEntryMin) {
        return mPropertySearchRepository.getPropertiesByMarketEntryDateMinContentProvider(contentResolver, marketEntryMin);
    }

    public List<Property> getPropertiesBySoldDateRangeContentProvider(ContentResolver contentResolver, long soldMin, long soldMax) {
        return mPropertySearchRepository.getPropertiesBySoldDateRangeContentProvider(contentResolver, soldMin, soldMax);
    }

    public List<Property> getPropertiesBySoldDateMinContentProvider(ContentResolver contentResolver, long soldMin) {
        return mPropertySearchRepository.getPropertiesBySoldDateMinContentProvider(contentResolver, soldMin);
    }

    public List<Media> getMediasByPropertyIdCountRangeContentProvider(ContentResolver contentResolver, int mediaCountMin, int mediaCountMax) {
        return mPropertySearchRepository.getMediasByPropertyIdCountRangeContentProvider(contentResolver, mediaCountMin, mediaCountMax);
    }

    public List<Media> getMediasByPropertyIdCountMinContentProvider(ContentResolver contentResolver, int mediaCountMin) {
        return mPropertySearchRepository.getMediasByPropertyIdCountMinContentProvider(contentResolver, mediaCountMin);
    }

    public void searchDataProcess(ContentResolver contentResolver, String propertyType, CharSequence priceMin,
                                  CharSequence priceMax, CharSequence surfaceMin, CharSequence surfaceMax, CharSequence roomsMin,
                                  CharSequence roomsMax, CharSequence bathroomsMin, CharSequence bathroomsMax, CharSequence bedroomsMin,
                                  CharSequence bedroomsMax, Place place, CharSequence radius, ArrayList<String> placesTypes,
                                  Date marketEntryDateMin, Date marketEntryDateMax, Date soldDateMin, Date soldDateMax,
                                  CharSequence mediaCountMin, CharSequence mediaCountMax) {

        SearchCriteria searchCriteria = new SearchCriteria();

        searchCriteria.setPropertyType(propertyType);
        if (isCharSequenceNotNullOrEmpty(priceMin)) searchCriteria.setPriceMin(Integer.parseInt(priceMin.toString()));
        if (isCharSequenceNotNullOrEmpty(priceMax)) searchCriteria.setPriceMax(Integer.parseInt(priceMax.toString()));
        if (isCharSequenceNotNullOrEmpty(surfaceMin)) searchCriteria.setSurfaceMin(Integer.parseInt(surfaceMin.toString()));
        if (isCharSequenceNotNullOrEmpty(surfaceMax)) searchCriteria.setSurfaceMax(Integer.parseInt(surfaceMax.toString()));
        if (isCharSequenceNotNullOrEmpty(roomsMin)) searchCriteria.setRoomsMin(Integer.parseInt(roomsMin.toString()));
        if (isCharSequenceNotNullOrEmpty(roomsMax)) searchCriteria.setRoomsMax(Integer.parseInt(roomsMax.toString()));
        if (isCharSequenceNotNullOrEmpty(bathroomsMin)) searchCriteria.setBathroomsMin(Integer.parseInt(bathroomsMin.toString()));
        if (isCharSequenceNotNullOrEmpty(bathroomsMax)) searchCriteria.setBathroomsMax(Integer.parseInt(bathroomsMax.toString()));
        if (isCharSequenceNotNullOrEmpty(bedroomsMin)) searchCriteria.setBedroomsMin(Integer.parseInt(bedroomsMin.toString()));
        if (isCharSequenceNotNullOrEmpty(bedroomsMax)) searchCriteria.setBedroomsMax(Integer.parseInt(bedroomsMax.toString()));
        if (place != null && isCharSequenceNotNullOrEmpty(radius)) {
            LatLng center = new LatLng(place.getLatitude(), place.getLongitude());
            searchCriteria.setBounds(Utils.generateBounds(center, Integer.parseInt(radius.toString())));
        }
        if (placesTypes != null && !placesTypes.isEmpty()) searchCriteria.setPlacesTypes(placesTypes);
        if (marketEntryDateMin != null) searchCriteria.setMarketEntryDateMin(Utils.convertDateToLong(marketEntryDateMin));
        if (marketEntryDateMax != null) searchCriteria.setMarketEntryDateMax(Utils.convertDateToLong(marketEntryDateMax));
        if (soldDateMin != null) searchCriteria.setSoldDateMin(Utils.convertDateToLong(soldDateMin));
        if (soldDateMax != null) searchCriteria.setSoldDateMax(Utils.convertDateToLong(soldDateMax));
        if (isCharSequenceNotNullOrEmpty(mediaCountMin)) searchCriteria.setMediaCountMin(Integer.parseInt(mediaCountMin.toString()));
        if (isCharSequenceNotNullOrEmpty(mediaCountMax)) searchCriteria.setMediaCountMax(Integer.parseInt(mediaCountMax.toString()));

        if (searchCriteria.isAtLeastOneOfCriteriaNonNull()) {
            searchProperty(contentResolver, searchCriteria);
        } else if (place != null && !isCharSequenceNotNullOrEmpty(radius)) {
            mPropertySearchError.postValue("Must specify a radius");
        }
        else if (isCharSequenceNotNullOrEmpty(radius) && place != null && Integer.parseInt(radius.toString()) < 0) {
            mPropertySearchError.postValue("Radius must be positive");
        }
        else {
            mPropertySearchError.postValue("At least 1 field must be filled");
        }
    }

    public boolean isCharSequenceNotNullOrEmpty(CharSequence charSequence) {
        return (charSequence != null && !charSequence.toString().isEmpty());
    }

    public void searchProperty(ContentResolver contentResolver, SearchCriteria searchCriteria) {
        mExecutor.execute(() -> {
            ArrayList<ArrayList<Long>> arrayOfIdsArrays = new ArrayList<>();
            if (searchCriteria.getPropertyType() != null) {
                ArrayList<Long> propertiesIds = getIdListFromPropertyList(getPropertiesByPropertyTypeContentProvider(contentResolver, searchCriteria.getPropertyType()));
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getPriceMin() != null || searchCriteria.getPriceMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getPriceMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceRangeContentProvider(contentResolver, 0, searchCriteria.getPriceMax()));
                } else if (searchCriteria.getPriceMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceMinContentProvider(contentResolver, searchCriteria.getPriceMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByPriceRangeContentProvider(contentResolver, searchCriteria.getPriceMin(), searchCriteria.getPriceMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getSurfaceMin() != null || searchCriteria.getSurfaceMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getSurfaceMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceRangeContentProvider(contentResolver, 0, searchCriteria.getSurfaceMax()));
                } else if (searchCriteria.getSurfaceMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceMinContentProvider(contentResolver, searchCriteria.getSurfaceMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySurfaceRangeContentProvider(contentResolver, searchCriteria.getSurfaceMin(), searchCriteria.getSurfaceMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getRoomsMin() != null || searchCriteria.getRoomsMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getRoomsMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsRangeContentProvider(contentResolver, 0, searchCriteria.getRoomsMax()));
                } else if (searchCriteria.getRoomsMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsMinContentProvider(contentResolver, searchCriteria.getRoomsMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByRoomsRangeContentProvider(contentResolver, searchCriteria.getRoomsMin(), searchCriteria.getRoomsMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getBathroomsMin() != null || searchCriteria.getBathroomsMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getBathroomsMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsRangeContentProvider(contentResolver, 0, searchCriteria.getBathroomsMax()));
                } else if (searchCriteria.getBathroomsMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsMinContentProvider(contentResolver, searchCriteria.getBathroomsMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBathroomsRangeContentProvider(contentResolver, searchCriteria.getBathroomsMin(), searchCriteria.getBathroomsMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getBedroomsMin() != null || searchCriteria.getBedroomsMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getBedroomsMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsRangeContentProvider(contentResolver, 0, searchCriteria.getBedroomsMax()));
                } else if (searchCriteria.getBedroomsMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsMinContentProvider(contentResolver, searchCriteria.getBedroomsMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByBedroomsRangeContentProvider(contentResolver, searchCriteria.getBedroomsMin(), searchCriteria.getBedroomsMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getBounds() != null) {
                ArrayList<Long> propertiesIds = getIdListFromPropertyList(getPropertiesInRadiusContentProvider(contentResolver, searchCriteria.getBounds().southwest.latitude, searchCriteria.getBounds().southwest.longitude, searchCriteria.getBounds().northeast.latitude, searchCriteria.getBounds().northeast.longitude));
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getPlacesTypes() != null && !searchCriteria.getPlacesTypes().isEmpty()) {
                for (String type : searchCriteria.getPlacesTypes()) {
                    arrayOfIdsArrays.add(getPropertiesIdsForAPlaceTypeContentProvider(contentResolver, type));
                }
            }
            if (searchCriteria.getMarketEntryDateMin() != null || searchCriteria.getMarketEntryDateMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getMarketEntryDateMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, 0, searchCriteria.getMarketEntryDateMax()));
                } else if (searchCriteria.getMarketEntryDateMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateMinContentProvider(contentResolver, searchCriteria.getMarketEntryDateMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesByMarketEntryDateRangeContentProvider(contentResolver, searchCriteria.getMarketEntryDateMin(), searchCriteria.getMarketEntryDateMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getSoldDateMin() != null || searchCriteria.getSoldDateMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getSoldDateMin() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateRangeContentProvider(contentResolver, 0, searchCriteria.getSoldDateMax()));
                } else if (searchCriteria.getSoldDateMax() == null) {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateMinContentProvider(contentResolver, searchCriteria.getSoldDateMin()));
                } else {
                    propertiesIds = getIdListFromPropertyList(getPropertiesBySoldDateRangeContentProvider(contentResolver, searchCriteria.getSoldDateMin(), searchCriteria.getSoldDateMax()));
                }
                arrayOfIdsArrays.add(propertiesIds);
            }
            if (searchCriteria.getMediaCountMin() != null || searchCriteria.getMediaCountMax() != null) {
                ArrayList<Long> propertiesIds;
                if (searchCriteria.getMediaCountMin() == null) {
                    propertiesIds = getPropertyIdListFromMediaList(getMediasByPropertyIdCountRangeContentProvider(contentResolver, 0, searchCriteria.getMediaCountMax()));
                } else if (searchCriteria.getMediaCountMax() == null) {
                    propertiesIds = getPropertyIdListFromMediaList(getMediasByPropertyIdCountMinContentProvider(contentResolver, searchCriteria.getMediaCountMin()));
                } else {
                    propertiesIds = getPropertyIdListFromMediaList(getMediasByPropertyIdCountRangeContentProvider(contentResolver, searchCriteria.getMediaCountMin(), searchCriteria.getMediaCountMax()));
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

    public ArrayList<Long> getIdListFromPropertyList(List<Property> properties) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Property property : properties) {
            if (!ids.contains(property.getId())) ids.add(property.getId());
        }
        return ids;
    }

    public ArrayList<Long> getPropertyIdListFromMediaList(List<Media> medias) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Media media : medias) {
            if (!ids.contains(media.getPropertyId())) ids.add(media.getPropertyId());
        }
        return ids;
    }

    public ArrayList<Long> getCommonIdsFromIdLists(ArrayList<ArrayList<Long>> arrayOfIdsArrays) {
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
