package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.project1example.adapter.for_owner_list_adapter;
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

public class OwnersListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout progressbar;
    String Base_URL,str_searchvalue;
    EditText search;
    Button btn_search;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners_list);
        recyclerView=findViewById(R.id.ownwerecyclelist);
        progressbar=findViewById(R.id.recycle_progressbar);
        btn_search=findViewById(R.id.btn_search);
        search=findViewById(R.id.search);

        str_searchvalue = search.getText().toString().trim();


        configureToolbar();
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(str_searchvalue)){
                    search.setError("Enter data here");
                }else{
                    Intent i = new Intent(OwnersListActivity.this,OwnerSearchActivity.class);
                    i.putExtra("searchvalue",str_searchvalue);
                    startActivity(i);
                }
            }
        });

        getResponse();


    }

    private void getResponse() {
        progressbar.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();

        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.get_owners_list("owner");
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
                progressbar.setVisibility(View.GONE);
                Log.i("retrofail", "Failed");
            }
        });
    }

    private void Writetv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")){
                ArrayList<owner_list_model> retroModelArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++){
                    owner_list_model retroModel = new owner_list_model();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    retroModel.setUid(dataobj.getString("uid"));
                    retroModel.setRole(dataobj.getString("role"));
                    retroModel.setName(dataobj.getString("name"));
                    retroModel.setMobile(dataobj.getString("mobile"));
                    retroModel.setImage(dataobj.getString("image"));
                    retroModel.setAddress(dataobj.getString("village")+dataobj.getString("district"));
                    retroModel.setNeyojakavargam(dataobj.getString("neyojakavargam"));
                    retroModel.setPincode(dataobj.getString("pincode"));
                    retroModel.setCreated_on(dataobj.getString("created_on"));

                    retroModelArrayList.add(retroModel);
                }

                progressbar.setVisibility(View.GONE);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                for_owner_list_adapter adapter = new for_owner_list_adapter(retroModelArrayList, this);
                recyclerView.setAdapter(adapter);

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