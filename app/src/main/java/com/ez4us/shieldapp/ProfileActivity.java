package com.ez4us.shieldapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ez4us.shieldapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    TextView nameTxt, uniqueidTxt, emailTxt, phoneTxt, occupationTxt, workplaceTxt,linkTxt;
    DatabaseReference reff;
    private FirebaseStorage storage;
    private StorageReference storageReferenceFetchImage;
    String UniqueID;
    ImageView profileImageView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //------------------------------------------Bottom Navigation----------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.userProfile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.emergency:
                        Intent intent = new Intent(getApplicationContext(), SMSsender.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.domestic:
                        Intent intent1 = new Intent(getApplicationContext(),DomesticVoilence.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.homeNav:
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.repcomplain:
                        Intent intent3 = new Intent(getApplicationContext(),reportbutton.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.userProfile:
                        break;
                }
                return false;
            }
        });


        //------------------------------------------------edit profile..............................
        Button edit=findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prof=new Intent(ProfileActivity.this,EditActivity.class);
                startActivity(prof);
            }
        });
        //...................................................sign out.................................
        Button logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intToMain);
            }
        });
        //.............................................................................................


        nameTxt=findViewById(R.id.name_textview);
        uniqueidTxt=findViewById(R.id.uniqueid_textview);
        emailTxt=findViewById(R.id.email_textview);
        phoneTxt=findViewById(R.id.phone_textview);
        occupationTxt=findViewById(R.id.occupation_textview);
        workplaceTxt=findViewById(R.id.work_textview);
        linkTxt=findViewById(R.id.link_textview);
        profileImageView=findViewById(R.id.user_imageview);


        show();


    }
    public void show(){
        mAuth= FirebaseAuth.getInstance();
        UniqueID = mAuth.getCurrentUser().getUid();//get the unique id of user
        reff= FirebaseDatabase.getInstance().getReference().child("Users").child(UniqueID);
        Toast.makeText(ProfileActivity.this,UniqueID,Toast.LENGTH_LONG).show();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email=snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String phone=snapshot.child("phone").getValue().toString();
                String profession=snapshot.child("profession").getValue().toString();
                String age=snapshot.child("age").getValue().toString();
                String workplace=snapshot.child("workplace").getValue().toString();

               // String photoLink=snapshot.child("profilePhoto").getValue().toString();
                //Picasso.get().load(photoLink).into(profileImageView);
                fetchImage();
                emailTxt.setText(email);
                nameTxt.setText(name);
                phoneTxt.setText(phone);
                occupationTxt.setText(profession);
                linkTxt.setText(age+" Years");
                workplaceTxt.setText(workplace);
                uniqueidTxt.setText(UniqueID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void fetchImage() {
        storage= FirebaseStorage.getInstance();
        storageReferenceFetchImage=storage.getReferenceFromUrl("gs://shieldappsih.appspot.com/Profile/"+UniqueID+"/").child("profile_pic.jpg");

        final File file;
        try {
            file = File.createTempFile("image","jpg");
            storageReferenceFetchImage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    profileImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}