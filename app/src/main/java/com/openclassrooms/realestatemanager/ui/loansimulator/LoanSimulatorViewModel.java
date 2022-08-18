package com.openclassrooms.realestatemanager.ui.loansimulator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoanSimulatorViewModel extends ViewModel {

    public MutableLiveData<String> loanSimulatorResult;

    public LoanSimulatorViewModel() {
        loanSimulatorResult = new MutableLiveData<>();
    }

    public LiveData<String> getLoanSimulatorResult() {
        return loanSimulatorResult;
    }

    public long getLoanAmount(long propertyPrice, long downPayment) {
        return propertyPrice-downPayment;
    }

    public double getMonthlyInterestRate(double interestRatePercent) {
        return (interestRatePercent/100)/12;
    }

    public long getTotalNumberOfPayments(long loanNumberOfYears) {
        return loanNumberOfYears*12;
    }

    public int mortgageCalculator(long propertyPrice, long downPayment, long loanNumberOfYears, double interestRatePercent) {
        long loanAmount = getLoanAmount(propertyPrice, downPayment);
        double monthlyInterestRate = getMonthlyInterestRate(interestRatePercent);
        long totalNumberOfPayments = getTotalNumberOfPayments(loanNumberOfYears);
        double a = (1+monthlyInterestRate);
        double b = Math.pow(a, totalNumberOfPayments);
        double monthlyMortgagePayment = loanAmount*((monthlyInterestRate*b)/(b-1));
        return (int) Math.round(monthlyMortgagePayment);
    }

    public void mortgageDataProcess(CharSequence propertyPriceChar, CharSequence downPaymentChar, CharSequence loanNumberOfYearsChar, CharSequence interestRatePercentChar) {
        long propertyPrice = 0;
        long downPayment = 0;
        long loanNumberOfYears = 0;
        double interestRatePercent = 0;
        if (isCharSequenceNotNullOrEmpty(propertyPriceChar)) propertyPrice = Long.parseLong(propertyPriceChar.toString());
        if (isCharSequenceNotNullOrEmpty(downPaymentChar)) downPayment = Long.parseLong(downPaymentChar.toString());
        if (isCharSequenceNotNullOrEmpty(loanNumberOfYearsChar)) loanNumberOfYears = Long.parseLong(loanNumberOfYearsChar.toString());
        if (isCharSequenceNotNullOrEmpty(interestRatePercentChar)) interestRatePercent = Double.parseDouble(interestRatePercentChar.toString());

        String mortgageText = "";
        if (propertyPrice > 0 && loanNumberOfYears > 0 && interestRatePercent > 0) {
            int mortgage = mortgageCalculator(propertyPrice, downPayment, loanNumberOfYears, interestRatePercent);
            mortgageText = String.valueOf(mortgage);
        }
        loanSimulatorResult.postValue(mortgageText);
    }

    public boolean isCharSequenceNotNullOrEmpty(CharSequence charSequence) {
        return (charSequence != null && !charSequence.toString().isEmpty());
    }
}
