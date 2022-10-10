package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AfterCustomerSelect extends AppCompatActivity {

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
                rowView = inflater.inflate(R.layout.customer_unfinishedorders_row, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.orderno);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.deliverydate);
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
    List<Item> items1;

    String roles,userID,custName,custcode;
    TextView CustomerNameText,todayDateText2;
    ListView lvorders;
    ItemsListAdapter myItemsListAdapter1 ;
    Button next;
    private SQLiteDatabase dbOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_customer_select);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Salesmanbriefcase");
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        custcode = intent.getStringExtra("custcode");
        custName = intent.getStringExtra("custName");
        lvorders =(ListView)  findViewById(R.id.lvorders);
        CustomerNameText =(TextView)  findViewById(R.id.CustomerNameText);
        todayDateText2 =(TextView)  findViewById(R.id.todayDateText2);
        next = (Button) findViewById(R.id.buttonNext2);
        CustomerNameText.setText(custName);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        todayDateText2.setText(tm);

        getUnfinishedOrders(tm,custcode);
        myItemsListAdapter1 = new ItemsListAdapter(AfterCustomerSelect.this, items1);
        lvorders.setAdapter(myItemsListAdapter1);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AfterCustomerSelect.this,OrderHeader.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("custcode",custcode);
                i.putExtra("custName",custName);
                i.putExtra("neworder","neworder");
                startActivity(i);
            }
        });

    }
    public void getUnfinishedOrders(String orderdate,String CustomerCode)
    {
        items1 = new ArrayList<Item>();
        Cursor cursororderids = dbOrders.rawQuery("select * from  OrderHeaders where CustomerCode='"+CustomerCode+"' and OrderDate='"+orderdate+"'", null);

        if (cursororderids.moveToFirst()) {
            do {


                Item item = new Item(cursororderids.getString(cursororderids.getColumnIndex("OrderNumber")), cursororderids.getString(cursororderids.getColumnIndex("DeliveryDate")),cursororderids.getString(cursororderids.getColumnIndex("ID")));
                items1.add(item);

            } while (cursororderids.moveToNext());
        }
        cursororderids.close();
    }
}