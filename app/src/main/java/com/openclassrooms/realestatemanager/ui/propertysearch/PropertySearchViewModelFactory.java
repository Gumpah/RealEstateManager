package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.Database;
import com.openclassrooms.realestatemanager.data.PropertyRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PropertySearchViewModelFactory implements ViewModelProvider.Factory {

    private static volatile PropertySearchViewModelFactory factory;
    private final PropertyRepository mPropertyRepository;
    private final Executor mExecutor;

    public static PropertySearchViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (PropertySearchViewModelFactory.class) {
                if (factory == null) {
                    factory = new PropertySearchViewModelFactory(context.getApplicationContext());
                }
            }
        }
        return factory;
    }

    private PropertySearchViewModelFactory(Context context) {
        Database database = Database.getDatabase(context);
        mPropertyRepository = new PropertyRepository(database.propertyDao(), database.mediaDao(), database.placeDao(), database.propertyPlaceDao(), database.searchDao());
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertySearchViewModel.class)) {
            return (T) new PropertySearchViewModel(mPropertyRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
