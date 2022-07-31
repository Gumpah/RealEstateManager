package com.openclassrooms.realestatemanager.ui.addproperty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.PropertyType;
import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsPagerAdapter;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.FileManager;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;

public class AddPropertyFragment extends Fragment implements AddPropertyCallback {

    private FragmentAddPropertyBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private PlacesViewModel mPlacesViewModel;
    private ArrayList<Bitmap> mBitmapList;
    private RecyclerView mRecyclerView;
    private AddPropertyMediasAdapter mAddPropertyMediasAdapter;
    private PropertyDetailsPagerAdapter mPropertyDetailsPagerAdapter;
    private String mPropertyType;

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
        configureViewModel();
        initRecyclerView();
        setMediaClickListener();
        setClickListener();
        initSpinner();
        //mPlacesViewModel.fetchPlaces(BuildConfig.MAPS_API_KEY, "48.8335697,2.2553826");
        return binding.getRoot();
    }

    private void configureViewModel() {
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
        binding.buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void setMediaClickListener() {
        binding.imageButtonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                uri.launch(i);
            }
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
        ArrayList<String> propertyTypesTranslated = Utils.getPropertyTypesInUserLanguage(requireContext(), PropertyType.types);
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
        binding.autoCompleteTextViewPropertyType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
            }
        });
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        //Adapter adapter = adapterView.getAdapter();
        mPropertyType = PropertyType.types.get(position);
        System.out.println("Item selected");
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
        String address = "";
        if (binding.textInputLayoutAddress.getEditText() != null) {
            binding.textInputLayoutAddress.setErrorEnabled(false);
            address = binding.textInputLayoutAddress.getEditText().getText().toString();
        }
        String marketEntryDate = "";
        if (binding.textInputLayoutMarketEntryDate.getEditText() != null) {
            binding.textInputLayoutMarketEntryDate.setErrorEnabled(false);
            marketEntryDate = binding.textInputLayoutMarketEntryDate.getEditText().getText().toString();
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
        if (address.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (marketEntryDate.isEmpty()) {
            binding.textInputLayoutMarketEntryDate.setError("Can't be empty");
            return;
        }
        if (agent.isEmpty()) {
            binding.textInputLayoutAgent.setError("Can't be empty");
            return;
        }
        ArrayList<Media> mediaList = new ArrayList<>();
        for (Bitmap bitmap : mBitmapList) {
            Uri storedImage = FileManager.createFile(bitmap, requireActivity().getFilesDir());
            if (storedImage != null) {
                mediaList.add(new Media(storedImage.toString(), "Image"));
            }
        }
        Property property = new Property(mPropertyType, Double.valueOf(price), Integer.parseInt(surface), Integer.parseInt(roomsCount), description, address, PropertyStatus.AVAILABLE, marketEntryDate, agent);
        mPropertyViewModel.insertPropertyAndMedias(property, mediaList);
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void removeMedia(int index) {
        mBitmapList.remove(index);
        mAddPropertyMediasAdapter.setData(mBitmapList);
    }
}