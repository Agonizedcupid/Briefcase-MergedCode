package com.reginald.briefcaseglobal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pk.codebase.requests.FormData;
import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class NetworkChangedReceiver extends BroadcastReceiver {

    private Context mContext;

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    private AlarmReciever alarmReciever;
    private int id = 0;



    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        AppApplication.isInternetWorking();
        new android.os.Handler().postDelayed(() -> {
            try {
                if (AppApplication.isNetworkAvailable(context)) {
                    AppApplication.hasInternet = true;
                    startRepeatingTask();
                    Log.e("NetworkAvailable", "-------------");

                } else {
                    AppApplication.hasInternet = false;
                    stopRepeatingTask();
                    Log.e("Network Error", "-------------");
//
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }, 10000);
    }


    void stopRepeatingTask() {
        if (alarmReciever != null) {
            AppApplication.getContext().unregisterReceiver(alarmReciever);
            alarmReciever = null;
            alarmMgr.cancel(pendingIntent);
        }
    }

    void startRepeatingTask() {
        startAlarmManager();
    }

    public void startAlarmManager() {
        if (alarmReciever != null) {
            return;
        }
        alarmReciever = new AlarmReciever();
        IntentFilter filter = new IntentFilter("fire");
        AppApplication.getContext().registerReceiver(alarmReciever, filter);
        Intent dialogIntent = new Intent("fire");
        alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(mContext, id, dialogIntent, PendingIntent.FLAG_IMMUTABLE);
        Calendar time = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        Date date = new Date();
        time.setTime(date);
        cal_now.setTime(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 60000, pendingIntent);
        } else if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, 6000, 60000, pendingIntent);
        } else {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 60000, pendingIntent);
        }
    }

    private void getPackagesList() {
        new Thread(() -> {

        }).start();
    }

    public void pushToServer(String nameString, String ameId) {
       /* HttpRequest request = new HttpRequest();
        request.setOnResponseListener(response -> {
            if (response.code == HttpResponse.HTTP_OK) {
                Log.e("HTTP_OK", " " + "----------------------- ");

            } else if (response.code == HttpResponse.HTTP_BAD_REQUEST) {
                Log.e("HTTP_BAD_REQUEST", " " + "----------------------- ");
            } else if (response.code == HttpResponse.HTTP_NOT_FOUND) {
                Log.e("HTTP_NOT_FOUND", " " + "----------------------- ");
            }
        });
        request.setOnErrorListener(error -> {
            switch (error.code) {
                case HttpError.CONNECTION_TIMED_OUT:
                    break;
                case HttpError.NETWORK_UNREACHABLE:
                    break;
                case HttpError.INVALID_URL:
                    break;
                case HttpError.UNKNOWN:
                    break;
            }
        });
        FormData formData;
        formData = new FormData();
        formData.put("string", nameString);
        formData.put("id", ameId);
        request.post("http://102.37.0.48/driversappdemo/poststring.php", formData);*/
    }

    class AlarmReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar time = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            Date date = new Date();
            time.setTime(date);
            cal_now.setTime(date);
            time.set(Calendar.MILLISECOND, 60000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {

            } else {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis() + 60000, pendingIntent);
            }
            getPackagesList();
        }
    }
}
