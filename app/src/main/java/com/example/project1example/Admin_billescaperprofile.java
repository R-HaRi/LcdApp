package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1example.model.billescapers_list_model;

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
    String Base_URL, bid, uidi;
    LinearLayout delete_billEsc, edit_billEsc;
    String esc_name, esc_amount, esc_mobile1, esc_mobile2, esc_village, esc_district, esc_niyojakavargam, esc_pincode, esc_createdOn;
    RelativeLayout progress_layout, error_layout;
    TextView name, amount, phone_number1, phone_number2, villege, district, niyojakavargam, pincode, createdOn;

    String getbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_billescaperprofile);


        getbid = getIntent().getStringExtra("bid");

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
                i.putExtra("edit_bid",getbid);
                startActivity(i);
            }
        });

        get_billEsc_profile();
//        Toast.makeText(Admin_billescaperprofile.this,getbid,Toast.LENGTH_SHORT).show();

    }

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

    private void get_billEsc_profile() {

        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.get_escaper_profile_bid(getbid);
        Call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("ResponseString", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("ResponseString", response.body().toString());
                        String jsonresponse = response.body().toString();
                        WriteTV_billEsc(jsonresponse);
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

    private void WriteTV_billEsc(String jsonresponse) {
        try {
            JSONObject obj = new JSONObject(jsonresponse);
            if (obj.optString("status").equalsIgnoreCase("true")) {
                progress_layout.setVisibility(View.GONE);
                JSONArray dataArray = obj.getJSONArray("data");
                JSONObject dataobj = dataArray.getJSONObject(0);

                esc_name = dataobj.getString("e_name");
                esc_amount = dataobj.getString("e_amount");
                esc_mobile1 = dataobj.getString("e_mobile1");
                esc_mobile2 = dataobj.getString("e_mobile2");
                esc_village = dataobj.getString("e_village");
                esc_district = dataobj.getString("e_district");
                esc_niyojakavargam = dataobj.getString("e_neyojakavargam");
                esc_pincode = dataobj.getString("e_pincode");
                esc_createdOn = dataobj.getString("e_created_on");

                name.setText((CharSequence) esc_name);
                amount.setText((CharSequence) esc_amount);
                phone_number1.setText((CharSequence) esc_mobile1);
                phone_number2.setText((CharSequence) esc_mobile2);
                villege.setText((CharSequence) esc_village);
                district.setText((CharSequence) esc_district);
                niyojakavargam.setText((CharSequence) esc_niyojakavargam);
                pincode.setText((CharSequence) esc_pincode);
                createdOn.setText((CharSequence) esc_createdOn);


            }
             else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

