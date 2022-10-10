package com.reginald.briefcaseglobal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Adapter.ScheduleAdapter;
import com.reginald.briefcaseglobal.Model.DailyScheduleModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailySchedule extends AppCompatActivity {


    private Button dateFrom, dateTo,btndatefilter;
    private RecyclerView recyclerView;

    private DatePickerDialog datePickerDialog;
    ProgressDialog progressDoalog;
    private Calendar calendar;
    int day, month, year;
    String date = "";

    private ScheduleAdapter adapter;

    private List<DailyScheduleModel> list = new ArrayList<>();

    private ProgressBar progressBar;

    String userId = "18";
    DatePickerDialog pickerto,picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        initUI();
        startProgress("Please Wait...");
        loadData();
    }

    private void initUI() {

        dateFrom = findViewById(R.id.dateFroms);
        progressBar = findViewById(R.id.progressbar);
        dateTo = findViewById(R.id.dateTos);
        recyclerView = findViewById(R.id.dailyScheduleRecyclerview);
        btndatefilter = findViewById(R.id.btndatefilter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        dateFrom.setText(tm);
        dateTo.setText(tm);

        //dateFrom.setText(getTodayDate());
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DailySchedule.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateFrom.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                               /// loadData();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                pickerto = new DatePickerDialog(DailySchedule.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateTo.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                pickerto.show();

            }
        });
        btndatefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgress("Please Wait...");
                loadData();
            }
        });


      /*  dateTo.setText(getTodayDate());
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });*/

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        return dateFormat.format(date);    // 2021/03/22 16:37:15
    }

    private void showDatePicker() {
        datePickerDialog = new DatePickerDialog(DailySchedule.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                int j = i1 + 1;

                date = i + "-" + j + "-" + i2;
                Toast.makeText(DailySchedule.this, "You've selected " + date, Toast.LENGTH_LONG).show();
                //Toast.makeText(AddProperty.this, "" + availableStatus, Toast.LENGTH_SHORT).show();
                dateTo.setText(date);
                loadData();
            }
        }, day, month, year);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(DailySchedule.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Doing some work for you.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
    private void loadData() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);

        Call<ResponseBody> call = apiService.getDailySchedule(globalVariable.getUserId(), dateFrom.getText().toString(), dateTo.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                   // Log.e("myerro",""+response.body().string());

                    JSONArray array = new JSONArray(response.body().string());
                   // Log.e("myerro",""+array);
                    list.clear();
                    if (array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject single = array.getJSONObject(i);
                            String StoreName = single.getString("StoreName");
                            String code = single.getString("code");
                            JSONObject obj = single.getJSONObject("datevisited");
                            String date = obj.getString("date");
                            String timezone_type = obj.getString("timezone_type");
                            String timezone = obj.getString("timezone");

                            DailyScheduleModel model = new DailyScheduleModel(
                                    StoreName,
                                    code,
                                    date,
                                    timezone_type,
                                    timezone
                            );

                            list.add(model);
                        }
                        adapter = new ScheduleAdapter(DailySchedule.this, list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        progressDoalog.dismiss();


                    } else {
                        progressBar.setVisibility(View.GONE);
                        progressDoalog.dismiss();
                        Toast.makeText(DailySchedule.this, "No data found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("ERROR_CHECKING", "1: " + e.getMessage());
                    Log.e("ERROR_CHECKING"," "+ e.getMessage());
                    Toast.makeText(DailySchedule.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                progressBar.setVisibility(View.GONE);
                Log.d("ERROR_CHECKING", "2: " + e.getMessage());
                Toast.makeText(DailySchedule.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}