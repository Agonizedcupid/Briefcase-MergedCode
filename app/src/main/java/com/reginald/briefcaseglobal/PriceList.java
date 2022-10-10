package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PriceList extends AppCompatActivity {

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
        private List<PriceList.Item> list;

        ItemsListAdapter(Context c, List<PriceList.Item> l) {
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
                rowView = inflater.inflate(R.layout.productpricelist_row, null);

                PriceList.ViewHolder viewHolder = new PriceList.ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.the_cust_name);
                viewHolder.text2 = (TextView) rowView.findViewById(R.id.contacts);
                rowView.setTag(viewHolder);
            }

            PriceList.ViewHolder holder = (PriceList.ViewHolder) rowView.getTag();
            // holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).ItemString);
            holder.text2.setText(list.get(position).ItemString3);


            return rowView;
        }

        public List<PriceList.Item> getList() {
            return list;
        }
    }
    List<Item> items1,items2;
    ItemsListAdapter myItemsListAdapter1,myItemsListAdapter2 ;
    private SQLiteDatabase db;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    EditText SelectedProductText,EditSearch,editCost,editSellingPrice,editMarginInPerc;
    ListView productslist,listprices;
    Button buttonSearch,MarginInPercText;
    String roles,userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
       // File file = new File("Salesmanbriefcase");

        // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        if (getIntent() != null) {
            roles = getIntent().getStringExtra("roles");
            userID = getIntent().getStringExtra("userID");

        }


        productslist = (ListView) findViewById(R.id.productslist);
        SelectedProductText = (EditText) findViewById(R.id.SelectedProductText);
        listprices = (ListView) findViewById(R.id.listprices);
        EditSearch = (EditText) findViewById(R.id.EditSearch);
        editCost = (EditText) findViewById(R.id.editCost);
        editSellingPrice = (EditText) findViewById(R.id.editSellingPrice);
        editMarginInPerc = (EditText) findViewById(R.id.editMarginInPerc);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        MarginInPercText = (Button) findViewById(R.id.MarginInPercText);

        productLookUp("");


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productLookUp(EditSearch.getText().toString());
            }
        });
        MarginInPercText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(editSellingPrice.getText().toString().trim())) {
                    editSellingPrice.setError("Pleas enter the selling price!");
                    editSellingPrice.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(editCost.getText().toString().trim())) {
                    editCost.setError("No Cost");
                    editCost.requestFocus();
                    return;
                }
                if ( editCost.getText().toString().trim().equals("0")) {
                    editCost.setError("The cost is zero");
                    editCost.requestFocus();
                    return;
                }
                double fCost = Double.parseDouble(editCost.getText().toString());
                double fsell = Double.parseDouble(editSellingPrice.getText().toString());

                if(!TextUtils.isEmpty(editCost.getText().toString().trim() )&& !TextUtils.isEmpty(editSellingPrice.getText().toString().trim()) )
                {
                    editMarginInPerc.setText(String.format("%.2f", new BigDecimal(marginCalculator(fCost,fsell))));
                }


            }
        });


        productslist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                editCost.setText("");
                editSellingPrice.setText("");
                editMarginInPerc.setText("");

                Item selectedItem2 = (Item)(adapterView.getItemAtPosition(i));
                SelectedProductText.setText(""+selectedItem2.ItemString.toString());
                editCost.setText(""+selectedItem2.ItemString3.toString());
                productPriceList(selectedItem2.ItemString2.toString());
                myItemsListAdapter2 = new ItemsListAdapter(PriceList.this, items2);
                listprices.setAdapter(myItemsListAdapter2);

                return false;
            }
        });

        listprices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item selectedItemdeal = (Item)(adapterView.getItemAtPosition(i));
                editSellingPrice.setText(""+selectedItemdeal.ItemString2.toString());
            }
        });
    }

    private void productLookUp(String search){
        items1 = new ArrayList<Item>();

        Cursor c = db.rawQuery("SELECT * FROM Products WHERE [strDesc] like '%" + search + "%'   order by [strDesc] ", null);
        int nameIndex = c.getColumnIndex("strPartNumber");
        int nameIndex2 = c.getColumnIndex("strDesc");
        if (c.moveToFirst()) {
            do {
                String s = c.getString(nameIndex);
                // String contacts = c.getString(nameIndex3)+" "+c.getString(nameIndex4);

                String s2 = c.getString(nameIndex2);
                Item item = new Item(s2.trim(), c.getString(nameIndex).trim(),c.getString(c.getColumnIndex("Cost")));
                items1.add(item);
            } while (c.moveToNext());
        }
        myItemsListAdapter1 = new ItemsListAdapter(PriceList.this, items1);
        productslist.setAdapter(myItemsListAdapter1);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PriceList.this);
            builder.setMessage("You sure you want to go back?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent myIntent = new Intent(PriceList.this,ButtonActivity.class);
                            myIntent.putExtra("userID",userID);
                            myIntent.putExtra("roles",roles);
                            startActivity(myIntent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void productPriceList(String search){
        items2 = new ArrayList<Item>();

        Log.e("search","**************search A"+search);
        Cursor c = db.rawQuery("select distinct CustomerPriceList,Price from CustomerPriceLists  WHERE [strPartNumber] = '" + search + "'   order by [CustomerPriceList] limit 100", null);
        int nameIndex = c.getColumnIndex("Price");
        int nameIndex2 = c.getColumnIndex("CustomerPriceList");
        if (c.moveToFirst()) {
            do {
                String s = c.getString(nameIndex);
                // String contacts = c.getString(nameIndex3)+" "+c.getString(nameIndex4);

                String s2 = c.getString(nameIndex2);
                Item item = new Item(s2.trim(), c.getString(nameIndex).trim(),c.getString(nameIndex).trim());
                items2.add(item);
            } while (c.moveToNext());
        }

    }
    public double marginCalculator(double cost,double onCellVal)
    {
        return (1-(cost/onCellVal))*100;
    }
}