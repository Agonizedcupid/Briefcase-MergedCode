package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CustomerInfo extends AppCompatActivity {

    ImageView imageviewpricelists,imagedeals,iamgeassets,imagevisists;
    String roles,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        imageviewpricelists = (ImageView) findViewById(R.id.imageviewpricelists);
        imagedeals = (ImageView) findViewById(R.id.imagedeals);
        imagevisists = (ImageView) findViewById(R.id.imagevisists);
        iamgeassets = (ImageView) findViewById(R.id.iamgeassets);

        imageviewpricelists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imagedeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerInfo.this,CustomerCentalActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("method","deals");
                startActivity(i);
            }
        });
        imagevisists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CustomerInfo.this,CustomerCentalActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("method","visits");
                startActivity(i);

            }
        });
        iamgeassets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
        imageviewpricelists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerInfo.this,PriceList.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("method","visits");
                startActivity(i);


            }
        });

    }
}