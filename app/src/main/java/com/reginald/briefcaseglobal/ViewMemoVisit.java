package com.reginald.briefcaseglobal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewMemoVisit extends AppCompatActivity {

    private WebView webView;
    private ImageView backBtn;
    private ProgressBar progressBar;
    private TextView dateFrom, dateTo;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;

    private String type = "";
    private TextView toolbarText;
    private TextView name, code;
    private Button submit;
    DatePickerDialog pickerto,picker;
    String CustCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memo_visit);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        initUI();

        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            CustCode = getIntent().getStringExtra("code");
            toolbarText.setText("View " + type);

       //     name.setText(getIntent().getStringExtra("name"));
         //   code.setText(getIntent().getStringExtra("code"));
        }
    }

    private void initUI() {

        name = findViewById(R.id.queumessages);
        code = findViewById(R.id.ccCode);
        submit = findViewById(R.id.submit);

        dateFrom = findViewById(R.id.dateFrom);
        dateTo = findViewById(R.id.dateTo);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tm =  sdf.format(c.getTime());
        dateFrom.setText(tm);
        dateTo.setText(tm);

        toolbarText = findViewById(R.id.toolbarText);


        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ViewMemoVisit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateFrom.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                pickerto = new DatePickerDialog(ViewMemoVisit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateTo.setText(year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                pickerto.show();
            }
        });



        webView = findViewById(R.id.webView);
        backBtn = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.progressbar);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTheLink(type);
            }
        });
    }

    private void getTheLink(String type) {
        String url = "";
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
        Log.e("IPoncreate","***************"+globalVariable.getIP());
        String Ip = globalVariable.getIP();
        if (type.equals("Visit")) {
            url = Ip+"GetVisits.php?from=" + dateFrom.getText().toString() + "&to=" + dateTo.getText().toString() + "&customercode=" + CustCode;
            loadWebView(url);
        } else if (type.equals("Memo")) {
            url = Ip+"GetReminderswebview.php?from=" + dateFrom.getText().toString() + "&to=" + dateTo.getText().toString()+ "&customercode=" + CustCode;
            loadWebView(url);
        }
    }

    private void loadWebView(String urlLink) {
        webView.loadUrl(urlLink);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}