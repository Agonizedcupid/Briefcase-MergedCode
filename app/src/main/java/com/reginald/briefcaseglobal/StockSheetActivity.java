package com.reginald.briefcaseglobal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class StockSheetActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView backBtn;
    private ProgressBar progressBar;
    String IPs,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_sheet);
        initUI();
    }

    private void initUI() {
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
        final  AppApplication globalVariable = (AppApplication) getApplicationContext();
       // Log.e("IPoncreate","***************"+globalVariable.getIP());
        IPs=globalVariable.getIP();
        userid=globalVariable.getUserId();
        loadWebView(IPs+"GetStockSheet.php?userid="+userid);
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