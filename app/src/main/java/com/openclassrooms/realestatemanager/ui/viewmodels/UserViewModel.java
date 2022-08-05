package com.openclassrooms.realestatemanager.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.utils.NetworkMonitoring;
import com.openclassrooms.realestatemanager.utils.NetworkStateManager;

public class UserViewModel extends ViewModel {

    public NetworkMonitoring mNetworkMonitoring;

    public UserViewModel(NetworkMonitoring networkMonitoring) {
        mNetworkMonitoring = networkMonitoring;
        mNetworkMonitoring.checkNetworkState();
        mNetworkMonitoring.registerNetworkCallbackEvents();
    }

    public LiveData<Boolean> getConnectionStatus() {
        return NetworkStateManager.getInstance().getNetworkConnectivityStatus();
    }
}
