package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.OneTimeWorkRequest;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    ImageView imageViewOrders,imageViewCustomerInfo,imageViewCPanel,iamgeViewMemo;
    String roles,userID;
    ProgressDialog progressDoalog;
    private CardView orderAndLoadedDeals;
    private CardView salesTargets;
    private CardView stockSheet;
    private CardView visitMemos;
    private CardView collaborating;
    private CardView cPanel;
    private CardView createCustomers;

    private View bottomSheet;
    private BottomSheetBehavior behavior;

    private EditText customerName, customerCode;
    private Button getHistoryBtn;

    private EditText getSalesEditText;
    private Button getSalesBtn;

    private ImageView closeBottomSheet;

    private LinearLayout orderHistoryLayout,salesLayout;
    private CardView createReport;
    private CardView dailySchedule;
    private SQLiteDatabase db;
    private OneTimeWorkRequest mWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_home_screen);
        setContentView(R.layout.activity_landing_new);

        mWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        DriverPermission();
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IPGlobal","***************"+globalVariable.getIP());
        orderHistoryLayout = findViewById(R.id.orderHistoryLayout);
        salesLayout = findViewById(R.id.salesLayout);

        orderAndLoadedDeals = findViewById(R.id.ordersLoadedDeals);
        salesTargets = findViewById(R.id.salesTargets);
        stockSheet = findViewById(R.id.stockSheet);
        visitMemos = findViewById(R.id.visitMemos);
        collaborating = findViewById(R.id.collaborating);
        cPanel = findViewById(R.id.cPanel);
        createReport = findViewById(R.id.createReport);
        dailySchedule = findViewById(R.id.dailySchedule);

        closeBottomSheet = findViewById(R.id.closeBottomSheet);

        if(CheckIfMerchie()> 0){
            salesTargets.setVisibility(View.INVISIBLE);
            visitMemos.setVisibility(View.INVISIBLE);
            collaborating.setVisibility(View.INVISIBLE);
            createReport.setVisibility(View.INVISIBLE);
            stockSheet.setVisibility(View.INVISIBLE);
            dailySchedule.setVisibility(View.INVISIBLE);
        }

      //  imageViewOrders = (ImageView) findViewById(R.id.imageViewOrders);
      //  imageViewCustomerInfo = (ImageView) findViewById(R.id.imageViewCustomerInfo);
        //imageViewCPanel = (ImageView) findViewById(R.id.imageViewCPanel);
        //iamgeViewMemo = (ImageView) findViewById(R.id.iamgeViewMemo);

        createReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this, ButtonActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        roles = intent.getStringExtra("roles");
        Log.e("Loggedin","********************userID***********"+userID);

       /* imageViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startProgress("Wait");
                Intent i = new Intent(HomeScreen.this,CustomersActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
            }
        });
        imageViewCustomerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this,CustomerInfo.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
            }
        });
        imageViewCPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this,ControlPanel.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                i.putExtra("method","cpanel");
                startActivity(i);

            }
        });
        iamgeViewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        cPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setMessage("You are going to the Control Panel Page, you will be logged out!")
                        .setCancelable(false)
                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(HomeScreen.this,ControlPanel.class);
                                i.putExtra("userID",userID);
                                i.putExtra("roles",roles);
                                i.putExtra("method","cpanel");
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


            }
        });
        closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                orderHistoryLayout.setVisibility(View.GONE);
                salesLayout.setVisibility(View.GONE);
            }
        });

        bottomSheet = findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        customerName = findViewById(R.id.customerNameEdtText);
        customerCode = findViewById(R.id.customerCodeEdtText);
        getHistoryBtn = findViewById(R.id.orderHistoryBtn);


        getSalesEditText = findViewById(R.id.getSalesEdtText);
        Log.e("getSalesEditText","******************************"+getSalesEditText);
        Log.e("userID","******************************"+userID);
        getSalesEditText.setText(userID);
        getSalesBtn = findViewById(R.id.getSalesBtn);

        orderAndLoadedDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreen.this,CustomersActivity.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
            }
        });

        salesTargets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("getSalesEditText","******************************"+getSalesEditText.getText().toString());
                Log.e("userID","******************************"+userID);
                //getSalesEditText.setText(userID);
                //orderHistoryLayout.setVisibility(View.GONE);
                //salesLayout.setVisibility(View.VISIBLE);
               // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Intent intent = new Intent(HomeScreen.this, Sales.class);
                intent.putExtra("code", userID);
                startActivity(intent);
            }
        });

        getSalesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (TextUtils.isEmpty(getSalesEditText.getText().toString().trim())) {
                    getSalesEditText.setError("Pleas enter the code!");
                    getSalesEditText.requestFocus();
                    return;
                }

                Intent intent = new Intent(HomeScreen.this, Sales.class);
                intent.putExtra("code", getSalesEditText.getText().toString().trim());
                startActivity(intent);
            }
        });

        getHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intent = new Intent(HomeScreen.this, OrderActivity.class);
                intent.putExtra("name", customerName.getText().toString());
                intent.putExtra("code", customerCode.getText().toString());
                startActivity(intent);
            }
        });

        stockSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, StockSheetActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        if(checkvisitslog()>0){
            visitMemos.setBackgroundColor(Color.rgb(255,0,0));
        }

        visitMemos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, CustomerNameActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
//VisitQueue
        visitMemos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent i = new Intent(HomeScreen.this,VisitQueue.class);
                i.putExtra("userID",userID);
                i.putExtra("roles",roles);
                startActivity(i);
                return false;
            }
        });
        collaborating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, CollaborationActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        dailySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, DailySchedule.class));
            }
        });

    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DriverService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
            builder.setMessage("You are going back to the Login Screen, are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent myIntent = new Intent(HomeScreen.this,LoginActivity.class);
                            startActivity(myIntent);
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
    private void DriverPermission() {
        Log.e("Testse","Tes--------------------");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/,
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        if (isMyServiceRunning()) {
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(HomeScreen.this, DriverService.class));
                        } else {
                            startService(new Intent(HomeScreen.this, DriverService.class));
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(HomeScreen.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Doing some work for you.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
    public int CheckIfMerchie(){
        Cursor cursor2 = db.rawQuery("SELECT *  from BriefcaseRoles where RoleDescription ='Merchie'", null);

        return cursor2.getCount();
    }
    public int checkvisitslog(){
        Cursor cursor2 = db.rawQuery("SELECT *  from Visits", null);

        return cursor2.getCount();
    }

}