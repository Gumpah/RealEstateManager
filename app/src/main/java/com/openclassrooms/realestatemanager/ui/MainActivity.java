package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.propertieslist.PropertiesListFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModelFactory;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel mUserViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI(savedInstanceState);
        /*
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout_fragmentContainer);
        if (navHostFragment != null) mNavController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(mNavController.getGraph()).build();
        setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(
                binding.toolbar, mNavController, appBarConfiguration);


        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (getSupportActionBar() == null) {
                    return;
                }
                if (navDestination.getId() == R.id.navigation_propertyList) {
                    System.out.println("Working");
                    getSupportActionBar().setTitle(getString(R.string.toolbar_propertyList_title));
                }
                if (navDestination.getId() == R.id.navigation_addProperty) {
                    getSupportActionBar().setTitle(getString(R.string.toolbar_addProperty_title));
                }
                if (navDestination.getId() == R.id.navigation_propertyDetails) {
                    getSupportActionBar().setTitle(getString(R.string.toolbar_propertyDetails_title));
                }
                if (navDestination.getId() == R.id.navigation_detailsMap) {
                    getSupportActionBar().setTitle(getString(R.string.toolbar_propertyDetailsMap_title));
                }
                if (navDestination.getId() == R.id.navigation_mediaDisplay) {
                    getSupportActionBar().setTitle(getString(R.string.toolbar_mediaDisplay_title));
                }
            }
        });
         */

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
        mUserViewModel = new ViewModelProvider(this, UserViewModelFactory.getInstance(this)).get(UserViewModel.class);
    }
}
