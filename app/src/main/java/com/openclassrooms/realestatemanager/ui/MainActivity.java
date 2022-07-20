package com.openclassrooms.realestatemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;
    private Button buttonMain;
    private Utils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);
        buttonMain = findViewById(R.id.button);

        mUtils = new Utils();

        configureTextViewMain();
        configureTextViewQuantity();
        setClickListener();
    }

    private void configureTextViewMain(){
        textViewMain.setTextSize(15);
        textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        String quantity = String.valueOf(Utils.convertDollarToEuro(100));
        textViewQuantity.setTextSize(20);
        textViewQuantity.setText(quantity);
    }

    private void setClickListener() {
        buttonMain.setOnClickListener(v -> {
            buttonMain.setText(String.valueOf(mUtils.isInternetAvailable(this)));
        });
    }
}
