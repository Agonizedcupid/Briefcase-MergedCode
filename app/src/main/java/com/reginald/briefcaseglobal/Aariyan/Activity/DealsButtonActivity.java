package com.reginald.briefcaseglobal.Aariyan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.reginald.briefcaseglobal.Aariyan.Fragment.CreateDealsFragment;
import com.reginald.briefcaseglobal.Aariyan.Fragment.UpdateDealsFragment;
import com.reginald.briefcaseglobal.R;

public class DealsButtonActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_button);


        intUI();
    }


    private void intUI() {
//        findViewById(R.id.createDeals).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        findViewById(R.id.updateDeals).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        toolbarTitle = findViewById(R.id.toolbarTitle);
        setFragment(new CreateDealsFragment());
        toolbarTitle.setText("Create new customer deals for : ");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.createDealsMenu:
                        setFragment(new CreateDealsFragment());
                        toolbarTitle.setText("Create new customer deals for : ");
                        return true;
                    case R.id.updateDealsMenu:
                        setFragment(new UpdateDealsFragment());
                        toolbarTitle.setText("Update deals for existing customer: ");
                        return true;
                }
                return false;
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}