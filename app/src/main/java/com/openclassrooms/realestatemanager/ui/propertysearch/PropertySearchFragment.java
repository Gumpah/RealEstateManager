package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyType;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertySearchBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
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
    private PropertySearchViewModel mPropertySearchViewModel;
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
        setToolbar();
        configureViewModels();
        initAutocompleteErrorListener();
        initSearchDataErrorListener();
        initPropertyTypeField();
        initAutocompleteAddress();
        initPropertyPlaceListener();
        initDateButtons();
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }
        initSearchButtonListener();
        initPropertySearchResults();
        return binding.getRoot();
    }

    private void setToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarPropertySearch);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        binding.toolbarToolbarPropertySearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void configureViewModels() {
        mPropertySearchViewModel = new ViewModelProvider(requireActivity(), PropertySearchViewModelFactory.getInstance()).get(PropertySearchViewModel.class);
        mPlacesViewModel = new ViewModelProvider(this, PlacesViewModelFactory.getInstance()).get(PlacesViewModel.class);
    }

    private void initAutocompleteErrorListener() {
        mPlacesViewModel.getAutocompleteRequestError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), R.string.autocompleteError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSearchDataErrorListener() {
        mPropertySearchViewModel.getPropertySearchError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Snackbar.make(requireView(), error, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void initAutocompleteAddress() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_address_searchProperty);
        if (autocompleteFragment != null) {
            mPlacesViewModel.autocompleteRequest(autocompleteFragment);
        }
    }

    private void initPropertyPlaceListener() {
        mPlacesViewModel.getPropertyPlaceMutableLiveData().observe(getViewLifecycleOwner(), place -> {
            mPropertyPlace = place;
        });
    }

    private void initPropertySearchResults() {
        mPropertySearchViewModel.getPropertySearchResultsLiveData().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
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

        Calendar cal = Calendar.getInstance();
        if (dateButton == MARKET_ENTRY_DATE_MIN && mMarketEntryDateMax != null) {
            cal.setTime(mMarketEntryDateMax);
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        }
        if (dateButton == MARKET_ENTRY_DATE_MAX && mMarketEntryDateMin != null) {
            cal.setTime(mMarketEntryDateMin);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        }
        if (dateButton == SOLD_DATE_MIN && mSoldDateMax != null) {
            cal.setTime(mSoldDateMax);
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        }
        if (dateButton == SOLD_DATE_MAX && mSoldDateMin != null) {
            cal.setTime(mSoldDateMin);
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        }
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
        mPropertySearchViewModel.searchDataProcess(
                requireActivity().getContentResolver(),
                mPropertyType,
                binding.priceMin.getText(), binding.priceMax.getText(),
                binding.surfaceMin.getText(), binding.surfaceMax.getText(),
                binding.roomsMin.getText(), binding.roomsMax.getText(),
                binding.bathroomsMin.getText(), binding.bathroomsMax.getText(),
                binding.bedroomsMin.getText(), binding.bedroomsMax.getText(),
                mPropertyPlace,
                binding.addressRadius.getText(),
                getPlacesTypesChecked(),
                mMarketEntryDateMin, mMarketEntryDateMax,
                mSoldDateMin, mSoldDateMax,
                binding.mediaCountMin.getText(), binding.mediaCountMax.getText());
    }

    private ArrayList<String> getPlacesTypesChecked() {
        ArrayList<String> typesChecked = new ArrayList<>();
        if (binding.checkBoxAmusementPark.isChecked()) typesChecked.add("amusement_park");
        if (binding.checkBoxPark.isChecked()) typesChecked.add("park");
        if (binding.checkBoxUniversity.isChecked()) typesChecked.add("university");
        if (binding.checkBoxPrimarySchool.isChecked()) typesChecked.add("primary_school");
        if (binding.checkBoxSecondarySchool.isChecked()) typesChecked.add("secondary_school");
        if (binding.checkBoxSchool.isChecked()) typesChecked.add("school");
        if (binding.checkBoxStore.isChecked()) typesChecked.add("store");
        if (binding.checkBoxSubwayStation.isChecked()) typesChecked.add("subway_station");
        if (binding.checkBoxTrainStation.isChecked()) typesChecked.add("train_station");
        if (binding.checkBoxBusStation.isChecked()) typesChecked.add("bus_station");
        if (binding.checkBoxSupermarket.isChecked()) typesChecked.add("supermarket");
        if (binding.checkBoxMovieTheater.isChecked()) typesChecked.add("movie_theater");

        return typesChecked;
    }
}