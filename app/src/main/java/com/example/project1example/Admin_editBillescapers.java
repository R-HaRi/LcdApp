package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.project1example.model.SharedPrefs_model;
import com.example.project1example.model.billescapers_list_model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Admin_editBillescapers extends AppCompatActivity {

    String e_bid, Base_URL;
    TextInputEditText name, amount, phone_number1, phone_number2, villege, district, niyojakavargam, pincode;
    MaterialButton update;
    billescapers_list_model spm;
    RelativeLayout progress_layout;
    String esc_name, esc_amount, esc_mobile1, esc_mobile2, esc_village, esc_district, esc_niyojakavargam, esc_pincode;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_billescapers);
        e_bid = getIntent().getStringExtra("edit_bid");

        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        phone_number1 = findViewById(R.id.mobilenumber1);
        phone_number2 = findViewById(R.id.mobilenumber2);
        villege = findViewById(R.id.village);
        district = findViewById(R.id.district);
        niyojakavargam = findViewById(R.id.neyojakavargam);
        pincode = findViewById(R.id.pincode);
        update = findViewById(R.id.update_edtBillESc);
        spm = new billescapers_list_model();
        progress_layout = findViewById(R.id.progress_layout);

        edit_billEsc();

        configureToolbar();

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_string_values();
                update_billEsc();
                Intent i = new Intent(Admin_editBillescapers.this,Admin_billescaperprofile.class);
                i.putExtra("update_bid",e_bid);
                startActivity(i);
            }
        });

    }

    private void edit_billEsc() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.get_escaper_profile_bid(e_bid);
        Call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("ResponseString", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("ResponseString", response.body().toString());
                        String jsonresponse = response.body().toString();
                        WriteTV_edit_billESc(jsonresponse);
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

    private void WriteTV_edit_billESc(String jsonresponse) {
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

                name.setText((CharSequence) esc_name);
                amount.setText((CharSequence) esc_amount);
                phone_number1.setText((CharSequence) esc_mobile1);
                phone_number2.setText((CharSequence) esc_mobile2);
                villege.setText((CharSequence) esc_village);
                district.setText((CharSequence) esc_district);
                niyojakavargam.setText((CharSequence) esc_niyojakavargam);
                pincode.setText((CharSequence) esc_pincode);
            } else if (obj.optString("status").equalsIgnoreCase("false")){
                progress_layout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void update_billEsc() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.update_billescaperprofile(e_bid,esc_name,esc_mobile1,esc_mobile2,esc_amount,esc_village,esc_district,esc_niyojakavargam,pincode);
        Call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("ResponseString", response.body().toString());
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        Log.i("ResponseString",response.body().toString());
                        String jsonresponse = response.body().toString();
                        WriteTV_update_billESc(jsonresponse);
                    } else {
                        Log.i("ResponseString","returned empty response");
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.i("Retrofail","Failed");

            }
        });
    }

    private void WriteTV_update_billESc(String jsonresponse) {
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

                name.setText((CharSequence) esc_name);
                amount.setText((CharSequence) esc_amount);
                phone_number1.setText((CharSequence) esc_mobile1);
                phone_number2.setText((CharSequence) esc_mobile2);
                villege.setText((CharSequence) esc_village);
                district.setText((CharSequence) esc_district);
                niyojakavargam.setText((CharSequence) esc_niyojakavargam);
                pincode.setText((CharSequence) esc_pincode);


            }
            else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void get_string_values() {
        esc_name = name.getText().toString().trim();
        esc_amount = amount.getText().toString().trim();
        esc_mobile1 = phone_number1.getText().toString().trim();
        esc_mobile2 = phone_number2.getText().toString().trim();
        esc_village = villege.getText().toString().trim();
        esc_district = district.getText().toString().trim();
        esc_niyojakavargam = niyojakavargam.getText().toString().trim();
        esc_pincode = pincode.getText().toString().trim();
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Bill Escapers" );
        toolbar.setNavigationIcon(  getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }

}