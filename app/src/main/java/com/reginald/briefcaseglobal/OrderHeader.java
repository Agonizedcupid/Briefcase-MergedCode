package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reginald.briefcaseglobal.Interface.CurrentLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderHeader extends AppCompatActivity {

    public class Item {

        String ItemString;
        String ItemString2;

        Item(String t, String t2) {
            ItemString = t;
            ItemString2 = t2;
        }
    }
    static class ViewHolder {
        //ImageView icon;
        TextView text;
        TextView text2;
    }
    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.deliveryaddress_row, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.addressname);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.addid);

                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }

    TextView custcodeselected,OrderHeaderTodayText,selectedcustomername,notescreate,createordernumber,delvaddress,ID,coordinates;
    EditText selectdelivedate,nearestlandmark,entstoreaddress,cellnumber;
    Button nextorderheader,changeaddress,dialogclose,btncheckin,btnstartcheck;
    String roles,userID,custName,custcode,neworder,refIdExistingOrder;
    ImageView closecheckdialog;
    DatePickerDialog picker;
    private SQLiteDatabase db;
    int countAddressId=0;
    ListView lvaddresses;
    FusedLocationProviderClient client;
    Dialog dialogView;
    double lats = 0,lons = 0;
    List<Item> items1 ;
    ItemsListAdapter myItemsListAdapter1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_header);
        final AppApplication globalVariable = (AppApplication) getApplicationContext();
        client = LocationServices.getFusedLocationProviderClient(this);
       // File file = new File("Salesmanbriefcase");

        // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        if (ActivityCompat.checkSelfPermission(OrderHeader.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderHeader.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        custcode = intent.getStringExtra("custcode");
        custName = intent.getStringExtra("custName");
        neworder = intent.getStringExtra("neworder");



        custcodeselected = (TextView)  findViewById(R.id.custcodeselected);
        OrderHeaderTodayText = (TextView)  findViewById(R.id.OrderHeaderTodayText);
        ID = (TextView)  findViewById(R.id.idorder);
        delvaddress = (TextView)  findViewById(R.id.delvaddress);
        selectedcustomername = (TextView)  findViewById(R.id.selectedcustomername);
        selectdelivedate = (EditText)  findViewById(R.id.selectdelivedate);
        createordernumber = (EditText)  findViewById(R.id.createordernumber);
        notescreate = (EditText)  findViewById(R.id.notescreate);
        nextorderheader = (Button)  findViewById(R.id.nextorderheader);
        changeaddress = (Button)  findViewById(R.id.changeaddress);
        btncheckin = (Button)  findViewById(R.id.btncheckin);
        if (intent.hasExtra("existingOrder")) {
            neworder = intent.getStringExtra("existingOrder");
        } else {
            // Do something else
        }


        if(countAddress(custcode) == 0){
            countAddressId = 0;
            changeaddress.setVisibility(View.GONE);
        }


        if(neworder.equals("neworder"))
        {
            //get new order id
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            String subscriberId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            final String idTimestamp = subscriberId+"-"+ts;
            ID.setText(idTimestamp);
        }
        else{
            //get the ID select on this order
        }
        delvaddress.setText(deliveryAddress(custcode));

        custcodeselected.setText(custcode);
        selectedcustomername.setText(custName);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        OrderHeaderTodayText.setText(tm);
        selectdelivedate.setText(tm);

        if (intent.hasExtra("SelectedDelDate")) {
            selectdelivedate.setText(intent.getStringExtra("SelectedDelDate"));
        } else {
            // Do something else
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            selectdelivedate.setShowSoftInputOnFocus(false);

        } else { // API 11-20
            selectdelivedate.setTextIsSelectable(true);
        }

        selectdelivedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(OrderHeader.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
                                if((""+monthOfYear).length() < 2){

                                    if((""+dayOfMonth).length()<2){
                                        String dateSelected = year  + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                        selectdelivedate.setText(""+dateSelected);
                                    }else{
                                        String dateSelected = year  + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        selectdelivedate.setText(""+dateSelected);
                                    }

                                }else{
                                    if((""+dayOfMonth).length()<2){
                                        String dateSelected = year  + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                                        selectdelivedate.setText(""+dateSelected);
                                    }else{
                                        String dateSelected = year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        selectdelivedate.setText(""+dateSelected);
                                    }

                                }
/*
                                try {
                                    Date d=dateFormat.parse(dateSelected);
                                    Log.e("d","dddddddddddddddddddddddddddd"+""+dateFormat.format(d));

                                    selectdelivedate.setText(""+dateFormat.format(d));
                                }
                                catch(Exception e) {
                                    //java.text.ParseException: Unparseable date: Geting error
                                    System.out.println("Excep"+e);
                                    Log.e("derrr","eeeeeeeeeeeerrrrrrrrrrrrrrr"+dateSelected);
                                    selectdelivedate.setText(""+dateSelected);
                                }*/


                            }
                        }, year, month, day);
                picker.show();
            }
        });

        changeaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogView = new Dialog(OrderHeader.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.multiaddresslist);

                deliveryAddressList(custcode);
                myItemsListAdapter1 = new ItemsListAdapter(OrderHeader.this, items1);
                lvaddresses = (ListView) dialogView.findViewById(R.id.lvaddresses);
                dialogclose = (Button) dialogView.findViewById(R.id.dialogclose);
                lvaddresses.setAdapter(myItemsListAdapter1);
                lvaddresses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Item selectedItem2 = (Item)(adapterView.getItemAtPosition(i));
                        countAddressId = Integer.parseInt(selectedItem2.ItemString2);
                        delvaddress.setText(selectedItem2.ItemString);
                        dialogView.dismiss();
                        return false;
                    }
                });
                dialogclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogView.dismiss();
                    }
                });
                dialogView.show();

            }
        });
        btncheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCurrentLocation(new CurrentLocation() {
                    @Override
                    public void getLocation(double latitude, double longitude) {
                        lats = latitude;
                        lons = longitude;

                        Log.e("presses","********** i am pressed here "+countAddressId);

                        Intent i = new Intent(OrderHeader.this, CheckingActivity.class);
                        i.putExtra("userID",userID);
                        i.putExtra("roles",roles);
                        i.putExtra("custcode",custcode);
                        i.putExtra("custName",custName);
                        i.putExtra("neworder",neworder);
                        i.putExtra("countAddressId",""+countAddressId);
                        i.putExtra("lats",""+lats);
                        i.putExtra("lons",""+lons);
                        startActivity(i);


                    }
                });

            }
        });

        nextorderheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("countAddressId","_____________________"+countAddressId);
                Intent i = new Intent(OrderHeader.this,MainActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("custcode",custcode);
                i.putExtra("custName",custName);
                i.putExtra("notes",notescreate.getText().toString());
                i.putExtra("orderNo",createordernumber.getText().toString());
                i.putExtra("date",selectdelivedate.getText().toString());
                i.putExtra("neworder",neworder);
                i.putExtra("addressid",""+countAddressId);
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(OrderHeader.this, CustomersActivity.class);
            i.putExtra("userID",userID);
            i.putExtra("roles",roles);
            startActivity(i);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @SuppressLint("Range")
    public String deliveryAddress(String CustomerCode)
    {
       String results = "";
        Cursor cursororderids = db.rawQuery("select * from  Customers where CustomerCode ='"+CustomerCode+"'", null);

        if (cursororderids.moveToFirst()) {
            do {

                results =cursororderids.getString(cursororderids.getColumnIndex("CustomerAddress1"))+"\n"+cursororderids.getString(cursororderids.getColumnIndex("CustomerAddress2"))
                        +"\n"+cursororderids.getString(cursororderids.getColumnIndex("CustomerAddress3"))+cursororderids.getString(cursororderids.getColumnIndex("CustomerAddress4"))+cursororderids.getString(cursororderids.getColumnIndex("CustomerAddress5"));



            } while (cursororderids.moveToNext());
        }
        cursororderids.close();

        return results;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap == null) return false;
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network n: networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        } else {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            for (NetworkInfo nInfo: networks) {
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        }

        return false;
    }
    private void getCurrentLocation(CurrentLocation currentLocation) {
        //Check the location permission:
        if (ActivityCompat.checkSelfPermission(OrderHeader.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(OrderHeader.this, Locale.getDefault());
                            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            currentLocation.getLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } else {
            //request for the location access
            ActivityCompat.requestPermissions(OrderHeader.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }
    @SuppressLint("Range")
    public void deliveryAddressList(String CustomerCode)
    {
        items1 = new ArrayList<Item>();
       String results = "";
        Cursor cursororderids = db.rawQuery("select * from DeliveryAddress where CustomerPastelCode ='"+CustomerCode+"'", null);

        if (cursororderids.moveToFirst()) {
            do {

                // DAddress1,DAddress2,DAddress3,DAddress4,DAddress5
                results =cursororderids.getString(cursororderids.getColumnIndex("DAddress1"))+"\n"+cursororderids.getString(cursororderids.getColumnIndex("DAddress2"))
                        +"\n"+cursororderids.getString(cursororderids.getColumnIndex("DAddress3"))+cursororderids.getString(cursororderids.getColumnIndex("DAddress4"))+' '+cursororderids.getString(cursororderids.getColumnIndex("DAddress5"));

                Log.e("results","=----"+results);
                Item listItem = new Item(results,cursororderids.getString(cursororderids.getColumnIndex("DeliveryAddressId")));
                items1.add(listItem);

            } while (cursororderids.moveToNext());
        }
        cursororderids.close();


    }
    public int countAddress(String CustomerCode)
    {

        Cursor cursororderids = db.rawQuery("select * from DeliveryAddress where CustomerPastelCode ='"+CustomerCode+"'", null);
        return cursororderids.getCount();
    }
    public void multiaddress(){

    }

    private void postDataUsingVolley(String custcode,int addressId,String address, String landmark,String cellnumber,String latitude,String longitude,String postUrl) {
        // url to post our data
        String url = postUrl+"Checkin.php";
        // loadingPB.setVisibility(View.VISIBLE);
        Log.e("response","response******************"+url);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(OrderHeader.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replaceAll("\"", "");

                try {
                    Log.e("response","response******************"+response);
                    JSONObject respObj = new JSONObject(response);

                    String success = respObj.getString("Result");
                    Log.e("response","success******************"+success);
                    if (success.equals("Success")) {
                        dialogView.dismiss();
                        Toast.makeText(OrderHeader.this, "Data is successfully saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderHeader.this, "Failed to save data!", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(OrderHeader.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("addressId", ""+addressId);
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