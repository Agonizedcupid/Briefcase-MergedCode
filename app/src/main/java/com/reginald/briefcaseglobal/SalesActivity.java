package com.reginald.briefcaseglobal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesActivity extends AppCompatActivity {

    public String name = "",userID="";

    private TextView userName, howMuchIMade, toGo, target, gp, toGoText, weeksLeft, weeksLeftAmount;
    private TextView daysLeft, daysLeftAmount;

    private ProgressBar progressBar;

    private PieChart pieChart;
    private PieChart previousmonth;

    PieData pieData;
    PieData pieData2;
    List<PieEntry> pieEntryList = new ArrayList<>();
    List<PieEntry> pieEntryList2 = new ArrayList<>();
    private SQLiteDatabase db;
    Spinner txtprevmonth;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales2);
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        initUI();

        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            userID = getIntent().getStringExtra("userID");
            userName.setText(name);
            loadData(name);
        }

        txtprevmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Change","*********change drop"+txtprevmonth.getSelectedItem().toString());
                loadDataprev(name,txtprevmonth.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(CheckIfMerchie()> 0){ Intent i = new Intent(SalesActivity.this,HomeScreen.class);
            i.putExtra("userID",userID);
            i.putExtra("roles","");
            startActivity(i);}

        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SalesActivity.this,HomeScreen.class);
                i.putExtra("userID",userID);
                i.putExtra("roles","");
                startActivity(i);
            }
        });
    }

    private void initUI() {
        try{

            userName = findViewById(R.id.userNames);
            howMuchIMade = findViewById(R.id.howMuchIMade);
            toGo = findViewById(R.id.toGo);
            txtprevmonth = findViewById(R.id.txtprevmonth);
            target = findViewById(R.id.target);
            gp = findViewById(R.id.gp);
            toGoText = findViewById(R.id.toGoText);
            weeksLeft = findViewById(R.id.weeksLeft);
            weeksLeftAmount = findViewById(R.id.weeksLeftAmount);
            daysLeft = findViewById(R.id.daysLeft);
            daysLeftAmount = findViewById(R.id.daysLeftAmount);

            pieChart = findViewById(R.id.pieChart);
            previousmonth = findViewById(R.id.previousmonth);
            pieChart.setUsePercentValues(false);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Sales");

            previousmonth.setUsePercentValues(false);
            previousmonth.setEntryLabelColor(Color.BLACK);
            previousmonth.getDescription().setEnabled(false);
            previousmonth.setCenterText("Sales");
            List<String> labels = new ArrayList<String>();

            labels.add("Choose Month");
            labels.add("Jan");
            labels.add("Feb");
            labels.add("Mar");
            labels.add("Apr");
            labels.add("May");
            labels.add("Jun");
            labels.add("Jul");
            labels.add("Aug");
            labels.add("Sep");
            labels.add("Oct");
            labels.add("Nov");
            labels.add("Dec");
            ArrayAdapter<String> ordertypeA =
                    new ArrayAdapter<String>(SalesActivity.this,
                            android.R.layout.simple_spinner_item, labels);
            ordertypeA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            txtprevmonth.setAdapter(ordertypeA);


            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setEnabled(true);
            l.setDrawInside(true);

            Legend l2 = pieChart.getLegend();
            l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l2.setOrientation(Legend.LegendOrientation.VERTICAL);
            l2.setEnabled(true);
            l2.setDrawInside(true);

            progressBar = findViewById(R.id.progressbar);

        }catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
            builder.setMessage(" Error, submit your error to support team")
                    .setCancelable(false)
                    .setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            //DELETE BEFORE GOING BACK
            AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
            builder.setMessage("Are you sure you want to go back to the login screen?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(SalesActivity.this,LoginActivity.class);

                            startActivity(i);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    dialog.dismiss();
                }
            })
            ;
            AlertDialog alert = builder.create();
            alert.show();


            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void loadData(String n) {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getSalesTarget(n);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                   // Log.e("reponse","****************************"+response.body().string());
                   // Log.e("mnyMade","respond****************************"+response.body().string());
                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject singleObject = array.getJSONObject(i);
                        String mnyMade = singleObject.getString("mnyMade");
                        String mnyTarget = singleObject.getString("mnyTarget");
                        String mnyTogo = singleObject.getString("mnyTogo");
                        String mnyGP = singleObject.getString("mnyGP");
                        int intweeksleft = singleObject.getInt("intweeksleft");
                        int intDaysLeft = singleObject.getInt("intDaysLeft");
                        String mnyToAchieveintheremainingweeks = singleObject.getString("mnyToAchieveintheremainingweeks");
                        String mnyToAchieveintheremainingdays = singleObject.getString("mnyToAchieveintheremainingdays");
                        String strRepName = singleObject.getString("strRepName");

                        howMuchIMade.setText(mnyMade);
                        target.setText(mnyTarget);
                        toGo.setText(mnyTogo);
                        gp.setText(mnyGP);
                        toGoText.setText("TO Go: " + mnyTogo);
                        weeksLeft.setText("" + intweeksleft);
                        weeksLeftAmount.setText(mnyToAchieveintheremainingweeks);
                        daysLeft.setText("" + intDaysLeft);
                        daysLeftAmount.setText(mnyToAchieveintheremainingdays);

                        int tg = (int) Double.parseDouble(mnyTogo);
                        int t = (int) Double.parseDouble(mnyMade);

                        pieEntryList.add(new PieEntry(tg, "To go"));
                        pieEntryList.add(new PieEntry(t, "Made"));

                        ArrayList<Integer> colors = new ArrayList<>();
                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Sales");
                        pieDataSet.setColors(colors);
                        pieData = new PieData(pieDataSet);
                        pieData.setDrawValues(true);
                        pieData.setValueTextColor(Color.BLACK);
                        pieData.setValueTextSize(12);
                        //pieData.setValueFormatter( new DefaultAxisValueFormatter(10));
                        pieChart.setData(pieData);
                        pieChart.invalidate();
                    }
                    progressBar.setVisibility(View.GONE);


                } catch (Exception e) {
                    Toast.makeText(SalesActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadDataprev(String n, String Month) {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IPmnyMade","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getSalesTargetPrev(n,Month);
        pieEntryList2 = new ArrayList<>();
        startProgress("...Wait");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    // Log.e("mnyMade","****************************"+response.body().string());
                    JSONArray array = new JSONArray(response.body().string());
                    Log.e("length","****************************"+array.length());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject singleObject = array.getJSONObject(i);

                        Log.e("singleObject","****************************"+singleObject);
                        String mnyMade = singleObject.getString("sales");
                        String mnyTogo = singleObject.getString("togo");

                        Log.e("mnyTogo","mnyTogo***************"+mnyTogo);
                        Log.e("mnyMade","mnyMade***************"+mnyMade);
                        int tg = (int) Double.parseDouble(mnyTogo);
                        int t = (int) Double.parseDouble(mnyMade);

                        pieEntryList2.add(new PieEntry(tg, "To go"));
                        pieEntryList2.add(new PieEntry(t, "Made"));

                        ArrayList<Integer> colors = new ArrayList<>();
                        for (int color : ColorTemplate.MATERIAL_COLORS) {
                            colors.add(color);
                        }

                        PieDataSet pieDataSet = new PieDataSet(pieEntryList2, "Sales");
                        pieDataSet.setColors(colors);
                        pieData2 = new PieData(pieDataSet);
                        pieData2.setDrawValues(true);
                        pieData2.setValueTextColor(Color.BLACK);
                        pieData2.setValueTextSize(12);
                        //pieData.setValueFormatter( new DefaultAxisValueFormatter(10));
                        previousmonth.setData(pieData2);
                        previousmonth.invalidate();
                    }
                   // progressBar.setVisibility(View.GONE);
                    progressDoalog.dismiss();

                } catch (Exception e) {
                    Toast.makeText(SalesActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("errrrrrrr","err***************"+e.getMessage());
                    progressDoalog.dismiss();
                    //progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               // progressBar.setVisibility(View.GONE);
                progressDoalog.dismiss();
            }
        });
    }
    public int CheckIfMerchie(){
        Cursor cursor2 = db.rawQuery("SELECT *  from BriefcaseRoles where RoleDescription ='Merchie'", null);

        return cursor2.getCount();
    }
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(SalesActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Doing some work for you.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
}