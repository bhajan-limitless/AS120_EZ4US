package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Puranjan Code.................................
    private Button logout;



    //................................


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Assign variables to xml buttons and edit text
        logout=findViewById(R.id.log_out);












        //Puranjan Code............................................
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });
        

    }
}