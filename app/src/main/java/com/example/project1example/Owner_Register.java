package com.example.project1example;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Owner_Register extends AppCompatActivity {
    EditText name,phone_number,address,district,niyojakavargam,pincode,serialnumber;
    String str_name,str_phone_number,str_address,str_district,str_niyojakavargam,str_pincode,str_image,str_serialnum;
    Button register_btn;
    ProgressBar progress_bar;
    Toolbar toolbar;
    ImageView i2;


    private final int CAMERA_RESULT = 101, GALLERY_RESULT = 102;
    private boolean permission = false;
    int img_click; String val_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner__register);

        configureToolbar();
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );



        i2 =findViewById( R.id.addadvert );
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(Owner_Register.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });



        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        district=findViewById( R.id.districtowner);
        niyojakavargam = findViewById(R.id.niyojakavargam);
        pincode = findViewById(R.id.pincode);
        serialnumber=findViewById( R.id.serialnumber );
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
        str_district=district.getText().toString();
        str_niyojakavargam = niyojakavargam.getText().toString();
        str_pincode = pincode.getText().toString();
        str_serialnum=serialnumber.getText().toString();


        try {//for converting image to base64 encoding
            if (i2.getDrawable() == null) {
                val_logo = "";
                        Toast.makeText(Owner_Register.this, "Please select a logo", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap image = ((BitmapDrawable) i2.getDrawable()).getBitmap();
                ByteArrayOutputStream byteA = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteA);
                val_logo = Base64.encodeToString(byteA.toByteArray(), Base64.DEFAULT);
            }
        } catch (Exception e) {
//                    Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
        }





        if (!str_name.equalsIgnoreCase("") && !str_phone_number.equalsIgnoreCase("")
                && !str_address.equalsIgnoreCase("") && !str_district.equalsIgnoreCase("")  && !str_niyojakavargam.equalsIgnoreCase("")
                && !str_pincode.equalsIgnoreCase("") && !str_serialnum.equalsIgnoreCase(""))  {
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
        Call<String> call = api.register(str_name,str_phone_number,str_address,str_district,str_niyojakavargam,str_pincode,"12345","owner",val_logo,"active",str_serialnum);

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

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Owner Register" );
        toolbar.setNavigationIcon(  getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }




    //camera work
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            i2.setImageURI(fileUri);

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


}