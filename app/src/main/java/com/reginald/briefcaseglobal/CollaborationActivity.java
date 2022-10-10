package com.reginald.briefcaseglobal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reginald.briefcaseglobal.Constant.Constant;
import com.reginald.briefcaseglobal.Interface.MessageListInterface;
import com.reginald.briefcaseglobal.Model.MessageModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.Network.Networking;

import java.util.List;

public class CollaborationActivity extends AppCompatActivity {

    private TextView sales,warehouse,purchasing,dispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        initUI();

        getMessage();
    }

    private void getMessage() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());

        //APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Networking networking = new Networking(CollaborationActivity.this,globalVariable.getIP());
        networking.getMessageList(new MessageListInterface() {
            @Override
            public void messageList(List<MessageModel> list) {
                if (!list.isEmpty()) {
                    Constant.listOfMessage = list;
                }
            }

            @Override
            public void error(String error) {
                Toast.makeText(CollaborationActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {

        sales = findViewById(R.id.salesTextView);
        warehouse = findViewById(R.id.warehouseTextView);
        purchasing = findViewById(R.id.purchasingTextView);
        dispatch = findViewById(R.id.dispatchTextView);

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollaborationActivity.this,MessageActivity.class);
                intent.putExtra("type","Sales");
                startActivity(intent);
            }
        });

        warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollaborationActivity.this,MessageActivity.class);
                intent.putExtra("type","Warehouse");
                startActivity(intent);
            }
        });

        purchasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollaborationActivity.this,MessageActivity.class);
                intent.putExtra("type","Purchasing");
                startActivity(intent);
            }
        });

        dispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollaborationActivity.this,MessageActivity.class);
                intent.putExtra("type","Dispatch");
                startActivity(intent);
            }
        });


        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}