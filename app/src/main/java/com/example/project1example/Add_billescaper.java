package com.example.project1example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Add_billescaper extends AppCompatActivity {

    String uid,str_name,str_phone_number1,str_phone_number2,str_villege,str_district,str_niyojakavargam,str_pincode,val_image,str_amount,str_image,str_desc;
    EditText name,phone_number1,phone_number2,villege,district,niyojakavargam,pincode,amount,description;
    ImageView image1;
    Button btn_add_escaper;
    RelativeLayout progress_layout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_billescaper);
        uid = getIntent().getStringExtra("uid");
        btn_add_escaper = findViewById(R.id.btn_add_escaper);
        name = findViewById(R.id.name);
        phone_number1 = findViewById(R.id.phone_number1);
        phone_number2 = findViewById(R.id.phone_number2);
        villege = findViewById(R.id.villege);
        district=findViewById( R.id.distict );
        niyojakavargam = findViewById(R.id.neyojakavargam);
        pincode = findViewById(R.id.pincode);
        amount = findViewById(R.id.amount);
        description=findViewById( R.id.description );
        image1 = findViewById(R.id.image1);
        progress_layout = findViewById(R.id.progress_layout);

        configureToolbar();

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Add_billescaper.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            image1.setImageURI(fileUri);

//            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!
//
//                    //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "ImagePicker error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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
        Call<String> call = api.register_escaper(str_name,str_phone_number1,str_phone_number2,str_amount,str_villege,str_district,str_niyojakavargam,str_pincode,uid,val_image,"pending",str_desc);
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
        str_villege = villege.getText().toString();
        str_district=district.getText().toString();
        str_pincode = pincode.getText().toString();
        str_amount = amount.getText().toString().trim();
        str_desc=description.getText().toString().trim();
        str_niyojakavargam = niyojakavargam.getText().toString();

        try {//for converting image to base64 encoding
            if (image1.getDrawable() == null) {
                val_image = "";
//                        Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap image = ((BitmapDrawable) image1.getDrawable()).getBitmap();
                ByteArrayOutputStream byteA = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteA);
                val_image = Base64.encodeToString(byteA.toByteArray(), Base64.DEFAULT);

            }
        } catch (Exception e) {
//                    Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
        }
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Add Bill Escaper" );
        toolbar.setNavigationIcon(  getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }

}