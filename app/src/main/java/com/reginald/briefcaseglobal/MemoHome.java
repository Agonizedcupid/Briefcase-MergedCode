package com.reginald.briefcaseglobal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MemoHome extends AppCompatActivity {

    private CardView logVisit,createMemo,viewMemo,viewVisit;
    private TextView customerName,customerCode;

    Button btnproblematicItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_home);

        initUI();

        if (getIntent() != null) {
            customerName.setText(getIntent().getStringExtra("name"));
            customerCode.setText(getIntent().getStringExtra("code"));
        }
    }

    private void initUI() {

        try{

        logVisit = findViewById(R.id.logVisit);
        createMemo = findViewById(R.id.createMemo);
        viewMemo = findViewById(R.id.viewMemo);
        viewVisit = findViewById(R.id.viewVisit);

        customerName = findViewById(R.id.cName);
        customerCode = findViewById(R.id.cCode);
        btnproblematicItems = findViewById(R.id.btnproblematicItems);

        btnproblematicItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemoHome.this,OrderPatternItemsActivity.class);
                i.putExtra("customercode",customerCode.getText().toString());
                i.putExtra("customerName",customerName.getText().toString());
                startActivity(i);
            }
        });

        logVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MemoHome.this,LogVisit.class)
                        .putExtra("name",customerName.getText().toString())
                        .putExtra("code",customerCode.getText().toString())
                );
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        createMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MemoHome.this,CreateMemo.class)
                        .putExtra("name",customerName.getText().toString())
                        .putExtra("code",customerCode.getText().toString())
                );
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        viewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MemoHome.this,ViewMemoVisit.class)
                        .putExtra("type","Memo")
                        .putExtra("name",customerName.getText().toString())
                        .putExtra("code",customerCode.getText().toString())
                );
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        viewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MemoHome.this,ViewMemoVisit.class)
                        .putExtra("type","Visit")
                        .putExtra("name",customerName.getText().toString())
                        .putExtra("code",customerCode.getText().toString())
                );
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemoHome.this,CustomerNameActivity.class);
                startActivity(i);
            }
        });
        } catch (Exception e) {
            Toast.makeText(MemoHome.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("errrrrrrr","err***************"+e.getMessage());
            //progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            //DELETE BEFORE GOING BACK
            Intent i = new Intent(MemoHome.this,CustomerNameActivity.class);
            startActivity(i);

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}