package com.openclassrooms.realestatemanager.ui.loansimulator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentLoanSimulatorBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentPropertiesListBinding;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModel;
import com.openclassrooms.realestatemanager.ui.viewmodels.PlacesViewModelFactory;
import com.openclassrooms.realestatemanager.utils.Utils;

public class LoanSimulatorFragment extends Fragment {

    private FragmentLoanSimulatorBinding binding;
    private LoanSimulatorViewModel mLoanSimulatorViewModel;

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
        configureViewModel();
        initMortgageTextListener();
        initHomePriceTextChangeListener();
        initDownPaymentTextChangeListener();
        initLoanLengthTextChangeListener();
        initInterestRateTextChangeListener();
        return binding.getRoot();
    }

    private void configureViewModel() {
        mLoanSimulatorViewModel = new ViewModelProvider(this, LoanSimulatorViewModelFactory.getInstance()).get(LoanSimulatorViewModel.class);
    }

    private void initMortgageTextListener() {
        mLoanSimulatorViewModel.getLoanSimulatorResult().observe(getViewLifecycleOwner(), text -> {
            binding.textViewSimulatorResult.setText(text);
        });
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
        mLoanSimulatorViewModel.mortgageDataProcess(binding.editTextHomePrice.getText(), binding.editTextDownPayment.getText(), binding.editTextLoanLength.getText(), binding.editTextInterestRate.getText());
    }

}