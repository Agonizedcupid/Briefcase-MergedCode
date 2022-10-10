package com.reginald.briefcaseglobal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ProblematicItemsActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView backBtn;
    private ProgressBar progressBar;
    String IPs,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problematic_items);
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
        IPs=globalVariable.getDIMSIP();
        userid=globalVariable.getUserId();
        Log.e("IPoncreate","***************"+IPs+"getProblematicItem/"+userid);
        loadWebView(IPs+"getProblematicItem/"+userid);
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