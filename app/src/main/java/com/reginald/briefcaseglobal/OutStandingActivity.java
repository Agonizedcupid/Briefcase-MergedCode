package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OutStandingActivity extends AppCompatActivity {
    public class Item {

        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;


        Item(String t, String t2,String t3, String t4) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;

        }
    }

    public class ItemLines {

        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;


        ItemLines(String t, String t2,String t3, String t4) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;

        }
    }


    static class ViewHolder {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;

    }
    static class ViewHolderLines {
        //ImageView icon;
        TextView text;
        TextView text2;
        TextView text3;

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
                rowView = inflater.inflate(R.layout.orderheaders_outstanding, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.storename_order);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.ref_id);
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.row_del_date);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);
            holder.text3.setText(list.get(position).ItemString3);


            return rowView;
        }

        public List<Item> getList() {
            return list;
        }
    }


    public class ItemsListAdapterLines extends BaseAdapter {

        private Context context;
        private List<ItemLines> list;

        ItemsListAdapterLines(Context c, List<ItemLines> l) {
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
                rowView = inflater.inflate(R.layout.row_order_lines, null);

                ViewHolderLines viewHolder = new ViewHolderLines();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.textView50);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.row_item_qty);

                rowView.setTag(viewHolder);
            }

            ViewHolderLines holder = (ViewHolderLines) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString2);

            return rowView;
        }

        public List<ItemLines> getList() {
            return list;
        }
    }
    ListView lvorderheader,lvorderline;
    Button btncontinueorder;
    ImageView deleteorder,close_outstanding;
    private SQLiteDatabase dbOrders;
    List<Item> items2;
    List<ItemLines> itemslines;

    ItemsListAdapter myItemsListAdapter;
    ItemsListAdapterLines myItemsListAdapterLines;
    TextView order_id;
    String roles,userID,searchText,IP,DimsIp,storenames,customercodes;
    String SelectedDelDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_standing);
        lvorderheader = findViewById(R.id.lvorderheader);
        lvorderline = findViewById(R.id.lvorderline);
        btncontinueorder = findViewById(R.id.btncontinueorder);
        deleteorder = findViewById(R.id.deleteorder);
        close_outstanding = findViewById(R.id.close_outstanding);
        order_id = findViewById(R.id.order_id);
        order_id.setText("NO ID");
        btncontinueorder.setVisibility(View.INVISIBLE);
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");

        OrderHeaders();
        lvorderheader.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item selectedItem2 = (Item)(adapterView.getItemAtPosition(i));
                order_id.setText(selectedItem2.ItemString2);
                SelectedDelDate = selectedItem2.ItemString3;
                OrderLines(selectedItem2.ItemString2);
                return false;
            }
        });

        btncontinueorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(OutStandingActivity.this,OrderHeader.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("custcode",customercodes);
                i.putExtra("custName",storenames);
                i.putExtra("neworder",order_id.getText().toString());
                i.putExtra("existingOrder",order_id.getText().toString());
                i.putExtra("SelectedDelDate",SelectedDelDate);
                startActivity(i);

            }
        });
        //!(custcode.getText().toString()).equals("NO ID")
        close_outstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(OutStandingActivity.this,CustomersActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);

            }
        });

        deleteorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(!(order_id.getText().toString()).equals("NO ID")) {
Log.e("DeleteO","Delete Orders//////////////////////////");
                  deleteOrderByID(order_id.getText().toString());
              }


            }
        });


    }
    public void OrderHeaders(){
        items2 = new ArrayList<Item>();
        String isUploaded = "0";
        Cursor cursor = dbOrders.rawQuery("SELECT distinct l.CustomerCode,l.LineDeliveryDate,l.ID,l.StoreName,ifnull(h.Uploaded,0) Uploaded FROM OrderLines l left outer join OrderHeaders h on l.ID = h.ID" +
                " order by OrderLinesAutoId desc ", null);

        if (cursor.moveToFirst()) {
            do {

                Log.e("Uploaded","*********************************"+ cursor.getString(cursor.getColumnIndex("Uploaded")));
                isUploaded = cursor.getString(cursor.getColumnIndex("Uploaded"));
                if(!isUploaded.equals("1")){
                @SuppressLint("Range") Item listItem = new Item(
                        "[ "+cursor.getString(cursor.getColumnIndex("CustomerCode"))+" ] "+cursor.getString(cursor.getColumnIndex("StoreName")),
                        cursor.getString(cursor.getColumnIndex("ID")), cursor.getString(cursor.getColumnIndex("LineDeliveryDate")),""
                         );
                items2.add(listItem);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        myItemsListAdapter = new ItemsListAdapter(OutStandingActivity.this, items2);
        lvorderheader.setAdapter(myItemsListAdapter);
        myItemsListAdapter.notifyDataSetChanged();
    }

    public void OrderLines(String ID){
        itemslines = new ArrayList<ItemLines>();
        Cursor cursor = dbOrders.rawQuery("SELECT   strDesc,Quantity,ID,StoreName,CustomerCode FROM OrderLines where ID='"+ID+"'", null);

        if (cursor.moveToFirst()) {
            do {

              //  Log.e("Desc","*********************************"+ cursor.getString(cursor.getColumnIndex("strDesc")));
                @SuppressLint("Range") ItemLines listIteml = new ItemLines(
                        cursor.getString(cursor.getColumnIndex("strDesc")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),"",""
                );
                storenames =  cursor.getString(cursor.getColumnIndex("StoreName"));
                customercodes =  cursor.getString(cursor.getColumnIndex("CustomerCode")) ;
                itemslines.add(listIteml);

            } while (cursor.moveToNext());
        }
        cursor.close();
        btncontinueorder.setVisibility(View.VISIBLE);
        myItemsListAdapterLines = new ItemsListAdapterLines(OutStandingActivity.this, itemslines);
        lvorderline.setAdapter(myItemsListAdapterLines);
        myItemsListAdapterLines.notifyDataSetChanged();
    }

    public void deleteOrderByID(String subscriberId){
        dbOrders.rawQuery("delete from OrderLines where ID='"+subscriberId+"'",null);
        Log.e("I am In","++++++++++++++++++++"+subscriberId);
        OrderLines(subscriberId);
        OrderHeaders();
        order_id.setText("NO ID");
    }
}