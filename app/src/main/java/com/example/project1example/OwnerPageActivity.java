package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.project1example.adapter.for_billescaper_list_adapter;
import com.example.project1example.adapter.for_owner_list_adapter;
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

public class OwnerPageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout progressbar;
    String uid,Base_URL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_page);
        progressbar=findViewById(R.id.recycle_progressbar);
        recyclerView=findViewById(R.id.recycl_billescaper);
        uid = getIntent().getStringExtra("uid");

        getResponse();

    }

    private void getResponse() {
        progressbar.setVisibility(View.VISIBLE);
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

                    retroModelArrayList.add(retroModel);
                }

                progressbar.setVisibility(View.GONE);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(gridLayoutManager);
                for_billescaper_list_adapter adapter = new for_billescaper_list_adapter(retroModelArrayList, this,recyclerView);
                recyclerView.setAdapter(adapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}