package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OrderPatternItemsActivity extends AppCompatActivity {


    public class Item {
        String ItemString;
        String ItemString2;
        String ItemString3;
        String ItemString4;
        String ItemString5;
        String ItemString6;



        Item(String t, String t2,String t3,String t4,String t5,String t6) {
            ItemString = t;
            ItemString2 = t2;
            ItemString3 = t3;
            ItemString4 = t4;
            ItemString5 = t5;
            ItemString6 = t6;

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

                MainActivity.ViewHolder viewHolder = new MainActivity.ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.product_name);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.prodcode );
                viewHolder.text3 = (TextView) rowView.findViewById(R.id.trend);
                viewHolder.text4 = (TextView) rowView.findViewById(R.id.uom);
                viewHolder.text5 = (TextView) rowView.findViewById(R.id.costs);

                rowView.setTag(viewHolder);
            }

            MainActivity.ViewHolder holder = (MainActivity.ViewHolder) rowView.getTag();
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
    List<Item> items1;
    private SQLiteDatabase db;
    ItemsListAdapterOrderPattern myListAdapterOderPattern;
    ListView lvItems;
    TextView txtcustname;
    TextView textView42;
    Button btnvisit;
    Button btnplanvisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pattern_items);
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        lvItems = findViewById(R.id.lvItems);
        txtcustname = findViewById(R.id.txtcustname);
        textView42 = findViewById(R.id.textView42);
        btnvisit = findViewById(R.id.btnvisit);
        btnplanvisit = findViewById(R.id.btnplanvisit);
        if (getIntent() != null) {
            txtcustname.setText(getIntent().getStringExtra("customerName"));
            textView42.setText(getIntent().getStringExtra("customercode"));
        }
        btnplanvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderPatternItemsActivity.this,CreateMemo.class)
                        .putExtra("name",txtcustname.getText().toString())
                        .putExtra("code",textView42.getText().toString())
                );
            }
        });
        btnvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderPatternItemsActivity.this,LogVisit.class)
                        .putExtra("name",txtcustname.getText().toString())
                        .putExtra("code",textView42.getText().toString())
                );
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               // context.startActivity(new Intent(context, AddItemToSpecialActivity.class)
                Item selectedItem2 = (Item)(adapterView.getItemAtPosition(i));
                        Intent p =  new Intent(OrderPatternItemsActivity.this,AddItemToSpecialActivity.class)
                        .putExtra("customercode", textView42.getText().toString())
                        .putExtra("itemcode", selectedItem2.ItemString2)
                        .putExtra("storename",txtcustname.getText().toString())
                        .putExtra("getPastelDescription", selectedItem2.ItemString);
                        startActivity(p);
                return false;
            }
        });
        returnOrderPattern(textView42.getText().toString());

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            //DELETE BEFORE GOING BACK

            Intent i = new Intent(OrderPatternItemsActivity.this,MemoHome.class);
            i.putExtra("code",textView42.getText().toString());
            i.putExtra("name",txtcustname.getText().toString());
            startActivity(i);

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnOrderPattern(String custcode)
    {
       // startProgress("Please Wait!");
        items1 = new ArrayList<Item>();
        Cursor cursor = db.rawQuery("SELECT PastelDescription,PastelCode,Tax,intAvailable,Cost,TrendingId FROM OrderPattern " +
                "inner join Products on Products.strPartnumber = OrderPattern.PastelCode  WHERE CustomerCode='"+custcode+"' and TrendingId in(2,4)  order by [PastelDescription] ", null);

        if (cursor.moveToFirst()) {
            do {

                Item listItem = new Item( cursor.getString(cursor.getColumnIndex("PastelDescription")),cursor.getString(cursor.getColumnIndex("PastelCode")),
                        cursor.getString(cursor.getColumnIndex("Tax")), cursor.getString(cursor.getColumnIndex("intAvailable")), cursor.getString(cursor.getColumnIndex("Cost")),
                        cursor.getString(cursor.getColumnIndex("TrendingId")));
                items1.add(listItem);

            } while (cursor.moveToNext());
        }

        myListAdapterOderPattern = new ItemsListAdapterOrderPattern(OrderPatternItemsActivity.this, items1);
        lvItems.setAdapter(myListAdapterOderPattern);
        // myListAdapterOderPattern.notifyDataSetChanged();

    }
}