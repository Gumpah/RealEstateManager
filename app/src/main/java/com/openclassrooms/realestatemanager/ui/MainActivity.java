package com.openclassrooms.realestatemanager.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.propertieslist.PropertiesListFragment;
import com.openclassrooms.realestatemanager.ui.propertiesmap.UserViewModel;
import com.openclassrooms.realestatemanager.ui.propertiesmap.UserViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertydetails.PropertyDetailsFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PropertyViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel mUserViewModel;
    private boolean internetJustDisconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        initUI(savedInstanceState);
        internetJustDisconnected = false;
        initNetworkStatus();
    }


    private void initNetworkStatus() {
        mUserViewModel.getConnectionStatus().observe(this, this::onNetworkStatusChange);
    }

    private void onNetworkStatusChange(boolean isConnected) {
        if (isConnected && internetJustDisconnected) {
            internetJustDisconnected = false;
            Snackbar.make(binding.getRoot(), R.string.internetConnected, Toast.LENGTH_SHORT).show();
        } else if (!isConnected) {
            internetJustDisconnected = true;
            Snackbar.make(binding.getRoot(), R.string.internetDisconnected, Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && extras.getLong("PropertyId", 0) != 0) {
            long id = extras.getLong("PropertyId", 0);
            startPropertyDetailsFragment(id);
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new PropertiesListFragment(), "PropertiesList")
                    .commit();
        }
    }

    private void startPropertyDetailsFragment(long propertyId) {
        PropertyDetailsFragment propertyDetailsFragment = new PropertyDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("PropertyId", propertyId);
        propertyDetailsFragment.setArguments(args);getIntent().removeExtra("PropertyId");
        getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_fragmentContainer, propertyDetailsFragment, "PropertyDetails")
                .addToBackStack("PropertyDetails")
                .commit();
    }

    private void configureViewModel() {
        mUserViewModel = new ViewModelProvider(this, UserViewModelFactory.getInstance(this)).get(UserViewModel.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}
