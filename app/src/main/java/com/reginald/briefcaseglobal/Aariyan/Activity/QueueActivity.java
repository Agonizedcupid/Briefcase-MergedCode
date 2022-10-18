package com.reginald.briefcaseglobal.Aariyan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reginald.briefcaseglobal.Aariyan.Adapter.QueueAdapter;
import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.HeadersInterface;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Interface.SuccessInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Networking.PostingToServer;
import com.reginald.briefcaseglobal.Aariyan.Networking.SinglePostingFromAdapter;
import com.reginald.briefcaseglobal.CustomersActivity;
import com.reginald.briefcaseglobal.Interface.CurrentLocation;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.R;

import java.util.List;
import java.util.Locale;

public class QueueActivity extends AppCompatActivity implements HeadersInterface {

    private TextView upToDate;
    private RecyclerView recyclerView;
    private DatabaseAdapter databaseAdapter;

    private QueueAdapter adapter;

    RestApis restApis;
    private String tId = "";

    FusedLocationProviderClient client;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        sharedPreferences = getSharedPreferences("IP_FILE", MODE_PRIVATE);

        client = LocationServices.getFusedLocationProviderClient(this);
        databaseAdapter = new DatabaseAdapter(this);

        initUI();
    }

    private void initUI() {
        upToDate = findViewById(R.id.upToDateText);
        recyclerView = findViewById(R.id.rView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String url = sharedPreferences.getString("url", "");
        restApis = ApiClient.getClient(url).create(RestApis.class);

        loadData();

    }

    private void loadData() {
        List<HeadersModel> listOfHeaders = databaseAdapter.getHeadersByUploaded();
        if (listOfHeaders.size() == 0 || listOfHeaders.isEmpty()) {
            upToDate.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            upToDate.setVisibility(View.GONE);

            //Show data:
            adapter = new QueueAdapter(this, listOfHeaders, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void singleHeaderForPosting(HeadersModel model, int position) {

        //get Location:
        getCurrentLocation(new CurrentLocation() {
            @Override
            public void getLocation(double latitude, double longitude) {
                Log.d("LOCATION_CHECKING", "getLocation: " + latitude + " - " + longitude);
                //check data is also posted to the server or not:
                new SinglePostingFromAdapter(databaseAdapter, restApis, latitude, longitude)
                        .tryDirectPostingToServer(new SuccessInterface() {
                            @Override
                            public void onSuccess(String successMessage) {
                                Toast.makeText(QueueActivity.this, "" + successMessage, Toast.LENGTH_SHORT).show();
                                adapter.notifyItemRemoved(position);
                                adapter.notifyDataSetChanged();
                                loadData();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(QueueActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }, model);
            }
        });
    }

    private void getCurrentLocation(CurrentLocation currentLocation) {
        //Check the location permission:
        if (ActivityCompat.checkSelfPermission(QueueActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(QueueActivity.this, Locale.getDefault());
                            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            currentLocation.getLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } else {
            //request for the location access
            ActivityCompat.requestPermissions(QueueActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }
}