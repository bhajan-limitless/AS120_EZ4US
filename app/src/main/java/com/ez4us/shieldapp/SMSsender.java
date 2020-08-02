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
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ez4us.shieldapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SMSsender extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference;
    String currentUserUid;
    Button saveContact;

    String ph1;
    String ph2;
    String ph3;

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

        mAuth= FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();//get the unique id of user



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

        editText1=  findViewById(R.id.editTextNumber);
        editText2=findViewById(R.id.editTextNumber2);
        editText3= findViewById(R.id.editTextNumber3);
        saveContact=findViewById(R.id.Save);



        saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph1=editText1.getText().toString();
                ph2=editText2.getText().toString();
                ph3=editText3.getText().toString();
                create(ph1,ph2,ph3);
            }
        });

        show();

    }

    private void create(String ph1, String ph2, String ph3) {
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserUid).child("EmergencyNumbers");

        reference.child("Phone1").setValue(ph1);
        reference.child("Phone2").setValue(ph2);
        reference.child("Phone3").setValue(ph3);
    }

    public  void show(){

        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserUid);
        final DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserUid).child("EmergencyNumbers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("EmergencyNumbers") ) {

                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            String p1 = snapshot.child("Phone1").getValue().toString();
                            String p2 = snapshot.child("Phone2").getValue().toString();
                            String p3 = snapshot.child("Phone3").getValue().toString();

                            editText1.setText(p1);
                            editText2.setText(p2);
                            editText3.setText(p3);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                else {
                    Toast.makeText(SMSsender.this,"Add Contact Numbers to send message during emergency",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}