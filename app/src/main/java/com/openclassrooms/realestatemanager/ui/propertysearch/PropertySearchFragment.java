package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.PlaceType;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyType;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PropertySearchFragment extends Fragment {

    private final int MARKET_ENTRY_DATE_MIN = 1;
    private final int MARKET_ENTRY_DATE_MAX = 2;
    private final int SOLD_DATE_MIN = 3;
    private final int SOLD_DATE_MAX = 4;

    private FragmentPropertySearchBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private PlacesViewModel mPlacesViewModel;

    private String mPropertyType;
    private Place mPropertyPlace;
    private Date mMarketEntryDateMin;
    private Date mMarketEntryDateMax;
    private Date mSoldDateMin;
    private Date mSoldDateMax;

    public PropertySearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPropertySearchBinding.inflate(inflater, container, false);
        configureViewModels();
        initPropertyTypeField();
        initAutocompleteAddress();
        initPropertyPlaceListener();
        initDateButtons();
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }
        //initPlacesTypesField();
        initSearchButtonListener();
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mPlacesViewModel = new ViewModelProvider(this, PlacesViewModelFactory.getInstance()).get(PlacesViewModel.class);
    }

    private void initPropertyTypeField() {
        ArrayList<String> propertyTypesTranslated = Utils.getTypesInUserLanguage(requireContext(), PropertyType.types);
        ArrayAdapter<String> propertyTypesAdapter = new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item, propertyTypesTranslated);
        binding.autoCompleteTextViewPropertyType.setAdapter(propertyTypesAdapter);
        binding.autoCompleteTextViewPropertyType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPropertyType = null;
            }
        });
        binding.autoCompleteTextViewPropertyType.setOnItemClickListener(this::onPropertyTypeSelectedHandler);
    }

    private void onPropertyTypeSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        mPropertyType = PropertyType.types.get(position);
    }

    private void initPlacesTypesField() {
        ArrayList<String> placeTypesTranslated = Utils.getTypesInUserLanguage(requireContext(), PlaceType.types);
        ArrayAdapter<String> placeTypesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_multiple_choice, placeTypesTranslated);
        binding.listViewPlaceTypesCheckboxes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        binding.listViewPlaceTypesCheckboxes.setAdapter(placeTypesAdapter);
    }

    private ArrayList<String> getPlaceTypes() {
        SparseBooleanArray sp = binding.listViewPlaceTypesCheckboxes.getCheckedItemPositions();

        ArrayList<String> placeTypes = new ArrayList<>();

        for(int i=0; i<sp.size() ; i++){
            if(sp.valueAt(i)){
                placeTypes.add(PlaceType.types.get(i));
            }
        }

        return placeTypes;
    }

    private void initAutocompleteAddress() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_address);
        if (autocompleteFragment != null) {
            mPlacesViewModel.autocompleteRequest(autocompleteFragment);
        }
    }

    private void initPropertyPlaceListener() {
        mPlacesViewModel.getPropertyPlaceMutableLiveData().observe(getViewLifecycleOwner(), place -> {
            mPropertyPlace = place;
        });
    }

    private void initDateButtons() {
        binding.buttonMarketEntryDateMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(MARKET_ENTRY_DATE_MIN);
            }
        });
        binding.buttonMarketEntryDateMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(MARKET_ENTRY_DATE_MAX);
            }
        });
        binding.buttonSoldDateMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(SOLD_DATE_MIN);
            }
        });
        binding.buttonSoldDateMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(SOLD_DATE_MAX);
            }
        });
    }

    private void displayDateDialog(int dateButton) {
        Calendar calendar = Calendar.getInstance();
        switch (dateButton) {
            case MARKET_ENTRY_DATE_MIN:
                if (mMarketEntryDateMin != null) calendar.setTime(mMarketEntryDateMin);
                break;
            case MARKET_ENTRY_DATE_MAX:
                if (mMarketEntryDateMax != null) calendar.setTime(mMarketEntryDateMax);
                break;
            case SOLD_DATE_MIN:
                if (mSoldDateMin != null) calendar.setTime(mSoldDateMin);
                break;
            case SOLD_DATE_MAX:
                if (mSoldDateMax != null) calendar.setTime(mSoldDateMax);
                break;
        }
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Picker_Date, getOnDateSetListener(dateButton), selectedYear, selectedMonth, selectedDayOfMonth);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener getOnDateSetListener(int selectedButton) {
        return (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            switch (selectedButton) {
                case MARKET_ENTRY_DATE_MIN:
                    mMarketEntryDateMin = cal.getTime();
                    binding.buttonMarketEntryDateMin.setText(Utils.convertDateToString(mMarketEntryDateMin));
                    break;
                case MARKET_ENTRY_DATE_MAX:
                    mMarketEntryDateMax = cal.getTime();
                    binding.buttonMarketEntryDateMax.setText(Utils.convertDateToString(mMarketEntryDateMax));
                    break;
                case SOLD_DATE_MIN:
                    mSoldDateMin = cal.getTime();
                    binding.buttonSoldDateMin.setText(Utils.convertDateToString(mSoldDateMin));
                    break;
                case SOLD_DATE_MAX:
                    mSoldDateMax = cal.getTime();
                    binding.buttonSoldDateMax.setText(Utils.convertDateToString(mSoldDateMax));
                    break;
            }
        };
    }

    private void initSearchButtonListener() {
        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });
    }

    private void onSubmit() {
        String propertyType;
        Integer priceMin = null;
        Integer priceMax = null;
        Integer surfaceMin = null;
        Integer surfaceMax = null;
        Integer roomsMin = null;
        Integer roomsMax = null;
        Place propertyPlace;
        Integer radius = null;
        Date marketEntryDateMin;
        Date marketEntryDateMax;
        Date soldDateMin;
        Date soldDateMax;

        propertyType = mPropertyType;
        if (binding.textInputLayoutPriceMin.getEditText() != null && !binding.textInputLayoutPriceMin.getEditText().getText().toString().equals(""))
            priceMin = Integer.parseInt(binding.textInputLayoutPriceMin.getEditText().getText().toString());
        if (binding.textInputLayoutPriceMax.getEditText() != null && !binding.textInputLayoutPriceMax.getEditText().getText().toString().equals(""))
            priceMax = Integer.parseInt(binding.textInputLayoutPriceMax.getEditText().getText().toString());
        if (binding.textInputLayoutSurfaceMin.getEditText() != null && !binding.textInputLayoutSurfaceMin.getEditText().getText().toString().equals(""))
            surfaceMin = Integer.parseInt(binding.textInputLayoutSurfaceMin.getEditText().getText().toString());
        if (binding.textInputLayoutSurfaceMax.getEditText() != null && !binding.textInputLayoutSurfaceMax.getEditText().getText().toString().equals(""))
            surfaceMax = Integer.parseInt(binding.textInputLayoutSurfaceMax.getEditText().getText().toString());
        if (binding.textInputLayoutRoomsMin.getEditText() != null && !binding.textInputLayoutRoomsMin.getEditText().getText().toString().equals(""))
            roomsMin = Integer.parseInt(binding.textInputLayoutRoomsMin.getEditText().getText().toString());
        if (binding.textInputLayoutRoomsMax.getEditText() != null  && !binding.textInputLayoutRoomsMax.getEditText().getText().toString().equals(""))
            roomsMax = Integer.parseInt(binding.textInputLayoutRoomsMax.getEditText().getText().toString());
        propertyPlace = mPropertyPlace;
        if (binding.textInputLayoutAddressRadius.getEditText() != null  && !binding.textInputLayoutAddressRadius.getEditText().getText().toString().equals(""))
            radius = Integer.parseInt(binding.textInputLayoutAddressRadius.getEditText().getText().toString());
        marketEntryDateMin = mMarketEntryDateMin;
        marketEntryDateMax = mMarketEntryDateMax;
        soldDateMin = mSoldDateMin;
        soldDateMax = mSoldDateMax;
        isAtLeastOneFieldFilled(propertyType, priceMin, priceMax, surfaceMin, surfaceMax, roomsMin, roomsMax, propertyPlace, radius, marketEntryDateMin, marketEntryDateMax, soldDateMin, soldDateMax);
    }

    private void isAtLeastOneFieldFilled(String propertyType, Integer priceMin, Integer priceMax, Integer surfaceMin, Integer surfaceMax, Integer roomsMin, Integer roomsMax, Place propertyPlace, Integer radius, Date marketEntryDateMin, Date marketEntryDateMax, Date soldDateMin, Date soldDateMax) {
        if (propertyType != null ||
                priceMin != null ||
                priceMax != null ||
                surfaceMin != null ||
                surfaceMax != null ||
                roomsMin != null ||
                roomsMax != null ||
                (propertyPlace != null && radius != null) ||
                marketEntryDateMin != null ||
                marketEntryDateMax != null ||
                soldDateMin != null ||
                soldDateMax != null) {
            LatLngBounds bounds = null;
            if (propertyPlace != null && radius != null) {
                LatLng center = new LatLng(propertyPlace.getLatitude(), propertyPlace.getLongitude());
                bounds = Utils.generateBounds(center, radius);
            }
            mPropertyViewModel.searchProperty(propertyType, priceMin, priceMax, surfaceMin, surfaceMax, roomsMin, roomsMax, bounds, marketEntryDateMin, marketEntryDateMax, soldDateMin, soldDateMax);
        } else if (mPropertyPlace != null && radius == null) {
            Snackbar.make(requireView(), "Must specify a radius", Toast.LENGTH_SHORT).show();
        }
        else if (mPropertyPlace != null && radius < 0) {
            Snackbar.make(requireView(), "Radius must be positive", Toast.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(requireView(), "At least 1 field must be filled", Toast.LENGTH_SHORT).show();
        }
    }
}