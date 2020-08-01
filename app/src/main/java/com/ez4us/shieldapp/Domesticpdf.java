package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.ez4us.shieldapp.R;

public class Domesticpdf extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domesticpdf);

        Toast.makeText(Domesticpdf.this, "Loading , Please wait ...", Toast.LENGTH_SHORT).show();

        webView = (WebView) findViewById(R.id.webpdfView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("https://drive.google.com/file/d/1u-Uh7azxR_strSJzmQuiDb6h3VwsaIc_/view?usp=sharing") ;

    }
}