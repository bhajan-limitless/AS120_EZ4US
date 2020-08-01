package com.ez4us.shieldapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class reportbutton extends AppCompatActivity {

    //variables
    EditText StName,DisName,LocAdd,Pin,Res;
    Button REPbutton;
    Button checkButton;
    int maxid=0;
    FirebaseDatabase ParentElement;
    DatabaseReference ReferenceElement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportpage);

        //------------------------------------------Bottom Navigation----------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.repcomplain);
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

        //------------------------------------------------Report Floating Button-------------------------------
        checkButton = findViewById(R.id.extFAbtn);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        //--------------------------------------------------------------------------------------------------------
        //connect all editText and button
        StName = findViewById(R.id.stateTF);
        StName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        DisName = findViewById(R.id.districtTF);
        LocAdd = findViewById(R.id.locAdrressTF);
        Pin = findViewById(R.id.pincodeTF);
        Res = findViewById(R.id.reasonTF);
        REPbutton = findViewById(R.id.repBTN);

        //------------------checking textfield code-----------------------------------
        StName.addTextChangedListener(loginTextWatcher);
        DisName.addTextChangedListener(loginTextWatcher);
        LocAdd.addTextChangedListener(loginTextWatcher);
        Pin.addTextChangedListener(loginTextWatcher);
        Res.addTextChangedListener(loginTextWatcher);
        //----------------------------------------------------------------------------

        ParentElement = FirebaseDatabase.getInstance();
        ReferenceElement = ParentElement.getReference().child("ReportedData");



        ReferenceElement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists())
                    maxid = (int) datasnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final HelpingClass helpingClass = new HelpingClass();
        //save data in firebase on click
        REPbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ParentElement = FirebaseDatabase.getInstance();
                ReferenceElement = ParentElement.getReference().child("ReportedData");*/

                //get all the values
                /*String statetextview = StName.getEditableText().toString();
                String districttextview = DisName.getEditableText().toString();
                String locaddtextview = LocAdd.getEditableText().toString();
                String reasontextview = Res.getEditableText().toString();*/

                /*Long pincode = Long.parseLong(Pin.getText().toString().trim());*/

                helpingClass.setStateName(StName.getText().toString().trim());
                helpingClass.setDistrictName(DisName.getText().toString().trim());
                helpingClass.setLocationAddress(LocAdd.getText().toString().trim());
                helpingClass.setReason(Res.getText().toString().trim());
                helpingClass.setPincode(Pin.getText().toString().trim());


                ReferenceElement.child(String.valueOf(maxid+1)).setValue(helpingClass);
                /*ReferenceElement.push().setValue(helpingClass);*/
                Toast.makeText(reportbutton.this, "File Reported Sucessfully", Toast.LENGTH_SHORT).show();

                //------------------------------------sending data to casesReport---------------------------
                /*String sendData = StName.getText().toString().trim();
                Intent sendintent = new Intent(reportbutton.this,CasesReport.class);
                sendintent.putExtra("sendData",sendData);
                startActivity(sendintent);*/
                //-------------------------------------------------------------------------------------------
            }
        });


    }

    //---------------------------------extended floating button--------------------------
    public void openActivity() {
        Intent intent = new Intent(this, CasesReport.class);
        startActivity(intent);
    }

    //-------------------------checking textfield code--------------------------------------
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String stateinput = StName.getText().toString().trim();
            String districtinput = DisName.getText().toString().trim();
            String locationinput = LocAdd.getText().toString().trim();
            String pincodeinput = Pin.getText().toString().trim();
            String reasoninput = Res.getText().toString().trim();

            REPbutton.setEnabled(!stateinput.isEmpty() && !districtinput.isEmpty() && !locationinput.isEmpty() && !pincodeinput.isEmpty() && !reasoninput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
//-------------------------------------------------------------------------------------
}



class HelpingClass{
    private String stateName, districtName, locationAddress, reason,pincode;


    public HelpingClass() {
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

