package com.openclassrooms.realestatemanager.ui.addproperty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.data.model.entities.Media;
import com.openclassrooms.realestatemanager.data.model.entities.Property;
import com.openclassrooms.realestatemanager.data.model.entities.PropertyStatus;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.FileManager;

import java.util.ArrayList;

public class AddPropertyFragment extends Fragment implements AddPropertyCallback {

    private FragmentAddPropertyBinding binding;
    private PropertyViewModel mPropertyViewModel;
    private PlacesViewModel mPlacesViewModel;
    private ArrayList<Bitmap> mBitmapList;
    private RecyclerView mRecyclerView;
    private AddPropertyMediasAdapter mAddPropertyMediasAdapter;

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

    private void onSubmit() {
        String propertyType = "";
        if (binding.textInputLayoutPropertyType.getEditText() != null) {
            propertyType = binding.textInputLayoutPropertyType.getEditText().getText().toString();
        }
        String price = "";
        if (binding.textInputLayoutPrice.getEditText() != null) {
            price = binding.textInputLayoutPrice.getEditText().getText().toString();
        }
        String surface = "";
        if (binding.textInputLayoutSurface.getEditText() != null) {
            surface = binding.textInputLayoutSurface.getEditText().getText().toString();
        }
        String roomsCount = "";
        if (binding.textInputLayoutRoomsCount.getEditText() != null) {
            roomsCount = binding.textInputLayoutRoomsCount.getEditText().getText().toString();
        }
        String description = "";
        if (binding.textInputLayoutDescription.getEditText() != null) {
            description = binding.textInputLayoutDescription.getEditText().getText().toString();
        }
        String address = "";
        if (binding.textInputLayoutAddress.getEditText() != null) {
            address = binding.textInputLayoutAddress.getEditText().getText().toString();
        }
        String marketEntryDate = "";
        if (binding.textInputLayoutMarketEntryDate.getEditText() != null) {
            marketEntryDate = binding.textInputLayoutMarketEntryDate.getEditText().getText().toString();
        }
        String agent = "";
        if (binding.textInputLayoutAgent.getEditText() != null) {
            agent = binding.textInputLayoutAgent.getEditText().getText().toString();
        }

        if (address.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (agent.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (description.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (marketEntryDate.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (price.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (propertyType.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (roomsCount.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        if (surface.isEmpty()) {
            binding.textInputLayoutAddress.setError("Can't be empty");
            return;
        }
        ArrayList<Media> mediaList = new ArrayList<>();
        for (Bitmap bitmap : mBitmapList) {
            Uri storedImage = FileManager.createFile(bitmap, requireActivity().getFilesDir());
            if (storedImage != null) {
                mediaList.add(new Media(storedImage.toString(), "Image"));
            }
        }
        Property property = new Property(propertyType, Double.valueOf(price), Integer.parseInt(surface), Integer.parseInt(roomsCount), description, address, PropertyStatus.AVAILABLE, marketEntryDate, agent);
        mPropertyViewModel.insertPropertyAndMedias(property, mediaList);
        requireActivity().getSupportFragmentManager().popBackStack();
    }


    @Override
    public void removeMedia(int index) {
        mBitmapList.remove(index);
        mAddPropertyMediasAdapter.setData(mBitmapList);
    }
}