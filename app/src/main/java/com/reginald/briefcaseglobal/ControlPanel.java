package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ControlPanel extends AppCompatActivity {

    TextView users,stock,stocktext,stock_count,deliveryaddress,usertype,orderpattern,prices,groupspecials,customer,pricelist,downloading_cust_history,downloadoverallspecial;
    private SQLiteDatabase db,dbOrders;
    Button sync;
    final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    String userID,roles,method,custcode,selectedcustomer,IP="",Locations,DownloadPath;
    ProgressDialog progressDoalog,progressDialog;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        clearCache();
       // File file = new File( "Salesmanbriefcase");

        // final String dir =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) +"/";
        db = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+ "/LinxBriefcaseDB.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);


        // final String dir2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/";
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        dbOrders.execSQL("CREATE TABLE IF NOT EXISTS tblSettings  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,Location TEXT NOT NULL DEFAULT  '1',IP TEXT ,DimsIp TEXT )");
        Cursor cursor = dbOrders.rawQuery("SELECT * from tblSettings where DimsIp is not null", null);
        if(cursor.getCount() < 1 && checkLast() > 0){
            Intent i = new Intent(ControlPanel.this,LoginActivity.class);
            startActivity(i);
        }else
        {

            if (cursor.moveToFirst()) {
                do {

                    IP = cursor.getString(cursor.getColumnIndex("IP"));
                    DownloadPath = cursor.getString(cursor.getColumnIndex("DownloadPath"));
                } while (cursor.moveToNext());
            }
            cursor.close();

        }
        if (getIntent() != null) {

            userID = getIntent().getStringExtra("userID");
        }

        users = (TextView)  findViewById(R.id.users_count);

        stocktext = (TextView)  findViewById(R.id.stocktext_count);
        deliveryaddress = (TextView)  findViewById(R.id.deliveryaddress_count);
        usertype = (TextView)  findViewById(R.id.usertype_count);
        orderpattern = (TextView)  findViewById(R.id.orderpattern_count);
        prices = (TextView)  findViewById(R.id.prices_count);
        groupspecials = (TextView)  findViewById(R.id.groupspecials_count);
        customer = (TextView)  findViewById(R.id.customer_count);
        pricelist = (TextView)  findViewById(R.id.pricelist_count);
        downloadoverallspecial = (TextView)  findViewById(R.id.downloadoverallspecial);
        sync = (Button)  findViewById(R.id.sync);
        downloading_cust_history = (TextView)  findViewById(R.id.downloading_cust_history_count);
        usertype.setText("0");
        usertype.setBackgroundColor(Color.GREEN);
        Cursor cursors = db.rawQuery("SELECT * from tblSettings", null);
        //ArrayList<SettingsModel> settIP = dbH.getSettings();

        if (cursors.moveToFirst()) {
            do {

                IP = cursors.getString(cursors.getColumnIndex("IP"));
                Locations = cursors.getString(cursors.getColumnIndex("Location"));

            } while (cursors.moveToNext());
        }
        final AppApplication globalVariable = (AppApplication) getApplicationContext();
        IP = globalVariable.getIP();

        progressDialog = new ProgressDialog(ControlPanel.this);
        progressDialog.setMessage("Downloading... ");
        progressDialog.setCancelable(false);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(getApplicationContext().getFilesDir(),"LinxBriefcaseDB.db");
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("file Deleted :"  );
                    } else {
                        System.out.println("file not Deleted :" );
                    }
                }
                deleteCache(getBaseContext());

                Log.e("IPdb", "zip*******************************************************" + IP+"LinxBriefcaseDB.db");
                // new DownloadFileFromURL().execute(IP+"LinxBriefcaseDB.zip");
               //MyTask myTask=new MyTask(IP+"LinxBriefcaseDB.db");
               MyTask myTask=new MyTask(IP+DownloadPath);
                myTask.execute();

            }
        });

        //  pricelist.setText("Sluddo");
        pricelist.setText(""+returnCounts("SELECT *  from CustomerPriceLists"));
        stocktext.setText(""+returnCounts("SELECT *  from Products"));
        customer.setText(""+returnCounts("SELECT *  from Customers"));
        prices.setText(""+returnCounts("SELECT *  from CustomerSpecialsLines"));
        groupspecials.setText(""+returnCounts("SELECT *  from GroupSpecialsLines"));
        orderpattern.setText(""+returnCounts("SELECT * from OrderPattern"));
        deliveryaddress.setText(""+returnCounts("SELECT * from DeliveryAddress"));
        users.setText(""+returnCounts("SELECT * from Users"));
        downloadoverallspecial.setText(""+returnCounts("SELECT * from OverallSpecials"));
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }else{
                    Log.e("cache","***********clear***");
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    public int returnCounts(String Query)
    {
        //X/Y

        int getString = 0;
        int totalOrders = 0;
        try {
            Cursor cursor = db.rawQuery(Query, null);
            totalOrders = cursor.getCount();
            Log.e("count", "result*************************************************countResult" + getString);
        }catch (Exception e)
        {

        }
        return totalOrders;
    }
    public int checkLast()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        Cursor cursor2 = dbOrders.rawQuery("SELECT * from LoggedIn where LastDate ='"+tm+"'", null);
        return cursor2.getCount();


    }
    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(ControlPanel.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...."+msg);
        progressDoalog.setTitle("Doing some work for you.");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.show();
    }
    public static boolean unpackZip(String filePath) {

        InputStream is;
        ZipInputStream zis;
        try {

            File zipfile = new File(filePath);
            String parentFolder = zipfile.getParentFile().getPath();
            String filename;

            is = new FileInputStream(filePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(parentFolder + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(parentFolder + "/" + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();

        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            //DELETE BEFORE GOING BACK
            Intent i = new Intent(ControlPanel.this,LoginActivity.class);
            startActivity(i);

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private static void downloadFile(String url, File outputFile) {
        try {

            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

          /*  unpackZip( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Salesmanbriefcase"
                    + "/LinxBriefcaseDB.zip");*/

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
            Log.e("done**","***********************");
        } catch(FileNotFoundException e) {
            Log.e("FileNotFoundException**","on sync"+e);
            return; // swallow a 404
        } catch (IOException e) {
            Log.e("e**","on sync"+e);
            return; // swallow a 404
        }
    }
    public  boolean clearCache() {
        try {

            // create an array object of File type for referencing of cache files
            File[] files = getBaseContext().getCacheDir().listFiles();

            // use a for etch loop to delete files one by one
            for (File file : files) {

                /* you can use just [ file.delete() ] function of class File
                 * or use if for being sure if file deleted
                 * here if file dose not delete returns false and condition will
                 * will be true and it ends operation of function by return
                 * false then we will find that all files are not delete
                 */
                if (!file.delete()) {
                    return false;         // not success
                }
            }

            // if for loop completes and process not ended it returns true
            return true;      // success of deleting files

        } catch (Exception e) {}

        // try stops deleting cache files
        return false;       // not success
    }
    public class MyTask extends AsyncTask<String,Void,String> {
        String urls;
        public MyTask(String urls) {
            this.urls = urls;
        }

        @Override
        protected String doInBackground(String... strings) {
            downloadFile(urls, file);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("getName","++++++++++++++++++++++++++"+file.getName() );
            //  unpackZip(  "LinxBriefcasedb.zip");
           // Toast.makeText(ControlPanel.this, "File " + file.getName() + " Done Downloading." + file.getPath(), Toast.LENGTH_SHORT).show();
            Toast.makeText(ControlPanel.this,   " Done Downloading!!!"  , Toast.LENGTH_SHORT).show();
            Log.e("File222", ":" +  file.getName() + "path22222" + ":" +  file.getPath());
            progressDialog.dismiss();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tm =  sdf.format(c.getTime());
            dbOrders.execSQL("DELETE FROM LoggedIn");
            ContentValues cv = new ContentValues();
            cv.put("UserID", userID);
            cv.put("LastDate", tm);
            dbOrders.insert("LoggedIn", null, cv);

            Intent i = new Intent(ControlPanel.this, LoginActivity.class);
            startActivity(i);
        }
    }
}