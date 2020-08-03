package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                login();
            }
        });
    }
    public void login(){
        String user=username.getText().toString();
        String pwd=password.getText().toString();

        if (user.equals("admin")&&pwd.equals("12345")){
            Intent ii=new Intent(PoliceLoginActivity.this,PoliceMainActivity.class);
            startActivity(ii);
        }
        if(!user.equals("admin")){
            username.setError("Username Incorrect");
            username.requestFocus();
        }
        else if(!pwd.equals("12345")){
            username.setError("Password Incorrect");
            username.requestFocus();
        }
        else{
            Toast.makeText(this,"Please fill the correct username and password",Toast.LENGTH_LONG).show();

        }
    }
}