package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;

import com.jraska.livedata.TestObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NetworkStateManagerTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    NetworkMonitoring mNetworkMonitoring;

    @Before
    public void setUp() throws Exception {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mNetworkMonitoring = new NetworkMonitoring(instrumentationContext);
        mNetworkMonitoring.checkNetworkState();
        mNetworkMonitoring.registerNetworkCallbackEvents();
    }

    @Test
    public void isInternetAvailableWhenOnline() throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");
        Thread.sleep(5000);
        LiveData<Boolean> connectionStatus = NetworkStateManager.getInstance().getNetworkConnectivityStatus();

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        TestObserver.test(connectionStatus)
                .awaitNextValue()
                .assertValue(true);
    }

    @Test
    public void isInternetAvailableWhenOffline() throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        Thread.sleep(5000);
        LiveData<Boolean> connectionStatus = NetworkStateManager.getInstance().getNetworkConnectivityStatus();
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");
        TestObserver.test(connectionStatus)
                .awaitNextValue()
                .assertValue(false);
    }

}