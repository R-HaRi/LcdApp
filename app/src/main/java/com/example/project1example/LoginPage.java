package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1example.model.SharedPrefs_model;
import com.example.project1example.model.login_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.armcha.elasticview.ElasticView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginPage extends AppCompatActivity {
    EditText phone_number,passwrd;
    ElasticView login_btn;
    String str_phone,str_password;
    ProgressBar progress_bar;
    SharedPreferences sharedPreferences;
    Boolean isLogged;
    String s_name,s_phone,s_uid,s_role,s_address,s_pincode,s_niyojakavargam,s_image;

    SharedPrefs_model spm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        phone_number=findViewById(R.id.phone_number);
        passwrd=findViewById(R.id.passwrd);
        progress_bar=findViewById(R.id.progress_bar);
        spm = new SharedPrefs_model(this);

        login_btn=findViewById(R.id.login_btn);
        sharedPreferences = this.getSharedPreferences("loggeduser", MODE_PRIVATE);
        isLogged = sharedPreferences.getBoolean("islogged", false);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_phone = phone_number.getText().toString();
                str_password = passwrd.getText().toString();
                if (TextUtils.isEmpty(str_phone)){
                    phone_number.setError("Enter Registered Phone number");
                }
                if (TextUtils.isEmpty(str_password)){
                    passwrd.setError("Enter Password");
                }

                getResponse();

            }
        });


    }

    private void getResponse() {

        progress_bar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(login_interface.JSON_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        login_interface api = retrofit.create(login_interface.class);

        Call<String> call = api.login(str_phone,str_password);

        call.enqueue(new Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress_bar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("retrofit_response", response.body().toString());
                        String jsonresponse = response.body();
                        writeTv(jsonresponse);

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
//                error_layout.setVisibility(View.VISIBLE);
                Log.i("retrofit_response", "response failed " + t.toString());
            }
        });
    }


    private void writeTv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")) {
                JSONArray dataArray = obj.getJSONArray("data");
                JSONObject dataobj = dataArray.getJSONObject(0);

                spm.setIslogged(true);
                spm.setUid(dataobj.getString("uid"));
                spm.setName(dataobj.getString("name"));
                spm.setMobile(dataobj.getString("mobile"));
                spm.setRole(dataobj.getString("role"));
                spm.setImage(dataobj.getString("image"));
                spm.setAddress(dataobj.getString("address"));
                spm.setPincode(dataobj.getString("pincode"));
                spm.setNeyojakavargam(dataobj.getString("neyojakavargam"));
                spm.setP_status(dataobj.getString("p_status"));

                progress_bar.setVisibility(View.GONE);
                Toast.makeText(LoginPage.this, "Login Success", Toast.LENGTH_SHORT).show();
                if (spm.getRole().equalsIgnoreCase("admin")) {
                    Intent i = new Intent(LoginPage.this, AdminPanelActivity.class);
                    startActivity(i);
                } else if (spm.getRole().equalsIgnoreCase("owner")) {
                    Intent i = new Intent(LoginPage.this, Dashboard.class);
                    i.putExtra("uid",spm.getUid());
                    startActivity(i);
                }

            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(LoginPage.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(LoginPage.this, "Error in server", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}