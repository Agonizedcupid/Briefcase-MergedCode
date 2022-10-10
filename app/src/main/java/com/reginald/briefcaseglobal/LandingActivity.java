package com.reginald.briefcaseglobal;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;

public class LandingActivity extends AppCompatActivity {
    final static int Conn = 333;
    Button continuebtn;
    Context context = this;
    private SQLiteDatabase db,dbOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        continuebtn =(Button) findViewById(R.id.continuebtn);

        Log.e("paths","**********************"+getApplicationContext().getFilesDir());
/*
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 1);
        }*/
        File direct = new File( "Salesmanbriefcase");

        if (!direct.exists()) {
            direct.mkdirs();
            System.out.println("prepare " + direct.mkdirs());
            Log.e("PATH", ":" +   direct.mkdirs());
        }

       // File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Salesmanbriefcase");

        //db = this.openOrCreateDatabase( file.getPath()+ "/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        //dbOrders = this.openOrCreateDatabase( file.getPath()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
 //Download in here

                Intent i = new Intent(LandingActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                File direct = new File( "Salesmanbriefcase");

                if (!direct.exists()) {
                    direct.mkdirs();
                    System.out.println("prepare " + direct.mkdirs());
                    Log.e("PATH", ":" +   direct.mkdirs());
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}