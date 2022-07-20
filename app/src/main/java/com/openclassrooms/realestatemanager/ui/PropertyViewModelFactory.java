package com.openclassrooms.realestatemanager.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.Database;
import com.openclassrooms.realestatemanager.data.PropertyRepository;

import org.jetbrains.annotations.NotNull;

public class PropertyViewModelFactory implements ViewModelProvider.Factory {

    private static PropertyViewModelFactory factory;
    private final PropertyRepository mPropertyRepository;

    public static PropertyViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (PropertyViewModelFactory.class) {
                if (factory == null) {
                    factory = new PropertyViewModelFactory(context.getApplicationContext());
                }
            }
        }
        return factory;
    }

    private PropertyViewModelFactory(Context context) {
        Database database = Database.getDatabase(context);
        mPropertyRepository = new PropertyRepository(database.propertyDao());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)) {
            return (T) new PropertyViewModel(mPropertyRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
