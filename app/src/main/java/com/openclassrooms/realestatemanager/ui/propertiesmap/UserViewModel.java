package com.openclassrooms.realestatemanager.ui.propertiesmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.utils.NetworkMonitoring;
import com.openclassrooms.realestatemanager.utils.NetworkStateManager;

public class UserViewModel extends ViewModel {

    public NetworkMonitoring mNetworkMonitoring;
    public MutableLiveData<Boolean> locationPermissionStatus;

    public UserViewModel(NetworkMonitoring networkMonitoring) {
        mNetworkMonitoring = networkMonitoring;
        mNetworkMonitoring.checkNetworkState();
        mNetworkMonitoring.registerNetworkCallbackEvents();
        locationPermissionStatus = new MutableLiveData<>();
    }

    public LiveData<Boolean> getConnectionStatus() {
        return NetworkStateManager.getInstance().getNetworkConnectivityStatus();
    }

    public MutableLiveData<Boolean> getLocationPermissionStatus() {
        return locationPermissionStatus;
    }
}
