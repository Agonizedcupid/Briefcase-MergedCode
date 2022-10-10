package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class RegisterActivity extends AppCompatActivity {

    private SQLiteDatabase dbOrders;
    EditText editTextCompanyPassword,editEmailAddress,theusername;
    Button btnsaved;
    String IP="http://102.37.0.48/TandB/",customerOrders,registrationIp="";
    int len = 0;
    ProgressDialog progressDoalog,progressDialog;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //final String dir2 =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/";
        File dir2 = new File("Salesmanbriefcase");
        dbOrders = this.openOrCreateDatabase(getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        editTextCompanyPassword = (EditText) findViewById(R.id.editTextCompanyPassword);
        editEmailAddress = (EditText) findViewById(R.id.editConnectionStrings);
        theusername = (EditText) findViewById(R.id.editTextTextPersonName3);
        btnsaved = (Button) findViewById(R.id.btnsaved);
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Downloading... ");
        progressDialog.setCancelable(false);


        btnsaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getIPs(editTextCompanyPassword.getText().toString(),editEmailAddress.getText().toString());

            }
        });
    }
    public void getIPs(String keys,String Ips) {
        HttpRequest request = new HttpRequest();

        request.setOnResponseListener(response -> {
            if (response.code == HttpResponse.HTTP_OK) {

                try {
                    JSONArray jsonArray = response.toJSONArray();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e("IPs", "------------------------" + jsonArray.getJSONObject(i));
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if(jsonObject.getString("result").equals("ERROR"))
                        {
                            final  AlertDialog.Builder dialogretry = new  AlertDialog.Builder(RegisterActivity.this);
                            dialogretry.setTitle("Oops")
                                    .setMessage("Wrong data entered please, try again")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                            paramDialogInterface.dismiss();
                                        }
                                    });
                            dialogretry.show();
                        }else
                        {

                            Log.e("returnedIP","**********************"+jsonObject.getString("IP"));
                            Log.e("editEmailAddress","**********************"+editEmailAddress.getText().toString());
                            String returnedIP =jsonObject.getString("IP");
                           /* if(returnedIP.equals(editEmailAddress.getText().toString()))
                            {*/
                            registrationIp = returnedIP;
                                Log.e("INSIDE","********************** THE SLUDDOOOOOO");
                            String androidId = Settings.Secure.getString(getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                                Log.e("returnedIP","**********************"+jsonObject.getString("IP"));

                            Log.e("mogwanthi","***********************"+jsonObject.getString("IP")+"RegisterDevice.php?key="+androidId+"&username="+theusername.getText().toString());
                             startProgress("Busy with the registration.");
                             new registerDevice().execute(jsonObject.getString("IP")+"RegisterDevice.php?key="+androidId+"&username="+theusername.getText().toString());

                            //}
                        }
                        //db.execSQL("CREATE TABLE IF NOT EXISTS Users  (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,UserID INTEGER,UserName TEXT,UserPin TEXT,UserTokenAuth TEXT)");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (response.code == HttpResponse.HTTP_BAD_REQUEST) {

            } else if (response.code == HttpResponse.HTTP_NOT_FOUND) {
            }
        });
        request.setOnErrorListener(error -> {
            switch (error.code) {
                case HttpError.CONNECTION_TIMED_OUT:
                    break;
                case HttpError.NETWORK_UNREACHABLE:
                    break;
                case HttpError.INVALID_URL:
                    break;
                case HttpError.UNKNOWN:
                    break;
            }
        });
        Log.e("locations","***********************"+IP+"Registration.php?key="+keys+"&IP="+Ips);
        request.get(IP+"Registration.php?key="+keys);
    }

    private class registerDevice extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            len = result.length();
            customerOrders = result.toString();
            Log.e("len***t", "len**************" + len);
            if (len > 0) {
                //lastmess
                progressDoalog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Registered, your account will be fully activate within 30 minutes.")
                        .setMessage("ID :"+customerOrders)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ContentValues values = new ContentValues();
                                values.put("IP",registrationIp);
                                values.put("DimsIp",registrationIp);
                                dbOrders.insert("tblSettings", null, values);
                                file = new File(getApplicationContext().getFilesDir(),"LinxBriefcaseDB.db");
                                if (file.exists()) {
                                    if (file.delete()) {
                                        System.out.println("file Deleted :"  );
                                    } else {
                                        System.out.println("file not Deleted :" );
                                    }
                                }

                                    MyTask myTask=new MyTask(registrationIp+"LinxBriefcaseDB.db");
                                    myTask.execute();

                              //  new DownloadFileFromURL().execute(registrationIp+"LinxBriefcaseDB.zip");
                                //dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }


    public static String GET(String urlp) {

        String movieJsonStr = "";
        String result = "";
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        URL url;

        try{
            url = new URL(urlp);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Initialize a new string buffer object
            StringBuffer stringBuffer = new StringBuffer();

            String line ="";
            // Loop through the lines
            while((line= bufferedReader.readLine())!=null){
                // Append the current line to string buffer
                stringBuffer.append(line);
            }

            movieJsonStr =  stringBuffer.toString();

        } catch (Throwable e) {
            Log.e("backgroundtask", "EXCEPTION", e);
        } finally {
            connection.disconnect();
            try {


                if(bufferedReader != null)
                    bufferedReader.close();
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.e("READER.CLOSE()", e.toString());
            }
        }

        try {
            result = movieJsonStr;
        } catch (Throwable e) {
            Log.e("BACKGROUNDTASK", "EXCEPTION FROM jsonParse()", e);
        }
        return result;
    }



    public void startProgress(String msg)
    {
        progressDoalog = new ProgressDialog(RegisterActivity.this);
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
            //String parentFolder = zipfile.getParentFile().getPath();
            String filename;

            is = new FileInputStream(filePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File( filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream( filename);

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
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
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
          //  unpackZip(  "LinxBriefcasedb.zip");
            Toast.makeText(RegisterActivity.this, "File 2" + file.getName() + " exists in path 222222222" + file.getPath(), Toast.LENGTH_SHORT).show();
            Log.e("File222", ":" +  file.getName() + "path22222" + ":" +  file.getPath());
            progressDialog.dismiss();
        }
    }

}