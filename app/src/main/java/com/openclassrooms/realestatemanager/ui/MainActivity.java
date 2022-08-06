package com.openclassrooms.realestatemanager.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.propertieslist.PropertiesListFragment;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.UserViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel mUserViewModel;

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
        mUserViewModel = new ViewModelProvider(this, UserViewModelFactory.getInstance(this)).get(UserViewModel.class);
    }
}
