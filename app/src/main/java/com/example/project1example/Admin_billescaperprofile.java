package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1example.model.billescapers_list_model;
import com.example.project1example.model.owner_list_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Admin_billescaperprofile extends AppCompatActivity {
    String Base_URL, bid;
    LinearLayout delete_billEsc, edit_billEsc;
    RelativeLayout progress_layout, error_layout;
    TextView name, amount, phone_number1, phone_number2, villege, district, niyojakavargam, pincode, createdOn;
    ImageView profile_img;
    String getbid;
    Toolbar toolbar;

    billescapers_list_model billescapers_list_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_billescaperprofile);



        billescapers_list_model = (billescapers_list_model) this.getIntent().getSerializableExtra( "list" );

        getbid = billescapers_list_model.getBid();

        progress_layout = findViewById(R.id.progress_layout);
        delete_billEsc = findViewById(R.id.delete_lv);
        edit_billEsc = findViewById(R.id.edit_lv);
        name = findViewById(R.id.nameesc);
        amount = findViewById(R.id.amountesc);
        phone_number1 = findViewById(R.id.mobilenumber1esc);
        phone_number2 = findViewById(R.id.mobilenumber2esc);
        villege = findViewById(R.id.villageesc);
        district = findViewById(R.id.districtesc);
        niyojakavargam = findViewById(R.id.neyojakavargamesc);
        pincode = findViewById(R.id.pincodeesc);
        createdOn = findViewById(R.id.created_onesc);
        profile_img=findViewById( R.id.profile_img );
        configureToolbar();

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        name.setText( billescapers_list_model.getName() );
        amount.setText( billescapers_list_model.getAmount() );
        phone_number1.setText( billescapers_list_model.getMobile1() );
        phone_number2.setText( billescapers_list_model.getMobile2() );
        villege.setText( billescapers_list_model.getAddress() );
        district.setText( billescapers_list_model.getDistrict() );
        niyojakavargam.setText( billescapers_list_model.getNeyojakavargam() );
        pincode.setText( billescapers_list_model.getPincode() );
        createdOn.setText( billescapers_list_model.getCreated_on() );
        Glide.with(getApplicationContext()).load(login_interface.JSON_URL + billescapers_list_model.getImage()).placeholder(R.drawable.dummylogo).into(profile_img);

        delete_billEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_billescapers();
            }
        });

        edit_billEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_billescaperprofile.this, Admin_editBillescapers.class);
                i.putExtra("list",billescapers_list_model);
                startActivity(i);
            }
        });

    }


    //delete bill escapers
    private void delete_billescapers() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();

        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.delete_billescapers(getbid);
        Call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("ResponseString", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("ResponseString", response.body().toString());
                        String jsonresponse = response.body().toString();
                        Writetv_dltBillEsc(jsonresponse);

                    } else {
                        Log.i("ResponseString", "Returned empty response");

                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                Log.i("retrofail", "Failed");
            }
        });

    }
//delete bill escapers
    private void Writetv_dltBillEsc(String jsonresponse) {
        try {
            JSONObject obj = new JSONObject(jsonresponse);
            if (obj.optString("status").equalsIgnoreCase("true")) {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_billescaperprofile.this, bid, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Admin_billescaperprofile.this, AdminPanelActivity.class);
                startActivity(i);
            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Bill Escapers" );
        toolbar.setNavigationIcon(  getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }

}

