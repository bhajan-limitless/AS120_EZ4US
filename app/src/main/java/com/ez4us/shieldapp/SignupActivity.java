package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ez4us.shieldapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    CheckBox cb;
    int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth= FirebaseAuth.getInstance();
        emailId=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        btnSignUp=findViewById(R.id.button);
        tvSignIn=findViewById(R.id.textView);
        cb=findViewById(R.id.checkBox);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb.isChecked()){
                   temp=1;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailId.getText().toString();
                String pwd=password.getText().toString();
                if(email.isEmpty())
                {
                    emailId.setError("Provide email id");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if (temp==0){
                    cb.setError("Select the checkbok");
                    cb.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Fields are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                            if(!task.isSuccessful()){
                                //Toast.makeText(SignupActivity.this, "SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else{
                                mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "SignUp Successful, Please check your email for verification", Toast.LENGTH_SHORT).show();
                                            //startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            FirebaseUserMetadata muser=FirebaseAuth.getInstance().getCurrentUser().getMetadata();
                                            if (muser.getCreationTimestamp()==muser.getLastSignInTimestamp()) {           //check if first time login
                                                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();//call to the root node
                                                DatabaseReference reference = rootNode.getReference("Users");
                                                UserHelperClass helperClass = new UserHelperClass("", "", "", "", "", "", "");
                                                String UniqueID=mFirebaseAuth.getCurrentUser().getUid();
                                                reference.child(UniqueID).setValue(helperClass);//creates a child with Unique id
                                            }

                                            Intent aa=new Intent(SignupActivity.this,EditActivity.class);
                                            startActivity(aa);
                                            Toast.makeText(SignupActivity.this,"Don't forget to Verify Email",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignupActivity.this, "Error Occured!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }
}