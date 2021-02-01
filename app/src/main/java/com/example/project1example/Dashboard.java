package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project1example.model.SharedPrefs_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.armcha.elasticview.ElasticView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Dashboard extends AppCompatActivity {
    CardView search_owners,bill_escapers;
    TextView name,mobile,role;
    ImageView profilepic;
    RelativeLayout progress_layout,p_status_warning;
    SharedPrefs_model spm;
    LinearLayout dashboard_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        search_owners=findViewById(R.id.search_owners);
        bill_escapers=findViewById(R.id.bill_escapers);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        role=findViewById(R.id.role);
        profilepic=findViewById(R.id.profilepic);
        progress_layout=findViewById(R.id.progress_layout);

        p_status_warning=findViewById(R.id.p_status_warning);
        dashboard_data=findViewById(R.id.dashboard_data);
        spm = new SharedPrefs_model(this);



        if (spm.getP_status().equalsIgnoreCase("deactive")){
            dashboard_data.setVisibility(View.GONE);
            p_status_warning.setVisibility(View.VISIBLE);
        }else {
            dashboard_data.setVisibility(View.VISIBLE);
            p_status_warning.setVisibility(View.GONE);
        }

            Glide.with(this).load(login_interface.JSON_URL + spm.getImage()).placeholder(R.drawable.dummylogo).into(profilepic);
            name.setText(spm.getName());
            mobile.setText(spm.getMobile());
            role.setText("LED Wall "+spm.getRole());


        search_owners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ownersearch=new Intent(Dashboard.this,OwnersListActivity.class);
                startActivity(ownersearch);
            }
        });
        bill_escapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Dashboard.this,BillEscapersActivity.class);
                    i.putExtra("uid",spm.getUid());
                startActivity(i);
            }
        });
    }

}