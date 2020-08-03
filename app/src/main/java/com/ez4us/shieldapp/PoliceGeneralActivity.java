package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PoliceGeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_general);


        Button pol=findViewById(R.id.pol);
        Button ga=findViewById(R.id.ga);


        pol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PoliceGeneralActivity.this, PoliceMainActivity.class);
                startActivity(i);
            }
        });
        ga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PoliceGeneralActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}