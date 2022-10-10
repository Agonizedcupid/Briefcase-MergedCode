package com.reginald.briefcaseglobal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Adapter.ItemAdapter;
import com.reginald.briefcaseglobal.Adapter.OrderAdapter;
import com.reginald.briefcaseglobal.Model.ItemModel;
import com.reginald.briefcaseglobal.Model.OrderModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.onClickListener{

    String customerName = "", customerCode = "";
    private RecyclerView orderRecyclerView;
    private TextView cName, cCode, orderCount, dateFrom, dateTo;
    private Button getOrder;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;
    String date = "";
    private List<OrderModel> listOfOrder = new ArrayList<>();
    private List<ItemModel> listOfITem = new ArrayList<>();
    private ProgressBar progressBar;

    //order item:
    private RecyclerView itemRecyclerview;
    private LinearLayout itemLayout;
    private TextView invoiceNo,orderNo,itemMessage;
    DatePickerDialog pickerto,picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        if (getIntent() != null) {
            customerCode = getIntent().getStringExtra("code");
            customerName = getIntent().getStringExtra("name");
        }

        initUI();
    }

    private void initUI() {

        progressBar = findViewById(R.id.progressbar);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        itemRecyclerview = findViewById(R.id.itemsRecyclerview);
        itemRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        itemLayout = findViewById(R.id.itemsLayout);
        invoiceNo = findViewById(R.id.invoiceN);
        orderNo = findViewById(R.id.orderN);
        itemMessage = findViewById(R.id.itemMessage);

        cName = findViewById(R.id.cName);
        cCode = findViewById(R.id.cCode);
        orderCount = findViewById(R.id.totalOrders);
        getOrder = findViewById(R.id.getOrderBtn);

        dateFrom = findViewById(R.id.dateFromTextView);
        dateTo = findViewById(R.id.dateToTextView);

        cName.setText("Orders For: " + customerName);
        cCode.setText(customerCode);



        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(OrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateFrom.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
                pickerto = new DatePickerDialog(OrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateTo.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                pickerto.show();
            }
        });


        getOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOrderApiToGetTheData();
            }
        });
    }

    private void callOrderApiToGetTheData() {
        progressBar.setVisibility(View.VISIBLE);
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());

        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getOrderList(customerCode, dateFrom.getText().toString(), dateTo.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    listOfOrder.clear();
                    JSONArray finalResponse = new JSONArray(response.body().string());
                    for (int i=0; i<finalResponse.length(); i++) {
                        JSONObject singleObject = finalResponse.getJSONObject(i);
                        String StoreName = singleObject.getString("StoreName");
                        String CustomerPastelCode = singleObject.getString("CustomerPastelCode");

                        String orderDate;
                        int orderTimeZoneType;
                        String orderTimeZone;

                        String deliveryDate;
                        int deliveryTimeZoneType;
                        String deliveryTimeZone;
                        JSONObject oDate = singleObject.getJSONObject("OrderDate");
                        orderDate = oDate.getString("date");
                        orderTimeZoneType = oDate.getInt("timezone_type");
                        orderTimeZone = oDate.getString("timezone");

                        JSONObject dDate = singleObject.getJSONObject("DeliveryDate");
                        deliveryDate = dDate.getString("date");
                        deliveryTimeZoneType = dDate.getInt("timezone_type");
                        deliveryTimeZone = dDate.getString("timezone");

                        String InvoiceNo = singleObject.getString("InvoiceNo");
                        String OrderNo = singleObject.getString("OrderNo");
                        String OrderId = singleObject.getString("OrderId");

                        OrderModel model = new OrderModel(
                                StoreName,
                                CustomerPastelCode,
                                orderDate,
                                orderTimeZoneType,
                                orderTimeZone,
                                deliveryDate,
                                deliveryTimeZoneType,
                                deliveryTimeZone,
                                InvoiceNo,
                                OrderNo,
                                OrderId
                        );

                        listOfOrder.add(model);
                        orderRecyclerView.setVisibility(View.VISIBLE);
                        orderCount.setText(""+listOfOrder.size() + " Orders");
                        OrderAdapter adapter = new OrderAdapter(OrderActivity.this,listOfOrder,OrderActivity.this);
                        orderRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    orderRecyclerView.setVisibility(View.GONE);

                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                } catch (IOException e) {
                    orderRecyclerView.setVisibility(View.GONE);

                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                orderRecyclerView.setVisibility(View.GONE);
                Toast.makeText(OrderActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTheDate(String type) {
        datePickerDialog = new DatePickerDialog(OrderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                int j = i1 + 1;

                date = i + " - " + j + " - " + i2;
                if (type.equals("from")) {
                    dateFrom.setText(date);
                } else {
                    dateTo.setText(date);
                }

                //Toast.makeText(AddProperty.this, "" + availableStatus, Toast.LENGTH_SHORT).show();

            }
        }, day, month, year);

      //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    @Override
    public void onItemClick(int position) {
        itemLayout.setVisibility(View.GONE);
        OrderModel orderModel = listOfOrder.get(position);
        invoiceNo.setText("Invoice No: "+orderModel.getInvoiceNo());
        orderNo.setText("Order number: "+orderModel.getOrderId());
        getItemData(orderModel.getOrderId());
    }

    private void getItemData(String orderId) {
        progressBar.setVisibility(View.VISIBLE);
         final  AppApplication globalVariable = (AppApplication) getApplicationContext();

        APIs apiService = ApiClient.getClient( globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getItemList(orderId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    listOfITem.clear();
                    JSONArray finalResponse = new JSONArray(response.body().string());
                    if (finalResponse.length() > 0) {
                        itemLayout.setVisibility(View.VISIBLE);
                        for (int i=0; i<finalResponse.length(); i++) {
                            JSONObject singleObject = finalResponse.getJSONObject(i);
                            String ItemName = singleObject.getString("ItemName");
                            String ItemCode = singleObject.getString("ItemCode");
                            int qty = singleObject.getInt("Qty");
                            double Price = singleObject.getDouble("Price");
                            double Total = singleObject.getDouble("Total");
                            double Tax = singleObject.getDouble("Tax");
                            String MESSAGESINV = singleObject.getString("MESSAGESINV");
                            itemMessage.setText("Message: "+MESSAGESINV);

                            ItemModel model = new ItemModel(
                                    ItemName,ItemCode,qty,Price,Total,Tax,MESSAGESINV
                            );

                            listOfITem.add(model);
                        }
                        itemRecyclerview.setVisibility(View.VISIBLE);
                        ItemAdapter adapter = new ItemAdapter(OrderActivity.this,listOfITem);
                        itemRecyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(OrderActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
                        itemLayout.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    itemRecyclerview.setVisibility(View.GONE);
                    itemLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                } catch (IOException e) {
                    itemRecyclerview.setVisibility(View.GONE);
                    itemLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                orderRecyclerView.setVisibility(View.GONE);
                Toast.makeText(OrderActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}