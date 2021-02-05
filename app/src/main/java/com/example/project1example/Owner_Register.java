package com.example.project1example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
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
    EditText name,phone_number,address,district,niyojakavargam,pincode;
    String str_name,str_phone_number,str_address,str_district,str_niyojakavargam,str_pincode,str_image;
    Button register_btn;
    ProgressBar progress_bar;
    Toolbar toolbar;
    ImageView i2;
    Uri imageUri;


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
                img_click = 1;
                cameragallery(i2);
            }
        });



        try {//for converting image to base64 encoding
            if (i2.getDrawable() == null) {
                val_logo = "";
//                        Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap image = ((BitmapDrawable) i2.getDrawable()).getBitmap();
                ByteArrayOutputStream byteA = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteA);
                val_logo = Base64.encodeToString(byteA.toByteArray(), Base64.DEFAULT);
            }
        } catch (Exception e) {
//                    Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
        }




        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);
        district=findViewById( R.id.distict);
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
       // str_district=district.getText().toString();
        str_niyojakavargam = niyojakavargam.getText().toString();
        str_pincode = pincode.getText().toString();

        if (!str_name.equalsIgnoreCase("") && !str_phone_number.equalsIgnoreCase("")
                && !str_address.equalsIgnoreCase("")  && !str_niyojakavargam.equalsIgnoreCase("")
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
        Call<String> call = api.register(str_name,str_phone_number,str_address,str_district,str_niyojakavargam,str_pincode,"12345","owner",val_logo,"active");

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





    //For camera and gallery
    @TargetApi(Build.VERSION_CODES.M)
    public void cameragallery(final ImageView img) {
        if (ContextCompat.checkSelfPermission(Owner_Register.this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(Owner_Register.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            GalleryPictureIntent();
            if (ContextCompat.checkSelfPermission(Owner_Register.this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                img.setImageBitmap(r.getBitmap());
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                                img.setImageBitmap(null);
                            }
                        }).show(Owner_Register.this);
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_RESULT);
            }
        } else {
            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, GALLERY_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (img_click == 1) {
                    cameragallery(i2);
                }
            } else {
                //Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                permssiondialog();
            }
        }
        if (requestCode == GALLERY_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (img_click == 1) {
                    cameragallery(i2);
                }
            } else {
                // Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                permssiondialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
                else {
                    //code for deny
                    Toast.makeText(Owner_Register.this, "Please grant permissions", Toast.LENGTH_LONG).show();
                }
            }


        }
    }

    private void permssiondialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Register.this);
        builder.setCancelable(false);
        builder.setTitle("App requires Storage permissions to work perfectly..!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                permission = true;
                dialog.dismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }



}