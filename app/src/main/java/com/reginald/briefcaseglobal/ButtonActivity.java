package com.reginald.briefcaseglobal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ButtonActivity extends AppCompatActivity {

    String roles,userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        if (getIntent() != null) {
            roles = getIntent().getStringExtra("roles");
            userID = getIntent().getStringExtra("userID");

        }
        findViewById(R.id.customerStoppedBuying).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ButtonActivity.this,ReportActivity.class));
            }
        });

        findViewById(R.id.problematicitems).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ButtonActivity.this,ProblematicItemsActivity.class));
            }
        });
        findViewById(R.id.btnpricedeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ButtonActivity.this,PriceList.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ButtonActivity.this);
            builder.setMessage("You are going back to the Home Screen, are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(ButtonActivity.this,HomeScreen.class);
                            i.putExtra("userID",userID);
                            i.putExtra("roles",roles);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}