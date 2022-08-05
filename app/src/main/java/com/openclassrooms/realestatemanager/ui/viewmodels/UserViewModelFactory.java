package com.openclassrooms.realestatemanager.ui.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.utils.NetworkMonitoring;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private static volatile UserViewModelFactory factory;
    private final NetworkMonitoring mNetworkMonitoring;

    public static UserViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (UserViewModelFactory.class) {
                if (factory == null) {
                    factory = new UserViewModelFactory(context.getApplicationContext());
                }
            }
        }
        return factory;
    }

    private UserViewModelFactory(Context context) {
        mNetworkMonitoring = new NetworkMonitoring(context);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mNetworkMonitoring);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
