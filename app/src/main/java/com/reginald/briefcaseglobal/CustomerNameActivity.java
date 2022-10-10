package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.reginald.briefcaseglobal.Adapter.CustomerAdapter;
import com.reginald.briefcaseglobal.Adapter.DecreasingSalesAdapter;
import com.reginald.briefcaseglobal.Interface.CustomerListInterface;
import com.reginald.briefcaseglobal.Model.CustomerModel;
import com.reginald.briefcaseglobal.Model.DecreasingSalesModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.Network.Networking;
import com.reginald.briefcaseglobal.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerNameActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private EditText search;
    private ProgressBar progressBar;

    //Problematic Items
    private RecyclerView recyclerViews;
    private List<DecreasingSalesModel> lists = new ArrayList<>();
    private DecreasingSalesAdapter decreasingSalesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_name);

        initUI();

        loadProblematicCustomer();
    }

    private void loadProblematicCustomer() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);


        //   APIs api = ApiClient.getClient2().create(APIs.class);
        Call<ResponseBody> call = apiService.getCustomerDecreasingSales(globalVariable.getUserId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONArray finalResponse = new JSONArray(response.body().string());
                    Log.e("salesproblem","********************reponse"+finalResponse);
                    lists.clear();
                    for (int i=0; i<finalResponse.length(); i++) {
                        JSONObject singleObject = finalResponse.getJSONObject(i);
                        String Code = singleObject.getString("Code");
                        String StoreName = singleObject.getString("StoreName");
                        String LastYear = singleObject.getString("LastYear");
                        String lym = singleObject.getString("lym");
                        String cym = singleObject.getString("cym");
                        String diff = singleObject.getString("diff");

                        DecreasingSalesModel model = new DecreasingSalesModel(
                                Code,StoreName,LastYear,lym,cym,diff
                        );

                        lists.add(model);
                        decreasingSalesAdapter = new DecreasingSalesAdapter(CustomerNameActivity.this,lists);
                        recyclerViews.setAdapter(decreasingSalesAdapter);
                        decreasingSalesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }
                }catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CustomerNameActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("salesproblem","******************** except"+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CustomerNameActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("salesproblem","******************** on Failure"+e.getMessage());
            }
        });
    }

    private void initUI() {


        recyclerViews = findViewById(R.id.recyclerViewProblematicCustomer);
        recyclerViews.setLayoutManager(new LinearLayoutManager(this));

        recyclerView = findViewById(R.id.customerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        search = findViewById(R.id.searchHere);
        progressBar = findViewById(R.id.progressbar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadData();
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerNameActivity.this,HomeScreen.class);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            //DELETE BEFORE GOING BACK
            Intent i = new Intent(CustomerNameActivity.this,HomeScreen.class);
            startActivity(i);

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadData() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IPloaddd","*******"+globalVariable.getIP());
        Networking networking = new Networking(CustomerNameActivity.this,globalVariable.getIP());
        networking.getCustomerList(new CustomerListInterface() {
            @Override
            public void customerList(List<CustomerModel> list) {
                if (!list.isEmpty()) {
                    adapter = new CustomerAdapter(CustomerNameActivity.this, list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CustomerNameActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(String message) {
                Toast.makeText(CustomerNameActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        },globalVariable.getUserId());
    }
}