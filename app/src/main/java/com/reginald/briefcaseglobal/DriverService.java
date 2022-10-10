package com.reginald.briefcaseglobal;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pk.codebase.requests.FormData;
import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class DriverService extends Service {

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    public static String latitude;
    public static String longitude;

    private long LocationInterval = 60000;
    private long LocationFastestInterval = 60000;

    private BroadcastReceiver mNetworkReceiver;
    public static DriverService instance;
    private SQLiteDatabase db,dbOrders;

    public DriverService() {
        super();
    }

    public static DriverService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        startForeground(1, showNotification("briefcase App", startId));
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //startLocationUpdates();
        Log.e("Service Running","Is it really running**********");
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from Visits limit 1", null);
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();

        if (cursor2.moveToFirst()) {
            do {

                postDataUsingVolley(cursor2.getString(cursor2.getColumnIndex("CustomerCode")),
                        cursor2.getString(cursor2.getColumnIndex("nextvisit")),
                        cursor2.getString(cursor2.getColumnIndex("Lat")),
                        cursor2.getString(cursor2.getColumnIndex("Lon")),
                        cursor2.getString(cursor2.getColumnIndex("answeroneid")),
                        cursor2.getString(cursor2.getColumnIndex("answeronetext")),
                        cursor2.getString(cursor2.getColumnIndex("answertwoid")),
                        cursor2.getString(cursor2.getColumnIndex("answertwotext")),
                        cursor2.getString(cursor2.getColumnIndex("answerthreeid")),
                        cursor2.getString(cursor2.getColumnIndex("answerthreetext")),
                        cursor2.getString(cursor2.getColumnIndex("customersatisfactoyanswer")),
                        cursor2.getString(cursor2.getColumnIndex("userid")),
                        cursor2.getString(cursor2.getColumnIndex("catchupnotes")),
                        cursor2.getString(cursor2.getColumnIndex("notes")),
                        globalVariable.getIP(), cursor2.getString(cursor2.getColumnIndex("ID"))
                        );

            } while (cursor2.moveToNext());
        }


        mNetworkReceiver = new NetworkChangedReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        AppApplication.serviceRunning(true);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        AppApplication.serviceRunning(false);
        Log.e("stop","###################stopped services");
        stopSelf();

    }

    public void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
            ((NetworkChangedReceiver) mNetworkReceiver).stopRepeatingTask();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification showNotification(String messageBody, int id) {
        Intent i = new Intent();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, i,
                PendingIntent.FLAG_IMMUTABLE);
        String channelId = getPackageName();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setTicker(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(channelId)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = "Getting Location..";
            final int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setSound(null, null);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        return notificationBuilder.build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LocationInterval);
        mLocationRequest.setFastestInterval(LocationFastestInterval);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    LatLng currentLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                    System.out.println("Latitude: " + currentLocation);
                    String latitudeAndLongitude = latitude + "," + longitude;
                    pushLocationToServer(latitudeAndLongitude, AppApplication.getCurrenDatetime());

                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    public void pushLocationToServer(String strLatLng, String dteDateTime) {

    }


    private void postDataUsingVolley(String code, String date,String latitude,String longitude,
                                     String questionOneId,String questionOneAnswer,String questionTwoId,
                                     String questionTwoAnswer,String questionThreeId,String questionThreeAnswer,
                                     String specialComments,String UserId,String catchUpNotes,String notes,String postUrl,String ID) {
        // url to post our data
        String url = postUrl+"/LogvisitService.php";
        // loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(DriverService.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    dbOrders.execSQL("delete from Visits where ID='"+response+"'");

                    // on below line we are setting this string s to our text view.

                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e("response","response******************"+response);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(LogVisit.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                 /*
                    "" + code, "" + notes.getText().toString().trim(),
                                        "" + catchUpNotes.getText().toString(), "" + date,
                                        Integer.parseInt(globalVariable.getUserId()), latitude, longitude,
                                        "" + questionOneId, "" + questionOneAnswer,
                                        "" + questionTwoId, "" + questionTwoAnswer,
                                        "" + questionThreeId, "" + questionThreeAnswer,
                                        "" + specialComments.getText().toString()
                     */

                params.put("CustomerCode", code);
                params.put("nextvisit", date);
                params.put("Lat", latitude);
                params.put("Lon", longitude);
                params.put("answeroneid", questionOneId);
                params.put("answeronetext", questionOneAnswer);
                params.put("answertwoid", questionTwoId);
                params.put("answertwotext", questionTwoAnswer);
                params.put("answerthreeid", questionThreeId);
                params.put("answerthreetext", questionThreeAnswer);
                params.put("customersatisfactoyanswer",specialComments);
                params.put("userid", UserId);
                params.put("catchupnotes", catchUpNotes);
                params.put("notes", notes);
                params.put("ID", ID);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

}
