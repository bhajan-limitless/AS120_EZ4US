package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ez4us.shieldapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SMSsender extends AppCompatActivity {

    public static final String EXTRA_NUMBER3="com.example.shieldapp.EXTRA_NUMBER3";
    public static final String EXTRA_NUMBER="com.example.shieldapp.EXTRA_NUMBER";
    public static final String EXTRA_NUMBER1="com.example.shieldapp.EXTRA_NUMBER1";
    public static final String EXTRA_NUMBER2="com.example.shieldapp.EXTRA_NUMBER2";
    public static final String EXTRA_TEXT="com.example.shieldapp.EXTRA_TEXT";

    EditText editText1,editText2,editText3,editText4;

    public static int nof_calls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sender);

        //------------------------------------------Bottom Navigation----------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.emergency);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.emergency:
                        break;

                    case R.id.domestic:
                        Intent intent1 = new Intent(getApplicationContext(), DomesticVoilence.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.homeNav:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.repcomplain:
                        Intent intent2 = new Intent(getApplicationContext(), reportbutton.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.userProfile:
                        Intent intent3 = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });


//--------------------------------------------SMS service-------------------------------------------------------------

        editText1= (EditText) findViewById(R.id.editTextNumber);
        editText2= (EditText)findViewById(R.id.editTextNumber2);
        editText3= (EditText) findViewById(R.id.editTextNumber3);
        editText4= (EditText)findViewById(R.id.editText);

    }
    public void Save(View view){
        int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(permissionCheck== PackageManager.PERMISSION_GRANTED) {

            Sop();
        }

        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }
    }

    public void Sop(){

        //editText4.setText(editText4.getText().toString().trim());

        //String ph1=editText1.getText().toString().trim();
        //String ph2=editText2.getText().toString().trim();
        //String ph3=editText3.getText().toString().trim();
        //String Message=editText4.getText().toString().trim();

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(SMSsender.this);
        pref.edit().putString(EXTRA_NUMBER,editText1.getText().toString().trim()).apply();
        pref.edit().putString(EXTRA_NUMBER1,editText2.getText().toString().trim()).apply();
        pref.edit().putString(EXTRA_NUMBER2,editText3.getText().toString().trim()).apply();
        pref.edit().putString(EXTRA_TEXT,editText4.getText().toString().trim()).apply();


        String pho1=pref.getString(EXTRA_NUMBER,"");
        editText1.setText(pho1);
        String pho2=pref.getString(EXTRA_NUMBER1,"");
        editText2.setText(pho2);
        String pho3=pref.getString(EXTRA_NUMBER2,"");
        editText3.setText(pho3);
        String Message=pref.getString(EXTRA_TEXT,"");
        editText3.setText(Message);

        nof_calls += 1;

        Intent intent =new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_NUMBER,pho1);
        intent.putExtra(EXTRA_NUMBER1,pho2);
        intent.putExtra(EXTRA_NUMBER2,pho3);
        intent.putExtra(EXTRA_TEXT,Message);
        intent.putExtra(EXTRA_NUMBER3,nof_calls);
        startActivity(intent);

        Toast.makeText(this, "Edited! now press the SOS button", Toast.LENGTH_SHORT).show();

    }
}