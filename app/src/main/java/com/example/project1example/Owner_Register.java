package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Owner_Register extends AppCompatActivity {
    EditText name,phone_number,address,niyojakavargam,pincode;
    String str_name,str_phone_number,str_address,str_niyojakavargam,str_pincode,str_image;
    Button register_btn;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__register);
        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        niyojakavargam = findViewById(R.id.niyojakavargam);
        pincode = findViewById(R.id.pincode);
        register_btn = findViewById(R.id.register_btn);
        progress_bar = findViewById(R.id.progress_bar);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_vals();
            }
        });
    }

    private void validate_vals() {
        str_name = name.getText().toString();
        str_phone_number = phone_number.getText().toString();
        str_address = address.getText().toString();
        str_niyojakavargam = niyojakavargam.getText().toString();
        str_pincode = pincode.getText().toString();

        if (!str_name.equalsIgnoreCase("") && !str_phone_number.equalsIgnoreCase("")
                && !str_address.equalsIgnoreCase("") && !str_niyojakavargam.equalsIgnoreCase("")
                && !str_pincode.equalsIgnoreCase("")) {
            getResponse();
        } else {
            Toast.makeText(Owner_Register.this, "Fill all the values", Toast.LENGTH_SHORT).show();
        }
    }

    private void getResponse() {
        progress_bar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(login_interface.JSON_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> call = api.register(str_name,str_phone_number,str_address,str_niyojakavargam,str_pincode,"12345","owner","","active");

        call.enqueue(new Callback<String>(){

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
            if (obj.optString("status").equalsIgnoreCase("true")){
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(Owner_Register.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, AdminPanelActivity.class);
                startActivity(i);
                finish();

            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(Owner_Register.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(Owner_Register.this, "Error in server", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}