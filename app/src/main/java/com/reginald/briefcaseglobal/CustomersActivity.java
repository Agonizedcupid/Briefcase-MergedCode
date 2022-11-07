package com.reginald.briefcaseglobal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reginald.briefcaseglobal.Aariyan.Activity.DealsButtonActivity;
import com.reginald.briefcaseglobal.Aariyan.Activity.QueueActivity;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomersActivity extends AppCompatActivity {

    public class Item {

        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;


        Item(String t, String t2, String t3, String t4) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;

        }
    }

    public class Item2 {

        String ItemString;
        String ItemString2;
        String ItemString3;
        /*String ItemString4;
        String ItemString5;*/

        Item2(String t, String t3, String t2) {
            // ItemDrawable = drawable;
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
           /* ItemString4 = t4;
            ItemString5 = t5;*/
        }
    }

    public class ItemCustomerHistory {

        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;

        ItemCustomerHistory(String t, String t2, String t3, String t4, String t5) {
            // ItemDrawable = drawable;
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
        }
    }

    static class ViewHolder {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;

    }

    static class ViewHolder2 {
        //ImageView icon;
        TextView textproductDescription;
        TextView textQuantity;
        TextView textPrice;
      /*  TextView textVat;
        TextView textOnclusive;*/
    }

    static class ViewHolder3 {
        //ImageView icon;
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
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
                rowView = inflater.inflate(R.layout.customer_tel, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.the_cust_name);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.contacts);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.textView9);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString3);
            holder.text3.setText(list.get(position).ItemString2);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }

    public class ItemsListAdapterAlpha extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapterAlpha(Context c, List<Item> l) {
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
                rowView = inflater.inflate(R.layout.alphalist, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.textView);

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

    public class ItemsListAdapterOrders extends BaseAdapter {

        private Context context;
        private List<ItemCustomerHistory> list;

        ItemsListAdapterOrders(Context c, List<ItemCustomerHistory> l) {
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
                rowView = inflater.inflate(R.layout.dailyorders_row, null);

                ViewHolder3 viewHolder = new ViewHolder3();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text1 = (TextView) rowView.findViewById(R.id.textView12);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.textView13);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.textView14);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.textView15);
                rowView.setTag(viewHolder);
            }

            ViewHolder3 holder = (ViewHolder3) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text1.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);

            Log.e("xxxx", "*************************************" + list.get(position).ItemString4);
            if (list.get(position).ItemString4.equals("1")) {
                holder.text4.setText("YES");
                holder.text4.setBackgroundColor(Color.rgb(0, 255, 0));
            }
            if (list.get(position).ItemString4.equals("0")) {
                Log.e("text4", "*************************************" + list.get(position).ItemString4);
                holder.text4.setText("NO");
                holder.text4.setBackgroundColor(Color.rgb(255, 0, 0));
            }


            return rowView;
        }

        public List<ItemCustomerHistory> getList() {
            return list;
        }
    }

    List<Item> items1, items3AlphaList;
    List<ItemCustomerHistory> itemstodayorders, itemLinesselected;
    ItemsListAdapterAlpha myItemsListAdapter3Alpha;
    ItemsListAdapter myItemsListAdapter1;
    ItemsListAdapterOrders myItemOrdersAdapter, myItemOrdersAdapterSelected;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ProgressDialog progressDoalog;
    DatePickerDialog picker, pickerto, pickeraddspecialTo, pickeraddspecialfrom;

    EditText editSearch, selectedcustomer, editsearchproduct, strprodcode, strName, fltcosts, selingprice, eddatefrom, dteto, dtefrom, edtdtefromTo, edtsellingprice, searchproduct;
    Button buttonSearch, buttonCustomerHistory, buttonToday, buttonNext, outstandingorders, buttonOrderComplete, sendspecial, searchprodspecial, btnclose, close_ordersdialog, btnclosedialog, shistory, dltspecial, clssaving, gp, saverecord, btnaddspecials, btnsearch, btnsavespecial, btnclsadding;
    TextView custcode, dialogcustomername, prodname, productcode, txtcosts, etdgp, custcodedialog, custcodedialogtxt, txtcustomername, txtcustcode, acceptablegp, itemgps, specialheader;
    ;
    String roles, userID, searchText, IP, DimsIp;
    String Result = "", OrderId = "", subscriberId;
    private SQLiteDatabase db, dbOrders;
    Dialog dialogViewinner;
    ListView listcustomers, lvproducts, lvlistofcustomerorders, lvproductsavailable, lvselecteditems, lverrors;
    ImageView closeitemselected;


    private Button goNext;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startProgress("Please Wait!");
        setContentView(R.layout.activity_customers);
        File file = new File("Salesmanbriefcase");

        sharedPreferences = getSharedPreferences("IP_FILE", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
        db = this.openOrCreateDatabase(getApplicationContext().getFilesDir() + "/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        dbOrders = this.openOrCreateDatabase(getApplicationContext().getFilesDir() + "/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        editSearch = (EditText) findViewById(R.id.editSearch);
        selectedcustomer = (EditText) findViewById(R.id.selectedcustomer);
        custcode = (TextView) findViewById(R.id.custcode);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonCustomerHistory = (Button) findViewById(R.id.buttonCustomerHistory);
        buttonNext = (Button) findViewById(R.id.nextorderheader);
        buttonToday = (Button) findViewById(R.id.buttonToday);
        buttonOrderComplete = (Button) findViewById(R.id.nextcustomercentral);
        outstandingorders = (Button) findViewById(R.id.outstandingorders);
        listcustomers = (ListView) findViewById(R.id.listcustomers);

        goNext = findViewById(R.id.goNext);
        goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(CustomersActivity.this, ""+custcode.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                if (!custcode.getText().toString().trim().isEmpty() || !custcode.getText().toString().trim().equals("")) {
                    if (!sharedPreferences.getString("CODE", "SA005").equals(custcode.getText().toString().trim())) {
                        editor.putString("from", "Date From");
                        editor.putString("to", "Date To");
                    }
                    editor.putString("CODE", custcode.getText().toString().trim());
                    editor.putString("url", IP);
                    editor.commit();
                    startActivity(new Intent(CustomersActivity.this, DealsButtonActivity.class)
                            .putExtra("url", IP));
                } else {
                    Toast.makeText(CustomersActivity.this, "Select a customer", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (CheckIfMerchie() > 0) {
            buttonOrderComplete.setVisibility(View.INVISIBLE);
        }

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");

        outstandingorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomersActivity.this, OutStandingActivity.class);
                i.putExtra("userID", userID);
                i.putExtra("roles", roles);
                startActivity(i);
            }
        });

        CustomerLookupResult(" ");
        myItemsListAdapter1 = new ItemsListAdapter(CustomersActivity.this, items1);
        myItemsListAdapter3Alpha = new ItemsListAdapterAlpha(CustomersActivity.this, items3AlphaList);

        listcustomers.setAdapter(myItemsListAdapter1);

        Cursor cursor2 = dbOrders.rawQuery("SELECT * from tblSettings limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                IP = cursor2.getString(cursor2.getColumnIndex("IP"));
                DimsIp = cursor2.getString(cursor2.getColumnIndex("DimsIp"));

            } while (cursor2.moveToNext());
        }

        listcustomers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item selectedItem2 = (Item) (adapterView.getItemAtPosition(i));
                Dialog dialogView = new Dialog(CustomersActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.customerdialogoptions);

                txtcustomername = (TextView) dialogView.findViewById(R.id.txtcustomername);
                custcodedialogtxt = (TextView) dialogView.findViewById(R.id.txtcustcode);
                shistory = (Button) dialogView.findViewById(R.id.shistory);
                btnclosedialog = (Button) dialogView.findViewById(R.id.btnclosedialog);

                txtcustomername.setText(selectedItem2.ItemString.toString());
                custcodedialogtxt.setText(selectedItem2.ItemString2.toString());

                shistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CustomersActivity.this, OrderActivity.class);
                        intent.putExtra("name", txtcustomername.getText().toString());
                        intent.putExtra("code", custcodedialogtxt.getText().toString());
                        startActivity(intent);
                        dialogView.dismiss();
                    }
                });
                btnclosedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogView.dismiss();
                    }
                });

                dialogView.show();
                return false;
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (custcode.getText().toString().trim().length() > 1 && !(custcode.getText().toString()).equals("TextView")) {
                    Intent i = new Intent(CustomersActivity.this, OrderHeader.class);
                    i.putExtra("userID", userID);
                    i.putExtra("roles", roles);
                    i.putExtra("custcode", custcode.getText().toString());
                    i.putExtra("custName", selectedcustomer.getText().toString());
                    i.putExtra("neworder", "neworder");
                    startActivity(i);
                    //

                   /* Intent i = new Intent(CustomersActivity.this,AfterCustomerSelect.class);
                    i.putExtra("userID",userID);
                    i.putExtra("roles",roles);
                    i.putExtra("custcode",custcode.getText().toString());
                    i.putExtra("custName",selectedcustomer.getText().toString());
                    startActivity(i);*/
                } else {
                    final AlertDialog.Builder dialogretry = new AlertDialog.Builder(CustomersActivity.this);
                    dialogretry.setTitle("No Customer")
                            .setMessage("Please Select Customer.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    paramDialogInterface.dismiss();
                                }
                            });
                    dialogretry.show();
                }

            }
        });

        editSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("Touch", "********** here");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                editSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
                editSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                return false;
            }
        });

        listcustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item selectedItem = (Item) (adapterView.getItemAtPosition(i));
                selectedcustomer.setText("" + selectedItem.ItemString);
                custcode.setText("" + selectedItem.ItemString2);

            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = editSearch.getText().toString();
                if (searchText.equals("")) {
                    CustomerLookupResult("");
                } else {
                    CustomerLookupResult(searchText);
                }
                myItemsListAdapter1 = new ItemsListAdapter(CustomersActivity.this, items1);
                myItemsListAdapter3Alpha = new ItemsListAdapterAlpha(CustomersActivity.this, items3AlphaList);

                listcustomers.setAdapter(myItemsListAdapter1);
                //  alpha_listView.setAdapter(myItemsListAdapter3Alpha);
                editSearch.getText().clear();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTodaysOrders();
                myItemOrdersAdapter = new ItemsListAdapterOrders(CustomersActivity.this, itemstodayorders);

                Dialog dialogView = new Dialog(CustomersActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.todays_orders);


                lvlistofcustomerorders = (ListView) dialogView.findViewById(R.id.lvlistofcustomerorders);
                lverrors = (ListView) dialogView.findViewById(R.id.lverrors);
                close_ordersdialog = (Button) dialogView.findViewById(R.id.close_ordersdialog);
                lvlistofcustomerorders.setAdapter(myItemOrdersAdapter);

                getTodaysOrdersErrors();
                myItemOrdersAdapter = new ItemsListAdapterOrders(CustomersActivity.this, itemstodayorders);
                lverrors.setAdapter(myItemOrdersAdapter);

                lvlistofcustomerorders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        ItemCustomerHistory selectedItem2 = (ItemCustomerHistory) (adapterView.getItemAtPosition(i));
                        Dialog dialogViewLines = new Dialog(CustomersActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                        dialogViewLines.setCancelable(false);
                        dialogViewLines.setContentView(R.layout.orderlines_list);
                        getTodaysOrdersLines(selectedItem2.ItemString5);
                        lvselecteditems = (ListView) dialogViewLines.findViewById(R.id.lvselecteditems);
                        closeitemselected = (ImageView) dialogViewLines.findViewById(R.id.closeitemselected);
                        myItemOrdersAdapterSelected = new ItemsListAdapterOrders(CustomersActivity.this, itemLinesselected);
                        lvselecteditems.setAdapter(myItemOrdersAdapterSelected);

                        closeitemselected.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogViewLines.dismiss();
                            }
                        });

                        dialogViewLines.show();

                    }
                });

                lvlistofcustomerorders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ItemCustomerHistory selectedItem2 = (ItemCustomerHistory) (adapterView.getItemAtPosition(i));

                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                        builder.setMessage(" Check The Transaction.")
                                .setCancelable(false)

                                .setPositiveButton("Transact ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (getInvoiceNo(selectedItem2.ItemString5).equals("NO INVOICE")) {
                                            if (checkConnection()) {
                                                startProgress("Trying to Post");
                                                new UploadNewOrderLinesDetails(selectedItem2.ItemString5).execute();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                                                builder.setMessage("No connection")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                dialog.dismiss();
                                                            }
                                                        });

                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                            //repost
                                        } else {
                                            Log.e("pdforder", DimsIp + "pdforder/" + getInvoiceNo(selectedItem2.ItemString5));
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DimsIp + "pdforder/" + getInvoiceNo(selectedItem2.ItemString5).equals("NO INVOICE")));
                                            startActivity(browserIntent);
                                        }
                                    }
                                })
                        ;
                        AlertDialog alert = builder.create();
                        alert.show();


                        return false;
                    }
                });
                lverrors.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ItemCustomerHistory selectedItem2 = (ItemCustomerHistory) (adapterView.getItemAtPosition(i));

                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                        builder.setMessage(" Check The Transaction.")
                                .setCancelable(false)

                                .setPositiveButton("Transact ", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if (getInvoiceNo(selectedItem2.ItemString5).equals("NO INVOICE")) {
                                            if (checkConnection()) {
                                                startProgress("Trying to Post");
                                                new UploadNewOrderLinesDetails(selectedItem2.ItemString5).execute();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                                                builder.setMessage("No connection")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                dialog.dismiss();
                                                            }
                                                        });

                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                            //repost
                                        }
                                    }
                                })
                        ;
                        AlertDialog alert = builder.create();
                        alert.show();


                        return false;
                    }
                });

                close_ordersdialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogView.dismiss();
                    }
                });


                dialogView.show();
            }
        });

        buttonOrderComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getProducts("");
                myItemsListAdapter1 = new ItemsListAdapter(CustomersActivity.this, items1);

                Dialog dialogView = new Dialog(CustomersActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.customerpricing);


                sendspecial = (Button) dialogView.findViewById(R.id.sendspecial);
                btnaddspecials = (Button) dialogView.findViewById(R.id.btnaddspecials);
                editsearchproduct = (EditText) dialogView.findViewById(R.id.editsearchproduct);
                lvproducts = (ListView) dialogView.findViewById(R.id.lvproducts);
                searchprodspecial = (Button) dialogView.findViewById(R.id.searchprodspecial);
                dialogcustomername = (TextView) dialogView.findViewById(R.id.dialogcustomername);
                btnclose = (Button) dialogView.findViewById(R.id.btnclose);
                lvproducts.setAdapter(myItemsListAdapter1);
                dialogcustomername.setText(selectedcustomer.getText().toString());

                lvproducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Item selectedItem2 = (Item) (adapterView.getItemAtPosition(i));
                        dialogViewinner = new Dialog(CustomersActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogViewinner.setCancelable(false);
                        dialogViewinner.setContentView(R.layout.dialogupdatepricing);
                        clssaving = (Button) dialogViewinner.findViewById(R.id.clssaving);
                        dltspecial = (Button) dialogViewinner.findViewById(R.id.dltspecial);
                        gp = (Button) dialogViewinner.findViewById(R.id.gp);
                        saverecord = (Button) dialogViewinner.findViewById(R.id.saverecord);
                        strprodcode = (EditText) dialogViewinner.findViewById(R.id.strprodcode);
                        strName = (EditText) dialogViewinner.findViewById(R.id.strName);
                        fltcosts = (EditText) dialogViewinner.findViewById(R.id.fltcosts);
                        selingprice = (EditText) dialogViewinner.findViewById(R.id.selingprice);
                        eddatefrom = (EditText) dialogViewinner.findViewById(R.id.eddatefrom);
                        dteto = (EditText) dialogViewinner.findViewById(R.id.dteto);
                        itemgps = (TextView) dialogViewinner.findViewById(R.id.textView37);

                        dltspecial.setBackgroundColor(Color.RED);
                        getProductsSpecialToEdit(selectedItem2.ItemString3);

                        dltspecial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                                builder.setMessage(" This transaction will be deleted, are you sure?")
                                        .setCancelable(false)

                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                new DeleteCustomerSpecial(selectedItem2.ItemString3).execute();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        });

                        eddatefrom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar cldr = Calendar.getInstance();
                                int day = cldr.get(Calendar.DAY_OF_MONTH);
                                int month = cldr.get(Calendar.MONTH);
                                int year = cldr.get(Calendar.YEAR);
                                // date picker dialog
                                picker = new DatePickerDialog(CustomersActivity.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                eddatefrom.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                            }
                                        }, year, month, day);
                                picker.show();
                            }
                        });

                        dteto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar cldr = Calendar.getInstance();
                                int day = cldr.get(Calendar.DAY_OF_MONTH);
                                int month = cldr.get(Calendar.MONTH);
                                int year = cldr.get(Calendar.YEAR);
                                // date picker dialog
                                pickerto = new DatePickerDialog(CustomersActivity.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                dteto.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                            }
                                        }, year, month, day);
                                pickerto.show();
                            }
                        });
                        gp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                itemgps.setText("" + marginCalculator(Double.parseDouble(fltcosts.getText().toString()), Double.parseDouble(selingprice.getText().toString())));

                            }
                        });
                        saverecord.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              /*  if (marginCalculator(Double.parseDouble(fltcosts.getText().toString()), Double.parseDouble(selingprice.getText().toString())) < returnItemGP(strprodcode.getText().toString())) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
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
                                new UpdateCustomerSpecials(selectedItem2.ItemString3).execute();
                                //  }


                            }
                        });
                        clssaving.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                dialogViewinner.dismiss();
                            }
                        });


                        dialogViewinner.show();

                        return false;
                    }
                });

                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogView.dismiss();
                    }
                });

                sendspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DimsIp + "BriefcaseSpecials/" + custcode.getText().toString()));
                        startActivity(browserIntent);
                    }
                });
                searchprodspecial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getProducts(editsearchproduct.getText().toString());
                        // dialogView.dismiss();
                    }
                });

                btnaddspecials.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getProducts("");
                        myItemsListAdapter1 = new ItemsListAdapter(CustomersActivity.this, items1);

                        Dialog dialogViewaddspecials = new Dialog(CustomersActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogViewaddspecials.setCancelable(false);
                        dialogViewaddspecials.setContentView(R.layout.addspecial);

                        btnsearch = (Button) dialogViewaddspecials.findViewById(R.id.btnsearch);
                        btnsavespecial = (Button) dialogViewaddspecials.findViewById(R.id.btnsavespecial);
                        btnclsadding = (Button) dialogViewaddspecials.findViewById(R.id.btnclsadding);
                        lvproductsavailable = (ListView) dialogViewaddspecials.findViewById(R.id.lvproductsavailable);
                        dtefrom = (EditText) dialogViewaddspecials.findViewById(R.id.dtefrom);
                        edtdtefromTo = (EditText) dialogViewaddspecials.findViewById(R.id.edtdtefrom);
                        edtsellingprice = (EditText) dialogViewaddspecials.findViewById(R.id.edtsellingprice);
                        searchproduct = (EditText) dialogViewaddspecials.findViewById(R.id.searchproduct);
                        prodname = (TextView) dialogViewaddspecials.findViewById(R.id.prodname);
                        productcode = (TextView) dialogViewaddspecials.findViewById(R.id.productcode);
                        txtcosts = (TextView) dialogViewaddspecials.findViewById(R.id.txtcosts);
                        etdgp = (TextView) dialogViewaddspecials.findViewById(R.id.textView34);
                        custcodedialog = (TextView) dialogViewaddspecials.findViewById(R.id.custcodedialog);
                        acceptablegp = (TextView) dialogViewaddspecials.findViewById(R.id.acceptablegp);
                        specialheader = (TextView) dialogViewaddspecials.findViewById(R.id.specialheader);
                        acceptablegp.setText("0");
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                        subscriberId = ts + "-" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String tm = sdf.format(c.getTime());
                        dtefrom.setText(tm);
                        edtdtefromTo.setText(tm);
                        custcodedialog.setText(custcode.getText().toString());


                        returnProducts("");

                        dtefrom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar cldr = Calendar.getInstance();
                                int day = cldr.get(Calendar.DAY_OF_MONTH);
                                int month = cldr.get(Calendar.MONTH);
                                int year = cldr.get(Calendar.YEAR);
                                // date picker dialog
                                pickeraddspecialfrom = new DatePickerDialog(CustomersActivity.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                dtefrom.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                            }
                                        }, year, month, day);
                                pickeraddspecialfrom.show();
                            }
                        });
                        edtdtefromTo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar cldr = Calendar.getInstance();
                                int day = cldr.get(Calendar.DAY_OF_MONTH);
                                int month = cldr.get(Calendar.MONTH);
                                int year = cldr.get(Calendar.YEAR);
                                // date picker dialog
                                pickeraddspecialTo = new DatePickerDialog(CustomersActivity.this,
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                edtdtefromTo.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                            }
                                        }, year, month, day);
                                pickeraddspecialTo.show();
                            }
                        });
                        btnsearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                returnProducts(searchproduct.getText().toString());
                            }
                        });
                        btnclsadding.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogViewaddspecials.dismiss();
                            }
                        });
                        btnsavespecial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if (!txtcosts.getText().equals("Cost") && edtsellingprice.getText().toString().length() > 0) {
                                      /* if(marginCalculator(Double.parseDouble(txtcosts.getText().toString()) ,Double.parseDouble(edtsellingprice.getText().toString())) < Double.parseDouble(acceptablegp.getText().toString())){
                                           AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                                           builder.setMessage("Below Acceptable Product GP%")
                                                   .setCancelable(false)
                                                   .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                       public void onClick(DialogInterface dialog, int id) {

                                                           dialog.dismiss();
                                                       }
                                                   }) ;
                                           AlertDialog alert = builder.create();
                                           alert.show();

                                       }else{*/
                                    new AddCustomerSpecials().execute();
                                    //}

                                } else {
                                    Toast.makeText(CustomersActivity.this, " Make sure you have entered the price.", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        lvproductsavailable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Item selectedItem2 = (Item) (adapterView.getItemAtPosition(i));
                                prodname.setText(selectedItem2.ItemString2);
                                productcode.setText(selectedItem2.ItemString);
                                txtcosts.setText(selectedItem2.ItemString3);
                                acceptablegp.setText(selectedItem2.ItemString4);

                                etdgp.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        if (edtsellingprice.getText().toString().length() > 0) {
                                            etdgp.setText("" + String.format("%.2f", marginCalculator(Double.parseDouble(txtcosts.getText().toString()), Double.parseDouble(edtsellingprice.getText().toString()))) + "%");

                                        } else {
                                            Toast.makeText(CustomersActivity.this, " No Selling Price.", Toast.LENGTH_LONG).show();
                                        }
                                        return false;
                                    }
                                });

                            }
                        });
                        dialogViewaddspecials.show();

                    }
                });


                dialogView.show();
                /*}else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                    builder.setMessage(" No Specials Found for "+ selectedcustomer.getText().toString() + ", would you like to create some specials?")
                            .setCancelable(false)

                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }*/

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.queueMenu:
                startActivity(new Intent(CustomersActivity.this, QueueActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from tblSettings limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                IP = cursor2.getString(cursor2.getColumnIndex("IP"));
                DimsIp = cursor2.getString(cursor2.getColumnIndex("DimsIp"));

            } while (cursor2.moveToNext());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent myIntent = new Intent(CustomersActivity.this, HomeScreen.class);
            myIntent.putExtra("userID", userID);
            myIntent.putExtra("roles", roles);
            startActivity(myIntent);


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public double marginCalculator(double cost, double onCellVal) {
        return (1 - (cost / onCellVal)) * 100;


    }

    @SuppressLint("Range")
    public int casecount(String ID) {
        int caseC = 0;
        Cursor cursor2 = dbOrders.rawQuery("SELECT sum(Quantity) Quantity from OrderLines where ID ='" + ID + "' ", null);

        if (cursor2.moveToFirst()) {
            do {
                caseC = cursor2.getInt(cursor2.getColumnIndex("Quantity"));

            } while (cursor2.moveToNext());
        }

        return caseC;
    }

    public double returnItemGP(String productCode) {
        double costs = 0;
        Cursor cursor2 = db.rawQuery("SELECT gptolerance from Products where strPartNumber ='" + productCode + "' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {

                costs = cursor2.getDouble(cursor2.getColumnIndex("gptolerance"));
                Log.e("gptolerance", "gptolerance**************" + costs);

            } while (cursor2.moveToNext());
        }

        return costs;
    }

    public void returnProducts(String prodName) {
        startProgress("Please Wait!");
        items1 = new ArrayList<Item>();
        Cursor cursor = db.rawQuery("SELECT Products.strDesc,Products.strPartNumber,Products.Cost,Tax, gptolerance FROM Products   WHERE strDesc like '%" + prodName + "%' order by [strDesc] limit 100", null);

        if (cursor.moveToFirst()) {
            do {

                Item listItem = new Item(cursor.getString(cursor.getColumnIndex("strPartNumber")), cursor.getString(cursor.getColumnIndex("strDesc")),
                        cursor.getString(cursor.getColumnIndex("Cost")), cursor.getString(cursor.getColumnIndex("gptolerance")));
                items1.add(listItem);

            } while (cursor.moveToNext());
        }
        progressDoalog.dismiss();
        myItemsListAdapter1 = new ItemsListAdapter(CustomersActivity.this, items1);
        lvproductsavailable.setAdapter(myItemsListAdapter1);
        myItemsListAdapter1.notifyDataSetChanged();
        //textTotalBasedProduct.setText(returnTotals(subscriberId)) ;

    }

    public void getProducts(String Products) {

        items1 = new ArrayList<Item>();
        Cursor cursor = db.rawQuery("Select CustomerSpecialsLines.strPartNumber,CustomerSpecialsLines.strDesc,Products.Cost,Price,CustomerSpecial from CustomerSpecialsLines " +
                " inner join Products on Products.strPartNumber =CustomerSpecialsLines.strPartNumber  where CustomerId ='" + custcode.getText().toString() + "' and CustomerSpecialsLines.strDesc like '%" + Products + "%' order by CustomerSpecialsLines.strDesc", null);

        if (cursor.moveToFirst()) {
            do {

                Item listItem = new Item("( " + cursor.getString(cursor.getColumnIndex("strPartNumber")) + " ) " + cursor.getString(cursor.getColumnIndex("strDesc")),
                        "Price " + cursor.getString(cursor.getColumnIndex("Price")) + " Cost " + cursor.getString(cursor.getColumnIndex("Cost")), cursor.getString(cursor.getColumnIndex("CustomerSpecial")), "");
                items1.add(listItem);

            } while (cursor.moveToNext());
        }

    }

    public void getProductsSpecialToEdit(String specialID) {

        Cursor cursor = db.rawQuery("Select CustomerSpecialsLines.strPartNumber,CustomerSpecialsLines.strDesc,Products.Cost,Price,CustomerSpecial,DateFrom,DateTo from CustomerSpecialsLines " +
                " inner join Products on Products.strPartNumber =CustomerSpecialsLines.strPartNumber  where CustomerSpecial ='" + specialID + "'", null);

        if (cursor.moveToFirst()) {
            do {

                strprodcode.setText(cursor.getString(cursor.getColumnIndex("strPartNumber")));
                strName.setText(cursor.getString(cursor.getColumnIndex("strDesc")));
                fltcosts.setText(cursor.getString(cursor.getColumnIndex("Cost")));
                selingprice.setText(cursor.getString(cursor.getColumnIndex("Price")));
                eddatefrom.setText(cursor.getString(cursor.getColumnIndex("DateFrom")));
                dteto.setText(cursor.getString(cursor.getColumnIndex("DateTo")));

            } while (cursor.moveToNext());
        }
    }

    public boolean checkConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void getTodaysOrders() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm = sdf.format(c.getTime());
        Log.e("tm", "********tm*****************" + tm);
        itemstodayorders = new ArrayList<ItemCustomerHistory>();
        //Cursor cursor = dbOrders.rawQuery("select * from OrderHeaders   where OrderDate='"+tm+"' and Complete = 1" ,null);
        Cursor cursor = dbOrders.rawQuery("select * from OrderHeaders where Complete = 1 order by OrderHeaderAutoID desc Limit 50", null);
        String isQuotation = "Order";
        String OrderID = "Order";

        if (cursor.moveToFirst()) {
            do {

                Log.e("tm", "********tm*****************" + cursor.getString(cursor.getColumnIndex("isQuotation")));
                if (cursor.getInt(cursor.getColumnIndex("isQuotation")) == 1) {
                    isQuotation = "Quote";
                }
                OrderID = cursor.getString(cursor.getColumnIndex("OrderID"));
                @SuppressLint("Range") ItemCustomerHistory listItem = new ItemCustomerHistory(cursor.getString(cursor.getColumnIndex("StoreName")) + "\n Case Count: " + casecount(cursor.getString(cursor.getColumnIndex("ID"))), cursor.getString(cursor.getColumnIndex("OrderNumber")),
                        cursor.getString(cursor.getColumnIndex("DeliveryDate")) + "\n" + isQuotation + "\n" + OrderID, "" + cursor.getInt(cursor.getColumnIndex("Uploaded")), cursor.getString(cursor.getColumnIndex("ID")));
                itemstodayorders.add(listItem);

            } while (cursor.moveToNext());
        }

    }

    public void getTodaysOrdersErrors() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm = sdf.format(c.getTime());
        Log.e("tm", "********tm*****************" + tm);
        itemstodayorders = new ArrayList<ItemCustomerHistory>();
        //Cursor cursor = dbOrders.rawQuery("select * from OrderHeaders   where OrderDate='"+tm+"' and Complete = 1" ,null);
        Cursor cursor = dbOrders.rawQuery("select * from OrderHeaders where Complete = 1 and Uploaded = 0 order by OrderHeaderAutoID desc Limit 50", null);
        String isQuotation = "Order";
        String OrderID = "Order";

        if (cursor.moveToFirst()) {
            do {

                Log.e("tm", "********tm*****************" + cursor.getString(cursor.getColumnIndex("isQuotation")));
                if (cursor.getInt(cursor.getColumnIndex("isQuotation")) == 1) {
                    isQuotation = "Quote";
                }
                OrderID = cursor.getString(cursor.getColumnIndex("OrderID"));
                @SuppressLint("Range") ItemCustomerHistory listItem = new ItemCustomerHistory(cursor.getString(cursor.getColumnIndex("StoreName")) + "\n Case Count: " + casecount(cursor.getString(cursor.getColumnIndex("ID"))), cursor.getString(cursor.getColumnIndex("OrderNumber")),
                        cursor.getString(cursor.getColumnIndex("DeliveryDate")) + "\n" + isQuotation + "\n" + OrderID, "" + cursor.getInt(cursor.getColumnIndex("Uploaded")), cursor.getString(cursor.getColumnIndex("ID")));
                itemstodayorders.add(listItem);

            } while (cursor.moveToNext());
        }

    }

    public void getTodaysOrdersLines(String ID) {

        itemLinesselected = new ArrayList<ItemCustomerHistory>();
        Cursor cursor = dbOrders.rawQuery("select OrderLines.strDesc,OrderLines.Price,OrderLines.Quantity,OrderLines.strPartNumber,OrderHeaders.Uploaded from OrderLines inner join OrderHeaders on OrderHeaders.ID= OrderLines.ID  where OrderLines.ID='" + ID + "'  ", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") ItemCustomerHistory listItem = new ItemCustomerHistory(cursor.getString(cursor.getColumnIndex("strDesc")), cursor.getString(cursor.getColumnIndex("Price")) + "\n " + cursor.getString(cursor.getColumnIndex("Quantity")),
                        "", "" + cursor.getInt(cursor.getColumnIndex("Uploaded")), cursor.getString(cursor.getColumnIndex("strPartNumber")));
                itemLinesselected.add(listItem);

            } while (cursor.moveToNext());
        }
        myItemOrdersAdapterSelected = new ItemsListAdapterOrders(CustomersActivity.this, itemLinesselected);

    }

    public String getInvoiceNo(String ID) {
        String NoInv = "NO INVOICE";
        Cursor cursor = dbOrders.rawQuery("select OrderID from OrderHeaders where ID='" + ID + "' and (OrderID != '' or OrderID is not null or OrderID !='null' )  limit 1", null);
        if (cursor.moveToFirst()) {
            do {

                NoInv = cursor.getString(cursor.getColumnIndex("OrderID"));

            } while (cursor.moveToNext());
        }

        return NoInv;
    }

    public int countSpecials() {
        Cursor c = db.rawQuery("select strPartNumber from CustomerSpecialsLines ", null);
        return c.getCount();

    }

    private void CustomerLookupResult(String search) {
        items1 = new ArrayList<Item>();
        items3AlphaList = new ArrayList<Item>();

        //
        Cursor c = db.rawQuery("SELECT * FROM Customers WHERE [CustomerStoreName] like '%" + search + "%'  order by [CustomerStoreName] limit 100", null);
        int nameIndex = c.getColumnIndex("CustomerStoreName");
        int nameIndex2 = c.getColumnIndex("CustomerCode");
        int nameIndex3 = c.getColumnIndex("CustomerContactTelephone");
        int nameIndex4 = c.getColumnIndex("CustomerContactCellphone");
        if (c.moveToFirst()) {
            do {
                String s = "zz";
                if (c.getString(nameIndex).length() > 0) {
                    s = c.getString(nameIndex);
                }

                String contacts = c.getString(c.getColumnIndex("CustomerContactTelephone")) + " " + c.getString(c.getColumnIndex("CustomerContactCellphone"));
                stringArrayList.add(s.substring(0, 1));
                String s3 = c.getString(nameIndex2);
                Item item = new Item(s, s3, contacts, "");
                items1.add(item);
                Log.e("contacts", "contacts**********************" + contacts);
            } while (c.moveToNext());
        }

        //c.close();
      /*  Cursor letters = db.rawQuery("Select DISTINCT substr(CustomerStoreName, 1, 1)as Ltr From Customers WHERE [CustomerStoreName] like '%" + search + "%' order by Ltr", null);
        int alpha = letters.getColumnIndex("Ltr");
        if (letters.moveToFirst()) {
            //labels.add("Press Here");
            do {
                String sAlpha = letters.getString(alpha);
                Item itemAlpha = new Item(sAlpha, "","");
                items3AlphaList.add(itemAlpha);
            } while (letters.moveToNext());
        }*/
        //letters.close();
        //db.close();
    }

    public void lazyScrolllastId(int lastId) {

       /* Cursor cursor2 = db.rawQuery("SELECT * from Customers where id>"+lastId+" ", null);

        if (cursor2.moveToFirst()) {
            do {
                priceListnameReturned =cursor2.getString(cursor2.getColumnIndex("CustomerGroup"));

            } while (cursor2.moveToNext());
        }

        return priceListnameReturned;*/

    }

    public String XmlTemplate() {


        String Xml = "<Lines>" +
                "<strProduct>unik</strProduct>" +
                "<Price>moola</Price>" +
                "<Quantity>palo</Quantity>" +
                "</Lines>";
        return Xml;
    }

    private class UploadNewOrderLinesDetails extends AsyncTask<Void, Void, Void> {
        String id;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public UploadNewOrderLinesDetails(String id) {
            this.id = id;
        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            int count = 0;
            HttpPost httppost = new HttpPost(IP + "PostLinesNew.php");
            Log.e("PostOrderLines", "PostOrderLines----------******************" + IP + "PostLinesNew.php");
            try {
                // Add your data

                Cursor cursorh = dbOrders.rawQuery("SELECT * from OrderHeaders where Uploaded=0 and ID='" + id + "' limit 1", null);
                String header = "";

                if (cursorh.moveToFirst()) {
                    do {
                        //  header += "<Headers><ID>"+cursorh.getString(cursorh.getColumnIndex("ID"))+"</ID><CustomerCode>"+cursorh.getString(cursorh.getColumnIndex("CustomerCode"))+"</CustomerCode><DeliveryDate>"+cursorh.getString(cursorh.getColumnIndex("DeliveryDate"))+"</DeliveryDate><UserID>"+getPasswordByName()+"</UserID><Quote>"+cursorh.getInt(cursorh.getColumnIndex("isQuotation"))+"</Quote><DeliveryAddressID>"+cursorh.getInt(cursorh.getColumnIndex("DeliveryAddressID"))+"</DeliveryAddressID><Onum>"+cursorh.getString(cursorh.getColumnIndex("OrderNumber"))+"</Onum>";
                        header += "<Headers><ID>" + cursorh.getString(cursorh.getColumnIndex("ID")) + "</ID><CustomerCode>" + cursorh.getString(cursorh.getColumnIndex("CustomerCode")) + "</CustomerCode><DeliveryDate>" + cursorh.getString(cursorh.getColumnIndex("DeliveryDate")) + "</DeliveryDate><UserID>" + getPasswordByName() + "</UserID><Quote>" + cursorh.getInt(cursorh.getColumnIndex("isQuotation")) + "</Quote><DeliveryAddressID>" + cursorh.getInt(cursorh.getColumnIndex("DeliveryAddressID")) + "</DeliveryAddressID><Onum>" + cursorh.getString(cursorh.getColumnIndex("OrderNumber")) + "</Onum><notes>" + cursorh.getString(cursorh.getColumnIndex("Notes")) + "</notes><Coordinates>" + cursorh.getString(cursorh.getColumnIndex("Coordinates")) + "</Coordinates>";
                    } while (cursorh.moveToNext());
                }

                Cursor cursor2 = dbOrders.rawQuery("SELECT * from OrderLines where Uploaded = 0 and ID='" + id + "'", null);
                JSONArray jsonArray = new JSONArray();
                String xml = "";

                if (cursor2.moveToFirst()) {
                    do {
                        xml += XmlTemplate().toString()
                                .replace("unik", cursor2.getString(cursor2.getColumnIndex("strPartNumber")))
                                .replace("moola", "" + cursor2.getString(cursor2.getColumnIndex("Price")))
                                .replace("palo", "" + cursor2.getString(cursor2.getColumnIndex("Quantity")));
                    } while (cursor2.moveToNext());
                }

                String footer = "</Headers>";
                jsonArray.put(header + xml + footer);

                JSONObject finalInfo = new JSONObject();
                finalInfo.put("jsonobject", jsonArray);


                Log.e("JSONBef", finalInfo.toString()); // before pos
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                responseBody = responseBody.replaceAll("\"", "");
                Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);   //The response
                //Log.e("OrderLinesAutoId", "UPDATE  OrderLines SET Uploaded = 1 where OrderLinesAutoId in( " + responseBody + ")");
                //JSONArray BoardInfo = new JSONArray(responseBody);

                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String ID;

                    OrderId = BoardDetails.getString("OrderId").toString();
                    Result = BoardDetails.getString("Result");
                    ID = BoardDetails.getString("ID");

                    if (Result.equals("SUCCESS")) {
                        dbOrders.execSQL("UPDATE  OrderLines SET Uploaded = 1 where ID in(' " + ID + "')");
                        dbOrders.execSQL("UPDATE  OrderHeaders SET Uploaded = 1 where ID in( '" + ID + "')");

                        ContentValues cv = new ContentValues();
                        cv.put("OrderID", OrderId);
                        dbOrders.update("OrderHeaders", cv, "ID=?", new String[]{id});
                    }
                }

                if (Result.equals("SUCCESS")) {
                    //db.execSQL("Update StockTakeLines set blnScanned=0  where blnScanned = 1 ");
                    //    db.execSQL("Delete from  StockTakeLines ");
                    progressDoalog.dismiss();

                    new Thread() {
                        public void run() {
                            CustomersActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    //Do your UI operations like dialog opening or Toast here

                                    final AlertDialog.Builder dialogll = new AlertDialog.Builder(CustomersActivity.this);
                                    dialogll.setTitle("POST")
                                            .setMessage("Your Records Posted Successfully.")
                                            .setNegativeButton("Done Done", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                    //db.execSQL("delete from StockTakeLines");
                                                    Intent i = new Intent(CustomersActivity.this, CustomersActivity.class);
                                                    i.putExtra("userID", getPasswordByName());
                                                    i.putExtra("roles", "");
                                                    startActivity(i);
                                                }
                                            }).setPositiveButton("View Transaction", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                    //db.execSQL("delete from StockTakeLines");
                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DimsIp + "pdforder/" + OrderId));
                                                    startActivity(browserIntent);
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

    private class UpdateCustomerSpecials extends AsyncTask<Void, Void, Void> {
        String specialId;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public UpdateCustomerSpecials(String specialId) {
            this.specialId = specialId;
        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            int count = 0;
            HttpPost httppost = new HttpPost(IP + "UpdateCustomerSpecials.php");
            Log.e("UpdateCustomerSpecials", "UpdateCustomerSpecials----------******************");
            try {
                // Add your data
                JSONArray jsonArray = new JSONArray();
                JSONObject json = new JSONObject();

                json.put("selingprice", selingprice.getText().toString());
                json.put("fltcosts", fltcosts.getText().toString());
                json.put("eddatefrom", eddatefrom.getText().toString());
                json.put("dteto", dteto.getText().toString());
                json.put("specialId", specialId);
                json.put("userid", getPasswordByName());

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
                responseBody = responseBody.replaceAll("\"", "");

                Log.e("response", "***" + responseBody);

               /* ContentValues cv = new ContentValues();
                cv.put("Price", selingprice.getText().toString());
                db.update("CustomerSpecialsLines", cv, "CustomerSpecial = ?", new int[]{Integer. parseInt(responseBody)});*/

                db.rawQuery("update CustomerSpecialsLines set Price='" + selingprice.getText().toString() + "' where CustomerSpecial='" + responseBody + "'", null);
                // dbH.updateDeals("UPDATE  OrderLines SET Uploaded = 1 where OrderDetailId in( " + responseBody + ")");
                dialogViewinner.dismiss();


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

    private class DeleteCustomerSpecial extends AsyncTask<Void, Void, Void> {
        String specialId;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public DeleteCustomerSpecial(String specialId) {
            this.specialId = specialId;
        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            int count = 0;
            HttpPost httppost = new HttpPost(IP + "DeleteCustomerSpecials.php");
            Log.e("DeleteCustomerSpecials", "DeleteCustomerSpecials----------******************");
            try {
                // Add your data
                JSONArray jsonArray = new JSONArray();
                JSONObject json = new JSONObject();

                json.put("specialId", specialId);
                json.put("userid", getPasswordByName());

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


                Log.e("response", "***" + responseBody);

                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String strDesc, strPartNumber, SpecialHeaderId, DateFrom, DateTo, Price, CustomerId, CustomerSpecial;


                    CustomerSpecial = BoardDetails.getString("specials");

                    String[] whereArgs = new String[]{String.valueOf(CustomerSpecial)};
                    db.delete("CustomerSpecialsLines", "CustomerSpecial=?", whereArgs);


                    new Thread() {
                        public void run() {
                            CustomersActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                                    builder.setMessage(" Transaction Deleted Successfully")
                                            .setCancelable(false)

                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialogViewinner.dismiss();
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
            Log.e("CreateCustomerSpecials", "CreateCustomerSpecials----------******************");
            try {
                // Add your data
                JSONArray jsonArray = new JSONArray();
                JSONObject json = new JSONObject();


                json.put("productCode", productcode.getText().toString());
                json.put("customerCode", custcodedialog.getText().toString());
                json.put("selingprice", edtsellingprice.getText().toString());
                json.put("fltcosts", txtcosts.getText().toString());
                json.put("eddatefrom", dtefrom.getText().toString());
                json.put("dteto", edtdtefromTo.getText().toString());
                json.put("subscriberId", subscriberId);
                json.put("userid", getPasswordByName());

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


                Log.e("response", "***" + responseBody);

                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String strDesc, strPartNumber, SpecialHeaderId, DateFrom, DateTo, Price, CustomerId, CustomerSpecial;

                    strDesc = BoardDetails.getString("strDesc");
                    strPartNumber = BoardDetails.getString("strPartNumber");
                    SpecialHeaderId = BoardDetails.getString("SpecialHeaderId");
                    DateFrom = BoardDetails.getString("DateFrom");
                    DateTo = BoardDetails.getString("DateTo");
                    Price = BoardDetails.getString("Price");
                    CustomerId = BoardDetails.getString("CustomerId");
                    CustomerSpecial = BoardDetails.getString("CustomerSpecial");

                    ContentValues cv = new ContentValues();
                    cv.put("strDesc", strDesc);
                    cv.put("strPartNumber", strPartNumber);
                    cv.put("SpecialHeaderId", Integer.parseInt(SpecialHeaderId));
                    cv.put("DateFrom", DateFrom);
                    cv.put("DateTo", DateTo);
                    cv.put("Price", Double.parseDouble(Price));
                    cv.put("CustomerId", CustomerId);
                    cv.put("Added", 0);
                    cv.put("CustomerSpecial", Integer.parseInt(CustomerSpecial));

                    db.insert("CustomerSpecialsLines", null, cv);
                    new Thread() {
                        public void run() {
                            CustomersActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                                    builder.setMessage(" Transaction Posted Successfully")
                                            .setCancelable(false)

                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    edtsellingprice.setText("");
                                                    txtcosts.setText("Costs");
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


    public String getPasswordByName() {

        String password = "";
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from LoggedIn limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                password = cursor2.getString(cursor2.getColumnIndex("UserID"));

            } while (cursor2.moveToNext());
        }

        return password;

    }


    public void startProgress(String msg) {
        progressDoalog = new ProgressDialog(CustomersActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...." + msg);
        progressDoalog.setTitle("Doing some work for you.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }

    public int CheckIfMerchie() {
        Cursor cursor2 = dbOrders.rawQuery("SELECT *  from BriefcaseRoles where RoleDescription ='Merchie'", null);
        return cursor2.getCount();
    }


}