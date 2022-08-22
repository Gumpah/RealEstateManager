package com.openclassrooms.realestatemanager.ui.addproperty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.BitmapAndString;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyType;
import com.openclassrooms.realestatemanager.databinding.DialogAddImageBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.ui.AddAndModifyPropertyCallback;
import com.openclassrooms.realestatemanager.ui.PropertyMediasAdapter;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.FileManager;
import com.openclassrooms.realestatemanager.utils.PropertyCreationNotificationService;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddPropertyFragment extends Fragment implements AddAndModifyPropertyCallback {

    private FragmentAddPropertyBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private PlacesViewModel mPlacesViewModel;
    private ArrayList<BitmapAndString> mBitmapAndStringList;
    private PropertyMediasAdapter mPropertyMediasAdapter;
    private String mPropertyType;
    private Date marketEntryDate;
    private Place propertyPlace;
    private PropertyCreationNotificationService notificationService;
    private Property propertyToCreate;

    private String mCurrentPhotoPath;

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
        mBitmapAndStringList = new ArrayList<>();
        setToolbar();
        configureViewModels();
        mPlacesViewModel.getPlacesMutableLiveData().postValue(null);
        initErrorsListener();
        initRecyclerView();
        initClickListeners();
        initSpinner();
        initDate();
        initPropertyPlaceListener();
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }
        initAutocompleteAddress();
        initPropertyCreationListeners();
        notificationService = new PropertyCreationNotificationService(requireContext());
        return binding.getRoot();
    }

    private void setToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarToolbarAddProperty);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        binding.toolbarToolbarAddProperty.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mPlacesViewModel = new ViewModelProvider(this, PlacesViewModelFactory.getInstance()).get(PlacesViewModel.class);
    }

    private void initErrorsListener() {
        mPlacesViewModel.getFetchNearbyPlacesError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), R.string.nearbySearchError, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                propertyToCreate = null;
            }
        });
        mPlacesViewModel.getAutocompleteRequestError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), R.string.autocompleteError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        binding.recyclerViewImages.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        binding.recyclerViewImages.setLayoutManager(layoutManager);
        mPropertyMediasAdapter = new PropertyMediasAdapter(new ArrayList<>(), this);
        binding.recyclerViewImages.setAdapter(mPropertyMediasAdapter);
    }

    private void initClickListeners() {
        initMediaFromCameraClickListener();
        initMediaFromGalleryClickListener();
        initCreateButtonClickListener();
        initDateButton();
    }

    private void initCreateButtonClickListener() {
        binding.buttonCreate.setOnClickListener(v -> onSubmit());
    }

    private void initMediaFromGalleryClickListener() {
        binding.imageButtonAddImageGallery.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            uriGallery.launch(i);
        });
    }

    private void initMediaFromCameraClickListener() {
        binding.imageButtonAddImageCamera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = null;
            try {
                photoFile = FileManager.createImageFile(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (IOException ex) {
                //Error
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.openclassrooms.realestatemanager.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                uriCamera.launch(cameraIntent);
            }
        });
    }

    private final ActivityResultLauncher<Intent> uriCamera =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), i -> {
                if (i.getResultCode() == Activity.RESULT_OK && i.getData() != null) {
                    Bitmap mImageBitmap = null;
                    try {
                        mImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mImageBitmap != null) {
                        showImageDescriptionDialog(mImageBitmap);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> uriGallery =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), i -> {
                if (i.getResultCode() == Activity.RESULT_OK && i.getData() != null) {
                    Uri selectedImageUri = i.getData().getData();
                    Bitmap bitmap = FileManager.convertUriToBitmap(selectedImageUri, requireActivity().getContentResolver());
                    showImageDescriptionDialog(bitmap);
                }
            });

    private void showImageDescriptionDialog(Bitmap bitmap) {
        final AlertDialog dialog = getImageDescriptionDialog(bitmap);
        dialog.show();
    }

    private AlertDialog getImageDescriptionDialog(Bitmap bitmap) {
        DialogAddImageBinding binding = DialogAddImageBinding.inflate(LayoutInflater.from(requireContext()), this.binding.getRoot(), false);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireContext(), R.style.AlertDialog);
        alertBuilder.setView(binding.getRoot());

        AlertDialog dialog = alertBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                binding.buttonPositiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.textInputLayoutImageDescription.getEditText() != null) {
                            onPositiveButtonClick(dialog, binding.textInputLayoutImageDescription.getEditText(), bitmap);
                        }
                    }
                });
                binding.buttonNegativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    private void onPositiveButtonClick(DialogInterface dialogInterface, EditText descriptionEditText, Bitmap bitmap) {
        String description = descriptionEditText.getText().toString();
        if (!description.isEmpty()) {
            mBitmapAndStringList.add(new BitmapAndString(bitmap, description));
            mPropertyMediasAdapter.setData(mBitmapAndStringList);
            dialogInterface.dismiss();
        } else {
            descriptionEditText.setError(requireContext().getString(R.string.noDescriptionError));
        }
    }

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
                if (propertyTypesTranslated.contains(s.toString())) {
                    mPropertyType = PropertyType.types.get(propertyTypesTranslated.indexOf(s.toString()));
                } else {
                    mPropertyType = null;
                }
            }
        });
        binding.autoCompleteTextViewPropertyType.setOnItemClickListener(this::onItemSelectedHandler);
    }

    private void initAutocompleteAddress() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_address_addProperty);
        if (autocompleteFragment != null) {
            mPlacesViewModel.autocompleteRequest(autocompleteFragment);
        }
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        mPropertyType = PropertyType.types.get(position);
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
        Double price = -1d;
        EditText editTextPrice = binding.textInputLayoutPrice.getEditText();
        if (editTextPrice != null && !editTextPrice.getText().toString().isEmpty()) {
            price = Double.parseDouble(editTextPrice.getText().toString());
        }
        Integer surface = -1;
        EditText editTextSurface = binding.textInputLayoutSurface.getEditText();
        if (editTextSurface != null && !editTextSurface.getText().toString().isEmpty()) {
            surface = Integer.parseInt(editTextSurface.getText().toString());
        }
        Integer roomsCount = -1;
        EditText editTextRooms = binding.textInputLayoutRoomsCount.getEditText();
        if (editTextRooms != null && !editTextRooms.getText().toString().isEmpty()) {
            roomsCount = Integer.parseInt(editTextRooms.getText().toString());
        }
        Integer bathroomsCount = -1;
        EditText editTextBathrooms = binding.textInputLayoutBathroomsCount.getEditText();
        if (editTextBathrooms != null && !editTextBathrooms.getText().toString().isEmpty()) {
            bathroomsCount = Integer.parseInt(editTextBathrooms.getText().toString());
        }
        Integer bedroomsCount = -1;
        EditText editTextBedrooms = binding.textInputLayoutBedroomsCount.getEditText();
        if (editTextBedrooms != null && !editTextBedrooms.getText().toString().isEmpty()) {
            bedroomsCount = Integer.parseInt(editTextBedrooms.getText().toString());
        }
        String description = null;
        if (binding.textInputLayoutDescription.getEditText() != null) {
            description = binding.textInputLayoutDescription.getEditText().getText().toString();
        }
        String agent = null;
        if (binding.textInputLayoutAgent.getEditText() != null) {
            binding.textInputLayoutAgent.setErrorEnabled(false);
            agent = binding.textInputLayoutAgent.getEditText().getText().toString();
        }

        if (mPropertyType == null || mPropertyType.isEmpty()) {
            Snackbar.make(requireView(), R.string.noPropertyTypeSelected, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!PropertyType.types.contains(mPropertyType)) {
            Snackbar.make(requireView(),R.string.propertyTypeSelectedNotInList, Toast.LENGTH_SHORT).show();
            return;
        }
        if ((roomsCount != -1 && bathroomsCount != -1) && roomsCount < bathroomsCount) {
            binding.textInputLayoutBathroomsCount.setError(requireContext().getString(R.string.countCantBeSuperiorThanRoomsCount));
            return;
        }
        if ((roomsCount != -1 && bedroomsCount != -1) && roomsCount < bedroomsCount) {
            binding.textInputLayoutBedroomsCount.setError(requireContext().getString(R.string.countCantBeSuperiorThanRoomsCount));
            return;
        }
        if (propertyPlace == null) {
            Snackbar.make(requireView(),R.string.noAddressSpecified, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBitmapAndStringList.isEmpty()) {
            Snackbar.make(requireView(),R.string.noImage, Toast.LENGTH_SHORT).show();
            return;
        }
        long marketEntryDateMillis = Utils.convertDateToLong(marketEntryDate);

        propertyToCreate = new Property(
                mPropertyType,
                price,
                surface,
                roomsCount,
                bathroomsCount,
                bedroomsCount,
                description,
                propertyPlace.getAddress(),
                propertyPlace.getLatitude(),
                propertyPlace.getLongitude(),
                PropertyStatus.AVAILABLE,
                marketEntryDateMillis,
                agent);
        nearbyPlacesRequest();
    }

    private void nearbyPlacesRequest() {
        binding.progressBar.setVisibility(View.VISIBLE);
        if (propertyToCreate != null) {
            mPlacesViewModel.fetchPlaces(BuildConfig.MAPS_API_KEY, Utils.createLocationString(propertyToCreate.getLatitude(), propertyToCreate.getLongitude()));
        }
    }

    private void initPropertyCreationListeners() {
        mPlacesViewModel.getPlacesMutableLiveData().observe(getViewLifecycleOwner(), places -> {
            if (places != null) {
                ArrayList<Media> mediaList = new ArrayList<>();
                for (BitmapAndString bitmapAndString : mBitmapAndStringList) {
                    Uri storedImage = FileManager.createFile(bitmapAndString.getMedia(), requireActivity().getFilesDir());
                    if (storedImage != null) {
                        mediaList.add(new Media(storedImage.toString(), bitmapAndString.getMedia_description()));
                    }
                }
                mPropertyViewModel.insertPropertyAndMediasAndPlaces(propertyToCreate, mediaList, places);
            }
        });
        mPropertyViewModel.getPropertyCreatedIdLiveData().observe(getViewLifecycleOwner(), id -> {
            binding.progressBar.setVisibility(View.GONE);
            notificationService.showNotification(id);
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void removeMedia(int index) {
        mBitmapAndStringList.remove(index);
        mPropertyMediasAdapter.setData(mBitmapAndStringList);
    }
}