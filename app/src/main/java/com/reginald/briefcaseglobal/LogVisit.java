package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.reginald.briefcaseglobal.Interface.CurrentLocation;
import com.reginald.briefcaseglobal.Model.SurveyModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reginald.briefcaseglobal.AppApplication;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogVisit extends AppCompatActivity {

    private Button datePickerBtn;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;

    private TextView customerName, customerCode;
    private String name, code;
    private TextView lastVisit, lastMessage;
    private String date;

    private Button saveLogVisit;

    private EditText notes, catchUpNotes;

    FusedLocationProviderClient client;

    private static double latitude = 0.0, longitude = 0.0;

    private TextView firstQuestion, secondQuestion, thirdQuestion;
    private RadioButton firstOption, secondOption, thirdOption, fourthOption, fifthOption, sixthOption;
    private EditText specialComments;
    Context context;

    private static String questionOneAnswer, questionOneId, questionTwoAnswer, questionTwoId, questionThreeAnswer, questionThreeId;

    private List<SurveyModel> surveyModels = new ArrayList<>();
    private SQLiteDatabase dbOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_visit);
        dbOrders = this.openOrCreateDatabase( getApplicationContext().getFilesDir()+"/LinxBriefcaseOrders.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        client = LocationServices.getFusedLocationProviderClient(this);

        if (getIntent() != null) {
            name = getIntent().getStringExtra("name");
            code = getIntent().getStringExtra("code");
        }

        initUI();

        loadUserMessage();

        loadSurveyQuestion();

        if (ActivityCompat.checkSelfPermission(LogVisit.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LogVisit.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    private void loadSurveyQuestion() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);

        Call<ResponseBody> call = apiService.surveyQuestion();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONArray array = new JSONArray(response.body().string());
                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject singleObject = array.getJSONObject(i);
                            String intAuto = singleObject.getString("intAuto");
                            String strMessage = singleObject.getString("strMessage");
                            String dteActiveFrom = singleObject.getString("dteActiveFrom");
                            String dteActiveTo = singleObject.getString("dteActiveTo");
                            JSONObject ob = singleObject.getJSONObject("dtmCreate");
                            String date = ob.getString("date");
                            SurveyModel model = new SurveyModel(intAuto, strMessage, dteActiveFrom, dteActiveTo, date);
                            //  Toast.makeText(LogVisit.this, "" + intAuto, Toast.LENGTH_SHORT).show();
                            surveyModels.add(model);
                        }

                        showQuestion(surveyModels);
                    } else {
                        questionOneId = "1";
                        questionTwoId = "2";
                        questionThreeId = "3";
                    }

                } catch (Exception e) {
                    Toast.makeText(LogVisit.this, "APIs not working: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void showQuestion(List<SurveyModel> surveyModels) {
        firstQuestion.setText(surveyModels.get(0).getStrMessage());
        questionOneId = surveyModels.get(0).getIntAuto();
        secondQuestion.setText(surveyModels.get(1).getStrMessage());
        questionTwoId = surveyModels.get(1).getIntAuto();
        thirdQuestion.setText(surveyModels.get(2).getStrMessage());
        questionThreeId = surveyModels.get(2).getIntAuto();
    }

    private boolean checkQuestionAnswer() {
        if ((firstOption.isChecked() || secondOption.isChecked()) && (thirdOption.isChecked() || fourthOption.isChecked()) &&
                (fifthOption.isChecked() || sixthOption.isChecked())) {
            return true;
        } else {
            return false;
        }
    }

    private void getCurrentLocation(CurrentLocation currentLocation) {
        //Check the location permission:
        if (ActivityCompat.checkSelfPermission(LogVisit.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(LogVisit.this, Locale.getDefault());
                            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            currentLocation.getLocation(list.get(0).getLatitude(), list.get(0).getLongitude());
                        } catch (Exception e) {

                        }
                    }
                }
            });
        } else {
            //request for the location access
            ActivityCompat.requestPermissions(LogVisit.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    private void loadUserMessage() {
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IP","*******"+globalVariable.getIP());
        APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
        Call<ResponseBody> call = apiService.getCustomerVisitMessage(code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body() != null) {
                        JSONArray finalResponse = new JSONArray(response.body().string());
                        if (finalResponse.length() != 0) {
                            for (int i = 0; i < finalResponse.length(); i++) {
                                JSONObject object = finalResponse.getJSONObject(i);
                                String notes = object.getString("notes");
                                String lastDate = object.getString("lastDate");

                                lastMessage.setText(notes);
                                lastVisit.setText("Last Visit   " + lastDate);
                            }
                        } else {
                            lastMessage.setText("No data found!");
                            lastVisit.setText("No last visit date found!");
                        }
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LogVisit.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {

        firstQuestion = findViewById(R.id.firstQuestion);
        secondQuestion = findViewById(R.id.secondQuestion);
        thirdQuestion = findViewById(R.id.thirdQuestion);

        firstOption = findViewById(R.id.firstOption);
        secondOption = findViewById(R.id.secondOption);
        thirdOption = findViewById(R.id.thirdOption);
        fourthOption = findViewById(R.id.fourthOption);
        fifthOption = findViewById(R.id.fifthOption);
        sixthOption = findViewById(R.id.sixthOption);

        specialComments = findViewById(R.id.specialComments);

        firstOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstOption.setChecked(true);
                secondOption.setChecked(false);
                questionOneAnswer = "Yes";
            }
        });
        secondOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstOption.setChecked(false);
                secondOption.setChecked(true);
                questionOneAnswer = "No";
            }
        });

        thirdOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdOption.setChecked(true);
                fourthOption.setChecked(false);
                questionTwoAnswer = "Yes";
            }
        });

        fourthOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdOption.setChecked(false);
                fourthOption.setChecked(true);
                questionTwoAnswer = "No";
            }
        });

        fifthOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fifthOption.setChecked(true);
                sixthOption.setChecked(false);
                questionThreeAnswer = "Yes";
            }
        });

        sixthOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fifthOption.setChecked(false);
                sixthOption.setChecked(true);
                questionThreeAnswer = "No";
            }
        });

        saveLogVisit = findViewById(R.id.saveLogVisitBtn);
        lastVisit = findViewById(R.id.lastVisit);
        lastMessage = findViewById(R.id.lastMessage);

        notes = findViewById(R.id.notesEditText);
        catchUpNotes = findViewById(R.id.catchUpNoteEditText);

        customerName = findViewById(R.id.customerName);
        customerName.setText("Name: " + name);
        customerCode = findViewById(R.id.customerCode);
        customerCode.setText("Code: " + code);

        datePickerBtn = findViewById(R.id.datePickerBtn);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveLogVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){
                    if (TextUtils.isEmpty(notes.getText().toString())) {
                        notes.setError("Enter notes!");
                        notes.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(catchUpNotes.getText().toString())) {
                        catchUpNotes.setError("Enter notes!");
                        catchUpNotes.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(specialComments.getText().toString())) {
                        specialComments.setError("Enter notes!");
                        specialComments.requestFocus();
                        return;
                    }

                    if (!checkQuestionAnswer()) {
                        Toast.makeText(LogVisit.this, "Please answer all the question", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (datePickerBtn.getText().toString().equals("Date For My Next Visit")) {
                        Toast.makeText(LogVisit.this, "Please choose the date for your next visit!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final AppApplication globalVariable = (AppApplication) getApplicationContext();
                    Log.e("IPloaddd","*******"+globalVariable.getIP());
                    APIs apiService = ApiClient.getClient(globalVariable.getIP()).create(APIs.class);
                    //APIs apiService = ApiClient.getClient2().create(APIs.class);

                    try{
                        getCurrentLocation(new CurrentLocation() {
                            @Override
                            public void getLocation(double latitude, double longitude) {
                                Log.e("questionTwoId","********questionTwoId"+questionTwoId);
                                Log.e("questionTwoId","********questionTwoAnswer"+questionTwoId);
                                Log.e("questionTwoAnswer","********questionTwoAnswer"+questionTwoAnswer);
                                Log.e("questionThreeId","********questionThreeAnswer"+questionThreeId);
                                Log.e("questionThreeAnswer","********questionThreeAnswer"+questionThreeAnswer);
                                Log.e("specialComments","********specialComments"+specialComments.getText().toString());

                                saveVisitsLocally( code, date, Double.toString(latitude), Double.toString(longitude),
                                          questionOneId, questionOneAnswer, questionTwoId,
                                         questionTwoAnswer, questionThreeId, questionThreeAnswer,
                                         specialComments.getText().toString(),globalVariable.getUserId(), catchUpNotes.getText().toString(), notes.getText().toString().trim(),globalVariable.getIP()+"Logvisit.php");
  /*postDataUsingVolley( code, date, Double.toString(latitude), Double.toString(longitude),
                                          questionOneId, questionOneAnswer, questionTwoId,
                                         questionTwoAnswer, questionThreeId, questionThreeAnswer,
                                         specialComments.getText().toString(),globalVariable.getUserId(), catchUpNotes.getText().toString(), notes.getText().toString().trim(),globalVariable.getIP()+"Logvisit.php");
*/
/*
                                Call<String> call = apiService.submitLogVisit(
                                        "" + code, "" + notes.getText().toString().trim(),
                                        "" + catchUpNotes.getText().toString(), "" + date,
                                        Integer.parseInt(globalVariable.getUserId()), latitude, longitude,
                                        "" + questionOneId, "" + questionOneAnswer,
                                        "" + questionTwoId, "" + questionTwoAnswer,
                                        "" + questionThreeId, "" + questionThreeAnswer,
                                        "" + specialComments.getText().toString()
                                );

                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String success = response.body();
                                        if (success.equals("Success")) {
                                            onBackPressed();
                                            Toast.makeText(LogVisit.this, "Data is successfully saved!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LogVisit.this, "Failed to save data!", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(LogVisit.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("errorsaving","t.getMessage()***"+t.getMessage());
                                    }
                                });*/
                            }
                        });
                    }catch (Exception e){
                        System.out.println("Error " + e.getMessage());
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LogVisit.this);
                        builder.setMessage("Error " + e.getMessage())
                                .setCancelable(false)
                                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LogVisit.this);
                    builder.setMessage("No Connection!")
                            .setCancelable(false)
                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap == null) return false;
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network n: networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        } else {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            for (NetworkInfo nInfo: networks) {
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        }

        return false;
    }
    private void showDate() {
        datePickerDialog = new DatePickerDialog(LogVisit.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                int j = i1 + 1;

                date = i + "-" + j + "-" + i2;
                Toast.makeText(LogVisit.this, "You've selected " + date, Toast.LENGTH_LONG).show();
                datePickerBtn.setText(date + "  Selected!");
                //Toast.makeText(AddProperty.this, "" + availableStatus, Toast.LENGTH_SHORT).show();

            }
        }, day, month, year);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }


    public void saveVisitsLocally(String code, String date,String latitude,String longitude,
                                  String questionOneId,String questionOneAnswer,String questionTwoId,
                                  String questionTwoAnswer,String questionThreeId,String questionThreeAnswer,
                                  String specialComments,String UserId,String catchUpNotes,String notes,String postUrl){

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String subscriberId = ts+"-"+android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        ContentValues params = new ContentValues();
        params.put("ID", subscriberId);
        params.put("CustomerCode", code);
        params.put("nextvisit", date);
        params.put("Lat", latitude);
        params.put("Lon", longitude);
        params.put("answeroneid", questionOneId);
        params.put("answeronetext", questionOneAnswer);
        params.put("answertwoid", questionTwoId);
        params.put("answertwotext", questionTwoAnswer);
        params.put("answerthreeid", questionThreeId);
        params.put("answerthreetext", questionThreeAnswer);
        params.put("customersatisfactoyanswer",specialComments);
        params.put("userid", UserId);
        params.put("catchupnotes", catchUpNotes);
        params.put("notes", notes);

        dbOrders.insert("Visits", null, params);

        Intent i = new Intent(LogVisit.this,HomeScreen.class);
        startActivity(i);
    }
}