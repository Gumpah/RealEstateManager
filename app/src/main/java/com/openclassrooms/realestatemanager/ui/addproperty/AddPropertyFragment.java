package com.openclassrooms.realestatemanager.ui.addproperty;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyType;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.FileManager;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddPropertyFragment extends Fragment implements AddPropertyCallback {

    private FragmentAddPropertyBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private PlacesViewModel mPlacesViewModel;
    private ArrayList<Bitmap> mBitmapList;
    private RecyclerView mRecyclerView;
    private AddPropertyMediasAdapter mAddPropertyMediasAdapter;
    private String mPropertyType;
    private Date marketEntryDate;
    private Place propertyPlace;

    public AddPropertyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPropertyBinding.inflate(inflater, container, false);
        mBitmapList = new ArrayList<>();
        configureViewModels();
        mPlacesViewModel.getPlacesMutableLiveData().postValue(null);
        initRecyclerView();
        setMediaClickListener();
        setClickListener();
        initSpinner();
        initDate();
        initDateButton();
        initPropertyPlaceListener();
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }
        initAutocompleteAddress();
        //mPlacesViewModel.fetchPlaces(BuildConfig.MAPS_API_KEY, "48.8335697,2.2553826");
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(requireActivity(), PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mPlacesViewModel = new ViewModelProvider(requireActivity(), PlacesViewModelFactory.getInstance()).get(PlacesViewModel.class);
    }

    private void initRecyclerView() {
        mRecyclerView = binding.recyclerViewImages;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAddPropertyMediasAdapter = new AddPropertyMediasAdapter(new ArrayList<>(), this);
        mRecyclerView.setAdapter(mAddPropertyMediasAdapter);
    }

    private void setClickListener() {
        binding.buttonCreate.setOnClickListener(v -> onSubmit());
    }

    private void setMediaClickListener() {
        binding.imageButtonAddImage.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            uri.launch(i);
        });
    }

    private final ActivityResultLauncher<Intent> uri =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), i -> {
                if (i.getResultCode() == Activity.RESULT_OK && i.getData() != null) {
                    Uri selectedImageUri = i.getData().getData();
                    Bitmap bitmap = FileManager.convertUriToBitmap(selectedImageUri, requireActivity().getContentResolver());
                    mBitmapList.add(bitmap);
                    mAddPropertyMediasAdapter.setData(mBitmapList);
                }
            });

    private void initSpinner() {
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
        binding.autoCompleteTextViewPropertyType.setOnItemClickListener(this::onItemSelectedHandler);
    }

    private void initAutocompleteAddress() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_address);
        if (autocompleteFragment != null) {
            mPlacesViewModel.autocompleteRequest(autocompleteFragment);
        }
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        mPropertyType = PropertyType.types.get(position);
        System.out.println("Item selected");
    }

    private void initDate() {
        Calendar cal = Calendar.getInstance();
        marketEntryDate = cal.getTime();
        binding.buttonMarketEntryDate.setText(Utils.convertDateToString(marketEntryDate));
    }

    private void initDateButton() {
        binding.buttonMarketEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog();
            }
        });
    }

    private void displayDateDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(marketEntryDate);
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Picker_Date, getOnDateSetListener(), selectedYear, selectedMonth, selectedDayOfMonth);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -14);
        long currentTime = cal.getTimeInMillis();
        datePickerDialog.getDatePicker().setMinDate(currentTime);

        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener getOnDateSetListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                marketEntryDate = cal.getTime();
                binding.buttonMarketEntryDate.setText(Utils.convertDateToString(marketEntryDate));
            }
        };
    }

    private void initPropertyPlaceListener() {
        mPlacesViewModel.getPropertyPlaceMutableLiveData().observe(getViewLifecycleOwner(), place -> {
            propertyPlace = place;
        });
    }

    private void onSubmit() {
        String price = "";
        if (binding.textInputLayoutPrice.getEditText() != null) {
            binding.textInputLayoutPrice.setErrorEnabled(false);
            price = binding.textInputLayoutPrice.getEditText().getText().toString();
        }
        String surface = "";
        if (binding.textInputLayoutSurface.getEditText() != null) {
            binding.textInputLayoutSurface.setErrorEnabled(false);
            surface = binding.textInputLayoutSurface.getEditText().getText().toString();
        }
        String roomsCount = "";
        if (binding.textInputLayoutRoomsCount.getEditText() != null) {
            binding.textInputLayoutRoomsCount.setErrorEnabled(false);
            roomsCount = binding.textInputLayoutRoomsCount.getEditText().getText().toString();
        }
        String description = "";
        if (binding.textInputLayoutDescription.getEditText() != null) {
            binding.textInputLayoutDescription.setErrorEnabled(false);
            description = binding.textInputLayoutDescription.getEditText().getText().toString();
        }
        String agent = "";
        if (binding.textInputLayoutAgent.getEditText() != null) {
            binding.textInputLayoutAgent.setErrorEnabled(false);
            agent = binding.textInputLayoutAgent.getEditText().getText().toString();
        }

        if (mPropertyType == null || mPropertyType.isEmpty()) {
            Snackbar.make(requireView(), "Select a property type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!PropertyType.types.contains(mPropertyType)) {
            Snackbar.make(requireView(),"The property type has to be in the list", Toast.LENGTH_SHORT).show();
            return;
        }
        if (price.isEmpty()) {
            binding.textInputLayoutPrice.setError("Can't be empty");
            return;
        }
        if (surface.isEmpty()) {
            binding.textInputLayoutSurface.setError("Can't be empty");
            return;
        }
        if (roomsCount.isEmpty()) {
            binding.textInputLayoutRoomsCount.setError("Can't be empty");
            return;
        }
        if (description.isEmpty()) {
            binding.textInputLayoutDescription.setError("Can't be empty");
            return;
        }
        if (propertyPlace == null) {
            Snackbar.make(requireView(),"Specify an address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (agent.isEmpty()) {
            binding.textInputLayoutAgent.setError("Can't be empty");
            return;
        }
        if (mBitmapList.isEmpty()) {
            Snackbar.make(requireView(),"Pick at least 1 image", Toast.LENGTH_SHORT).show();
            return;
        }
        String marketEntryDateString = Utils.convertDateToString(marketEntryDate);
        ArrayList<Media> mediaList = new ArrayList<>();
        for (Bitmap bitmap : mBitmapList) {
            Uri storedImage = FileManager.createFile(bitmap, requireActivity().getFilesDir());
            if (storedImage != null) {
                mediaList.add(new Media(storedImage.toString(), "Image"));
            }
        }
        Property property = new Property(
                mPropertyType,
                Double.valueOf(price),
                Integer.parseInt(surface),
                Integer.parseInt(roomsCount),
                description, propertyPlace.getAddress(),
                propertyPlace.getLatitude(),
                propertyPlace.getLongitude(),
                PropertyStatus.AVAILABLE,
                marketEntryDateString,
                agent);
        nearbyPlacesRequest(property, mediaList);
    }

    private void emptyFieldSnackBar() {
        Snackbar.make(requireView(),"Verify the form", Toast.LENGTH_SHORT).show();
    }

    private void nearbyPlacesRequest(Property property, ArrayList<Media> mediaList) {
        mPlacesViewModel.getPlacesMutableLiveData().observe(getViewLifecycleOwner(), places -> {
            if (places != null) {
                mPropertyViewModel.insertPropertyAndMediasAndPlaces(property, mediaList, places);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mPlacesViewModel.fetchPlaces(BuildConfig.MAPS_API_KEY, Utils.createLocationString(property.getLatitude(), property.getLongitude()));
    }

    @Override
    public void removeMedia(int index) {
        mBitmapList.remove(index);
        mAddPropertyMediasAdapter.setData(mBitmapList);
    }
}