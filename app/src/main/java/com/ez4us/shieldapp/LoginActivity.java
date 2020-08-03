package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ez4us.shieldapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn,police;
    TextView tvSignUp, forgotpass;

    Button changeLang;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);

        //--------------------------------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        //--------------------------------------------


        emailId=findViewById(R.id.editTextQ);
        password=findViewById(R.id.editText2Q);
        btnSignIn=findViewById(R.id.buttonQ);
        tvSignUp=findViewById(R.id.textViewQ);
        forgotpass=findViewById(R.id.sendlinkQ);
        police=findViewById(R.id.police);
        changeLang=findViewById(R.id.buttonLang);

        mFirebaseAuth= FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null && mFirebaseUser.isEmailVerified()) {


                    if (getIntent().hasExtra("category")) {
                        Intent intent = new Intent(LoginActivity.this, var_get.class);
                        intent.putExtra("link",getIntent().getStringExtra("link"));
                        intent.putExtra("category", getIntent().getStringExtra("category"));
                        intent.putExtra("UniqueId", getIntent().getStringExtra("UniqueId"));
                        startActivity(intent);

                    } else {

                        final String uniquePoliceId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference UserId= FirebaseDatabase.getInstance().getReference().child("Users");
                        final DatabaseReference policeUsers= FirebaseDatabase.getInstance().getReference().child("Users").child(uniquePoliceId);

                        UserId.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("police")){
                                policeUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String state = snapshot.child("state").getValue().toString();
                                        String district = snapshot.child("district").getValue().toString();
                                        String area = snapshot.child("area").getValue().toString();


                                        DatabaseReference pl = FirebaseDatabase.getInstance().getReference().child(state).child(district).child(area);
                                        if (snapshot.hasChild(uniquePoliceId)){
                                            Intent i = new Intent(LoginActivity.this, PoliceGeneralActivity.class);
                                            startActivity(i);

                                        }
                                        else{
                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(i);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else {
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Sign In",Toast.LENGTH_SHORT).show();
                }
            }
        };

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(LoginActivity.this,PoliceLoginActivity.class);
                startActivity(ii);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){

                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                Toast.makeText(LoginActivity.this,"SignIn Error, Please Try Again",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                if(mFirebaseAuth.getCurrentUser().isEmailVerified()){
                                    Intent intToHome = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intToHome);
                                } else{
                                    Toast.makeText(LoginActivity.this,"Please Verify your email address",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intSignUp);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intSignUp);
            }
        });

        //-----------------------------changing language----------------------
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });



    }

    private void showChangeLanguageDialog() {
        final String[] lang_items={"English","हिन्दी"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(lang_items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              if(i==0){
                  setLocale("en");
                  recreate();
              }
             else if (i==1){
                  setLocale("hi");
                  recreate();
              }
             dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}