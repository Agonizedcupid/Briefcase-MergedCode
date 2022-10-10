package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VisitsActivity extends AppCompatActivity {

    TextView codecust,CustomerNameText,networkstatus;
    EditText editTextBox,editTextNotes,editSigned;
    Button buttonDiscard,buttonUpload;

    public static boolean hasInternet = false;
    private static boolean success = false;
    ProgressDialog progressDoalog;
    Handler handler = new Handler();
    Runnable runnableUpload;
    int delayUpload = 1000;
    String userID,roles,method,custcode,selectedcustomer,IP="",Locations;
    private SQLiteDatabase db;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits);
        File file = new File( "Salesmanbriefcase");

        // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);


        codecust = (TextView) findViewById(R.id.codecust);
        CustomerNameText = (TextView) findViewById(R.id.CustomerNameText);
        networkstatus = (TextView) findViewById(R.id.networkstatus);
        editTextBox = (EditText) findViewById(R.id.editTextBox);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        editSigned = (EditText) findViewById(R.id.editSigned);

        buttonDiscard = (Button) findViewById(R.id.buttonDiscard);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        networkstatus.setText("NO INTERNET");
       // ArrayList<SettingsModel> settIP= dbH.getSettings();

        //for (SettingsModel orderAttributes: settIP){
            IP ="";// orderAttributes.getstrServerIp();
            Locations ="";// orderAttributes.getLocationId();
       // }
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        method = intent.getStringExtra("method");
        custcode = intent.getStringExtra("custcode");
        selectedcustomer = intent.getStringExtra("selectedcustomer");
        CustomerNameText.setText(selectedcustomer);
        codecust.setText(custcode);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadCustomerVisitsNotes().execute();
            }
        });
        buttonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(VisitsActivity.this,CustomerCentalActivity.class);
                myIntent.putExtra("userID",userID);
                myIntent.putExtra("roles",roles);
                myIntent.putExtra("method",method);
                startActivity(myIntent);
            }
        });

        handler.postDelayed(runnableUpload = new Runnable() {
            public void run() {
                Log.e("Connection", "-----------***********************---");
                isInternetWorking();
                if (  isNetworkAvailable(VisitsActivity.this)) {
                    hasInternet = true;
                    Log.e("NetworkAvailable", "-------------");
                    networkstatus.setText("You are online");
                    //   startProgress("Saving");
                }else
                {
                    networkstatus.setText("NOT CONNECTED");
                }

            }
        }, delayUpload);




        if (isMyServiceRunning()) {
            return;
        }


    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DriverService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

    private class UploadCustomerVisitsNotes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
        public UploadCustomerVisitsNotes(/*String orderDID,  String returnQty,String offLoadComment,String blnoffloaded,String reasons*/) {
        }
        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(IP + "PostVisits.php");
            try {
                // Add your data

                //ID integer primary key AUTOINCREMENT,OrderId integer,strTransferNo Text,Qty DOUBLE,QtyScanned DOUBLE,UnitCost Real,strProdCode TEXT,PastelDescription Text,Barcode Text,Binnumber Text,strCustomerReason Text,OrderDetailId Text,intCounter integer,blnScanned
                // Add your data

                JSONObject json = new JSONObject();
                json.put("codecust", codecust.getText().toString());
                json.put("userID", userID);
                json.put("strSignedBy", editSigned.getText().toString());
                json.put("strMessage", editTextNotes.getText().toString());
                json.put("editTextNotes", editTextNotes.getText().toString());

                Log.d("JSON", json.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);

                if(responseBody.equals("SUCCESS"))
                {

                    progressDoalog.dismiss();

                    new Thread()
                    {
                        public void run()
                        {
                            VisitsActivity.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    //Do your UI operations like dialog opening or Toast here
                                    final  AlertDialog.Builder dialogll = new  AlertDialog.Builder(VisitsActivity.this);
                                    dialogll.setTitle("Saved")
                                            .setMessage("Notes posted and saved successfully.")
                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                    Intent myIntent = new Intent(VisitsActivity.this,CustomerCentalActivity.class);
                                                    myIntent.putExtra("userID",userID);
                                                    myIntent.putExtra("roles",roles);
                                                    myIntent.putExtra("method",method);
                                                    startActivity(myIntent);
                                                }
                                            });
                                    dialogll.show();
                                }
                            });
                        }
                    }.start();

                }



            } catch (ClientProtocolException e) {
                Log.e("JSON", e.getMessage());
            } catch (IOException e) {
                Log.e("JSON", e.getMessage());
            } catch (Exception e) {
                Log.e("JSON", e.getMessage());
            }
            // db.close();
            return null;
        }
    }
}