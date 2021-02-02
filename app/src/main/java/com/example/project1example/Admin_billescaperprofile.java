package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Admin_billescaperprofile extends AppCompatActivity {
    String Base_URL, bid,uidi;
    LinearLayout delete_billEsc,edit_billEsc;
    RelativeLayout progress_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_billescaperprofile);

        delete_billEsc = findViewById(R.id.delete_lv);
        edit_billEsc = findViewById(R.id.edit_lv);


        delete_billEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_billescapers();
            }
        });

        edit_billEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_billescaperprofile.this,Edit_owner.class);
                i.putExtra("uid",uidi);
                startActivity(i);
            }
        });

    }

    private void delete_billescapers() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();

        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.delete_billescapers(bid);
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
            if (obj.optString("status").equalsIgnoreCase("true")){
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_billescaperprofile.this,bid,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Admin_billescaperprofile.this,AdminPanelActivity.class);
                startActivity(i);
            }else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}