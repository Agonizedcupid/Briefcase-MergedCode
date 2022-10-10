package com.reginald.briefcaseglobal;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public class Item {

        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;


        Item(String t, String t2, String t3,String t4) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;

        }
    }

    static class ViewHolder {
        //ImageView icon;
        TextView text;

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
                rowView = inflater.inflate(R.layout.pick_customer_row, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.pick_customer_text);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }

    List<Item> items1, items3AlphaList;
    ItemsListAdapter myItemsListAdapter1;

    Button btnregister,logInBtn;
    TextView theVersion,one,two,three,four,five,six,seven,eight,nine,backspace,clear,submit,hello,version;
    ListView lv;
    EditText mPasswordField,userNameEditText,pinCodeEditText;
    //private String userPin,userID,IP="http://groceryexpress.ddns.net:8181/LoadScan/";
    private String userPin,userID,IP="http://102.37.0.48/Briefcase/",Locations="1",roles,urlLogins = "http://102.37.0.48/BriefcaseCommonCloud/";
   // private DatabaseHelper mDatabaseHelper;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    private SQLiteDatabase db,dbOrders;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final static int Conn = 333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);

        /*if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermissions = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasLocationPermissions != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }*/
           // File file = new File("Salesmanbriefcase");

           // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
            db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);


           // final String dir2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/";
            dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            //Creating empty tables
            db.execSQL("CREATE TABLE IF NOT EXISTS Customers  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Products  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS CustomerPriceLists  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS CustomerSpecialsLines  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS GroupSpecialsLines  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS OrderPattern  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS DeliveryAddress  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Users  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");


            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS Users  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,UserName TEXT,UserPin TEXT ,UserRole TEXT)");
            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS CustomerNotes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,CustomerCode TEXT,Notes TEXT ,Coordinates TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS tblSettings  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Location TEXT NOT NULL DEFAULT  '1',IP TEXT ,DimsIp TEXT )");
            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS tblSettings  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,IP TEXT,DimsIp TEXT  )");
            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS LoggedIn  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,LastDate TEXT)");
            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS BriefcaseRoles(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,RoleID INTEGER,RoleDescription TEXT)");

            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS OrderHeaders (ID TEXT, OrderDate Text , DeliveryDate TEXT,OrderNumber TEXT,CustomerCode TEXT,CustomerDesc TEXT, Notes TEXT,DimsOrderId TEXT, UserID INT,DeliveryAddressID INT,Complete BOOLEAN,Uploaded BOOLEAN,LinesUploaded BOOLEAN,OrderType INTEGER,OrderHeaderAutoID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,isQuotation Boolean DEFAULT 0,StoreName TEXT,OrderID TEXT,Coordinates TEXT)");
            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS OrderLines  (ID TEXT, strPartNumber TEXT ,strDesc TEXT, Quantity DECIMAL,Price DECIMAL, Vat DECIMAL, Authorised BOOLEAN Default 0,Uploaded BOOLEAN,PriceInclusive DECIMAL,Uom TEXT ,LineTotalPriceInclusive DECIMAL,OrderLinesAutoId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Cost Decimal,CustomerCode TEXT,LineDeliveryDate TEXT,StoreName TEXT )");

            dbOrders.execSQL("CREATE TABLE IF NOT EXISTS Visits  (ID TEXT, CustomerCode TEXT ,nextvisit TEXT, Lat TEXT,Lon TEXT, answeroneid TEXT," +
                    " answeronetext TEXT,answertwoid TEXT,answertwotext TEXT,answerthreeid TEXT ,answerthreetext TEXT,customersatisfactoyanswer TEXT,userid TEXT,catchupnotes TEXT," +
                    "notes TEXT )");


        Cursor cursorm = dbOrders.rawQuery("SELECT * FROM OrderLines limit 1", null); // grab cursor for all data
        int deleteStateColumnIndex = cursorm.getColumnIndex("CustomerCode");  // see if the column is there
        int deleteStateColumnIndexStorename = cursorm.getColumnIndex("StoreName");  // see if the column is there
        if (deleteStateColumnIndex < 0) {
            // missing_column not there - add it
            dbOrders.execSQL("ALTER TABLE OrderLines ADD COLUMN CustomerCode TEXT;");
            dbOrders.execSQL("ALTER TABLE OrderLines ADD COLUMN LineDeliveryDate TEXT;");

        }
        if (deleteStateColumnIndexStorename < 0) {

            dbOrders.execSQL("ALTER TABLE OrderLines ADD COLUMN StoreName TEXT;");
        }
        Cursor cursorH = dbOrders.rawQuery("SELECT * FROM OrderHeaders limit 1", null);
        int CoordinatesColumnIndex = cursorH.getColumnIndex("Coordinates");
        if (CoordinatesColumnIndex < 0) {
            dbOrders.execSQL("ALTER TABLE OrderHeaders ADD COLUMN Coordinates TEXT;");
        }

           Cursor cursor = db.rawQuery("SELECT * from tblSettings", null);
            //ArrayList<SettingsModel> settIP = dbH.getSettings();

        /*if (cursor.moveToFirst()) {
            do {

                IP = cursor.getString(cursor.getColumnIndex("IP"));
                Locations = cursor.getString(cursor.getColumnIndex("Location"));

            } while (cursor.moveToNext());
        }*/
           // for (SettingsModel orderAttributes : settIP) {
               // IP = "";//orderAttributes.getstrServerIp();
                //Locations =""; //orderAttributes.getLocationId();
           // }
          /*  if (cursor.getCount() < 1) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            } else {*/

            //}
               // checkLast();
              /* if (checkConnection()) {
                    getUser();
                } else {
                    final AlertDialog.Builder dialogretry = new AlertDialog.Builder(LoginActivity.this);
                    dialogretry.setTitle("Oops")
                            .setMessage("Wrong Credentials, please retry.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    paramDialogInterface.dismiss();
                                }
                            });
                    dialogretry.show();
                }

            }*/


        String versionName = BuildConfig.VERSION_NAME;


        btnregister = (Button) findViewById(R.id.btnregister);
        logInBtn = (Button) findViewById(R.id.logInBtn);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        pinCodeEditText = (EditText) findViewById(R.id.pinCodeEditText);
        theVersion = (TextView) findViewById(R.id.theVersion);
        theVersion.setText(versionName);

        logInBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(userNameEditText.getText().toString().trim())) {
                userNameEditText.setError("Enter valid name");
                userNameEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(pinCodeEditText.getText().toString().trim())) {
                pinCodeEditText.setError("Enter valid pass");
                pinCodeEditText.requestFocus();
                return;
            }

            login(userNameEditText.getText().toString().trim(),pinCodeEditText.getText().toString().trim());
           // String password = getPasswordByName(userNameEditText.getText().toString().trim());
            //Toast.makeText(this, ""+password, Toast.LENGTH_SHORT).show();
           /* if (password.equals(pinCodeEditText.getText().toString().trim())) {
                saveUserID(getUserId(userNameEditText.getText().toString().trim()));
                Intent i = new Intent(LoginActivity.this,HomeScreen.class);
                i.putExtra("userID",getUserId(userNameEditText.getText().toString().trim()));
                i.putExtra("roles","");
                startActivity(i);
                Toast.makeText(this, "Matched", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not matched", Toast.LENGTH_SHORT).show();
            }*/

        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });



    }
    @Override
    public void onClick(View v) {
        // handle number button click
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            mPasswordField.append(((TextView) v).getText());
            return;
        }
        switch (v.getId()) {
           /* case R.id.t9_key_clear: { // handle clear button
                mPasswordField.setText(null);
            }
            break;*/
            case R.id.t9_key_backspace: { // handle backspace button
                // delete one character
                Editable editable = mPasswordField.getText();
                int charCount = editable.length();
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                }
            }
            break;
            case R.id.t9_key_clear: {

                String password = mPasswordField.getText().toString();
                Log.e("PasswordReal","++++++++++++++++++"+password+"**"+userPin);
                if (password.equals(userPin)) {
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "PassWord is Incorrect", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    private boolean isPermissionsGranted(){

        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else
        {
            int reatExternalStoragePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            int writecheck = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return reatExternalStoragePermission == PackageManager.PERMISSION_GRANTED  && writecheck == PackageManager.PERMISSION_GRANTED;

        }
    }

    private void takePermission(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){

            try{
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageManager())));
                startActivityForResult(intent,2000);
            }catch(Exception exception)
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,2000);
            }

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},Conn);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 100){
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
                        if(Environment.isExternalStorageManager()){
                            Toast.makeText(this,"Permision Granted n android 11",Toast.LENGTH_SHORT).show();
                        }else{
                            takePermission();
                        }
                    }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case Conn:
                if(grantResults.length > 0){
                    boolean storage =grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read =grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storage && read){
                        //
                    }
                    else{

                    }
                }

        }


    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
           /* if (DriverService.class.getName().equals(service.service.getClassName())) {
                return true;
            }*/
        }
        return false;
    }
    public  boolean checkConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
    public void volleyLogin(String Username, String PinCode){
        String postUrl = urlLogins+"Users.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", Username);
            postData.put("pincode", PinCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("star","Boy***************************************");
               // Log.e("response","**"+response);
                //checkLast();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("star","***************************************Error Volley");
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
    private void login(String Username, String PinCode){
        //Getting values from edit texts
        final String email = Username;
        final String password = PinCode;
        String postUrl = urlLogins+"Users.php";
        final AppApplication globalVariable = (AppApplication) getApplicationContext();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("****","Error**************"+response);
                        try {
                            JSONArray BoardInfo = new JSONArray(response);
                            dbOrders.execSQL("Drop Table tblSettings");
                             dbOrders.execSQL("CREATE TABLE IF NOT EXISTS tblSettings  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,IP TEXT,DimsIp TEXT,DownloadPath TEXT  )");

                            for (int j = 0; j < BoardInfo.length(); ++j) {
                                JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                                String strIp, strDimsIP,userid, strimage,ismerchie,Location,DownloadPath;
                                strIp = BoardDetails.getString("strIp");
                                strDimsIP = BoardDetails.getString("strDimsIP");
                                userid = BoardDetails.getString("UserId");
                                strimage = BoardDetails.getString("strImagePath");
                                ismerchie = BoardDetails.getString("strBriefcaseType");
                                Location = BoardDetails.getString("LocationId");
                                DownloadPath = BoardDetails.getString("DownloadPath");


                                Log.e("JSON-*", "RESPONSE IS HEADERS**: " + strIp);
                                Log.e("strDimsIP-*", "RESPONSE IS strDimsIP**: " + strDimsIP);

                                ContentValues values = new ContentValues();
                                values.put("IP",strIp);
                                values.put("DimsIp",strDimsIP);
                                values.put("DownloadPath",DownloadPath);
                                dbOrders.insert("tblSettings", null, values);
                                Cursor cursor = dbOrders.rawQuery("SELECT * from tblSettings where DimsIp is not null", null);
                                if (cursor.moveToFirst()) {
                                    do {

                                        IP = cursor.getString(cursor.getColumnIndex("IP"));
                                        globalVariable.setIP(IP);
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                                globalVariable.setIP(strIp);
                                globalVariable.setUserId(userid);
                                globalVariable.setDIMSIP(strDimsIP);
                                globalVariable.setImages(strimage);
                                globalVariable.setLocation(Location);
                                Log.e("IP","*****************************IP"+IP);

                               // checkLast();
                               // dbH.updateDeals("UPDATE  OrderHeaders SET Uploaded = 1,offloaded =1  where InvoiceNo = '" + ID + "'");

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String tm =  sdf.format(c.getTime());
                             /*   dbOrders.execSQL("DELETE FROM LoggedIn");
                                ContentValues cv = new ContentValues();
                                cv.put("UserID", userid);
                                cv.put("LastDate", tm);
                                dbOrders.insert("LoggedIn", null, cv);*/
                                dbOrders.execSQL("DELETE FROM BriefcaseRoles");
                                ContentValues cvmerchies = new ContentValues();
                                cvmerchies.put("UserID", userid);
                                cvmerchies.put("RoleDescription", ismerchie);
                                dbOrders.insert("BriefcaseRoles", null, cvmerchies);

                                Cursor cursor2 = dbOrders.rawQuery("SELECT * from LoggedIn where LastDate ='"+tm+"'", null);
                                if(cursor2.getCount() < 1)
                                {
                                    Intent i = new Intent(LoginActivity.this, ControlPanel.class);
                                    startActivity(i);
                                }else{

                                    dbOrders.execSQL("DELETE FROM LoggedIn");
                                    ContentValues cv = new ContentValues();
                                    cv.put("UserID", userid);
                                    cv.put("LastDate", tm);
                                    dbOrders.insert("LoggedIn", null, cv);


                                    Intent i = new Intent(LoginActivity.this,SalesActivity.class);
                                    i.putExtra("userID",userid);
                                    i.putExtra("roles","");
                                    i.putExtra("name",Username);
                                    startActivity(i);
                                }
                            }

                        } catch (Exception e) {
                            Log.e("JSON", e.getMessage());
                        }
                        //If we are getting success from server

                      /*  if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.EMAIL_SHARED_PREF, email);

                            //Saving values to editor
                            editor.commit();

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request

                params.put("username", Username);
                params.put("pincode", PinCode);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void getUser() {
        HttpRequest request = new HttpRequest();
        items1 = new ArrayList<Item>();

        request.setOnResponseListener(response -> {
            if (response.code == HttpResponse.HTTP_OK) {

                try {
                    JSONArray jsonArray = response.toJSONArray();
                    dbOrders.execSQL("Drop Table Users  ");
                    dbOrders.execSQL("CREATE TABLE IF NOT EXISTS Users  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,UserName TEXT,UserPin TEXT,UserTokenAuth TEXT,Location,UserRole TEXT)");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e("users", "------------------------" + jsonArray.getJSONObject(i));
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Item item = new Item(jsonObject.getString("UserName"), jsonObject.getString("UserID"),jsonObject.getString("UserIdentity"),jsonObject.getString("PinCode")); //
                        items1.add(item);

                        //db.execSQL("CREATE TABLE IF NOT EXISTS Users  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,UserName TEXT,UserPin TEXT,UserTokenAuth TEXT)");
                        ContentValues values = new ContentValues();
                        values.put("UserID", jsonObject.getString("UserID"));
                        values.put("UserName",jsonObject.getString("UserName"));
                        values.put("UserRole",jsonObject.getString("UserIdentity"));
                        values.put("UserPin",jsonObject.getString("PinCode"));


                        dbOrders.insert("Users", null, values);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (response.code == HttpResponse.HTTP_BAD_REQUEST) {

            } else if (response.code == HttpResponse.HTTP_NOT_FOUND) {
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
        Log.e("locations","***********************"+IP+"Users.php?location="+Locations);
        request.get(IP+"Users.php?location="+Locations);
    }

    public String getPasswordByName(String name) {

        String password ="";
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from Users where UserName ='"+name+"'", null);

        if (cursor2.moveToFirst()) {
            do {
                password =cursor2.getString(cursor2.getColumnIndex("UserPin"));

            } while (cursor2.moveToNext());
        }

        return password;

    }

    public String getUserId(String name) {

        String password ="";
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from Users where UserName ='"+name+"'", null);

        if (cursor2.moveToFirst()) {
            do {
                password =cursor2.getString(cursor2.getColumnIndex("UserID"));

            } while (cursor2.moveToNext());
        }

        return password;

    }
    public void saveUserID(String userid) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        dbOrders.execSQL("DELETE FROM LoggedIn");
        ContentValues cv = new ContentValues();
        cv.put("UserID", userid);
        cv.put("LastDate", tm);
        dbOrders.insert("LoggedIn", null, cv);

    }

    public void checkLast()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from LoggedIn where LastDate ='"+tm+"'", null);
        if(cursor2.getCount() < 1)
        {
            Intent i = new Intent(LoginActivity.this, ControlPanel.class);
            startActivity(i);
        }

    }


}
