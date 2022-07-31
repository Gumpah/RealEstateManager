package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.model.PropertyType;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.propertieslist.PropertiesListFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PropertyViewModel mPropertyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI(savedInstanceState);
        configureViewModel();
    }

    private void initUI(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new PropertiesListFragment(), "PropertiesList")
                    .commit();
        }

    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(this)).get(PropertyViewModel.class);
        Utils.getPropertyTypesInUserLanguage(this, PropertyType.types);
    }
}
