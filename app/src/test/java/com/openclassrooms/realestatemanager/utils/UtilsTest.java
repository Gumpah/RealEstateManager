package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void convertDollarToEuro() {
        int dollars = 100;
        int expected_euros = 98;
        int actual_euros = Utils.convertDollarToEuro(dollars);

        assertEquals(expected_euros, actual_euros);
    }

    @Test
    public void convertEuroToDollar() {
        int euros = 98;
        int expected_dollars = 100;
        int actual_dollars = Utils.convertEuroToDollar(euros);

        assertEquals(expected_dollars, actual_dollars);
    }

    @Test
    public void getTodayDate() {
        Calendar cal = Calendar.getInstance();
        String dayOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) dayOfMonth = ("0" + dayOfMonth);
        if (cal.get(Calendar.MONTH) + 1 < 10) month = ("0" + month);

        String expected_date = (dayOfMonth + "/" + month + "/" + year);
        String actual_date = Utils.getTodayDate();

        assertEquals(expected_date, actual_date);
    }
}