package com.openclassrooms.realestatemanager.ui.loansimulator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentLoanSimulatorBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

public class LoanSimulatorFragment extends Fragment {

    private FragmentLoanSimulatorBinding binding;

    public LoanSimulatorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoanSimulatorBinding.inflate(inflater, container, false);
        initHomePriceTextChangeListener();
        initDownPaymentTextChangeListener();
        initLoanLengthTextChangeListener();
        initInterestRateTextChangeListener();
        return binding.getRoot();
    }

    private void initHomePriceTextChangeListener() {
        binding.editTextHomePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateMonthlyPayment();
            }
        });
    }

    private void initDownPaymentTextChangeListener() {
        binding.editTextDownPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateMonthlyPayment();
            }
        });
    }

    private void initLoanLengthTextChangeListener() {
        binding.editTextLoanLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateMonthlyPayment();
            }
        });
    }

    private void initInterestRateTextChangeListener() {
        binding.editTextInterestRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateMonthlyPayment();
            }
        });
    }

    private void calculateMonthlyPayment() {
        long homePrice = 0;
        long downPayment = 0;
        long loanLength = 0;
        double interestRate = 0;
        if (binding.editTextHomePrice.getText() != null && !binding.editTextHomePrice.getText().toString().isEmpty()) {
            String homePriceString = binding.editTextHomePrice.getText().toString();
            homePrice = Long.parseLong(homePriceString);
        }
        if (binding.editTextDownPayment.getText() != null && !binding.editTextDownPayment.getText().toString().isEmpty()) {
            String downPaymentString = binding.editTextDownPayment.getText().toString();
            downPayment = Long.parseLong(downPaymentString);
        }
        if (binding.editTextLoanLength.getText() != null && !binding.editTextLoanLength.getText().toString().isEmpty()) {
            String loanLengthString = binding.editTextLoanLength.getText().toString();
            loanLength = Long.parseLong(loanLengthString);
        }
        if (binding.editTextInterestRate.getText() != null && !binding.editTextInterestRate.getText().toString().isEmpty()) {
            String interestRateString = binding.editTextInterestRate.getText().toString();
            interestRate = Double.parseDouble(interestRateString);
        }
        if (homePrice > 0 && loanLength > 0 && interestRate > 0) {
            binding.textViewSimulatorResult.setText(String.valueOf(Utils.mortgageCalculator(homePrice, downPayment, loanLength, interestRate)));
        } else {
            binding.textViewSimulatorResult.setText("");
        }
    }

}