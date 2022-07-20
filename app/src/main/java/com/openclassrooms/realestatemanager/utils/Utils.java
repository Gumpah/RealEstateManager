package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.97776);
    }

    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros * 1.02275);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public boolean isInternetAvailable (Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Network network = mConnectivityManager.getActiveNetwork();
                NetworkCapabilities actNw = mConnectivityManager.getNetworkCapabilities(network);
                return (network != null && actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            try {
                android.net.NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
                return (networkInfo != null && networkInfo.isConnected());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }
}
