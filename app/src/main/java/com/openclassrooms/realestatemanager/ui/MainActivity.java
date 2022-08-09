package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.propertieslist.PropertiesListFragment;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PropertyViewModel mPropertyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        initUI(savedInstanceState);
        initGetPropertyByIdListener();
    }

    private void initUI(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        System.out.println("TestIdMain");
        if (extras != null && extras.getLong("PropertyId", 0) != 0) {
            long id = extras.getLong("PropertyId", 0);
            System.out.println("TestId2 :" + id);
            mPropertyViewModel.getPropertyByIdContentProvider(getContentResolver(), id);
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new PropertiesListFragment(), "PropertiesList")
                    .commit();
        }
    }

    private void initGetPropertyByIdListener() {
        mPropertyViewModel.getPropertyByIdLiveData().observe(this, property -> {
            getIntent().removeExtra("PropertyId");
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new PropertyDetailsFragment(property), "PropertyDetails")
                    .addToBackStack("PropertyDetails")
                    .commit();
        });
    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(this)).get(PropertyViewModel.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}
