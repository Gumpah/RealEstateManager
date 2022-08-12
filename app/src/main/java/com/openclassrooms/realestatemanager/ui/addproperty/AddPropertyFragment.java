package com.openclassrooms.realestatemanager.ui.addproperty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
        notificationService = new PropertyCreationNotificationService(requireContext());
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mPlacesViewModel = new ViewModelProvider(this, PlacesViewModelFactory.getInstance()).get(PlacesViewModel.class);
    }

    private void initErrorsListener() {
        mPlacesViewModel.getFetchNearbyPlacesError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), "Error while fetching nearby places", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
        mPlacesViewModel.getAutocompleteRequestError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Snackbar.make(binding.getRoot(), "Error during autocomplete request", Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            uriCamera.launch(i);
        });
    }

    private final ActivityResultLauncher<Intent> uriCamera =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), i -> {
                if (i.getResultCode() == Activity.RESULT_OK && i.getData() != null) {
                    Bundle bundle = i.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    showImageDescriptionDialog(bitmap);
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
            descriptionEditText.setError("Must enter a description");
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
        String bathroomsCount = "";
        if (binding.textInputLayoutBathroomsCount.getEditText() != null) {
            binding.textInputLayoutBathroomsCount.setErrorEnabled(false);
            bathroomsCount = binding.textInputLayoutBathroomsCount.getEditText().getText().toString();
        }
        String bedroomsCount = "";
        if (binding.textInputLayoutBedroomsCount.getEditText() != null) {
            binding.textInputLayoutBedroomsCount.setErrorEnabled(false);
            bedroomsCount = binding.textInputLayoutBedroomsCount.getEditText().getText().toString();
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
        if (bathroomsCount.isEmpty()) {
            binding.textInputLayoutBathroomsCount.setError("Can't be empty");
            return;
        }
        if (Integer.parseInt(roomsCount) < Integer.parseInt(bathroomsCount)) {
            binding.textInputLayoutBathroomsCount.setError("Can't be superior than number of rooms");
            return;
        }
        if (bedroomsCount.isEmpty()) {
            binding.textInputLayoutBedroomsCount.setError("Can't be empty");
            return;
        }
        if (Integer.parseInt(roomsCount) < Integer.parseInt(bedroomsCount)) {
            binding.textInputLayoutBedroomsCount.setError("Can't be superior than number of rooms");
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
        if (mBitmapAndStringList.isEmpty()) {
            Snackbar.make(requireView(),"Pick at least 1 image", Toast.LENGTH_SHORT).show();
            return;
        }
        //String marketEntryDateString = Utils.convertDateToString(marketEntryDate);
        long marketEntryDateMillis = Utils.convertDateToLong(marketEntryDate);
        ArrayList<Media> mediaList = new ArrayList<>();
        for (BitmapAndString bitmapAndString : mBitmapAndStringList) {
            Uri storedImage = FileManager.createFile(bitmapAndString.getMedia(), requireActivity().getFilesDir());
            if (storedImage != null) {
                mediaList.add(new Media(storedImage.toString(), bitmapAndString.getMedia_description()));
            }
        }
        Property property = new Property(
                mPropertyType,
                Double.valueOf(price),
                Integer.parseInt(surface),
                Integer.parseInt(roomsCount),
                Integer.parseInt(bathroomsCount),
                Integer.parseInt(bedroomsCount),
                description, propertyPlace.getAddress(),
                propertyPlace.getLatitude(),
                propertyPlace.getLongitude(),
                PropertyStatus.AVAILABLE,
                marketEntryDateMillis,
                agent);
        nearbyPlacesRequest(property, mediaList);
    }

    private void emptyFieldSnackBar() {
        Snackbar.make(requireView(),"Verify the form", Toast.LENGTH_SHORT).show();
    }

    private void nearbyPlacesRequest(Property property, ArrayList<Media> mediaList) {
        binding.progressBar.setVisibility(View.VISIBLE);
        mPlacesViewModel.getPlacesMutableLiveData().observe(getViewLifecycleOwner(), places -> {
            if (places != null) {
                mPropertyViewModel.insertPropertyAndMediasAndPlaces(property, mediaList, places);
            }
        });
        mPropertyViewModel.getPropertyCreatedIdLiveData().observe(getViewLifecycleOwner(), id -> {
            binding.progressBar.setVisibility(View.GONE);
            notificationService.showNotification(id);
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        mPlacesViewModel.fetchPlaces(BuildConfig.MAPS_API_KEY, Utils.createLocationString(property.getLatitude(), property.getLongitude()));
    }

    @Override
    public void removeMedia(int index) {
        mBitmapAndStringList.remove(index);
        mPropertyMediasAdapter.setData(mBitmapAndStringList);
    }
}