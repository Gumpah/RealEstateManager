package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.data.PlacesStreams;

public class PlacesViewModelFactory implements ViewModelProvider.Factory {

    private static volatile PlacesViewModelFactory factory;
    private final PlacesStreams mPlacesStreams;

    public static PlacesViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (PlacesViewModelFactory.class) {
                if (factory == null) {
                    factory = new PlacesViewModelFactory();
                }
            }
        }
        return factory;
    }

    private PlacesViewModelFactory() {
        mPlacesStreams = new PlacesStreams();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PlacesViewModel.class)) {
            return (T) new PlacesViewModel(mPlacesStreams);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}