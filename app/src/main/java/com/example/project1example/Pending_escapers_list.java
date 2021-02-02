package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.project1example.adapter.for_adminpendingbillescaper_list_adapter;
import com.example.project1example.adapter.for_billescaper_list_adapter;
import com.example.project1example.adapter.for_pendingbillescaper_list_adapter;
import com.example.project1example.model.SharedPrefs_model;
import com.example.project1example.model.billescapers_list_model;
import com.example.project1example.model.login_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Pending_escapers_list extends AppCompatActivity {

    String uid,Base_URL;
    RecyclerView recyclerlist;
    RelativeLayout progress_layout,error_layout;
    SharedPreferences sharedPreferences;
    Boolean isLogged;
    SharedPrefs_model spm;
    String uidi;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_escapers_list);
        sharedPreferences = this.getSharedPreferences("loggeduser", MODE_PRIVATE);
        isLogged = sharedPreferences.getBoolean("islogged", false);
        spm = new SharedPrefs_model(this);


        recyclerlist = findViewById(R.id.recyclerlist);
        progress_layout = findViewById(R.id.progress_layout);
        error_layout = findViewById(R.id.error_layout);


        configureToolbar();
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        if (spm.getRole().equalsIgnoreCase("admin")){
            getResponseAdmin();
        }else{
            uid = getIntent().getStringExtra("uid");
            getResponse();
        }


    }

    private void getResponseAdmin() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();

        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.get_escapers_uid(uid);
        Call.enqueue(new Callback<String>(){

            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("Response String", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("onsuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        Writetvadmin(jsonresponse);

                    } else {
                        error_layout.setVisibility(View.VISIBLE);
                        Log.i("onEmptyResponse", "Returned empty response");

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
                Log.i("retrofail", "Failed");
            }
        });
    }

    private void Writetvadmin(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")){
                ArrayList<billescapers_list_model> retroModelArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++){
                    billescapers_list_model retroModel = new billescapers_list_model();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    retroModel.setBid(dataobj.getString("bid"));
                    retroModel.setAmount(dataobj.getString("e_amount"));
                    retroModel.setName(dataobj.getString("e_name"));
                    retroModel.setImage(dataobj.getString("e_image"));
                    retroModel.setAddress(dataobj.getString("e_address"));
                    retroModel.setNeyojakavargam(dataobj.getString("e_neyojakavargam"));
                    retroModel.setMobile1(dataobj.getString("e_mobile1"));
                    retroModel.setMobile2(dataobj.getString("e_mobile2"));
                    retroModel.setPincode(dataobj.getString("e_pincode"));
                    retroModel.setCreated_on(dataobj.getString("e_created_on"));
                    retroModel.setO_name(dataobj.getString("name"));
                    retroModel.setStatus(dataobj.getString("status"));

                    retroModelArrayList.add(retroModel);
                }
                progress_layout.setVisibility(View.GONE);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerlist.setLayoutManager(gridLayoutManager);
                for_adminpendingbillescaper_list_adapter adapter = new for_adminpendingbillescaper_list_adapter(retroModelArrayList, this);
                recyclerlist.setAdapter(adapter);

            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
                Toast.makeText(Pending_escapers_list.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                progress_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
                Toast.makeText(Pending_escapers_list.this, "Error in server", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResponse() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();

        login_interface api = retrofit.create(login_interface.class);
        uidi = getIntent().getStringExtra("uid");
        Call<String> Call = api.get_escapers_uid(uidi);
        Call.enqueue(new Callback<String>(){

            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("Response String", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("onsuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        Writetv(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
                Log.i("retrofail", "Failed");
            }
        });
    }

    private void Writetv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")){
                ArrayList<billescapers_list_model> retroModelArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++){
                    billescapers_list_model retroModel = new billescapers_list_model();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    retroModel.setBid(dataobj.getString("bid"));
                    retroModel.setAmount(dataobj.getString("e_amount"));
                    retroModel.setName(dataobj.getString("e_name"));
                    retroModel.setImage(dataobj.getString("e_image"));
                    retroModel.setAddress(dataobj.getString("e_address"));
                    retroModel.setNeyojakavargam(dataobj.getString("e_neyojakavargam"));
                    retroModel.setMobile1(dataobj.getString("e_mobile1"));
                    retroModel.setMobile2(dataobj.getString("e_mobile2"));
                    retroModel.setPincode(dataobj.getString("e_pincode"));
                    retroModel.setCreated_on(dataobj.getString("e_created_on"));
                    retroModel.setStatus(dataobj.getString("status"));

                    retroModelArrayList.add(retroModel);
                }

                progress_layout.setVisibility(View.GONE);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerlist.setLayoutManager(gridLayoutManager);
                for_pendingbillescaper_list_adapter adapter = new for_pendingbillescaper_list_adapter(retroModelArrayList, this);
                recyclerlist.setAdapter(adapter);

            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
                Toast.makeText(Pending_escapers_list.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                progress_layout.setVisibility(View.GONE);
                error_layout.setVisibility(View.VISIBLE);
                Toast.makeText(Pending_escapers_list.this, "Error in server", Toast.LENGTH_SHORT).show();
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