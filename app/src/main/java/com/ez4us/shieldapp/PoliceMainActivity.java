package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PoliceMainActivity extends AppCompatActivity {

    Button trafik,domestic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_main);

        trafik=findViewById(R.id.trafficking);
        domestic=findViewById(R.id.domestic);

        trafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(PoliceMainActivity.this,PoliceMainActivity.class);
                startActivity(ii);
            }
        });
        domestic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(PoliceMainActivity.this,PoliceMainActivity.class);
                startActivity(ii);
            }
        });

    }
}