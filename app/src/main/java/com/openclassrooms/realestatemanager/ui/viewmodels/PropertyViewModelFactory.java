package com.openclassrooms.realestatemanager.ui.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.Database;
import com.openclassrooms.realestatemanager.data.PropertyRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PropertyViewModelFactory implements ViewModelProvider.Factory {

    private static volatile PropertyViewModelFactory factory;
    private final PropertyRepository mPropertyRepository;
    private final Executor mExecutor;

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
        mPropertyRepository = new PropertyRepository(database.propertyDao(), database.mediaDao(), database.placeDao(), database.propertyPlaceDao(), database.searchDao());
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)) {
            return (T) new PropertyViewModel(mPropertyRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
