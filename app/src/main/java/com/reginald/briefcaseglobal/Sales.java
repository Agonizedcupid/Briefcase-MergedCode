package com.reginald.briefcaseglobal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sales extends AppCompatActivity {

    private String code;

    public TextView userName;
    public TextView februaryTarget;
    public TextView febraurySales;
    public TextView febrauaryPercentage;
    public TextView januaryTarget;
    public TextView januarySales;
    public TextView januaryPercentage;
    public TextView decemberTarget;
    public TextView decemberSales;
    public TextView decemberPercentage;
    public TextView novemberTarget;
    public TextView novemberSales;
    public TextView novemberPercentage;
    public TextView octoberTarget;
    public TextView octoberSales;
    public TextView octoberPercentage;
    public TextView septemberTarget;
    public TextView septemberSales;
    public TextView septemberPercentage;
    public TextView augustTarget;
    public TextView augustSales;
    public TextView augustPercentage;
    public TextView julyTarget;
    public TextView julySales;
    public TextView julyPercentage;
    public TextView juneTarget;
    public TextView juneSales;
    public TextView junePercentage;
    public TextView mayTarget;
    public TextView maySales;
    public TextView mayPercentage;
    public TextView aprilTarget;
    public TextView aprilSales;
    public TextView aprilPercentage;
    public TextView marchTarget;
    public TextView marchSales;
    public TextView marchPercentage;

    private ProgressBar progressBar;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        //instantiate UI:
        initUI();

        if (getIntent() != null) {
            code = getIntent().getStringExtra("code");
            Log.e("Code","________________"+code);
            progressBar.setVisibility(View.VISIBLE);
            loadSales(code);
        }
    }

    //Instantiate UI:
    private void initUI() {
        progressBar = findViewById(R.id.progressbar);
        scrollView = findViewById(R.id.scrollView);
        userName = findViewById(R.id.userName);
        februaryTarget = findViewById(R.id.februaryTargetValue);
        febraurySales = findViewById(R.id.februarySalesValue);
        febrauaryPercentage = findViewById(R.id.februaryPercentageValue);
        januaryTarget = findViewById(R.id.januaryTargetValue);
        januarySales = findViewById(R.id.januarySalesValue);
        januaryPercentage = findViewById(R.id.januaryPercentageValue);
        decemberTarget = findViewById(R.id.decemberTargetValue);
        decemberSales = findViewById(R.id.decemberSalesValue);
        decemberPercentage = findViewById(R.id.decemberPercentageValue);
        novemberTarget = findViewById(R.id.novemberTargetValue);
        novemberSales = findViewById(R.id.novemberSalesValue);
        novemberPercentage = findViewById(R.id.novemberPercentageValue);
        octoberTarget = findViewById(R.id.octoberTargetValue);
        octoberSales = findViewById(R.id.octoberSalesValue);
        octoberPercentage = findViewById(R.id.octoberPercentageValue);
        septemberTarget = findViewById(R.id.septemberTargetValue);
        septemberSales = findViewById(R.id.septemberSalesValue);
        septemberPercentage = findViewById(R.id.septemberPercentageValue);
        augustTarget = findViewById(R.id.augustTargetValue);
        augustSales = findViewById(R.id.augustSalesValue);
        augustPercentage = findViewById(R.id.augustPercentageValue);
        julyTarget = findViewById(R.id.julyTargetValue);
        julySales = findViewById(R.id.julySalesValue);
        julyPercentage = findViewById(R.id.julyPercentageValue);
        juneTarget = findViewById(R.id.juneTargetValue);
        juneSales = findViewById(R.id.juneSalesValue);
        junePercentage = findViewById(R.id.junePercentageValue);
        mayTarget = findViewById(R.id.mayTargetValue);
        maySales = findViewById(R.id.maySalesValue);
        mayPercentage = findViewById(R.id.mayPercentageValue);
        aprilTarget = findViewById(R.id.aprilTargetValue);
        aprilSales = findViewById(R.id.aprilSalesValue);
        aprilPercentage = findViewById(R.id.aprilPercentageValue);
        marchTarget = findViewById(R.id.marchTargetValue);
        marchSales = findViewById(R.id.marchSalesValue);
        marchPercentage = findViewById(R.id.marchPercentageValue);
    }

    private void loadSales(String code) {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());

        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getSales(code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONArray finalResponse = new JSONArray(response.body().string());
                    for (int i = 0; i < finalResponse.length(); i++) {
                        JSONObject singleObject = finalResponse.getJSONObject(i);
                        String strSalesName = singleObject.getString("strSalesName");
                        userName.setText(strSalesName);
                        String FebruaryTarget = singleObject.getString("FebruaryTarget");
                        februaryTarget.setText(FebruaryTarget);
                        String FebraurySales = singleObject.getString("FebraurySales");
                        febraurySales.setText(FebraurySales);
                        String FebrauaryPercentage = singleObject.getString("FebrauaryPercentage");
                        febrauaryPercentage.setText(FebrauaryPercentage);
                        String JanuaryTarget = singleObject.getString("JanuaryTarget");
                        januaryTarget.setText(JanuaryTarget);
                        String JanuarySales = singleObject.getString("JanuarySales");
                        januarySales.setText(JanuarySales);
                        String JanuaryPercentage = singleObject.getString("JanuaryPercentage");
                        januaryPercentage.setText(JanuaryPercentage);
                        String DecemberTarget = singleObject.getString("DecemberTarget");
                        decemberTarget.setText(DecemberTarget);
                        String DecemberSales = singleObject.getString("DecemberSales");
                        decemberSales.setText(DecemberSales);
                        String DecemberPercentage = singleObject.getString("DecemberPercentage");
                        decemberPercentage.setText(DecemberPercentage);
                        String NovemberTarget = singleObject.getString("NovemberTarget");
                        novemberTarget.setText(NovemberTarget);
                        String NovemberSales = singleObject.getString("NovemberSales");
                        novemberSales.setText(NovemberSales);
                        String NovemberPercentage = singleObject.getString("NovemberPercentage");
                        novemberPercentage.setText(NovemberPercentage);
                        String OctoberTarget = singleObject.getString("OctoberTarget");
                        octoberTarget.setText(OctoberTarget);
                        String OctoberSales = singleObject.getString("OctoberSales");
                        octoberSales.setText(OctoberSales);
                        String OctoberPercentage = singleObject.getString("OctoberPercentage");
                        octoberPercentage.setText(OctoberPercentage);
                        String SeptemberTarget = singleObject.getString("SeptemberTarget");
                        septemberTarget.setText(SeptemberTarget);
                        String SeptemberSales = singleObject.getString("SeptemberSales");
                        septemberSales.setText(SeptemberSales);
                        String SeptemberPercentage = singleObject.getString("SeptemberPercentage");
                        septemberPercentage.setText(SeptemberPercentage);
                        String AugustTarget = singleObject.getString("AugustTarget");
                        augustTarget.setText(AugustTarget);
                        String AugustSales = singleObject.getString("AugustSales");
                        augustSales.setText(AugustSales);
                        String AugustPercentage = singleObject.getString("AugustPercentage");
                        augustPercentage.setText(AugustPercentage);
                        String JulyTarget = singleObject.getString("JulyTarget");
                        julyTarget.setText(JulyTarget);
                        String JulySales = singleObject.getString("JulySales");
                        julySales.setText(JulySales);
                        String JulyPercentage = singleObject.getString("JulyPercentage");
                        julyPercentage.setText(JulyPercentage);
                        String JuneTarget = singleObject.getString("JuneTarget");
                        juneTarget.setText(JuneTarget);
                        String JuneSales = singleObject.getString("JuneSales");
                        juneSales.setText(JuneSales);
                        String JunePercentage = singleObject.getString("JunePercentage");
                        junePercentage.setText(JunePercentage);
                        String MayTarget = singleObject.getString("MayTarget");
                        mayTarget.setText(MayTarget);
                        String MaySales = singleObject.getString("MaySales");
                        maySales.setText(MaySales);
                        String MayPercentage = singleObject.getString("MayPercentage");
                        mayPercentage.setText(MayPercentage);
                        String AprilTarget = singleObject.getString("AprilTarget");
                        aprilTarget.setText(AprilTarget);
                        String AprilSales = singleObject.getString("AprilSales");
                        aprilSales.setText(AprilSales);
                        String AprilPercentage = singleObject.getString("AprilPercentage");
                        aprilPercentage.setText(AprilPercentage);
                        String MarchTarget = singleObject.getString("MarchTarget");
                        marchTarget.setText(MarchTarget);
                        String MarchSales = singleObject.getString("MarchSales");
                        marchSales.setText(MarchSales);
                        String MarchPercentage = singleObject.getString("MarchPercentage");
                        marchPercentage.setText(MarchPercentage);
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    e.getMessage();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Sales.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Sales.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}