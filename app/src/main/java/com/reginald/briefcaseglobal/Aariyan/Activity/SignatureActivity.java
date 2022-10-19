package com.reginald.briefcaseglobal.Aariyan.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reginald.briefcaseglobal.Aariyan.Common.Constant;
import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Interface.SuccessInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.SignatureModel;
import com.reginald.briefcaseglobal.Aariyan.Networking.PostSignature;
import com.reginald.briefcaseglobal.Aariyan.Networking.PostingToServer;
import com.reginald.briefcaseglobal.CustomersActivity;
import com.reginald.briefcaseglobal.HomeScreen;
import com.reginald.briefcaseglobal.Interface.CurrentLocation;
import com.reginald.briefcaseglobal.LogVisit;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SignatureActivity extends AppCompatActivity {

    private static String ipUrl = "";
    private DatabaseAdapter databaseAdapter;

    RestApis restApis;
    private String tId = "";

    FusedLocationProviderClient client;

    SignatureModel signatureModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        databaseAdapter = new DatabaseAdapter(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        if (getIntent() != null && getIntent().hasExtra("url")) {
            ipUrl = getIntent().getStringExtra("url");
            restApis = ApiClient.getClient(ipUrl).create(RestApis.class);
            tId = getIntent().getStringExtra("tID");
        }

        initUI();
    }


    private void initUI() {
        ImageView undoSignature = findViewById(R.id.undoSignature);
        undoSignature.setVisibility(View.GONE);
        SignaturePad ack_sign = findViewById(R.id.ack_sign);
        ack_sign.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                undoSignature.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSigned() {
                undoSignature.setVisibility(View.VISIBLE);
            }

            @Override
            public void onClear() {
                undoSignature.setVisibility(View.GONE);
            }
        });

        undoSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ack_sign.clear();
            }
        });

        findViewById(R.id.finalFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = ack_sign.getSignatureBitmap();
                //new UploadImage(signatureBitmap,IDs).execute();
                //new MainActivity.UploadImage(signatureBitmap,selectedItem.ItemString3,lat,lon, tomorrowDate).execute();
                if (addSignatureJpg(signatureBitmap, "image")) {
                    updateIsCompleteToOne(1);
                    Toast.makeText(SignatureActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignatureActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public boolean isInternetAvailable() {
//        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com");
//            //You can replace it with your name
//            return !ipAddr.equals("");
//        } catch (Exception e) {
//            return false;
//        }
//    }

    private void updateIsCompleteToOne(int flag) {
        if (databaseAdapter.updateDealsHeadersIsCompleted(flag) > 0) {
            Toast.makeText(this, "Updated Locally", Toast.LENGTH_SHORT).show();
        }

        if (!Constant.isInternetConnected(this)) {
            startProgress("All data saved in local!");
            progressdialog.setTitle("All data saved in local!");
            progressdialog.dismiss();
        } else {
            //get Location:
            progressdialog.setTitle("Posting Directly To Server!");
            getCurrentLocation(new CurrentLocation() {
                @Override
                public void getLocation(double latitude, double longitude) {
                    Log.d("LOCATION_CHECKING", "getLocation: " + latitude + " - " + longitude);
                    //check data is also posted to the server or not:
                    new PostingToServer(databaseAdapter, restApis, latitude, longitude)
                            .tryDirectPostingToServer(new SuccessInterface() {
                                @Override
                                public void onSuccess(String successMessage) {
                                    Toast.makeText(SignatureActivity.this, "" + successMessage, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignatureActivity.this, CustomersActivity.class));
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(SignatureActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            progressdialog.dismiss();
        }
    }

    private void getCurrentLocation(CurrentLocation currentLocation) {
        //Check the location permission:
        if (ActivityCompat.checkSelfPermission(SignatureActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(SignatureActivity.this, Locale.getDefault());
                            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            currentLocation.getLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } else {
            //request for the location access
            ActivityCompat.requestPermissions(SignatureActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    private boolean addSignatureJpg(Bitmap signature, String invoiceNo) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("BRIEFCASE_GLOBAL"), String.format("BRIEFCASE_GLOBAL_App_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo, invoiceNo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo, String InvoiceNo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        //  OutputStream stream = new FileOutputStream(photo);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteImage = outputStream.toByteArray();
        String signature = Base64.encodeToString(byteImage, Base64.DEFAULT);

        startProgress("Posting Signature");
        signatureModel = new SignatureModel(tId, signature);
        if (Constant.isInternetConnected(this)) {
            List<SignatureModel> list = new ArrayList<>();
            list.add(signatureModel);
            new PostSignature(restApis).postSignatureToServer(new SuccessInterface() {
                @Override
                public void onSuccess(String successMessage) {
                    Toast.makeText(SignatureActivity.this, "" + successMessage, Toast.LENGTH_SHORT).show();
                    progressdialog.dismiss();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(SignatureActivity.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    progressdialog.dismiss();
                }
            }, list);
        } else {
            progressdialog.setTitle("No Internet, Storing in Local");
            if (databaseAdapter.insertSignature(signatureModel) > 0) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            }
            progressdialog.dismiss();
        }

        //new postSignatureWithTransactionId(String.valueOf("" + transactionId()), signature).execute();
    }

    ProgressDialog progressdialog;

    public void startProgress(String msg) {
        progressdialog = new ProgressDialog(SignatureActivity.this);
        progressdialog.setMax(100);
        progressdialog.setMessage("Please Wait...." + msg);
        progressdialog.setTitle("Caution");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.show();
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            file.mkdir();
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    private String transactionId() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String subscriberId = ts + "-" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        return subscriberId;
    }

    private class postSignatureWithTransactionId extends AsyncTask<Void, Void, Void> {

        String transactionId, signature;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressdialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(SignatureActivity.this);
            builder
                    .setTitle("Signature Saved ")
                    .setMessage("Thank you")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }

        public postSignatureWithTransactionId(String transactionId, String signature) {
            this.transactionId = transactionId;
            this.signature = signature;

        }


        @Override
        protected Void doInBackground(Void... params) {
            HttpClient httpclient = new DefaultHttpClient();

            //dbCreation();
            //}
            HttpPost httppost = new HttpPost(ipUrl + "PostSignatures.php");
            try {
                // Add your data

                JSONObject json = new JSONObject();
                json.put("transactionId", transactionId);
                json.put("signature", signature);

                Log.d("JSON", json.toString());
                List nameValuePairs = new ArrayList(1);
                nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseBody = EntityUtils.toString(response.getEntity());
                Log.e("JSON-*", "RESPONSE is lines**: " + responseBody);
                JSONArray BoardInfo = new JSONArray(responseBody);

                for (int j = 0; j < BoardInfo.length(); ++j) {
                    JSONObject BoardDetails = BoardInfo.getJSONObject(j);
                    Log.d("SIGNATURE_POST", "doInBackground: " + BoardDetails.getString("results"));


                }

            } catch (ClientProtocolException e) {
                Log.e("JSON", e.getMessage());
            } catch (IOException e) {
                Log.e("JSON", e.getMessage());
            } catch (Exception e) {
                Log.e("JSON", e.getMessage());
            }
            //db.close();
            return null;
        }
    }
}