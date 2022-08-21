package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UtilsTest {

    @Test
    public void isInternetAvailableWhenOnline() throws InterruptedException {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");
        Thread.sleep(5000);

        boolean isInternetAvailableActual = Utils.isInternetAvailable(instrumentationContext);

        assertTrue(isInternetAvailableActual);
    }

    @Test
    public void isInternetAvailableWhenOffline() throws InterruptedException {
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");
        Thread.sleep(2000);


        boolean isInternetAvailableActual = Utils.isInternetAvailable(instrumentationContext);
        assertFalse(isInternetAvailableActual);
    }
}