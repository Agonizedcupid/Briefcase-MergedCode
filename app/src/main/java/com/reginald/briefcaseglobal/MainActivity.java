package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reginald.briefcaseglobal.Interface.CurrentLocation;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {


    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;
        String ItemString6;
        String ItemString7;



        Item(String t, String t2,String t3,String t4,String t5,String t6,String t7) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
            ItemString6 = t6;
            ItemString7 = t7;

        }
    }
    static class ViewHolder {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        TextView text6;
    }

    public class Item2 {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;
        String ItemString6;
        String ItemString7;



        Item2(String t, String t2,String t3,String t4,String t5,String t6,String t7) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
            ItemString6 = t6;
            ItemString7 = t7;

        }
    }
    static class ViewHolder2 {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        TextView text6;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.products, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.product_name);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.prodcode );
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.trend);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.uom);
                viewHolder.text5 = (TextView) rowView.findViewById(R.id.costs);

                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);
            holder.text4.setText(list.get(position).ItemString4);
            holder.text5.setText(list.get(position).ItemString5);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }

    }

    public class ItemsListAdapterOrderPattern extends BaseAdapter {

        private Context context;
        private List<Item> list;

        ItemsListAdapterOrderPattern(Context c, List<Item> l) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.orderpatternrows, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.product_name);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.prodcode );
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.trend);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.uom);
                viewHolder.text5 = (TextView) rowView.findViewById(R.id.costs);

                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString6);
            holder.text4.setText(list.get(position).ItemString4);
            holder.text5.setText(list.get(position).ItemString5);

            Log.e("ffff","************************"+list.get(position).ItemString6);
            if(list.get(position).ItemString6.equals("1")) {
                holder.text3.setBackgroundColor(Color.rgb(57, 255, 20));
                holder.text3.setText("GOOD");
            }
            if(list.get(position).ItemString6.equals("2")) {
                holder.text3.setBackgroundColor(Color.rgb(176, 224, 230));
                holder.text3.setText("DROPING");
            }
            if(list.get(position).ItemString6.equals("3")) {
                holder.text3.setBackgroundColor(Color.rgb(255,255,255));
                holder.text3.setText("MODERATE");
            }
            if(list.get(position).ItemString6.equals("4")) {
                holder.text3.setBackgroundColor(Color.rgb(255, 0, 0));
                holder.text3.setText("BAD");
            }

            return rowView;
        }

        public List<Item> getList() {
            return list;
        }

    }

    public class ItemsListAdapterOrderdetails extends BaseAdapter {

        private Context context;
        private List<Item2> list;

        ItemsListAdapterOrderdetails(Context c, List<Item2> l) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.orderdproducts, null);

                ViewHolder2 viewHolder = new ViewHolder2();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.ordered_product);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.textView7);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.qty_ordered);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.thisproductcode);
                /*viewHolder.text4 = (TextView) rowView.findViewById(R.id.linecomment);
                viewHolder.text5 = (TextView) rowView.findViewById(R.id.price);
                viewHolder.text6 = (TextView) rowView.findViewById(R.id.barcode);*/

              /*  cursor.getString(cursor.getColumnIndex("strDesc")),
                        cursor.getString(cursor.getColumnIndex("strPartNumber")),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Price"))),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Quantity"))),
                        Double.toString(cursor.getDouble(cursor.getColumnIndex("Vat")))
*/
                rowView.setTag(viewHolder);
            }

            ViewHolder2 holder = (ViewHolder2) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(""+list.get(position).ItemString3);
            holder.text3.setText(list.get(position).ItemString4);
            holder.text4.setText(list.get(position).ItemString2);


            return rowView;
        }

        public List<Item2> getList() {
            return list;
        }

    }

    public class ItemsListAdapterViewOrderdetails extends BaseAdapter {

        private Context context;
        private List<Item2> list;

        ItemsListAdapterViewOrderdetails(Context c, List<Item2> l) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                // rowView = inflater.inflate(R.layout.pick_customer_row, null);
                rowView = inflater.inflate(R.layout.vieworderlines_row, null);

                ViewHolder2 viewHolder = new ViewHolder2();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.textView16);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.textView18);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.textView19);

                /*viewHolder.text4 = (TextView) rowView.findViewById(R.id.linecomment);
                viewHolder.text5 = (TextView) rowView.findViewById(R.id.price);
                viewHolder.text6 = (TextView) rowView.findViewById(R.id.barcode);*/

              /*  cursor.getString(cursor.getColumnIndex("strDesc")),
                        cursor.getString(cursor.getColumnIndex("strPartNumber")),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Price"))),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Quantity"))),
                        Double.toString(cursor.getDouble(cursor.getColumnIndex("Vat")))
*/
                rowView.setTag(viewHolder);
            }

            ViewHolder2 holder = (ViewHolder2) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(""+list.get(position).ItemString2);
            holder.text3.setText(""+list.get(position).ItemString3);

            return rowView;
        }

        public List<Item2> getList() {
            return list;
        }

    }
    private boolean removeItemToList(List<Item> l, Item it){
        boolean result = l.remove(it);
        return result;
    }
    private boolean removeItemToListorderpatter(List<Item2> l, Item2 it){
        boolean result = l.remove(it);
        return result;
    }

    private boolean addItemToList(List<Item2> l, Item2 it){
        boolean result = l.add(it);
        return result;
    }

    private SQLiteDatabase db,dbOrders;
    List<Item> items1,ItemCustomerNotLoaded,itemsSyncProducts;
    List<Item2> items2,items3,itemsPartial;
    ItemsListAdapterOrderPattern myListAdapterOderPattern;
    ItemsListAdapter myItemsListAdapter,myListAdapterLoaded,myListAdapterSyncProduc;
    ItemsListAdapterOrderdetails myProductLists,myProductListsLoaded,myProductListsPartial;
    ItemsListAdapterViewOrderdetails myorderlines;
    String userID,roles,custcode,custName,neworder,notes,orderNo,date,IP,DimsIp;
    ListView ListProductsSelected,ListProducts,ListSellingPrices,lvproductlines,alternativepricelists;
    TextView textView24,textView25,dialog_product_name,status,textPackaging,TextMargin,textISQ,costs,
            textPackagingCurrentWeight,customerNameText,textTotalBasedProduct,textNoLines,todayDateText13,todayDateText12,
            textMarginBasedProduct,txtproductname,textView17prodcode,textPackagingLabel,textPackagingWeight,casecount;
    ProgressDialog progressDoalog;
    EditText editQTY,editZAR,editSearchProduct,qtytoupdate;
    Button buttonAddtocart,buttonClose,buttonUpload,buttonPattern,buttonProductsLists,buttonGetMargin,buttonSubmitProduct,btnupdateqty,dltitem,buttoncloseupdateorderline,viewitems,button;
    CheckBox checkBox;
    String subscriberId,addressid;
    String Result = "",OrderId="", LocationID= "1";
    ImageView imageview;
    URL url;
    AsyncTask mMyTask;
    TableLayout gridextras;
    double lats = 0,lons = 0;
    FusedLocationProviderClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        client = LocationServices.getFusedLocationProviderClient(this);

        ListProductsSelected = (ListView) findViewById(R.id.ListProductsSelected);
        ListProducts = (ListView) findViewById(R.id.ListProducts);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonPattern = (Button) findViewById(R.id.buttonPattern);
        viewitems = (Button) findViewById(R.id.viewitems);
        buttonProductsLists = (Button) findViewById(R.id.buttonProductsLists);
        buttonSubmitProduct = (Button) findViewById(R.id.buttonSubmitProduct);
        editSearchProduct = (EditText) findViewById(R.id.editSearchProduct);
        customerNameText = (TextView) findViewById(R.id.customerNameText);
        textTotalBasedProduct = (TextView) findViewById(R.id.textTotalBasedProduct);
        textNoLines = (TextView) findViewById(R.id.textNoLines);
        status = (TextView) findViewById(R.id.status);
        casecount = (TextView) findViewById(R.id.casecount);
        textMarginBasedProduct = (TextView) findViewById(R.id.textMarginBasedProduct);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        if(CheckIfMerchie()> 0){
            buttonPattern.setVisibility(View.INVISIBLE);
            buttonProductsLists.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
            status.setVisibility(View.INVISIBLE);
        }

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        try{
            AssignLocations();
        }catch(Exception e){
            Toast.makeText(MainActivity.this, "Location Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Loc Error","--------------------"+e.getMessage());
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        custcode = intent.getStringExtra("custcode");
        custName = intent.getStringExtra("custName");
        neworder = intent.getStringExtra("neworder");
        notes = intent.getStringExtra("notes");
        orderNo = intent.getStringExtra("orderNo");
        date = intent.getStringExtra("date");
        addressid = intent.getStringExtra("addressid");

        if(neworder.equals("neworder"))
        {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            subscriberId = ts+"-"+android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        }else{
            subscriberId = neworder;
        }


        customerNameText.setText(custName);
        getLoadedOrders(subscriberId);
        casecount.setText("Case Count "+ casecount(subscriberId));
        returnProducts("");
        Log.e("addressid:main","**************************"+addressid);

        Cursor cursor2 = dbOrders.rawQuery("SELECT * from tblSettings limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                IP =cursor2.getString(cursor2.getColumnIndex("IP"));
                DimsIp =cursor2.getString(cursor2.getColumnIndex("DimsIp"));

            } while (cursor2.moveToNext());
        }
        buttonPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("latLon","***LL************"+lats+","+lons);
                status.setText("Favourites");
                returnOrderPattern(editSearchProduct.getText().toString());
            }
        });
        viewitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogView = new Dialog(MainActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.vieworderdetails);


                lvproductlines = (ListView) dialogView.findViewById(R.id.lvproductlines);
                button = (Button) dialogView.findViewById(R.id.button);
                textView25 = (TextView) dialogView.findViewById(R.id.textView25);
                textView24 = (TextView) dialogView.findViewById(R.id.textView24); //Margin

                textView24.setText(returnOrderMagin(subscriberId));
                textView25.setText(returnTotals(subscriberId));
                getOrderLines( subscriberId);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogView.dismiss();
                    }
                });

                dialogView.show();


            }
        });
        buttonProductsLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("All Products");
                returnProducts(editSearchProduct.getText().toString());
            }
        });
        buttonSubmitProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                returnProducts(editSearchProduct.getText().toString());//
            }
        });
        editSearchProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("Touch","********** here");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                editSearchProduct.setImeOptions(EditorInfo.IME_ACTION_DONE);
                editSearchProduct.setInputType(InputType.TYPE_CLASS_TEXT);
                return false;
            }
        });


        ListProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item selectedItem2 = (Item)(adapterView.getItemAtPosition(i));


                Dialog dialogView = new Dialog(MainActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.screen1);


                final AppApplication globalVariable = (AppApplication) getApplicationContext();
                dialog_product_name = (TextView) dialogView.findViewById(R.id.dialog_product_name);
                editZAR = (EditText) dialogView.findViewById(R.id.editZAR);
                editQTY = (EditText) dialogView.findViewById(R.id.editQTY);
                textPackaging = (TextView) dialogView.findViewById(R.id.textPackaging);
                textPackagingCurrentWeight = (TextView) dialogView.findViewById(R.id.textPackagingCurrentWeight);
                TextMargin = (TextView) dialogView.findViewById(R.id.TextMargin);
                textISQ = (TextView) dialogView.findViewById(R.id.textISQ);
                todayDateText12 = (TextView) dialogView.findViewById(R.id.todayDateText12);
                todayDateText13 = (TextView) dialogView.findViewById(R.id.todayDateText12);
                imageview = (ImageView)findViewById(R.id.imageViewPicture2);
                textPackagingLabel = (TextView) dialogView.findViewById(R.id.textPackagingLabel);
                textPackagingWeight = (TextView) dialogView.findViewById(R.id.textPackagingWeight);
                alternativepricelists = (ListView) dialogView.findViewById(R.id.alternativepricelists);

   /*             if(globalVariable.getImages().length()>8){
                   // new DownloadTask().execute(stringToURL(globalVariable.getImages()+selectedItem2.ItemString2+".png"));
                }
*/
                //new DownloadImageFromInternet((ImageView) findViewById(R.id.imageViewPicture2)).execute(globalVariable.getImages()+selectedItem2.ItemString2+".png");
                //Bitmap bmp = BitmapFactory.decodeFile(new URL(globalVariable.getImages()+selectedItem2.ItemString2+".png").openStream());
                //imageview.setImageBitmap(bmp);

                Log.e("PNG","*********************"+globalVariable.getImages()+selectedItem2.ItemString2+".png");
                Log.e("selectedItem2.ItemString7 ","*********************"+selectedItem2.ItemString7 );

                editQTY.setText("1");
                textISQ.setText(selectedItem2.ItemString4.toString());


                buttonAddtocart = (Button) dialogView.findViewById(R.id.buttonAddtocart);
                buttonClose = (Button) dialogView.findViewById(R.id.buttonClose);
                buttonGetMargin = (Button) dialogView.findViewById(R.id.buttonGetMargin);
                ListSellingPrices = (ListView) dialogView.findViewById(R.id.ListSellingPrices);
                editZAR.setText(""+productPriceLookUp(selectedItem2.ItemString2.toString(),custcode));
                dialog_product_name.setText(selectedItem2.ItemString.toString()+"\n ["+selectedItem2.ItemString2+"]");
                textPackagingCurrentWeight.setText(selectedItem2.ItemString5.toString());
                textPackaging.setText(selectedItem2.ItemString7 );

                if(CheckIfMerchie()> 0) {

                    textPackagingWeight.setVisibility(View.INVISIBLE);
                    textPackagingLabel.setVisibility(View.INVISIBLE);
                    buttonGetMargin.setVisibility(View.INVISIBLE);
                    textPackaging.setVisibility(View.INVISIBLE);
                    textPackagingCurrentWeight.setVisibility(View.INVISIBLE);
                    textISQ.setVisibility(View.INVISIBLE);
                    TextMargin.setVisibility(View.INVISIBLE);
                    todayDateText13.setVisibility(View.INVISIBLE);
                    todayDateText12.setVisibility(View.INVISIBLE);
                    ListSellingPrices.setVisibility(View.INVISIBLE);

                }


                returnSellingPrices(selectedItem2.ItemString2.toString(),custcode);
                productPriceList(selectedItem2.ItemString2.toString());

                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogView.dismiss();
                    }
                });
                buttonGetMargin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextMargin.setText(""+String.format("%.2f", marginCalculator(Double.parseDouble(textPackagingCurrentWeight.getText().toString()) ,Double.parseDouble(editZAR.getText().toString()))) +"%");
                    }
                });
                buttonAddtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (TextUtils.isEmpty(editZAR.getText().toString().trim())){
                            Toast.makeText(MainActivity.this, "No Pricing", Toast.LENGTH_LONG).show();
                            editZAR.requestFocus();
                        }else{
                            Log.e("zero","*************************"+Double.parseDouble(editZAR.getText().toString()));
                            if(Double.parseDouble(editZAR.getText().toString()) ==0){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Are you sure you want to add this item to the cart with 0 pricing!")
                                        .setCancelable(false)
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if(status.getText().toString().equals("All Products"))
                                                {
                                                    ItemsListAdapter associatedAdapter = (ItemsListAdapter)(adapterView.getAdapter());
                                                    List<Item> associatedList = associatedAdapter.getList();
                                                    Item associatedItem = associatedList.get(i);
                                                    if(removeItemToList(associatedList, associatedItem)) {
                                                        insertData(subscriberId,dialog_product_name.getText().toString(),selectedItem2.ItemString2.toString(),editQTY.getText().toString(),Double.parseDouble(editZAR.getText().toString()),0,Double.parseDouble(selectedItem2.ItemString3.toString()));
                                                        view.invalidate();
                                                        associatedAdapter.notifyDataSetChanged();
                                                    }
                                                }else
                                                {
                                                    try {
                                                            ItemsListAdapterOrderPattern associatedAdapter = (ItemsListAdapterOrderPattern)(adapterView.getAdapter());
                                                            List<Item> associatedList = associatedAdapter.getList();
                                                            Item associatedItem = associatedList.get(i);
                                                            if(removeItemToList(associatedList, associatedItem)) {
                                                                insertData(subscriberId,dialog_product_name.getText().toString(),selectedItem2.ItemString2.toString(),editQTY.getText().toString(),Double.parseDouble(editZAR.getText().toString()),0,Double.parseDouble(selectedItem2.ItemString3.toString()));
                                                                view.invalidate();
                                                                associatedAdapter.notifyDataSetChanged();
                                                            }
                                                    }catch (Exception e) {
                                                            // TODO: handle exception
                                                        Log.e("error","*************************"+e.getMessage().toString());
                                                    }
                                                }
                                                try {
                                                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                                } catch (Exception e) {
                                                    // TODO: handle exception
                                                }
                                                dialogView.dismiss();
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
                            }else{
                                Log.e("status","*************************________________"+status.getText().toString());
                                if(status.getText().toString().equals("All Products"))
                                {
                                    ItemsListAdapter associatedAdapter = (ItemsListAdapter)(adapterView.getAdapter());
                                    List<Item> associatedList = associatedAdapter.getList();
                                    Item associatedItem = associatedList.get(i);
                                    if(removeItemToList(associatedList, associatedItem)) {
                                        insertData(subscriberId,dialog_product_name.getText().toString(),selectedItem2.ItemString2.toString(),editQTY.getText().toString(),Double.parseDouble(editZAR.getText().toString()),0,Double.parseDouble(selectedItem2.ItemString3.toString()));
                                        view.invalidate();
                                        associatedAdapter.notifyDataSetChanged();
                                    }
                                }else
                                {
                                    try {
                                            ItemsListAdapterOrderPattern associatedAdapter = (ItemsListAdapterOrderPattern)(adapterView.getAdapter());
                                            List<Item> associatedList = associatedAdapter.getList();
                                            Item associatedItem = associatedList.get(i);
                                            if(removeItemToList(associatedList, associatedItem)) {
                                                insertData(subscriberId,dialog_product_name.getText().toString(),selectedItem2.ItemString2.toString(),editQTY.getText().toString(),Double.parseDouble(editZAR.getText().toString()),0,Double.parseDouble(selectedItem2.ItemString3.toString()));
                                                view.invalidate();
                                                associatedAdapter.notifyDataSetChanged();
                                            }
                                    } catch (Exception e) {
                                        // TODO: handle exception

                                        Log.e("orderpattererror","+++++++++++++++++++++++++++++++"+e.getMessage().toString());
                                    }
                                }
                                try {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                                dialogView.dismiss();
                            }


                        }


                    }
                });


                dialogView.show();
                return false;
            }
        });

        ListProductsSelected.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Item2 selectedItem2 = (Item2)(adapterView.getItemAtPosition(i));
                Dialog dialogView = new Dialog(MainActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.update_orderline);


                txtproductname = (TextView) dialogView.findViewById(R.id.txtproductname);
                qtytoupdate = (EditText) dialogView.findViewById(R.id.qtytoupdate);
                textView17prodcode = (TextView) dialogView.findViewById(R.id.textView17);


                btnupdateqty = (Button) dialogView.findViewById(R.id.btnupdateqty);
                dltitem = (Button) dialogView.findViewById(R.id.dltitem);
                buttoncloseupdateorderline = (Button) dialogView.findViewById(R.id.buttoncloseupdateorderline);


                qtytoupdate.setText(selectedItem2.ItemString4.toString());
                txtproductname.setText(selectedItem2.ItemString.toString());
                textView17prodcode.setText(selectedItem2.ItemString2.toString());

                buttoncloseupdateorderline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialogView.dismiss();
                    }
                });
                btnupdateqty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ContentValues cv = new ContentValues();
                        cv.put("Quantity", qtytoupdate.getText().toString());
                        dbOrders.update("OrderLines", cv, "strPartNumber = ? and ID=?", new String[]{textView17prodcode.getText().toString(),subscriberId});
                        dialogView.dismiss();
                        getLoadedOrders(subscriberId);

                        myProductLists = new ItemsListAdapterOrderdetails(MainActivity.this, items2);
                        ListProductsSelected.setAdapter(myProductLists);
                        myProductLists.notifyDataSetChanged();
                        footers();
                    }
                });
                dltitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbOrders.delete("OrderLines", "strPartNumber" + "='" + textView17prodcode.getText().toString()+"' AND ID='"+subscriberId+"'", null);
                        dialogView.dismiss();
                        myProductLists = new ItemsListAdapterOrderdetails(MainActivity.this, items2);
                        ListProductsSelected.setAdapter(myProductLists);
                        myProductLists.notifyDataSetChanged();
                        footers();

                    }
                });
                dialogView.show();

                return false;
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasbeenUploaded(subscriberId) < 1) {

                    if (checkConnection()) {
                        insertDataHeader(subscriberId, date, neworder, custcode, custName, notes, userID, checkBox.isChecked());
                    /*Intent p = new Intent(MainActivity.this,CustomersActivity.class);
                    userID = intent.getStringExtra("userID");
                    roles = intent.getStringExtra("roles");
                    p.putExtra("userID",userID);
                    p.putExtra("roles","");
                    startActivity(p);*/
                        startProgress("Saving");
                        new UploadNewOrderLinesDetails(subscriberId).execute();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("You do not have data connectivity")
                                .setCancelable(false)
                                .setPositiveButton("Save, I will Retry Later", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        insertDataHeader(subscriberId, date, neworder, custcode, custName, notes, userID, checkBox.isChecked());
                                        Intent i = new Intent(MainActivity.this,HomeScreen.class);
                                        i.putExtra("userID",getPasswordByName());
                                        i.putExtra("roles","");
                                        startActivity(i);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startProgress("Saving");
                                        new UploadNewOrderLinesDetails(subscriberId).execute();
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{

                    Intent i = new Intent(MainActivity.this,HomeScreen.class);
                    i.putExtra("userID",getPasswordByName());
                    i.putExtra("roles","");
                    startActivity(i);
                }
            }
        });


    }
    private void productPriceList(String search){
        items2 = new ArrayList<Item2>();

        Log.e("search","**************search A"+search);
        Cursor c = db.rawQuery("select distinct CustomerPriceList,Price from CustomerPriceLists  WHERE [strPartNumber] = '" + search + "'   order by [CustomerPriceList] limit 100", null);
      //  int nameIndex = c.getColumnIndex("Price");
      //  int nameIndex2 = c.getColumnIndex("CustomerPriceList");
        if (c.moveToFirst()) {
            do {

                Item2 listItem = new Item2( c.getString(c.getColumnIndex("CustomerPriceList")),""+c.getString(c.getColumnIndex("Price")),
                        "", "", "",
                        "","");
                items2.add(listItem);

            } while (c.moveToNext());
        }
       // myorderlines
        myorderlines = new ItemsListAdapterViewOrderdetails(MainActivity.this, items2);
        alternativepricelists.setAdapter(myorderlines);


    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        protected void onPreExecute(){

        }
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;
            try{
                //Log.e("")
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            Log.e("result","++++++++++++++++"+result);

            if(result!=null){
                imageview.setImageBitmap(result);
            } else {
                // Notify user that an error occurred while downloading image
               // Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected URL stringToURL(String urln) {
        try {
            Log.e("urln","*****************"+urln);
            url = new URL(urln);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("Range")
    public void getLoadedOrders(String subscriberId){
        items2 = new ArrayList<Item2>();
        Cursor cursor = dbOrders.rawQuery("SELECT * FROM OrderLines  WHERE [ID] ='" + subscriberId + "'  order by [OrderLinesAutoId]", null);

        if (cursor.moveToFirst()) {
            do {

                Log.e("Desc","*********************************"+ cursor.getString(cursor.getColumnIndex("strDesc")));
                Item2 listItem = new Item2(
                        cursor.getString(cursor.getColumnIndex("strDesc")),
                        cursor.getString(cursor.getColumnIndex("strPartNumber")),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Price"))),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Quantity"))),
                        Double.toString(cursor.getDouble(cursor.getColumnIndex("Vat")))
                        ,"","");
                items2.add(listItem);

            } while (cursor.moveToNext());
        }
        cursor.close();
        myProductLists = new ItemsListAdapterOrderdetails(MainActivity.this, items2);
        ListProductsSelected.setAdapter(myProductLists);
        myProductLists.notifyDataSetChanged();
       // textTotalBasedProduct.setText(returnTotals(subscriberId)) ;
        footers();

    }
    public void getOrderLines(String subscriberId){
        items2 = new ArrayList<Item2>();
        Cursor cursor = dbOrders.rawQuery("SELECT * FROM OrderLines  WHERE [ID] ='" + subscriberId + "'  order by [strDesc]", null);

        if (cursor.moveToFirst()) {
            do {

                Log.e("Desc","*********************************"+ cursor.getString(cursor.getColumnIndex("strDesc")));
                Item2 listItem = new Item2(
                        cursor.getString(cursor.getColumnIndex("strDesc")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        " "+Double.toString(  cursor.getDouble(cursor.getColumnIndex("Price"))),
                        Double.toString(  cursor.getDouble(cursor.getColumnIndex("Quantity"))),
                        Double.toString(cursor.getDouble(cursor.getColumnIndex("Vat")))
                        ,"","");
                items2.add(listItem);

            } while (cursor.moveToNext());
        }
        cursor.close();
        myorderlines = new ItemsListAdapterViewOrderdetails(MainActivity.this, items2);
        lvproductlines.setAdapter(myorderlines);
        myorderlines.notifyDataSetChanged();
       // textTotalBasedProduct.setText(returnTotals(subscriberId)) ;
        footers();

    }
    public void returnProducts(String prodName)
    {
        startProgress("Please Wait!");
        items1 = new ArrayList<Item>();
        Cursor cursor = db.rawQuery("SELECT * FROM Products  WHERE strDesc like '%" + prodName + "%'  order by [strDesc] limit 100", null);

        if (cursor.moveToFirst()) {
            do {

                Item listItem = new Item( cursor.getString(cursor.getColumnIndex("strDesc")),cursor.getString(cursor.getColumnIndex("strPartNumber")),
                        cursor.getString(cursor.getColumnIndex("Tax")), cursor.getString(cursor.getColumnIndex("intAvailable")), cursor.getString(cursor.getColumnIndex("Cost")),"",
                        cursor.getString(cursor.getColumnIndex("strBulkUnit")) );
                items1.add(listItem);

            } while (cursor.moveToNext());
        }
        progressDoalog.dismiss();
        myItemsListAdapter = new ItemsListAdapter(MainActivity.this, items1);
        ListProducts.setAdapter(myItemsListAdapter);
        myProductLists.notifyDataSetChanged();
        //textTotalBasedProduct.setText(returnTotals(subscriberId)) ;
        footers();
    }
    public void returnOrderPattern(String prodName)
    {
        startProgress("Please Wait!");
        items1 = new ArrayList<Item>();
        Cursor cursor = db.rawQuery("SELECT PastelDescription,PastelCode,Tax,intAvailable,Cost,TrendingId,strBulkUnit FROM OrderPattern " +
                "inner join Products on Products.strPartnumber = OrderPattern.PastelCode  WHERE PastelDescription like '%" + prodName + "%' and CustomerCode='"+custcode+"'   order by [PastelDescription] ", null);

        if (cursor.moveToFirst()) {
            do {

                Item listItem = new Item( cursor.getString(cursor.getColumnIndex("PastelDescription")),cursor.getString(cursor.getColumnIndex("PastelCode")),
                        cursor.getString(cursor.getColumnIndex("Tax")), cursor.getString(cursor.getColumnIndex("intAvailable")), cursor.getString(cursor.getColumnIndex("Cost")),
                        cursor.getString(cursor.getColumnIndex("TrendingId")),cursor.getString(cursor.getColumnIndex("strBulkUnit")));
                items1.add(listItem);

            } while (cursor.moveToNext());
        }
        progressDoalog.dismiss();
        myListAdapterOderPattern = new ItemsListAdapterOrderPattern(MainActivity.this, items1);
        ListProducts.setAdapter(myListAdapterOderPattern);
       // myListAdapterOderPattern.notifyDataSetChanged();
        footers();
    }

    public void returnSellingPrices(String ProdCode,String CustomerCode)
    {
        //globalVariable
        String customerPriceList = returnCustomerPriceList( CustomerCode);
        items1 = new ArrayList<Item>();
        Cursor cursor = db.rawQuery("SELECT * from CustomerSpecialsLines where strPartNumber ='"+ProdCode+"' and CustomerId ='"+CustomerCode+"' and DateTo>='"+date+"'", null);
        Cursor cursor2 = db.rawQuery("SELECT * from GroupSpecialsLines where strPartNumber ='"+ProdCode+"' and GroupId ='"+returnCustomerGroupId(CustomerCode)+"' and DateTo>='"+date+"'", null);
        Cursor cursor3 = db.rawQuery("SELECT * from CustomerPriceLists where strPartNumber ='"+ProdCode+"' and CustomerPriceList ='"+customerPriceList+"'", null);
        Cursor cursor4 = db.rawQuery("SELECT * from OverallSpecials where strPartNumber ='"+ProdCode+"' and DateTo >='"+date+"'", null);

        if (cursor.moveToFirst()) {
            do {

                Item listItem = new Item( "Customer Loaded Price",""+cursor.getDouble(cursor.getColumnIndex("Price")),
                        "", "", "",
                       "","");
                items1.add(listItem);

            } while (cursor.moveToNext());
        }

        if (cursor2.moveToFirst()) {
            do {

                Item listItem = new Item( "Group Loaded Price",""+cursor2.getDouble(cursor2.getColumnIndex("Price")),
                        "", "", "",
                        "","");
                items1.add(listItem);

            } while (cursor2.moveToNext());
        }
        if (cursor3.moveToFirst()) {
            do {

                Item listItem = new Item( "List Price",""+cursor3.getDouble(cursor3.getColumnIndex("Price")),
                        "", "", "",
                        "","");
                items1.add(listItem);

            } while (cursor3.moveToNext());
        }
        if (cursor4.moveToFirst()) {
            do {

                Item listItem = new Item( "Overall Price",""+cursor4.getDouble(cursor4.getColumnIndex("Price")),
                        "", "", "",
                        "","");
                items1.add(listItem);

            } while (cursor4.moveToNext());
        }

        myListAdapterOderPattern = new ItemsListAdapterOrderPattern(MainActivity.this, items1);
        ListSellingPrices.setAdapter(myListAdapterOderPattern);
       // myListAdapterOderPattern.notifyDataSetChanged();
        footers();
    }
    public double productPriceLookUp(String ProdCode,String CustomerCode)
    {

        double priceReturned  = 0;
        double pricePriceList  = -1;
        double priceGroupSpecials  = -1;
        double priceCustomerSpecials  = -1;
        double overall  = -1;
        final AppApplication globalVariable = (AppApplication) getApplicationContext();
        LocationID = globalVariable.getLocation();
        Log.e("LocationID","************************"+LocationID);
        List<Double> list = new ArrayList<Double>();

        String customerPriceList = returnCustomerPriceList( CustomerCode);
        Cursor cursor = db.rawQuery("SELECT * from CustomerSpecialsLines where strPartNumber ='"+ProdCode+"' and CustomerId ='"+CustomerCode+"' and DateTo>='"+date+"'", null);
        Cursor cursor2 = db.rawQuery("SELECT * from GroupSpecialsLines where strPartNumber ='"+ProdCode+"' and GroupId ='"+returnCustomerGroupId(CustomerCode)+"' and DateTo>='"+date+"'", null);
        Cursor cursor3 = db.rawQuery("SELECT * from CustomerPriceLists where strPartNumber ='"+ProdCode+"' and CustomerPriceList ='"+customerPriceList+"'", null);
        Cursor cursor4 = db.rawQuery("SELECT * from OverallSpecials where strPartNumber ='"+ProdCode+"' and DateTo >='"+date+"' and intLocationID ='"+LocationID+"'", null);
        Log.e("PRICE","---------------------"+cursor.getCount());
        if(cursor.getCount() > 0)
        {
            if (cursor.moveToFirst()) {
                do {
                     priceCustomerSpecials=cursor.getDouble(cursor.getColumnIndex("Price"));

                } while (cursor.moveToNext());
            }


        }

        Log.e("groupspecials","---------------------"+cursor2.getCount());
        Log.e("groupspecials"," String ---------------------"+"SELECT * from GroupSpecialsLines where strPartNumber ='"+ProdCode+"' and GroupId ='"+returnCustomerGroupId(CustomerCode)+"' and DateTo>='"+date+"'");
        if(cursor2.getCount() > 0)
        {
            if (cursor2.moveToFirst()) {
                do {
                     priceGroupSpecials =cursor2.getDouble(cursor2.getColumnIndex("Price"));

                } while (cursor2.moveToNext());
            }
        }
        Log.e("CustomerPriceLists","---------------------"+cursor3.getCount());
        if(cursor3.getCount() > 0)
        {
            if (cursor3.moveToFirst()) {
                do {
                    pricePriceList =cursor3.getDouble(cursor3.getColumnIndex("Price"));

                } while (cursor3.moveToNext());
            }
        }
        if(cursor4.getCount() > 0)
        {
            if (cursor4.moveToFirst()) {
                do {
                    overall =cursor4.getDouble(cursor4.getColumnIndex("Price"));

                } while (cursor4.moveToNext());
            }
        }

        if(pricePriceList !=-1){
            list.add(pricePriceList);
        }
        if(priceGroupSpecials !=-1){
            list.add(priceGroupSpecials);
        }
        if(priceCustomerSpecials !=-1){
            list.add(priceCustomerSpecials);
        }
        if(overall !=-1){
            list.add(overall);
        }

        if(list.isEmpty()){
            priceReturned = 0;
        }else{
        priceReturned= Collections.min(list);
            Log.e("collection","************"+Collections.min(list));
        }

        Log.e("pricePriceList","************"+pricePriceList);
        Log.e("priceGroupSpecials","************"+priceGroupSpecials);
        Log.e("priceCustomerSpecials","************"+priceCustomerSpecials);
        Log.e("overall","************"+overall);


        return priceReturned;
    }
    public int hasbeenUploaded(String ID){
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from OrderHeaders where ID ='"+ID+"' limit 1", null);
        return cursor2.getCount();
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
    public String returnTotals(String ID)
    {
        String totals = "";
        Cursor cursor2 = dbOrders.rawQuery("SELECT sum(Quantity * Price) ex,sum(Quantity * Price*(100+Vat)/100) inc from OrderLines where ID ='"+ID+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
               String ex = String.format("%.2f", cursor2.getDouble(cursor2.getColumnIndex("ex")));
               String inc =String.format("%.2f", cursor2.getDouble(cursor2.getColumnIndex("inc")));

               if(CheckIfMerchie() > 0){
                   totals = "Total "+"["+inc+"]";
               }else{
                   totals ="Exc"+"["+ex+"]"+"Inc "+"["+inc+"]";
               }


            } while (cursor2.moveToNext());
        }

        return totals;
    }
    public void footers()
    {
        textNoLines.setText(NoOfLines(subscriberId));
        casecount.setText("Case Count "+casecount(subscriberId) );
        if(CheckIfMerchie() > 0){
            textMarginBasedProduct.setText("");
        }else{
            textMarginBasedProduct.setText(returnOrderMagin(subscriberId)+"%");
        }

        textTotalBasedProduct.setText(returnTotals(subscriberId));
    }
    public String returnOrderMagin(String ID)
    {
        String margin = "";
        Cursor cursor2 = dbOrders.rawQuery("SELECT sum(Quantity * Price) price,sum(Quantity * Cost) cost from OrderLines where ID ='"+ID+"' ", null);

        if (cursor2.moveToFirst()) {
            do {
                //marginCalculator(cursor2.getDouble(cursor2.getColumnIndex("cost")),cursor2.getDouble(cursor2.getColumnIndex("price")));
                margin =""+String.format("%.2f", marginCalculator(cursor2.getDouble(cursor2.getColumnIndex("cost")),cursor2.getDouble(cursor2.getColumnIndex("price"))));

            } while (cursor2.moveToNext());
        }

        return margin;
    }
    public double marginCalculator(double cost,double onCellVal)
    {
        return (1-(cost/onCellVal))*100;
    }
    public String NoOfLines(String ID){
        String lines = "";
        Cursor cursor2 = dbOrders.rawQuery("SELECT *  from OrderLines where ID ='"+ID+"'", null);
        lines = "No.Lines "+"["+cursor2.getCount()+"]";
        return lines;
    }
    public String returnCustomerName(String CustomerCode)
    {
        String priceListnameReturned = "";
        Cursor cursor2 = db.rawQuery("SELECT * from Customers where CustomerCode ='"+CustomerCode+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                priceListnameReturned =cursor2.getString(cursor2.getColumnIndex("CustomerStoreName"));

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
    public double returnItemCost(String productCode)
    {
        double costs =0;
        Cursor cursor2 = db.rawQuery("SELECT Cost from Products where strPartNumber ='"+productCode+"' limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                costs =cursor2.getDouble(cursor2.getColumnIndex("Cost"));

            } while (cursor2.moveToNext());
        }

        return costs;
    }
    @SuppressLint("Range")
    public int casecount(String ID)
    {
        int caseC =0;
        Cursor cursor2 = dbOrders.rawQuery("SELECT sum(Quantity) Quantity from OrderLines where ID ='"+ID+"' ", null);

        if (cursor2.moveToFirst()) {
            do {
                caseC =cursor2.getInt(cursor2.getColumnIndex("Quantity"));

            } while (cursor2.moveToNext());
        }

        return caseC;
    }
    public void insertData(String Id,String strDesc,String prodCdde,String Qty,double Price,int auth,double vat){

        Cursor cursor2 = dbOrders.rawQuery("SELECT * from OrderLines where strPartNumber ='"+prodCdde+"' and ID='"+Id+"' limit 1", null);
        if(cursor2.getCount() > 0){
            ContentValues cv = new ContentValues();
            //  cv.put("Loaded","1"); //These Fields should be your String values of actual column names
            cv.put("Quantity",Double.parseDouble(Qty));
            cv.put("Price", Price);
            dbOrders.update("OrderLines", cv, "strPartNumber = ? and ID=?", new String[]{prodCdde,subscriberId});
        }else
        {
            ContentValues cv = new ContentValues();
            cv.put("ID", Id);
            cv.put("strDesc", strDesc);
            cv.put("strPartNumber", prodCdde);
            cv.put("Quantity", Double.parseDouble(Qty));
            cv.put("Price",Price);
            cv.put("Authorised", auth);
            cv.put("Vat", vat);
            cv.put("Uploaded", 0);
            cv.put("CustomerCode", custcode);
            cv.put("StoreName", custName);
            cv.put("LineDeliveryDate", date);
            cv.put("Cost", returnItemCost(prodCdde));

            dbOrders.insert("OrderLines", null, cv);
        }



        getLoadedOrders(Id);

        myProductLists = new ItemsListAdapterOrderdetails(MainActivity.this, items2);
        ListProductsSelected.setAdapter(myProductLists);
        myProductLists.notifyDataSetChanged();
        footers();
        //textTotalBasedProduct.setText(returnTotals(subscriberId)) ;
    }
    public void insertDataHeader(String Id,String DeliveryDate,String OrderNumber,String CustomerCode,String CustomerDesc,String Notes,String UserID,boolean isquotation){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        Log.e("head**","tm*-----------------------------------------------------"+tm);
        ContentValues cv = new ContentValues();
        cv.put("ID", Id);
        cv.put("DeliveryDate", DeliveryDate);
        cv.put("OrderDate", tm);
        cv.put("OrderNumber", orderNo);
        cv.put("CustomerCode", CustomerCode);
        cv.put("StoreName", returnCustomerName(CustomerCode));
        cv.put("CustomerDesc",CustomerDesc);
        cv.put("Notes", notes);
        cv.put("UserID", UserID);

        cv.put("DeliveryAddressID",addressid);

        cv.put("isQuotation", isquotation);
        cv.put("DimsOrderId", "");
        cv.put("Complete", 1);
        cv.put("Uploaded", 0);
        cv.put("Coordinates", ""+lats+","+lons);

        Log.e("UserID**","UserID*-----------------------------------------------------"+UserID);
        dbOrders.insert("OrderHeaders", null, cv);
        getLoadedOrders(Id);

        myProductLists = new ItemsListAdapterOrderdetails(MainActivity.this, items2);
        ListProductsSelected.setAdapter(myProductLists);
        myProductLists.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            //DELETE BEFORE GOING BACK

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(" This transaction will not be saved")
                    .setCancelable(false)

                    .setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dbOrders.rawQuery("delete from OrderLines where ID='"+subscriberId+"'",null);
                            Intent i = new Intent(MainActivity.this,HomeScreen.class);
                            i.putExtra("userID",userID);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }).setNeutralButton("Save Draft",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(MainActivity.this,HomeScreen.class);
                            i.putExtra("userID",userID);
                            startActivity(i);

                            dialog.dismiss();
                        }
                    }).setNegativeButton("DO NOT DISCARD", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
        return super.onKeyDown(keyCode, event);
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
            Log.e("PostOrderLines","PostOrderLines----------******************"+IP + "PostLinesNew.php");
            try {
                // Add your data

                Cursor cursorh = dbOrders.rawQuery("SELECT * from OrderHeaders where Uploaded=0 and ID='"+id+"' limit 1", null);
                String header = "";

                if (cursorh.moveToFirst()) {
                    do {
                        header += "<Headers><ID>"+cursorh.getString(cursorh.getColumnIndex("ID"))+"</ID><CustomerCode>"+cursorh.getString(cursorh.getColumnIndex("CustomerCode"))+"</CustomerCode><DeliveryDate>"+cursorh.getString(cursorh.getColumnIndex("DeliveryDate"))+"</DeliveryDate><UserID>"+getPasswordByName()+"</UserID><Quote>"+cursorh.getInt(cursorh.getColumnIndex("isQuotation"))+"</Quote><DeliveryAddressID>"+cursorh.getInt(cursorh.getColumnIndex("DeliveryAddressID"))+"</DeliveryAddressID><Onum>"+cursorh.getString(cursorh.getColumnIndex("OrderNumber"))+"</Onum><notes>"+cursorh.getString(cursorh.getColumnIndex("Notes"))+"</notes><Coordinates>"+cursorh.getString(cursorh.getColumnIndex("Coordinates"))+"</Coordinates>";

                    } while (cursorh.moveToNext());
                }

                Cursor cursor2 = dbOrders.rawQuery("SELECT * from OrderLines where Uploaded = 0 and ID='"+id+"'", null);
                JSONArray jsonArray = new JSONArray();
                String xml = "";

                if (cursor2.moveToFirst()) {
                    do {
                        xml += XmlTemplate().toString()
                                .replace("unik",cursor2.getString(cursor2.getColumnIndex("strPartNumber")))
                                .replace("moola",""+ cursor2.getString(cursor2.getColumnIndex("Price")))
                                .replace("palo",""+cursor2.getString(cursor2.getColumnIndex("Quantity")));
                    } while (cursor2.moveToNext());
                }

                String footer = "</Headers>";
                jsonArray.put( header+xml+footer);

                JSONObject finalInfo = new JSONObject();
                finalInfo.put("jsonobject", jsonArray);


                Log.e("JSONBef", finalInfo.toString()); // before pos
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", finalInfo.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                responseBody =  responseBody.replaceAll("\"", "");
                Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);   //The response
                //Log.e("OrderLinesAutoId", "UPDATE  OrderLines SET Uploaded = 1 where OrderLinesAutoId in( " + responseBody + ")");
                //JSONArray BoardInfo = new JSONArray(responseBody);

                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {

                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    String ID, newdimsIp,protocols,connstring ;

                    OrderId = BoardDetails.getString("OrderId").toString();
                    Result = BoardDetails.getString("Result");
                    ID = BoardDetails.getString("ID");
                    connstring = BoardDetails.getString("connstring");
                    connstring = connstring.replace("-","/");
                    protocols = BoardDetails.getString("protocols");
                    newdimsIp = BoardDetails.getString("dimsIp");
                    Log.e("newdimsIp","*******************************"+newdimsIp);

                    if(Result.equals("SUCCESS"))
                    {
                        dbOrders.execSQL("UPDATE  OrderLines SET Uploaded = 1 where ID in(' " + ID + "')");
                        dbOrders.execSQL("UPDATE  OrderHeaders SET Uploaded = 1 where ID in( '" + ID + "')");

                        ContentValues cv = new ContentValues();
                        cv.put("OrderID", OrderId);
                        dbOrders.update("OrderHeaders", cv, "ID=?", new String[]{subscriberId});

                        Log.e("newdimsIp","*******************************"+protocols+"//"+ newdimsIp+":"+connstring);

                     // ContentValues cv2 = new ContentValues();
                     // cv.put("DimsIp",protocols+"//"+ newdimsIp+":"+connstring);
                      //dbOrders.update("tblSettings", cv2, "id>?", new String[]{"0"});

                        dbOrders.execSQL("UPDATE  tblSettings SET DimsIp =' "+protocols+"//"+ newdimsIp+":"+connstring+"' where id > 0");
                    }
                }

                if(Result.equals("SUCCESS"))
                {
                    //db.execSQL("Update StockTakeLines set blnScanned=0  where blnScanned = 1 ");
                    //    db.execSQL("Delete from  StockTakeLines ");
                    progressDoalog.dismiss();

                    new Thread()
                    {
                        public void run()
                        {
                            MainActivity.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    //Do your UI operations like dialog opening or Toast here

                                    final  AlertDialog.Builder dialogll = new  AlertDialog.Builder(MainActivity.this);
                                    dialogll.setTitle("POST")
                                            .setMessage("Your Records Posted Successfully.")
                                            .setNegativeButton("Done Done", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                    //db.execSQL("delete from StockTakeLines");
                                                    Intent i = new Intent(MainActivity.this,HomeScreen.class);
                                                    i.putExtra("userID",getPasswordByName());
                                                    i.putExtra("roles","");
                                                    startActivity(i);
                                                }
                                            }).setPositiveButton("View Transaction", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                    //db.execSQL("delete from StockTakeLines");
                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DimsIp+"pdforder/"+OrderId));
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

    public String XmlTemplate()
    {


        String Xml = "<Lines>" +
                "<strProduct>unik</strProduct>" +
                "<Price>moola</Price>" +
                "<Quantity>palo</Quantity>" +
                "</Lines>";
        return Xml;
    }
        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
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
    public String getPasswordByName() {

        String password ="";
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from LoggedIn limit 1", null);

        if (cursor2.moveToFirst()) {
            do {
                password =cursor2.getString(cursor2.getColumnIndex("UserID"));

            } while (cursor2.moveToNext());
        }

        return password;

    }

    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Doing some work for you.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
    public int CheckIfMerchie(){
        Cursor cursor2 = dbOrders.rawQuery("SELECT *  from BriefcaseRoles where RoleDescription ='Merchie'", null);

        return cursor2.getCount();
    }
    public void AssignLocations(){
        getCurrentLocation(new CurrentLocation() {
            @Override
            public void getLocation(double latitude, double longitude) {
                lats = latitude;
                lons = longitude;

            }
        });

    }
    private void getCurrentLocation(CurrentLocation currentLocation) {
        //Check the location permission:
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            currentLocation.getLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } else {
            //request for the location access
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }
}