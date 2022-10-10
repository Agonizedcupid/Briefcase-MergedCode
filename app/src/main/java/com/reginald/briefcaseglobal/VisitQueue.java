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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitQueue extends AppCompatActivity {
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
                rowView = inflater.inflate(R.layout.logvisitsqueuerow, null);

                ViewHolder viewHolder = new ViewHolder();
                //  viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.queumessages);

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
    List<Item> items1;
    ItemsListAdapter myItemsListAdapter1 ;
    private SQLiteDatabase dbOrders,db;
    ListView lvvisitsque;
    ImageView btnclosequeuvisits;
    Button btnrefresh;
    String roles,userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_queue);
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        lvvisitsque = findViewById(R.id.lvvisitsque);
        btnclosequeuvisits = findViewById(R.id.btnclosequeuvisits);
        btnrefresh = findViewById(R.id.btnrefresh);
        CustomerLookupResult();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");

        btnclosequeuvisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( VisitQueue.this,HomeScreen.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
            }
        });

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor2 = dbOrders.rawQuery("SELECT * from Visits limit 15", null);
                final  AppApplication globalVariable = (AppApplication) getApplicationContext();

                if (cursor2.moveToFirst()) {
                    do {
                        postDataUsingVolley(cursor2.getString(cursor2.getColumnIndex("CustomerCode")),
                                cursor2.getString(cursor2.getColumnIndex("nextvisit")),
                                cursor2.getString(cursor2.getColumnIndex("Lat")),
                                cursor2.getString(cursor2.getColumnIndex("Lon")),
                                cursor2.getString(cursor2.getColumnIndex("answeroneid")),
                                cursor2.getString(cursor2.getColumnIndex("answeronetext")),
                                cursor2.getString(cursor2.getColumnIndex("answertwoid")),
                                cursor2.getString(cursor2.getColumnIndex("answertwotext")),
                                cursor2.getString(cursor2.getColumnIndex("answerthreeid")),
                                cursor2.getString(cursor2.getColumnIndex("answerthreetext")),
                                cursor2.getString(cursor2.getColumnIndex("customersatisfactoyanswer")),
                                cursor2.getString(cursor2.getColumnIndex("userid")),
                                cursor2.getString(cursor2.getColumnIndex("catchupnotes")),
                                cursor2.getString(cursor2.getColumnIndex("notes")),
                                globalVariable.getIP(), cursor2.getString(cursor2.getColumnIndex("ID"))
                        );
                    } while (cursor2.moveToNext());


                }
                Intent y = new Intent(VisitQueue.this,VisitQueue.class);
                y.putExtra("userID",userID);
                y.putExtra("roles",roles);
                startActivity(y);
            }
        });

        //queumessages
    }
    private void CustomerLookupResult() {
        items1 = new ArrayList<Item>();

        Cursor c = dbOrders.rawQuery("SELECT ID,CustomerCode,notes FROM Visits order by [CustomerCode]", null);
        if (c.moveToFirst()) {
            do {

                Item item = new Item(CustomerNameLookUp(c.getString(c.getColumnIndex("CustomerCode")))+"\n"+c.getString(c.getColumnIndex("notes")), c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("CustomerCode")));
                items1.add(item);

            } while (c.moveToNext());
        }
        myItemsListAdapter1 = new ItemsListAdapter(this, items1);
        lvvisitsque.setAdapter(myItemsListAdapter1);

    }
    private String CustomerNameLookUp(String custCode) {

        String storename = "";
        Cursor c = db.rawQuery("SELECT CustomerStoreName FROM Customers WHERE CustomerCode = '" + custCode + "'  ", null);
        if (c.moveToFirst()) {
            do {
                storename = c.getString(c.getColumnIndex("CustomerStoreName"));

            } while (c.moveToNext());
        }
      return storename;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VisitQueue.this);
            builder.setMessage("You sure you want to go back?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent myIntent = new Intent(VisitQueue.this,HomeScreen.class);
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

    private void postDataUsingVolley(String code, String date,String latitude,String longitude,
                                     String questionOneId,String questionOneAnswer,String questionTwoId,
                                     String questionTwoAnswer,String questionThreeId,String questionThreeAnswer,
                                     String specialComments,String UserId,String catchUpNotes,String notes,String postUrl,String ID) {
        // url to post our data
        String url = postUrl+"/LogvisitService.php";
        // loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(VisitQueue.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //dbOrders.rawQuery("delete from Visits where ID='"+response+"'",null);

                dbOrders.execSQL("delete from Visits where ID='"+ID+"'");
                try {

                    Log.e("response","response First******************"+response);

                  //  JSONObject respObj = new JSONObject(response);

                 //   String success = respObj.getString("Result");
                  //  Log.e("response","success******************"+success);
                   // if (response.equals("Success")) {


                   // }

                   /* Log.e("not trimmed in","vvvvvvvvvvvvvvvvvvvvvvvvv "+response);
                    if (response.equalsIgnoreCase("EXISTS") ) {
                        Log.e("response","The EXISTS ---******************"+"delete from Visits where ID='"+ID+"'");
                        dbOrders.rawQuery("delete from Visits where ID='"+ID+"'",null);
                    }*/

                    // on below line we are setting this string s to our text view.

                } catch (Exception e) {
                   // e.printStackTrace();
                    Log.e("error message","errrr******************"+e.getMessage());
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                //Toast.makeText(LogVisit.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                params.put("CustomerCode", code);
                params.put("nextvisit", date);
                params.put("Lat", latitude);
                params.put("Lon", longitude);
                params.put("answeroneid", questionOneId);
                params.put("answeronetext", questionOneAnswer);
                params.put("answertwoid", questionTwoId);
                params.put("answertwotext", questionTwoAnswer);
                params.put("answerthreeid", questionThreeId);
                params.put("answerthreetext", questionThreeAnswer);
                params.put("customersatisfactoyanswer",specialComments);
                params.put("userid", UserId);
                params.put("catchupnotes", catchUpNotes);
                params.put("notes", notes);
                params.put("ID", ID);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}