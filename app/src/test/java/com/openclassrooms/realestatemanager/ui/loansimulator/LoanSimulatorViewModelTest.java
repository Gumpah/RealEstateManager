package com.openclassrooms.realestatemanager.ui.loansimulator;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jraska.livedata.TestObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;


public class LoanSimulatorViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    LoanSimulatorViewModel mLoanSimulatorViewModel;

    @Before
    public void setUp() throws Exception {
        mLoanSimulatorViewModel = new LoanSimulatorViewModel();
    }

    @Test
    public void getLoanAmount() {
        long propertyPrice = 5;
        long downPayment = 2;
        long loanAmountExpected = 3;
        long loanAmountActual = mLoanSimulatorViewModel.getLoanAmount(propertyPrice, downPayment);

        assertEquals(loanAmountExpected, loanAmountActual);
    }

    @Test
    public void getMonthlyInterestRate() {
        double delta = 1e-15;
        double interestRatePercent = 6;
        double monthlyInterestRateExpected = 0.005;
        double monthlyInterestRateActual = mLoanSimulatorViewModel.getMonthlyInterestRate(interestRatePercent);

        assertEquals(monthlyInterestRateExpected, monthlyInterestRateActual, delta);
    }

    @Test
    public void getTotalNumberOfPayments() {
        long numberOfYears = 5;

        long totalNumberOfPaymentsExpected = 60;
        long totalNumberOfPaymentsActual = mLoanSimulatorViewModel.getTotalNumberOfPayments(numberOfYears);

        assertEquals(totalNumberOfPaymentsExpected, totalNumberOfPaymentsActual);
    }

    @Test
    public void mortgageCalculator() {
        double delta = 1e-15;
        double mortgageExpected = 1591;
        double mortgageActual = mLoanSimulatorViewModel.mortgageCalculator(200000, 50000, 10, 5);

        assertEquals(mortgageExpected, mortgageActual, delta);
    }


    @Test
    public void mortgageDataProcess() throws InterruptedException {
        CharSequence propertyPriceChar = "200000";
        CharSequence downPaymentChar = "50000";
        CharSequence loanNumberOfYearsChar = "10";
        CharSequence interestRatePercentChar = "5";

        mLoanSimulatorViewModel.mortgageDataProcess(propertyPriceChar, downPaymentChar, loanNumberOfYearsChar, interestRatePercentChar);

        String mortgageStringExpected = "1591";
        TestObserver.test(mLoanSimulatorViewModel.getLoanSimulatorResult())
                .awaitValue()
                .assertValue(mortgageStringExpected);
    }

    @Test
    public void isCharSequenceNotNullOrEmpty() {
        boolean booleanActual = mLoanSimulatorViewModel.isCharSequenceNotNullOrEmpty(null);
        assertFalse(booleanActual);

        CharSequence charSequence = "";
        booleanActual = mLoanSimulatorViewModel.isCharSequenceNotNullOrEmpty(charSequence);
        assertFalse(booleanActual);

        charSequence = "a";
        booleanActual = mLoanSimulatorViewModel.isCharSequenceNotNullOrEmpty(charSequence);
        assertTrue(booleanActual);
    }
}