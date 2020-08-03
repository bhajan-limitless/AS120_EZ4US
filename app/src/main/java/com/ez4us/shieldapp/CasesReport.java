package com.ez4us.shieldapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CasesReport extends AppCompatActivity {

    DatabaseReference ref;

    String[] Statename;
    ArrayList<CaseModel> CMAL = new ArrayList<>();
    ArrayList<Integer> itemList;
    ListView listView;
    public int num=0;
    CaseModel p = new CaseModel();
    /*Button rpButton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.casesdata);


        listView = findViewById(R.id.listView);
        Statename=getResources().getStringArray(R.array.indian_States);
        /*itemList = new ArrayList<>();
        CaseModel p = new CaseModel();*/
        /*String[] Statename = new String[]{"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttarakhand", "Uttar Pradesh", "West Bengal"};
        int[] TotalCases = new int[Statename.length];*/
        for (int i = 0; i < 28; i++) {
            num = 0;
            ref = FirebaseDatabase.getInstance().getReference("ReportedData");
            Query query = ref.orderByChild("stateName").equalTo(Statename[i]);
            final int finalI = i;
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    /*num=0;*/
                    if (snapshot.exists())
                        num = (int) snapshot.getChildrenCount();
                    else
                        num=0;
                    /*Log.i("ABCD", String.valueOf(num));*/
                    /*itemList.add(num);*/
                    /*callnewactivity(finalI,num);*/
                    p = new CaseModel(Statename[finalI], num);
                    Log.i("ABCD1", String.valueOf(num));
                    CMAL.add(p);
                    CustomAdapter myCustomAdapter = new CustomAdapter(CasesReport.this, CMAL);
                    listView.setAdapter(myCustomAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    /*num=0;
                    p = new CaseModel(Statename[finalI], num);
                    Log.i("ABCD1", String.valueOf(num));
                    CMAL.add(p);
                    CustomAdapter myCustomAdapter = new CustomAdapter(CasesReport.this, CMAL);
                    listView.setAdapter(myCustomAdapter);*/
                }
            });



            /*Log.i("ABCD1", String.valueOf(num));*/

            /*p = new CaseModel(Statename[i], TotalCases[i]);
            CMAL.add(p);*/
        }


        /*CustomAdapter myCustomAdapter = new CustomAdapter(CasesReport.this, CMAL);
        listView.setAdapter(myCustomAdapter);*/


    }

    /*private void callnewactivity(int finalI, int i) {
        p = new CaseModel(Statename[finalI], num);
        Log.i("ABCD1", String.valueOf(num));
        CMAL.add(p);
        CustomAdapter myCustomAdapter = new CustomAdapter(CasesReport.this, CMAL);
        listView.setAdapter(myCustomAdapter);
    }*/
}



//---------------------------------------------------------------------------
class CustomAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<CaseModel> CMAL;

    public CustomAdapter(Context context, ArrayList<CaseModel> CMAL) {
        mContext = context;
        this.CMAL = CMAL;

    }

    @Override
    public int getCount() {
        return CMAL.size();
    }

    @Override
    public Object getItem(int i) {
        return CMAL.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
        }

        CaseModel temp = (CaseModel) getItem(i);
        TextView SName = (TextView)view.findViewById(R.id.StatesName);
        TextView TCases = (TextView)view.findViewById(R.id.CasesNo);

        SName.setText(temp.getStatename());
        TCases.setText(""+temp.getTotalcases());

        return view;
    }
}
//---------------------------------------------------
class CaseModel {
    public CaseModel() {
    }

    public CaseModel(String statename, int totalcases) {
        this.statename = statename;
        this.totalcases = totalcases;
    }

    String statename;
    int totalcases;

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public int getTotalcases() {
        return totalcases;
    }

    public void setTotalcases(int totalcases) {
        this.totalcases = totalcases;
    }

}
