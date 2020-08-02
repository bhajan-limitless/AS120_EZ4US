package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TraffickingDataPolice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafficking_data_police);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavPol);
        bottomNavigationView.setSelectedItemId(R.id.trafBtn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.trafBtn:
                        break;

                    case R.id.harVolBtn:
                        Intent intent1 = new Intent(getApplicationContext(), PoliceMainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;


                }
                return false;

            }
        });
    }
}