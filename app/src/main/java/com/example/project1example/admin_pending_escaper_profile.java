package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1example.model.billescapers_list_model;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class admin_pending_escaper_profile extends AppCompatActivity {

    String Base_URL, bid, uidi;
    RelativeLayout progress_layout;
    TextView name, amount, phone_number1, phone_number2, villege, district, niyojakavargam, pincode, createdOn,status;

    MaterialButton button;
    String getbid;
    Toolbar toolbar;

    billescapers_list_model billescapers_list_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_pending_escaper_profile );


        billescapers_list_model = (billescapers_list_model) this.getIntent().getSerializableExtra( "list" );

        getbid = billescapers_list_model.getBid();

        progress_layout = findViewById(R.id.progress_layout);
        name = findViewById(R.id.nameesc);
        amount = findViewById(R.id.amountesc);
        phone_number1 = findViewById(R.id.mobilenumber1esc);
        phone_number2 = findViewById(R.id.mobilenumber2esc);
        villege = findViewById(R.id.villageesc);
        district = findViewById(R.id.districtesc);
        niyojakavargam = findViewById(R.id.neyojakavargamesc);
        pincode = findViewById(R.id.pincodeesc);
        createdOn = findViewById(R.id.created_onesc);
        status=findViewById( R.id.activestatus );
        button=findViewById( R.id.update_edtBillESc);

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
        status.setText( billescapers_list_model.getStatus() );



        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse();
            }
        } );
    }





    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Bill Escapers" );
        toolbar.setNavigationIcon(  getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }


    private void getResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(login_interface.JSON_URL)
                .addConverterFactory( ScalarsConverterFactory.create())
                .build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> call = api.update_billescaper_status(getbid, "accepted");
        Toast.makeText(admin_pending_escaper_profile.this, getbid, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();

                        writeTv(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void writeTv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")) {
                Toast.makeText(admin_pending_escaper_profile.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(admin_pending_escaper_profile.this, AdminPanelActivity.class);
                startActivity(i);


            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                Toast.makeText(admin_pending_escaper_profile.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(admin_pending_escaper_profile.this, "Error in server", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}