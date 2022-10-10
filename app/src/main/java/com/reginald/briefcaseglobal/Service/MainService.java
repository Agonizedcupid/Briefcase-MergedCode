package com.reginald.briefcaseglobal.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.reginald.briefcaseglobal.AppApplication;
import com.reginald.briefcaseglobal.CustomersActivity;
import com.reginald.briefcaseglobal.LogVisit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainService extends Service {
    private SQLiteDatabase dbOrders;
    public MainService() {

        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+ "/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        getIntructionsToPost();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void getIntructionsToPost()
    {

        final AppApplication globalVariable = (AppApplication) getApplicationContext();
        Cursor cursor = dbOrders.rawQuery("select * from Visits Limit 1" ,null);
        if (cursor.moveToFirst()) {
            do {

                Log.e("tm","********tm*****************"+cursor.getString(cursor.getColumnIndex("isQuotation")));//ID
                postDataUsingVolley( cursor.getString(cursor.getColumnIndex("ID")),cursor.getString(cursor.getColumnIndex("CustomerCode")),
                        cursor.getString(cursor.getColumnIndex("nextvisit")),
                        cursor.getString(cursor.getColumnIndex("Lat")),  cursor.getString(cursor.getColumnIndex("Lon")),
                        cursor.getString(cursor.getColumnIndex("answeroneid")),  cursor.getString(cursor.getColumnIndex("answeronetext")),
                        cursor.getString(cursor.getColumnIndex("answertwoid")),
                        cursor.getString(cursor.getColumnIndex("answertwotext")),  cursor.getString(cursor.getColumnIndex("answerthreeid")),  cursor.getString(cursor.getColumnIndex("answerthreetext")),
                        cursor.getString(cursor.getColumnIndex("customersatisfactoyanswer")),
                        cursor.getString(cursor.getColumnIndex("userid")),
                        cursor.getString(cursor.getColumnIndex("catchupnotes")), cursor.getString(cursor.getColumnIndex("notes")),globalVariable.getIP()+"LogvisitService.php");


            } while (cursor.moveToNext());
        }
    }

    private void postDataUsingVolley(String ID,String code, String date,String latitude,String longitude,
                                     String questionOneId,String questionOneAnswer,String questionTwoId,
                                     String questionTwoAnswer,String questionThreeId,String questionThreeAnswer,
                                     String specialComments,String UserId,String catchUpNotes,String notes,String postUrl) {
        // url to post our data
        String url = postUrl;
        // loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainService.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // on below line we are displaying a success toast message.

                Log.e("response",response.replaceAll("\"", ""));
                response = response.replaceAll("\"", "");
                if (!response.equals("lol")) {
                    dbOrders.execSQL("delete from Visits where ID='"+response+"'" );

                } else {


                }
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    Log.e("response","response******************"+response);
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.

                    //String name = respObj.getString("name");
                    // String job = respObj.getString("job");
                    String success = respObj.getString("Result");
                    Log.e("response","success******************"+success);
                    if (success.equals("Success")) {

                    } else {

                    }

                    // on below line we are setting this string s to our text view.

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainService.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();


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