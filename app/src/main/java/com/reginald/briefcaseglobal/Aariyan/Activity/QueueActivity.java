package com.reginald.briefcaseglobal.Aariyan.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reginald.briefcaseglobal.Aariyan.Adapter.QueueAdapter;
import com.reginald.briefcaseglobal.Aariyan.Common.Constant;
import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.HeadersInterface;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Interface.SuccessInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;
import com.reginald.briefcaseglobal.Aariyan.Model.SignatureModel;
import com.reginald.briefcaseglobal.Aariyan.Networking.PostSignature;
import com.reginald.briefcaseglobal.Aariyan.Networking.PostingToServer;
import com.reginald.briefcaseglobal.Aariyan.Networking.SinglePostingFromAdapter;
import com.reginald.briefcaseglobal.CustomersActivity;
import com.reginald.briefcaseglobal.Interface.CurrentLocation;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QueueActivity extends AppCompatActivity implements HeadersInterface {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private TextView upToDate;
    private RecyclerView recyclerView;
    private DatabaseAdapter databaseAdapter;

    private QueueAdapter adapter;

    RestApis restApis;
    private String tId = "";

    FusedLocationProviderClient client;

    SharedPreferences sharedPreferences;

    //Dialog TextView:
    private TextView signatureTextView, dealTextView, title, missingFeedback;
    private TextView postBtn, closeBtn, deleteBtn;

    private ProgressBar progressBar, listProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        sharedPreferences = getSharedPreferences("IP_FILE", MODE_PRIVATE);

        client = LocationServices.getFusedLocationProviderClient(this);
        databaseAdapter = new DatabaseAdapter(this);

        initUI();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showAlert();
//            }
//        },1000);

        findViewById(R.id.backUpDatabase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    exportDBToInternalStorage();
                } else {
                    requestPermission();
                }
            }
        });
    }

    private void showAlert(HeadersModel model, int adapterPosition, double latitude, double longitude) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.custom_progressbar_for_posting, null));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        signatureTextView = dialog.findViewById(R.id.signatureDesign);
        dealTextView = dialog.findViewById(R.id.DealsDesign);
        title = dialog.findViewById(R.id.title);
        missingFeedback = dialog.findViewById(R.id.feedbackTextView);

        progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        postBtn = dialog.findViewById(R.id.postBtn);
        deleteBtn = dialog.findViewById(R.id.deleteFromDatabase);
        closeBtn = dialog.findViewById(R.id.cancelDialog);

        //Checking and View is viewable or not:
        // by default everything is not visible:
        dealTextView.setVisibility(View.GONE);
        signatureTextView.setVisibility(View.GONE);
        missingFeedback.setVisibility(View.GONE);

        postBtn.setVisibility(View.GONE);
        deleteBtn.setVisibility(View.GONE);

        //Checking the data:
        List<LinesModel> listOfLines = databaseAdapter.getLinesByTransactionId(model.getTransactionId());
        List<SignatureModel> signatureModel = databaseAdapter.getSignatureByTransactionId(model.getTransactionId());

        if (listOfLines.size() > 0) { // deals found!
            dealTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.tik_with_green_background, 0);
            missingFeedback.append("Deals Found! \n");
        } else {
            dealTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.close_with_red_background, 0);
            missingFeedback.append("Lines Not Found! \n");
        }
        dealTextView.setVisibility(View.VISIBLE);

        //Signature:
        if (signatureModel.size() > 0) {
            dealTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.tik_with_green_background, 0);
            missingFeedback.append("Signature Found! \n");
        } else {
            dealTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.close_with_red_background, 0);
            missingFeedback.append("Signature Not Found! \n");
        }
        signatureTextView.setVisibility(View.VISIBLE);
        missingFeedback.setVisibility(View.VISIBLE);

        postBtn.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.VISIBLE);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDeals_N_signature(model.getTransactionId(), adapterPosition);
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doThePosting(model, adapterPosition, latitude, longitude);
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }

    private void doThePosting(HeadersModel model, int adapterPosition, double latitude, double longitude) {
        postingSignature(model.getTransactionId(), new SuccessInterface() {
            @Override
            public void onSuccess(String successMessage) {
                new SinglePostingFromAdapter(databaseAdapter, restApis, latitude, longitude)
                        .tryDirectPostingToServer(new SuccessInterface() {
                            @Override
                            public void onSuccess(String successMessage) {
                                Toast.makeText(QueueActivity.this, "" + successMessage, Toast.LENGTH_SHORT).show();
                                adapter.notifyItemRemoved(adapterPosition);
                                adapter.notifyDataSetChanged();
                                loadData();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(QueueActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }, model);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(QueueActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDeals_N_signature(String transactionId, int adapterPosition) {
        long deletedSignature = databaseAdapter.deleteSignature(transactionId);
        long deletedLines = databaseAdapter.deleteLines(transactionId);
        long deletedDeals = databaseAdapter.deleteDeals(transactionId);

        adapter.notifyItemRemoved(adapterPosition);
        adapter.notifyDataSetChanged();
        loadData();
        Toast.makeText(this, "Transaction Deleted!", Toast.LENGTH_SHORT).show();
    }

    private void initUI() {
        listProgressbar = findViewById(R.id.pbars);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.queue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.backUpData:
                //startActivity(new Intent(QueueActivity.this, QueueActivity.class));
                if (checkPermission()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(QueueActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("It will take a back up of Local database, FOLDER: DOWNLOAD/BRIEF_CASE_BACKUP");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    exportDBToInternalStorage();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    requestPermission();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportDBToInternalStorage() {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/BRIEF_CASE_BACKUP");
        if (!path.exists()) {
            path.mkdir();
        }
        String fileName = "QUEUE_NOT_UPLOADED_N_" + System.currentTimeMillis();
//        File file = new File(path, fileName);
//        String filePath = file.getAbsolutePath();
       // File sd = Environment.getExternalStorageDirectory();
        if (path.canWrite()) {
            //final File[] databases = new File(getFilesDir().getParentFile().getPath() + "/databases").listFiles();
            //final File[] databases = new File(getFilesDir().getParentFile().getPath() + "/databases").listFiles();
            final File databases = new File(getFilesDir().getParentFile().getPath() + "/databases/BRIEFCASE_DB.db");
//            for (File file : databases) {
//                Log.d("CHECK_DB_LIST", "exportDBToInternalStorage: "+file);
//            }

            //for (File databaseFile: databases) {
                final String backupFilename = databases.getName() + "-QUEUE_NOT_UPLOADED_N_" +
                        "-" + System.currentTimeMillis() + ".db";
                File backupFile = new File(path, backupFilename);
                FileChannel inputChannel = null;
                FileChannel outputChannel = null;

                try {
                    Log.d(LOG_TAG, "Backing up: " + databases + " to file: " + backupFile);
                    inputChannel = new FileInputStream(databases.getAbsolutePath()).getChannel();
                    outputChannel = new FileOutputStream(backupFile).getChannel();
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputChannel != null) {
                        try {
                            inputChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (outputChannel != null) {
                        try {
                            outputChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
           // }
        } else {
            Log.w(LOG_TAG, "Can't write to sdcard");
        }


    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    exportDBToInternalStorage();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        // perform action when allow permission success
                        exportDBToInternalStorage();
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String url = sharedPreferences.getString("url", "");
        restApis = ApiClient.getClient(url).create(RestApis.class);

        loadData();

    }

    private void loadData() {
        listProgressbar.setVisibility(View.VISIBLE);
        List<HeadersModel> listOfHeaders = databaseAdapter.getHeadersByUploaded();
        if (listOfHeaders.size() == 0 || listOfHeaders.isEmpty()) {
            upToDate.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            listProgressbar.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            upToDate.setVisibility(View.GONE);

            //Show data:
            adapter = new QueueAdapter(this, listOfHeaders, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listProgressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void singleHeaderForPosting(HeadersModel model, int position) {

        //get Location:
        if (!Constant.isInternetConnected(this)) {
            Toast.makeText(this, "Sorry, No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        getCurrentLocation(new CurrentLocation() {
            @Override
            public void getLocation(double latitude, double longitude) {
                showAlert(model, position, latitude, longitude);
            }
        });
    }

    boolean isPosted = false;

    private void postingSignature(String transactionId, SuccessInterface successInterface) {
        List<SignatureModel> list = databaseAdapter.getSignatureByTransactionId(transactionId);
        Log.d("ERROR_CHECKING", "postingSignature: " + transactionId + " Called");
        if (list.size() == 0) {
            Toast.makeText(this, "No Signature found for this Transaction", Toast.LENGTH_SHORT).show();
            return;
        }
        new PostSignature(restApis).postSignatureToServer(new SuccessInterface() {
            @Override
            public void onSuccess(String successMessage) {
                Log.d("ERROR_CHECKING", "onSuccess: Called 1");
                Toast.makeText(QueueActivity.this, "" + successMessage, Toast.LENGTH_SHORT).show();
                successInterface.onSuccess("" + successMessage);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("ERROR_CHECKING", "onError: called 2" + errorMessage);
                Toast.makeText(QueueActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                successInterface.onError("" + errorMessage);
            }
        }, list);

    }

//    public boolean isInternetAvailable() {
//        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com");
//            //You can replace it with your name
//            return !ipAddr.equals("");
//
//        } catch (Exception e) {
//            return false;
//        }
//    }

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
                            Log.d("ERROR_CHECKING", "Exception: " + e.getMessage());
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