package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ez4us.shieldapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DomesticVoilence extends AppCompatActivity {

    // Date and Time
    Calendar c = Calendar.getInstance();
    SimpleDateFormat datetimeformat = new SimpleDateFormat("dd-MM-yy-hh:mm:ss aa");
    String datetime = datetimeformat.format(c.getTime());

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yy-hh");
    String date = dateformat.format(c.getTime());

    //variable declare
    Button sumbit;
    AutoCompleteTextView category;
    ImageView ddimg;
    EditText reason;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private static String[] Category_List;

    // Declare variable
    private static int Video_Request =101;
    Button btnRecordVideo;
    Button btnRecord;
    Button btnStop, btnPdf;
    String audiofile;
    MediaRecorder mediaRecorder;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domestic_voilence);

        //---------finding textfied using id-------------------
        category = findViewById(R.id.categoryET);
        reason = findViewById(R.id.reasonET);
        sumbit = findViewById(R.id.submit_report);
        ddimg = findViewById(R.id.DDimage);
        Category_List=getResources().getStringArray(R.array.problem);
        category.setThreshold(1);

        //---------UID--------------------------------
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String UniqueID = mAuth.getCurrentUser().getUid();

        //------------------------------------------Bottom Navigation----------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.domestic);
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
                        break;

                    case R.id.homeNav:
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        break;

                    case R.id.repcomplain:
                        Intent intent2 = new Intent(getApplicationContext(),reportbutton.class);
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

        // --------------------------------------------------> Video Service <---------------------------------------------

        btnRecordVideo = findViewById(R.id.video_record);
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureVideo();

            }
        });
        //------------------------------submit-report-------------------------------------
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ReportedData");
        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //-------------------------------------> Report file upload <-----------------------------------------

                String catET = category.getText().toString().trim().toUpperCase();
                String resET = reason.getText().toString().trim();
                databaseReference.child(catET).child(UniqueID).child(datetime).setValue(resET);
                Toast.makeText(DomesticVoilence.this, "File Reported Sucessfully", Toast.LENGTH_SHORT).show();
                category.getText().clear();
                reason.getText().clear();

            }
        });

        //----------dropdown menu-------------------------------------------------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,Category_List);
        category.setAdapter(adapter);
        ddimg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                category.showDropDown();
            }
        });

    }

    private void captureVideo() {

        File path = new File(getCacheDir()+"/shieldapp/videos/");
        if (!path.exists())
            path.mkdirs();

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,Video_Request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Video_Request) {
            Uri myvideo = data.getData();
            Toast.makeText(this, "Video saved succesfully", Toast.LENGTH_SHORT).show();

            // ---------------------------------> uploading to firebase <--------------------------------------------------

            final ProgressDialog progressDialog = new ProgressDialog(DomesticVoilence.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            final String UniqueID = mAuth.getCurrentUser().getUid();

            StorageReference storageReference;
            storageReference  = FirebaseStorage.getInstance().getReference();

            StorageReference riversRef = storageReference.child("Reports/Domestic/"+UniqueID+"/"+date+"/Videos/"+datetime+".mp4");

            riversRef.putFile(myvideo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle sucessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(DomesticVoilence.this, "Video uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(DomesticVoilence.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.setMessage("Uploading... ");
                        }
                    });
        }

    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(DomesticVoilence.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }



}