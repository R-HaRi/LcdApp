package com.example.project1example;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1example.model.billescapers_list_model;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Admin_editBillescapers extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String e_bid, Base_URL;
    TextInputEditText name, amount, phone_number1, phone_number2, villege, district, niyojakavargam, pincode;
    MaterialButton update;
    billescapers_list_model spm;
    RelativeLayout progress_layout;
    String esc_name, esc_amount, esc_mobile1, esc_mobile2, esc_village, esc_district, esc_niyojakavargam, esc_pincode,esc_status;
    Toolbar toolbar;
    String val_logo;

    billescapers_list_model billescapers_list_model;
    String[] Status={"accepted","pending",};

    ImageView  profileImage;

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_billescapers);
        billescapers_list_model = (billescapers_list_model) this.getIntent().getSerializableExtra( "list" );
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        phone_number1 = findViewById(R.id.mobilenumber1);
        phone_number2 = findViewById(R.id.mobilenumber2);
        villege = findViewById(R.id.village);
        district = findViewById(R.id.district);
        niyojakavargam = findViewById(R.id.neyojakavargam);
        pincode = findViewById(R.id.pincode);
        update = findViewById(R.id.update_edtBillESc);
        profileImage=findViewById( R.id.profile_img );

        spm = new billescapers_list_model();
        progress_layout = findViewById(R.id.progress_layout);
         e_bid = billescapers_list_model.getBid();

         spinner=findViewById( R.id.simpleSpinner );

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Status);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);



        configureToolbar();

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );


        profileImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Admin_editBillescapers.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        } );




        name.setText( billescapers_list_model.getName() );
        amount.setText( billescapers_list_model.getAmount() );
        phone_number1.setText( billescapers_list_model.getMobile1() );
        phone_number2.setText( billescapers_list_model.getMobile2() );
        villege.setText( billescapers_list_model.getAddress() );
        district.setText( billescapers_list_model.getDistrict() );
        niyojakavargam.setText( billescapers_list_model.getNeyojakavargam() );
        pincode.setText( billescapers_list_model.getPincode() );

        Glide.with(getApplicationContext()).load(login_interface.JSON_URL + billescapers_list_model.getImage()).placeholder(R.drawable.dummylogo).into(profileImage);

        Log.d("img2",billescapers_list_model.getImage());



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_vals();
            }
        });

    }

    private void validate_vals() {
        esc_name = name.getText().toString().trim();
        esc_amount = amount.getText().toString().trim();
        esc_mobile1 = phone_number1.getText().toString().trim();
        esc_mobile2 = phone_number2.getText().toString().trim();
        esc_village = villege.getText().toString().trim();
        esc_district = district.getText().toString().trim();
        esc_niyojakavargam = niyojakavargam.getText().toString().trim();
        esc_pincode = pincode.getText().toString().trim();


        try {//for converting image to base64 encoding
            if (profileImage.getDrawable() == null) {
                val_logo = "";
//                        Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap image = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                ByteArrayOutputStream byteA = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteA);
                val_logo = Base64.encodeToString(byteA.toByteArray(), Base64.DEFAULT);
            }
        } catch (Exception e) {
//                    Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
        }



        Log.d("img1",val_logo);
        Log.d("img2",billescapers_list_model.getImage());


        if (!esc_name.equalsIgnoreCase("") && !esc_amount.equalsIgnoreCase("")
                && !esc_mobile1.equalsIgnoreCase("") && !esc_village.equalsIgnoreCase("")  && !esc_niyojakavargam.equalsIgnoreCase("")
                && !esc_pincode.equalsIgnoreCase("") && !esc_status.equalsIgnoreCase( "" ) )  {
            update_billEsc();
        } else {
            Toast.makeText(Admin_editBillescapers.this, "Fill all the values", Toast.LENGTH_SHORT).show();
        }

    }


    private void update_billEsc() {
        progress_layout.setVisibility(View.VISIBLE);
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
                addConverterFactory(ScalarsConverterFactory.create()).build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> Call = api.update_billescaperprofile(e_bid,esc_name,esc_mobile1,esc_mobile2,esc_amount,esc_village,esc_district,esc_niyojakavargam,esc_pincode,esc_status,val_logo,billescapers_list_model.getImage());
        Call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i("ResponseString", response.body().toString());
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        Log.i("ResponseString",response.body().toString());
                        String jsonresponse = response.body().toString();
                        WriteTV_update_billESc(jsonresponse);
                    } else {
                        Log.i("ResponseString","returned empty response");
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.i("Retrofail","Failed");

            }
        });
    }

    private void WriteTV_update_billESc(String jsonresponse) {
        try {
            JSONObject obj = new JSONObject(jsonresponse);
            if (obj.optString("status").equalsIgnoreCase("true")) {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_editBillescapers.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, AdminPanelActivity.class);
                startActivity(i);
                finish();
            }
            else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_editBillescapers.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            progress_layout.setVisibility(View.GONE);
            Toast.makeText(Admin_editBillescapers.this, "Something went wrong Please try again later", Toast.LENGTH_SHORT).show();
        }
    }


    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Bill Escapers" );
        toolbar.setNavigationIcon(  getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), Status[position], Toast.LENGTH_LONG).show();
            esc_status=Status[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            profileImage.setImageURI(fileUri);

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