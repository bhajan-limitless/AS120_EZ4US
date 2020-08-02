package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PoliceLoginActivity extends AppCompatActivity {

    Button login;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policelogin);

        login=findViewById(R.id.signIn_police);
        username=findViewById(R.id.user_police);
        password=findViewById(R.id.password_police);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(PoliceLoginActivity.this,PoliceMainActivity.class);
                startActivity(ii);
            }
        });
    }
}