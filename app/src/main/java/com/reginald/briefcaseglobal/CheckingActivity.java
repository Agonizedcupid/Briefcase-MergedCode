package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckingActivity extends AppCompatActivity {

    EditText nearestlandmark,entstoreaddress,cellnumber;
    TextView coordinates;
    ImageView closecheckdialog;
    Button btnstartcheck;
    //double lats = 0,lons = 0;
    String roles,userID,custName,custcode,neworder,countAddressId,lats,lons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_dialog);

        entstoreaddress = (EditText) findViewById(R.id.entstoreaddress);
        nearestlandmark = (EditText) findViewById(R.id.nearestlandmark);
        cellnumber = (EditText) findViewById(R.id.cellnumber);
        coordinates = (TextView) findViewById(R.id.coordinates);
        btnstartcheck = (Button) findViewById(R.id.btnstartcheck);
        closecheckdialog = (ImageView) findViewById(R.id.closecheckdialog);
        final AppApplication globalVariable = (AppApplication) getApplicationContext();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        custcode = intent.getStringExtra("custcode");
        custName = intent.getStringExtra("custName");
        neworder = intent.getStringExtra("neworder");
        countAddressId = intent.getStringExtra("countAddressId");
        lats = intent.getStringExtra("lats");
        lons = intent.getStringExtra("lons");
        Log.e("Test","***************** countAddressId : "+countAddressId);
        // try {

        coordinates.setText(lats+","+lons);

        btnstartcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(entstoreaddress.getText().toString())) {
                    entstoreaddress.setError("Enter notes!");
                    entstoreaddress.requestFocus();
                    //return;
                }

                if (TextUtils.isEmpty(nearestlandmark.getText().toString())) {
                    nearestlandmark.setError("Enter notes!");
                    nearestlandmark.requestFocus();
                    //return;
                }
                if (TextUtils.isEmpty(cellnumber.getText().toString())) {
                    cellnumber.setError("Enter notes!");
                    cellnumber.requestFocus();
                    // return;
                }

                Log.e("Test","***************** I am within this");
                postDataUsingVolley(custcode,countAddressId,entstoreaddress.getText().toString(), nearestlandmark.getText().toString(),cellnumber.getText().toString(), lats, lons, globalVariable.getIP());

            }
        });
        closecheckdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckingActivity.this, OrderHeader.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("custcode",custcode);
                i.putExtra("custName",custName);
                i.putExtra("neworder",neworder);
                i.putExtra("countAddressId",countAddressId);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(CheckingActivity.this, OrderHeader.class);
            i.putExtra("userID",userID);
            i.putExtra("roles",roles);
            i.putExtra("custcode",custcode);
            i.putExtra("custName",custName);
            i.putExtra("neworder",neworder);
            i.putExtra("countAddressId",countAddressId);
            startActivity(i);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void postDataUsingVolley(String custcode,String addressId,String address, String landmark,String cellnumber,String latitude,String longitude,String postUrl) {
        // url to post our data
        String url = postUrl+"Checkin.php";
        // loadingPB.setVisibility(View.VISIBLE);
        Log.e("response","response******************"+url);
        Log.e("addressId","response******************"+addressId);
        Log.e("countAddressId","countAddressId******************"+countAddressId);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CheckingActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replaceAll("\"", "");

                try {
                    Log.e("response","success******************response "+response);
                   // JSONObject respObj = new JSONObject(response);

                    //String success = respObj.getString("Result");
                    //Log.e("response","success******************"+success);
                    if (response.equals("Success")) {
                        Intent i = new Intent(CheckingActivity.this, OrderHeader.class);
                        i.putExtra("userID",userID);
                        i.putExtra("roles",roles);
                        i.putExtra("custcode",custcode);
                        i.putExtra("custName",custName);
                        i.putExtra("neworder",neworder);
                        i.putExtra("countAddressId",countAddressId);
                        startActivity(i);
                        Toast.makeText(CheckingActivity.this, "Data is successfully saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CheckingActivity.this, "Failed to save data!", Toast.LENGTH_SHORT).show();

                    }

                    // on below line we are setting this string s to our text view.

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(CheckingActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                params.put("address", address);
                params.put("landmark", landmark);
                params.put("cellnumber", cellnumber);
                params.put("custcode", custcode);
                params.put("addressId", ""+countAddressId);
                params.put("Lat", latitude);
                params.put("Lon", longitude);
                params.put("UserId", userID);


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