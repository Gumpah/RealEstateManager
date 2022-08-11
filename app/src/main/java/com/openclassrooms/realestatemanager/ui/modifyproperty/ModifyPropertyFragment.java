package com.openclassrooms.realestatemanager.ui.modifyproperty;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.BitmapAndString;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Place;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyType;
import com.openclassrooms.realestatemanager.databinding.DialogAddImageBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentModifyPropertyBinding;
import com.openclassrooms.realestatemanager.ui.AddAndModifyPropertyCallback;
import com.openclassrooms.realestatemanager.ui.PropertyMediasAdapter;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModelFactory;
import com.openclassrooms.realestatemanager.utils.FileManager;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ModifyPropertyFragment extends Fragment implements AddAndModifyPropertyCallback {

    private static final String PROPERTY_ID_ARG = "PropertyId";

    private final int MARKET_ENTRY_DATE = 1;
    private final int SOLD_DATE = 2;

    private FragmentModifyPropertyBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private PlacesViewModel mPlacesViewModel;
    private UserViewModel mUserViewModel;

    private RecyclerView mRecyclerView;
    private PropertyMediasAdapter mPropertyMediasAdapter;

    private Property mProperty;
    private ArrayList<String> propertyTypesTranslated;

    //Property attributes
    private ArrayList<BitmapAndString> mBitmapAndStringList;
    private String mPropertyType;
    private Date marketEntryDate;
    private Date soldDate;
    private Place propertyPlace;


    public ModifyPropertyFragment() {
        // Required empty public constructor
    }

    public static ModifyPropertyFragment newInstance(long propertyId) {
        ModifyPropertyFragment fragment = new ModifyPropertyFragment();
        Bundle args = new Bundle();
        args.putLong(PROPERTY_ID_ARG, propertyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentModifyPropertyBinding.inflate(inflater, container, false);
        mBitmapAndStringList = new ArrayList<>();
        configureViewModels();
        initRecyclerView();
        initGetPropertyByIdListener();
        initSpinner();
        initDateButton();
        setMediaFromGalleryClickListener();
        setMediaFromCameraClickListener();
        setModifyButtonClickListener();
        propertyUpdatedListener();
        return binding.getRoot();
    }

    private void configureViewModels() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(requireContext())).get(PropertyViewModel.class);
        mPlacesViewModel = new ViewModelProvider(this, PlacesViewModelFactory.getInstance()).get(PlacesViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity(), UserViewModelFactory.getInstance(requireContext())).get(UserViewModel.class);
    }

    private void propertyUpdatedListener() {
        mPropertyViewModel.getPropertyUpdateLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result) requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void initGetPropertyByIdListener() {
        mPropertyViewModel.getPropertyByIdLiveData().observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                mProperty = property;
                initGetPropertyMediasListener();
                initUIData(property);
            }
        });
        if (getArguments() != null && getArguments().getLong(PROPERTY_ID_ARG, 0) != 0) {
            long propertyId = getArguments().getLong(PROPERTY_ID_ARG, 0);
            mPropertyViewModel.getPropertyByIdContentProvider(requireContext().getContentResolver(), propertyId);
        }
    }

    private void initGetPropertyMediasListener() {
        mPropertyViewModel.getMediasByPropertyIdLiveData().observe(getViewLifecycleOwner(), medias -> {
            for (Media media : medias) {
                Bitmap bitmap = FileManager.convertUriToBitmap(Uri.parse(media.getMedia_uri()), requireContext().getContentResolver());
                if (bitmap != null) mBitmapAndStringList.add(new BitmapAndString(bitmap, media.getName()));
            }
            mPropertyMediasAdapter.setData(mBitmapAndStringList);
        });
        mPropertyViewModel.getMediasByPropertyIdContentProvider(requireContext().getContentResolver(), mProperty.getId());
    }

    private void initUIData(Property property) {
        mPropertyType = property.getProperty_type();
        marketEntryDate = Utils.convertLongToDate(property.getMarket_entry());
        if (PropertyStatus.SOLD.equals(property.getStatus())) soldDate = Utils.convertLongToDate(property.getSold());
        propertyPlace = new Place("a", null, property.getLatitude(), property.getLongitude(), property.getAddress(), null);

        if (binding.textInputLayoutPropertyType.getEditText() != null) binding.textInputLayoutPropertyType.getEditText().setText(Utils.getTypeInUserLanguage(requireContext(), mPropertyType));
        if (binding.textInputLayoutPrice.getEditText() != null) binding.textInputLayoutPrice.getEditText().setText(String.valueOf(property.getPrice()));
        if (binding.textInputLayoutSurface.getEditText() != null) binding.textInputLayoutSurface.getEditText().setText(String.valueOf(property.getSurface()));
        if (binding.textInputLayoutRoomsCount.getEditText() != null) binding.textInputLayoutRoomsCount.getEditText().setText(String.valueOf(property.getRooms_count()));
        if (binding.textInputLayoutBathroomsCount.getEditText() != null) binding.textInputLayoutBathroomsCount.getEditText().setText(String.valueOf(property.getBathrooms_count()));
        if (binding.textInputLayoutBedroomsCount.getEditText() != null) binding.textInputLayoutBedroomsCount.getEditText().setText(String.valueOf(property.getBedrooms_count()));
        if (binding.textInputLayoutDescription.getEditText() != null) binding.textInputLayoutDescription.getEditText().setText(property.getDescription());
        binding.textViewAddress.setText(property.getAddress());
        binding.buttonMarketEntryDate.setText(Utils.convertDateToString(marketEntryDate));
        if (soldDate != null) {
            binding.buttonSoldDate.setText(Utils.convertDateToString(soldDate));
            binding.buttonSoldDate.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green));
        }
        if (binding.textInputLayoutAgent.getEditText() != null) binding.textInputLayoutAgent.getEditText().setText(property.getAgent());
    }

    private void initSpinner() {
        propertyTypesTranslated = Utils.getTypesInUserLanguage(requireContext(), PropertyType.types);
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

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        mPropertyType = PropertyType.types.get(position);
    }

    private void initDateButton() {
        binding.buttonMarketEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(MARKET_ENTRY_DATE);
            }
        });
        binding.buttonSoldDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDateDialog(SOLD_DATE);
            }
        });
    }

    private void displayDateDialog(int selectedButton) {
        Calendar calendar = Calendar.getInstance();
        switch (selectedButton) {
            case MARKET_ENTRY_DATE:
                if (marketEntryDate != null) calendar.setTime(marketEntryDate);
                break;
            case SOLD_DATE:
                if (soldDate != null) calendar.setTime(soldDate);
                break;
        }
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Picker_Date, getOnDateSetListener(selectedButton), selectedYear, selectedMonth, selectedDayOfMonth);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -14);
        long currentTime = cal.getTimeInMillis();
        datePickerDialog.getDatePicker().setMinDate(currentTime);

        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener getOnDateSetListener(int selectedButton) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                switch (selectedButton) {
                    case MARKET_ENTRY_DATE:
                        marketEntryDate = cal.getTime();
                        binding.buttonMarketEntryDate.setText(Utils.convertDateToString(marketEntryDate));
                        break;
                    case SOLD_DATE:
                        soldDate = cal.getTime();
                        binding.buttonSoldDate.setText(Utils.convertDateToString(soldDate));
                        if (soldDate != null) binding.buttonSoldDate.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green));
                        break;
                }
            }
        };
    }

    private void setMediaFromGalleryClickListener() {
        binding.imageButtonAddImageGallery.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            uriGallery.launch(i);
        });
    }

    private void setMediaFromCameraClickListener() {
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

    private void initRecyclerView() {
        mRecyclerView = binding.recyclerViewImages;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mPropertyMediasAdapter = new PropertyMediasAdapter(new ArrayList<>(), this);
        mRecyclerView.setAdapter(mPropertyMediasAdapter);
    }

    private void setModifyButtonClickListener() {
        binding.buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
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
                description,
                propertyPlace.getAddress(),
                propertyPlace.getLatitude(),
                propertyPlace.getLongitude(),
                PropertyStatus.AVAILABLE,
                marketEntryDateMillis,
                agent);
        if (soldDate != null) {
            property.setSold(Utils.convertDateToLong(soldDate));
            property.setStatus(PropertyStatus.SOLD);
        }
        property.setId(mProperty.getId());
        mPropertyViewModel.updatePropertyAndContent(requireContext().getContentResolver(), property, mediaList);
    }

    @Override
    public void removeMedia(int index) {
        mBitmapAndStringList.remove(index);
        mPropertyMediasAdapter.setData(mBitmapAndStringList);
    }
}