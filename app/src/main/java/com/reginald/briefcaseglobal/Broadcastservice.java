package com.reginald.briefcaseglobal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Broadcastservice extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isInternetConnected(context)) {

                 intent = new Intent(context, DriverService.class);
                context.startService(intent);

        } else {
            Log.d("TEST_INTERNET", "onReceive: No Internet");
        }
    }
    public static boolean isInternetConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info!= null) {
                for (int i=0; i<info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
