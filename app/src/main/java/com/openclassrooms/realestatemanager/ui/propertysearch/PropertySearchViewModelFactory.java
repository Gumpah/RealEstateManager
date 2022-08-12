package com.openclassrooms.realestatemanager.ui.propertysearch;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.Database;
import com.openclassrooms.realestatemanager.data.PropertyRepository;
import com.openclassrooms.realestatemanager.data.PropertySearchRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PropertySearchViewModelFactory implements ViewModelProvider.Factory {

    private static volatile PropertySearchViewModelFactory factory;
    private final PropertySearchRepository mPropertySearchRepository;
    private final Executor mExecutor;

    public static PropertySearchViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (PropertySearchViewModelFactory.class) {
                if (factory == null) {
                    factory = new PropertySearchViewModelFactory();
                }
            }
        }
        return factory;
    }

    private PropertySearchViewModelFactory() {
        mPropertySearchRepository = new PropertySearchRepository();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertySearchViewModel.class)) {
            return (T) new PropertySearchViewModel(mPropertySearchRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
