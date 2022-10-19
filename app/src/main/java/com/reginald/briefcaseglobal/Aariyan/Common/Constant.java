package com.reginald.briefcaseglobal.Aariyan.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Constant {
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            Log.d("NETWORK_STATE", "isInternetConnected: "+true);
            return true;
        }
        Log.d("NETWORK_STATE", "isInternetConnected: "+false);
        return false;

    }
}
