package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Add_billescaper extends AppCompatActivity {

    String uid,str_name,str_phone_number1,str_phone_number2,str_address,str_niyojakavargam,str_pincode,str_amount,str_image;
    EditText name,phone_number1,phone_number2,address,niyojakavargam,pincode,amount;
    ImageView image1;
    Button btn_add_escaper;
    RelativeLayout progress_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_billescaper);
        uid = getIntent().getStringExtra("uid");
        btn_add_escaper = findViewById(R.id.btn_add_escaper);
        name = findViewById(R.id.name);
        phone_number1 = findViewById(R.id.phone_number1);
        phone_number2 = findViewById(R.id.phone_number2);
        address = findViewById(R.id.address);
        niyojakavargam = findViewById(R.id.niyojakavargam);
        pincode = findViewById(R.id.pincode);
        amount = findViewById(R.id.amount);
        image1 = findViewById(R.id.image1);
        progress_layout = findViewById(R.id.progress_layout);

        btn_add_escaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val_vals();
                try {

                    if (!str_name.equalsIgnoreCase("")&&!str_phone_number1.equalsIgnoreCase("")&&!str_amount.equalsIgnoreCase("")) {

                        getResponse();
                    } else {
                        Toast.makeText(Add_billescaper.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Add_billescaper.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getResponse() {

        progress_layout.setVisibility(View.VISIBLE);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(80, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(login_interface.JSON_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> call = api.register_escaper(str_name,str_phone_number1,str_phone_number2,str_amount,str_address,str_niyojakavargam,str_pincode,uid,"","pending");
        call.enqueue(new Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        String jsonresponse = response.body().toString();
                        writeTv(jsonresponse);
                    } else {
                        Log.i("Responsestring", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                Log.i("Responsestring", "Failed");
            }
        });
    }

    private void writeTv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")) {

                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Add_billescaper.this, "Escaper Added Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Add_billescaper.this, Dashboard.class);
                i.putExtra("uid",uid);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Add_billescaper.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Add_billescaper.this, "Error in server", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void val_vals() {
        str_name = name.getText().toString();
        str_phone_number1 = phone_number1.getText().toString().trim();
        str_phone_number2 = phone_number2.getText().toString().trim();
        str_address = address.getText().toString();
        str_pincode = pincode.getText().toString();
        str_amount = amount.getText().toString().trim();
        str_niyojakavargam = niyojakavargam.getText().toString();

//        try {//for converting image to base64 encoding
//            if (i2.getDrawable() == null) {
//                val_logo = "";
////                        Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
//            } else {
//                Bitmap image = ((BitmapDrawable) i2.getDrawable()).getBitmap();
//                ByteArrayOutputStream byteA = new ByteArrayOutputStream();
//                image.compress(Bitmap.CompressFormat.JPEG, 100, byteA);
//                val_logo = Base64.encodeToString(byteA.toByteArray(), Base64.DEFAULT);
//
//            }
//        } catch (Exception e) {
////                    Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
//        }
    }
}