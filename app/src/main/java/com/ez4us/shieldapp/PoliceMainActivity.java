package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PoliceMainActivity extends AppCompatActivity {

    Button trafik,domestic,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_main);
//..............................navigation....................................................
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavPol);
        bottomNavigationView.setSelectedItemId(R.id.harVolBtn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {


                    case R.id.trafBtn:
                        Intent intent1 = new Intent(getApplicationContext(), TraffickingDataPolice.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.harVolBtn:
                        break;

                }
                return false;

            }
        });
//..............................navigation....................................................







        //...................................................sign out.................................
        logout=findViewById(R.id.logoutPolice);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(PoliceMainActivity.this, PoliceLoginActivity.class);
                startActivity(intToMain);
            }
        });
        //.............................................................................................















    }
}