package com.ez4us.shieldapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ez4us.shieldapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class CasesReport extends AppCompatActivity {

    DatabaseReference ref;

    String[] States = new String[]{"Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttarakhand","Uttar Pradesh","West Bengal"};
    int[] Cases = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    ArrayList<CaseModel> CaseModelArrayList;
    ListView listView;
    /*Button rpButton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casesdata);
        /*//------------------------------------------------Report Floating Button-------------------------------
        rpButton = findViewById(R.id.extFAbtn);
        rpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });*/

        /*//------------------------------------------Bottom Navigation----------------------------
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.data);
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

                    case R.id.data:
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
        });*/

        //--------------------------------------receiving data from reportbutton-------------------------------
       /* String receiveData = getIntent().getStringExtra("sendData");

        if(receiveData !=null) {
            for (int i = 0; i < States.length; i++) {
                if (States[i] == receiveData) {
                    Cases[i] = Cases[i] + 1;
                }
            }
        }
*/
        //------------------------------------------------Data Service-------------------------------------------------------------------------

        listView = findViewById(R.id.listView);

        //CaseModelArrayList = new ArrayList<>();
        CaseModelArrayList = populateList();

        CaseAdapter CaseAdapter = new CaseAdapter(this,CaseModelArrayList);
        listView.setAdapter(CaseAdapter);
        //---------------------------------------------------------------------------------------------------


    }

    private ArrayList<CaseModel> populateList(){

        ArrayList<CaseModel> list = new ArrayList<>();

        for(int i = 0; i < States.length; i++){
            CaseModel CaseModel = new CaseModel();
            CaseModel.setStates(States[i]);
            CaseModel.setCases(Cases[i]);
            list.add(CaseModel);
        }

        return list;
    }
    /*//---------------------------------extended floating button--------------------------
    public void openActivity() {
        Intent intent = new Intent(this, reportbutton.class);
        startActivity(intent);
    }*/

//---------------------------------extended floating button--------------------------
    /*@Override
    public void onClick(View view) {
        ExtendedFloatingActionButton extFAB = (ExtendedFloatingActionButton) view;
        if(extFAB.isExtended())
        {
            extFAB.shrink(true);
            startActivity(new Intent(CasesReport.this, reportbutton.class));
        }
        else
        {
            extFAB.extend(true);
        }
    }*/
}

//------------------caseAdapter class--------------------------------
class CaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CaseModel> CaseModelArrayList;

    public CaseAdapter(Context context, ArrayList<CaseModel> CaseModelArrayList) {

        this.context = context;
        this.CaseModelArrayList = CaseModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return CaseModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return CaseModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null, true);

            holder.StatesName = (TextView) convertView.findViewById(R.id.StatesName);
            holder.CasesNo = (TextView) convertView.findViewById(R.id.CasesNo);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.StatesName.setText(CaseModelArrayList.get(position).getStates());
        holder.CasesNo.setText(String.valueOf(CaseModelArrayList.get(position).getCases()));

        return convertView;
    }

    private class ViewHolder {

        protected TextView StatesName, CasesNo;

    }

}

//-------------------------casemodel class---------------------------
class CaseModel {

    private String states;
    private int cases;

    public String getStates() {
        return states;
    }

    public void setStates(String state) {
        this.states = state;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int Case) {
        this.cases = Case;
    }

}
