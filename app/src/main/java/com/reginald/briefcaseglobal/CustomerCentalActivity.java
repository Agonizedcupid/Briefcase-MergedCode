package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomerCentalActivity extends AppCompatActivity {

    public class Item {

        String ItemString;
        String ItemString2;
        String ItemString3;


        Item(String t, String t2,String t3) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;

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
                rowView = inflater.inflate(R.layout.customer_tel, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.the_cust_name);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.contacts);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString3);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }
    private SQLiteDatabase db;
    ItemsListAdapter myItemsListAdapter1 ;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    List<Item> items1,items3AlphaList;

    EditText editSearch,selectedcustomer;
    Button buttonSearch,buttonCustomerHistory,buttonToday,buttonNext,buttonOrderComplete,shistory;
    TextView custcode, txtcustomername,txtcustcode;
    String roles,userID,searchText,method;
    ListView listcustomers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cental);
       // File file = new File( "Salesmanbriefcase");
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+ "/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        editSearch =(EditText) findViewById(R.id.editSearch);
        selectedcustomer =(EditText) findViewById(R.id.selectedcustomer);
        custcode =(TextView) findViewById(R.id.custcode);
        buttonSearch =(Button) findViewById(R.id.buttonSearch);

        buttonNext =(Button) findViewById(R.id.nextcustomercentral);

        listcustomers =(ListView) findViewById(R.id.listcustomers);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        method = intent.getStringExtra("method");

        CustomerLookupResult(" ");
        myItemsListAdapter1 = new ItemsListAdapter(this, items1);
        listcustomers.setAdapter(myItemsListAdapter1);
        editSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("Touch","********** here");
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
                selectedcustomer.setText(""+selectedItem.ItemString);
                custcode.setText(""+selectedItem.ItemString2);

            }
        });
        listcustomers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item selectedItem2 = (Item)(adapterView.getItemAtPosition(i));
                Dialog dialogView = new Dialog(CustomerCentalActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setCancelable(false);
                dialogView.setContentView(R.layout.customerdialogoptions);

                txtcustomername = (TextView) dialogView.findViewById(R.id.txtcustomername);
                txtcustcode = (TextView) dialogView.findViewById(R.id.txtcustcode);
                shistory = (Button) dialogView.findViewById(R.id.shistory);

                txtcustomername.setText(selectedItem2.ItemString.toString());
                txtcustcode.setText(selectedItem2.ItemString2.toString());

                shistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialogView.show();
                return false;
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
                myItemsListAdapter1 = new ItemsListAdapter(CustomerCentalActivity.this, items1);
                listcustomers.setAdapter(myItemsListAdapter1);
                //  alpha_listView.setAdapter(myItemsListAdapter3Alpha);
                editSearch.getText().clear();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (method){
                    case "deals":
                        Intent i = new Intent(CustomerCentalActivity.this,DealActivity.class);
                        i.putExtra("userID",userID);
                        i.putExtra("roles",roles);
                        i.putExtra("method",method);
                        i.putExtra("custcode",custcode.getText().toString());
                        i.putExtra("selectedcustomer",selectedcustomer.getText().toString());
                        startActivity(i);
                        break;
                    case "visits":
                        Intent v= new Intent(CustomerCentalActivity.this,VisitsActivity.class);
                        v.putExtra("userID",userID);
                        v.putExtra("roles",roles);
                        v.putExtra("method",method);
                        v.putExtra("custcode",custcode.getText().toString());
                        v.putExtra("selectedcustomer",selectedcustomer.getText().toString());
                        startActivity(v);
                }
            }
        });

    }

    private void CustomerLookupResult(String search) {
        items1 = new ArrayList<Item>();

        //
        Cursor c = db.rawQuery("SELECT * FROM Customers WHERE [CustomerStoreName] like '%" + search + "%'  order by [CustomerStoreName]", null);
        int nameIndex = c.getColumnIndex("CustomerStoreName");
        int nameIndex2 = c.getColumnIndex("CustomerCode");
        int nameIndex3 = c.getColumnIndex("CustomerContactTelephone");
        int nameIndex4 = c.getColumnIndex("CustomerContactCellphone");
        if (c.moveToFirst()) {
            do {
                String s = "zz";
                if(c.getString(nameIndex).length() > 0)
                {
                    s = c.getString(nameIndex);
                }

                String contacts = c.getString(c.getColumnIndex("CustomerContactTelephone"))+" "+c.getString(c.getColumnIndex("CustomerContactCellphone"));
                stringArrayList.add(s.substring(0, 1));
                String s3 = c.getString(nameIndex2);
                Item item = new Item(s, s3,contacts);
                items1.add(item);
                Log.e("contacts","contacts**********************"+contacts);
            } while (c.moveToNext());
        }
        //c.close();

        //letters.close();
        //db.close();
    }

}