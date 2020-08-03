package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class PoliceLoginActivity extends AppCompatActivity {

    Button loginBt;
    EditText stateEt,districtEt,areaEt,usernameEt,passwordEt;

    private  String state,district,area,username,password;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policelogin);

        loginBt=findViewById(R.id.signIn_police);
        stateEt=findViewById(R.id.state_police);
        districtEt=findViewById(R.id.district_police);
        areaEt=findViewById(R.id.area_police);
        usernameEt=findViewById(R.id.user_police);
        passwordEt=findViewById(R.id.password_police);

        state=stateEt.getText().toString();
        district=districtEt.getText().toString();
        area=areaEt.getText().toString();
        username=usernameEt.getText().toString();
        password=passwordEt.getText().toString();









        mFirebaseAuth= FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                //final String uniquePoliceId=mFirebaseUser.getUid();
                if( mFirebaseUser != null && mFirebaseUser.isEmailVerified()) {



                    if (getIntent().hasExtra("category")) {
                        Intent intent = new Intent(PoliceLoginActivity.this, var_get.class);
                        intent.putExtra("link",getIntent().getStringExtra("link"));
                        intent.putExtra("category", getIntent().getStringExtra("category"));
                        intent.putExtra("UniqueId", getIntent().getStringExtra("UniqueId"));
                        startActivity(intent);

                    } else {



                        Intent i = new Intent(PoliceLoginActivity.this, PoliceMainActivity.class);
                        startActivity(i);

                    }

                    /**DatabaseReference policeUsers= FirebaseDatabase.getInstance().getReference().child("PoliceDepartment").child(state).child(district).child(area);
                    policeUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(uniquePoliceId)){

                                    Intent i = new Intent(PoliceLoginActivity.this, PoliceMainActivity.class);
                                    startActivity(i);

                            }
                            else{
                                Toast.makeText(PoliceLoginActivity.this,"Police User Not in Database",Toast.LENGTH_SHORT).show();

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });**/
                }
                else{
                    Toast.makeText(PoliceLoginActivity.this,"Please Sign In",Toast.LENGTH_SHORT).show();
                }
            }
        };



        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=stateEt.getText().toString();
                district=districtEt.getText().toString();
                area=areaEt.getText().toString();
                username=usernameEt.getText().toString();
                password=passwordEt.getText().toString();



                if(username.isEmpty()){
                    usernameEt.setError("Please enter email id");
                    usernameEt.requestFocus();
                }
                else  if(password.isEmpty()){
                    passwordEt.setError("Please enter your password");
                    passwordEt.requestFocus();
                }
                else  if(username.isEmpty() && password.isEmpty()){
                    Toast.makeText(PoliceLoginActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(username.isEmpty() && password.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(PoliceLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){

                                Toast.makeText(PoliceLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Toast.makeText(PoliceLoginActivity.this,"SignIn Error, Please Try Again",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                if(mFirebaseAuth.getCurrentUser().isEmailVerified()){
                                    Intent i = new Intent(PoliceLoginActivity.this, PoliceMainActivity.class);
                                    startActivity(i);

                                } else{
                                    Toast.makeText(PoliceLoginActivity.this,"Please Verify your email address",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else{
                    //Toast.makeText(PoliceLoginActivity.this,"Please Fill Your Details Correctly",Toast.LENGTH_SHORT).show();
                    Toast.makeText(PoliceLoginActivity.this,state+" "+district+" "+area+" "+username+" "+password,Toast.LENGTH_LONG);
                }

            }
        });

        /***tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(PoliceLoginActivity.this, SignupActivity.class);
                startActivity(intSignUp);
            }
        });**/







    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}