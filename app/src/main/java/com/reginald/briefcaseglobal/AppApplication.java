package com.reginald.briefcaseglobal;

/**
 * Created by Reginald on 31/08/2018.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppApplication extends Application {

    private static AppApplication appInstance;

    static final int Location_Service_ID = 175;
    static final String ACTION_START_LOCATION_SERVICE = "startLocationService";

    private static Context sContext;
    public static final String KEY_SERVICE = "service";
    private static boolean success = false;
    public static boolean hasInternet = false;
    private String IP;
    private String UserId;
    private String LocationId;
    private  String dimsIp;
    private  String images;


    public String getIP() {

        return IP;
    }
    public String getDIMSIP() {

        return dimsIp;
    }
    public void setIP(String aIP) {

        IP = aIP;

    }
    public void setDIMSIP(String adimsIp) {

        dimsIp = adimsIp;

    }
    public void setImages(String rimages) {

        images = rimages;

    }
    public String getUserId() {

        return UserId;
    }
    public void setUserId(String aUserId) {

        UserId = aUserId;

    }
    public void setLocation(String aLocationId) {

        LocationId = aLocationId;

    }
    public String getLocation() {

        return LocationId;
    }
    public String getImages() {

        return images;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appInstance;
    }


    public static String getCurrenDatetime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh.mm.ss");
        return sdf.format(c.getTime());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    public static boolean isInternetWorking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://google.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.connect();
                    success = connection.getResponseCode() == 200;
                    hasInternet = success;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return success;
    }

    public static boolean isServiceRunning() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEY_SERVICE, false);
    }

    public static void serviceRunning(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEY_SERVICE, type).apply();
    }


    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferenceManager() {
        return getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);
    }

    public static void clearSettings() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().clear().commit();
    }
}