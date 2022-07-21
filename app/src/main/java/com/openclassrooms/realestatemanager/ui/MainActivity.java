package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ClickCallback {

    private ActivityMainBinding binding;
    private PropertyViewModel mPropertyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI(savedInstanceState);
        configureViewModel();
        setSupportActionBar(binding.toolbarMainActivity);
    }

    private void initUI(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new PropertiesListFragment(this), "PropertiesList")
                    .addToBackStack("PropertiesList")
                    .commit();
        }
    }

    private void configureViewModel() {
        mPropertyViewModel = new ViewModelProvider(this, PropertyViewModelFactory.getInstance(this)).get(PropertyViewModel.class);
    }

    @Override
    public void myClickCallback(String fragment) {
        if (Objects.equals(fragment, "propertiesList")) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout_fragmentContainer, new AddPropertyFragment(this), "AddProperty")
                    .addToBackStack("AddProperty")
                    .commit();
        }

        if (Objects.equals(fragment, "addProperty")) {
            System.out.println("test");
            getSupportFragmentManager().popBackStack();
        }
    }
}
