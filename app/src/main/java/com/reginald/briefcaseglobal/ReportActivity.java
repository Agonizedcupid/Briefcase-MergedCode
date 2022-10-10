package com.reginald.briefcaseglobal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Adapter.DecreasingSalesAdapter;
import com.reginald.briefcaseglobal.Model.DecreasingSalesModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<DecreasingSalesModel> list = new ArrayList<>();
    private DecreasingSalesAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        initUI();

        loadData();
    }

    private void loadData() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getCustomerDecreasingSales(globalVariable.getUserId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONArray finalResponse = new JSONArray(response.body().string());
                    list.clear();
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
                        list.add(model);
                        adapter = new DecreasingSalesAdapter(ReportActivity.this,list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }
                }catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ReportActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReportActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressbar);
    }
}