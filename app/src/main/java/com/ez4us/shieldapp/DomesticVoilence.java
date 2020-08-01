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
import android.widget.Button;
import android.widget.Toast;

import com.ez4us.shieldapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DomesticVoilence extends AppCompatActivity {
    // Date and Time
    Calendar c = Calendar.getInstance();
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yy-hh:mm:ss aa");
    String datetime = dateformat.format(c.getTime());


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

//--------------------------------------------Audio Service---------------------------------------------------------------

        btnRecord = findViewById(R.id.audio_record);
        btnStop = findViewById(R.id.audio_stop);
        btnPdf = findViewById(R.id.domesticPDF);

        //-----------------------------OPEN PDF Domestic---------------------------------------------------------------
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DomesticVoilence.this, Domesticpdf.class ));
            }
        });

        // Intitalize variables
        btnStop.setEnabled(false);

        // Folder Creation
        File audiofolder = new File(getCacheDir()+"/shieldapp/audios");
        if(!audiofolder.exists()){
            audiofolder.mkdirs();
        }
        audiofile = getCacheDir()+"/shieldapp/audios/"+datetime+".3gp";

        // Start recording
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    btnStop.setEnabled(true);
                    btnRecord.setEnabled(false);
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(audiofile);
                    try {
                        mediaRecorder.prepare();
                    }catch (IOException e){
                        // failed
                    }
                    mediaRecorder.start();
                    Toast.makeText(DomesticVoilence.this, "Recording Started", Toast.LENGTH_SHORT).show();
                }
                else {
                    RequestPermissions();
                }
            }
        });

        // Stop recording
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setEnabled(false);
                btnRecord.setEnabled(true);
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder =null;
                Toast.makeText(DomesticVoilence.this, "Recording Stopped", Toast.LENGTH_SHORT).show();

                // ---------------------------------> uploading to firebase <--------------------------------------------------

                final ProgressDialog progressDialog = new ProgressDialog(DomesticVoilence.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                final String UniqueID = mAuth.getCurrentUser().getUid();

                StorageReference storageReference;
                storageReference  = FirebaseStorage.getInstance().getReference();

                Uri upfile = Uri.fromFile(new File(audiofile));
                StorageReference riversRef = storageReference.child("audios/"+UniqueID+"/"+datetime+".3gp");

                riversRef.putFile(upfile)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Handle sucessful uploads
                                progressDialog.dismiss();
                                Toast.makeText(DomesticVoilence.this, "Audio Uploaded", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                progressDialog.dismiss();
                                Toast.makeText(DomesticVoilence.this, "Upload Error", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {


                                progressDialog.setMessage("Uploading... ");
                            }
                        });

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

            StorageReference storageReference;
            storageReference  = FirebaseStorage.getInstance().getReference();

            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            final String UniqueID = mAuth.getCurrentUser().getUid();

            StorageReference riversRef = storageReference.child("videos/"+UniqueID+"/"+datetime+".mp4");

            riversRef.putFile(myvideo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle sucessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(DomesticVoilence.this, "Video Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(DomesticVoilence.this, "Upload Error", Toast.LENGTH_LONG).show();
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