package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddItemToSpecialActivity extends AppCompatActivity {

    TextView txcustomer;
    TextView txtitem;
    TextView txtsellingp;
    TextView txtthiscost;
    TextView acceptablegpadd;
    TextView txtcalgp;
    String storename="",itemcode="",customercode="",storecode="";
    EditText edtdatefrom;
    EditText edtdtetospecial;
    EditText sellingprice;
    Button btngetgp;
    Button btnsavespecialitem;
    private SQLiteDatabase db;
    DatePickerDialog picker,pickerto,pickeraddspecialTo,pickeraddspecialfrom;
    String IP="",userId="";

    private String dtefrom;
    private String dteTo;
    private String edSelling;
    String getPastelDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_special);
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+ "/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        IP = globalVariable.getIP();
        userId = globalVariable.getUserId();

        txcustomer =findViewById(R.id.txcustomer);
        txtitem =findViewById(R.id.txtitem);
        txtsellingp =findViewById(R.id.txtsellingp);
        txtthiscost =findViewById(R.id.txtthiscost);
        acceptablegpadd =findViewById(R.id.acceptablegpadd);
        txtcalgp =findViewById(R.id.txtcalgp);
        edtdatefrom =findViewById(R.id.edtdatefrom);
        edtdtetospecial =findViewById(R.id.edtdtetospecial);
        btngetgp =findViewById(R.id.btngetgp);
        btnsavespecialitem =findViewById(R.id.btnsavespecialitem);
        sellingprice =findViewById(R.id.sellingprice);



        if (getIntent() != null) {
            customercode = getIntent().getStringExtra("customercode");
            itemcode = getIntent().getStringExtra("itemcode");
            storename = getIntent().getStringExtra("storename");
            storecode = getIntent().getStringExtra("storename");
            getPastelDescription = getIntent().getStringExtra("getPastelDescription");

            txtitem.setText("Items: "+itemcode+"- "+getPastelDescription);
            txcustomer.setText("Store: "+storename);
            txtthiscost.setText(""+returnItemCost(itemcode));
            txtsellingp.setText("Current Selling Price: "+returnSellingPrices(itemcode,customercode));
            acceptablegpadd.setText("Acceptable GP %"+returnItemGP(itemcode));

        }
        btngetgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sellingprice.getText().toString().trim().length() > 0){
                    double sellingP =  Double.parseDouble(sellingprice.getText().toString());
                    txtcalgp.setText(""+marginCalculator(returnItemCost(itemcode),sellingP));
                }

            }
        });

        btnsavespecialitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                       /* if (marginCalculator(Double.parseDouble(txtthiscost.getText().toString()), Double.parseDouble(sellingprice.getText().toString())) < returnItemGP(itemcode)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddItemToSpecialActivity.this);
                            builder.setMessage("Below Acceptable Product GP%")
                                    .setCancelable(false)
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {*/
                            new AddCustomerSpecials().execute();
                        //}


                }
            }
        });

        edtdatefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddItemToSpecialActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtdatefrom.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        edtdtetospecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                pickerto = new DatePickerDialog(AddItemToSpecialActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtdtetospecial.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                pickerto.show();
            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        edSelling = sellingprice.getText().toString();
        dtefrom = edtdatefrom.getText().toString();
        dteTo = edtdtetospecial.getText().toString();
        if (edSelling.trim().isEmpty()) {
            sellingprice.setError("Please enter selling price");
            valid = false;
        } else {
            sellingprice.setError(null);
        }

        if (dtefrom.trim().isEmpty()) {
            edtdatefrom.setError("Please enter the date from");
            valid = false;
        } else {
            edtdatefrom.setError(null);
        }
        if (dteTo.trim().isEmpty()) {
            edtdtetospecial.setError("Please enter the date to");
            valid = false;
        } else {
            edtdtetospecial.setError(null);
        }
        return valid;
    }
    public double marginCalculator(double cost,double onCellVal)
    {
        return (1-(cost/onCellVal))*100;
    }
    public double returnItemCost(String productCode)
    {
        double costs =0;
        Cursor cursor2 = db.rawQuery("SELECT Cost from Products where strPartNumber ='"+productCode+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {

                costs =cursor2.getDouble(cursor2.getColumnIndex("Cost"));Log.e("costs","costs**************"+costs);

            } while (cursor2.moveToNext());
        }

        return costs;
    }
    public double returnItemGP(String productCode)
    {
        double costs =0;
        Cursor cursor2 = db.rawQuery("SELECT gptolerance from Products where strPartNumber ='"+productCode+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {

                costs =cursor2.getDouble(cursor2.getColumnIndex("gptolerance"));Log.e("gptolerance","gptolerance**************"+costs);

            } while (cursor2.moveToNext());
        }

        return costs;
    }
    public double returnSellingPrices(String ProdCode,String CustomerCode)
    {
        String customerPriceList = returnCustomerPriceList( CustomerCode);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date =  sdf.format(c.getTime());

        double price = 0;
        Cursor cursor = db.rawQuery("SELECT * from CustomerSpecialsLines where strPartNumber ='"+ProdCode+"' and CustomerId ='"+CustomerCode+"' and DateTo>='"+date+"'", null);
        Cursor cursor2 = db.rawQuery("SELECT * from GroupSpecialsLines where strPartNumber ='"+ProdCode+"' and GroupId ='"+returnCustomerGroupId(CustomerCode)+"' and DateTo>='"+date+"'", null);
        Cursor cursor3 = db.rawQuery("SELECT * from CustomerPriceLists where strPartNumber ='"+ProdCode+"' and CustomerPriceList ='"+customerPriceList+"'", null);

        if (cursor.moveToFirst()) {
            do {

                price = cursor.getDouble(cursor.getColumnIndex("Price"));

            } while (cursor.moveToNext());
        }

        if (cursor2.moveToFirst()) {
            do {

                price = cursor2.getDouble(cursor2.getColumnIndex("Price"));

            } while (cursor2.moveToNext());
        }
        if (cursor3.moveToFirst()) {
            do {

                price = cursor3.getDouble(cursor3.getColumnIndex("Price"));

            } while (cursor3.moveToNext());
        }

       return price;
    }
    public String returnCustomerPriceList(String CustomerCode)
    {
        String priceListnameReturned = "";
        Cursor cursor2 = db.rawQuery("SELECT * from Customers where CustomerCode ='"+CustomerCode+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                priceListnameReturned =cursor2.getString(cursor2.getColumnIndex("CustomerPriceList"));

            } while (cursor2.moveToNext());
        }

        return priceListnameReturned;
    }
    public String returnCustomerGroupId(String CustomerCode)
    {
        String priceListnameReturned = "";
        Cursor cursor2 = db.rawQuery("SELECT * from Customers where CustomerCode ='"+CustomerCode+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                priceListnameReturned =cursor2.getString(cursor2.getColumnIndex("CustomerGroup"));

            } while (cursor2.moveToNext());
        }

        return priceListnameReturned;
    }
    private class AddCustomerSpecials extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public AddCustomerSpecials() {

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            int count = 0;
            HttpPost httppost = new HttpPost(IP + "CreateCustomerSpecials.php");
            Log.e("CreateCustomerSpecials","CreateCustomerSpecials----------******************");
            try {
                // Add your data
                JSONArray jsonArray = new JSONArray();
                JSONObject json = new JSONObject();



                json.put("productCode",itemcode);
                json.put("customerCode", customercode);
                json.put("selingprice", sellingprice.getText().toString());
                json.put("fltcosts", returnItemCost(itemcode));
                json.put("eddatefrom", edtdatefrom.getText().toString());
                json.put("dteto", edtdtetospecial.getText().toString());
                json.put("userid",userId);

                jsonArray.put(json);
                count++;

                JSONObject finalInfo = new JSONObject();
                finalInfo.put("jsonobject", jsonArray);

                Log.e("JSON", finalInfo.toString()); // before pos
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());


                Log.e("response","***"+responseBody);

                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String strDesc, strPartNumber,SpecialHeaderId,DateFrom,DateTo,Price,CustomerId,CustomerSpecial;

                    strDesc = BoardDetails.getString("strDesc");
                    strPartNumber = BoardDetails.getString("strPartNumber");
                    SpecialHeaderId = BoardDetails.getString("SpecialHeaderId");
                    DateFrom = BoardDetails.getString("DateFrom");
                    DateTo = BoardDetails.getString("DateTo");
                    Price = BoardDetails.getString("Price");
                    CustomerId = BoardDetails.getString("CustomerId");
                    CustomerSpecial = BoardDetails.getString("CustomerSpecial");
                    //Log.e("")

                    ContentValues cv = new ContentValues();
                    cv.put("strDesc", strDesc);
                    cv.put("strPartNumber", strPartNumber);
                    cv.put("SpecialHeaderId", Integer.parseInt(SpecialHeaderId));
                    cv.put("DateFrom",DateFrom);
                    cv.put("DateTo", DateTo);
                    cv.put("Price", Double.parseDouble(Price) );
                    cv.put("CustomerId", CustomerId);
                    cv.put("Added", 0);
                    cv.put("CustomerSpecial",Integer.parseInt(CustomerSpecial) );

                    db.insert("CustomerSpecialsLines", null, cv);
                    new Thread()
                    {
                        public void run()
                        {
                            AddItemToSpecialActivity.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddItemToSpecialActivity.this);
                                    builder.setMessage(" Transaction Posted Successfully")
                                            .setCancelable(false)

                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    sellingprice.setText("");
                                                    txtcalgp.setText("Costs");
                                                    txtthiscost.setText("Costs");


                                                    Intent i = new Intent(AddItemToSpecialActivity.this,ProblematicItemActivity.class);
                                                    i.putExtra("name",storename );
                                                    i.putExtra("code", customercode);
                                                    startActivity(i);
                                                    dialog.dismiss();
                                                }
                                            })
                                    ;
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    }.start();

                }

               /* ContentValues cv = new ContentValues();
                cv.put("Price", selingprice.getText().toString());
                db.update("CustomerSpecialsLines", cv, "CustomerSpecial = ?", new int[]{Integer. parseInt(responseBody)});*/

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