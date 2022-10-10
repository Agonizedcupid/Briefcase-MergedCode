package com.reginald.briefcaseglobal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reginald.briefcaseglobal.Adapter.ProblematicAdapter;
import com.reginald.briefcaseglobal.Model.ProblematicModel;
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

public class ProblematicItemActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView storeName,storeCode;
    private String sName,sCode;
    private String userId = "0";
    private ProblematicAdapter adapter;
    private List<ProblematicModel> list = new ArrayList<>();

    private ProgressBar progressBar;
    Button logVisit;
    Button btnplanv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problematic_item);

        initUI();

        if (getIntent() != null) {
            sName = getIntent().getStringExtra("name");
            sCode = getIntent().getStringExtra("code");

            storeName.setText("Store: "+sName);
            storeCode.setText("Code: "+sCode);

            loadData(sCode);
        }
    }

    private void initUI() {

        progressBar = findViewById(R.id.progressbar);
        storeName = findViewById(R.id.storeNames);
        storeCode = findViewById(R.id.storeCodes);
        recyclerView = findViewById(R.id.recyclerView);
        logVisit = findViewById(R.id.logVisits);
        btnplanv = findViewById(R.id.btnplanv);
        logVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(ProblematicItemActivity.this,LogVisit.class);

                i.putExtra("name",sName);
                i.putExtra("code",sCode);
                startActivity(i);
            }
        });
        btnplanv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(ProblematicItemActivity.this,CreateMemo.class);

                i.putExtra("name",sName);
                i.putExtra("code",sCode);
                startActivity(i);
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProblematicItemActivity.this,CustomerNameActivity.class);
                startActivity(i);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData(String name) {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        userId= globalVariable.getUserId();
        Call<ResponseBody> call = apiService.getProblematicItem(userId,name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONArray array = new JSONArray(response.body().string());
                    list.clear();
                    Log.e("respo","Items *******"+array);
                    for (int i=0; i<array.length(); i++) {
                        JSONObject single = array.getJSONObject(i);
                        String CustomerPastelCode = single.getString("CustomerPastelCode");
                        String StoreName = single.getString("StoreName");
                        String PastelDescription = single.getString("PastelDescription");
                        String PastelCode = single.getString("PastelCode");
                        String strSalesName = single.getString("strSalesName");
                        String decLastYear = single.getString("decLastYear");
                        String decLastMonth = single.getString("decLastMonth");
                        String decPriorMonth = single.getString("decPriorMonth");
                        String decDifference = single.getString("decDifference");
                        String wasNotActive = single.getString("wasNotActive");

                        ProblematicModel model = new ProblematicModel(
                                CustomerPastelCode,
                                StoreName,
                                PastelDescription,
                                PastelCode,
                                strSalesName,
                                decLastYear,
                                decLastMonth,
                                decPriorMonth,
                                decDifference,
                                wasNotActive
                        );

                        list.add(model);
                    }
                    adapter = new ProblematicAdapter(ProblematicItemActivity.this,list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }catch (Exception e) {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(ProblematicItemActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(ProblematicItemActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}