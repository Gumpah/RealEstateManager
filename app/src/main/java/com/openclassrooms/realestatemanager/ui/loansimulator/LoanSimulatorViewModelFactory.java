package com.openclassrooms.realestatemanager.ui.loansimulator;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LoanSimulatorViewModelFactory implements ViewModelProvider.Factory {

    private static volatile LoanSimulatorViewModelFactory factory;

    public static LoanSimulatorViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (LoanSimulatorViewModelFactory.class) {
                if (factory == null) {
                    factory = new LoanSimulatorViewModelFactory();
                }
            }
        }
        return factory;
    }

    private LoanSimulatorViewModelFactory() {
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoanSimulatorViewModel.class)) {
            return (T) new LoanSimulatorViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}