package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1example.adapter.for_adminpendingbillescaper_list_adapter;
import com.example.project1example.model.SharedPrefs_model;
import com.example.project1example.model.billescapers_list_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Admin_ownerProfile extends AppCompatActivity {
    String o_uid, Base_URL;

    RelativeLayout progress_layout, error_layout;
    String str_name, str_mobile, str_role, str_profilepic, uidi, str_pstatus, str_ostatus;
    TextView name, mobile, role;
    ImageView profilepic;
    RecyclerView recyclerView;
    LinearLayout admin_control_layout, activate_lv, suspand_lv, delete_lv, edit_lv, call, WhatsApp;
    SharedPrefs_model spm;
    Toolbar toolbar;
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 101;
    String Phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_owner_profile );
        progress_layout = findViewById( R.id.progress_layout );
        o_uid = getIntent().getStringExtra( "uid" );

        Log.d( "uid", o_uid );

        name = findViewById( R.id.name );
        mobile = findViewById( R.id.mobile );
        role = findViewById( R.id.role );
        profilepic = findViewById( R.id.profilepic );
        recyclerView = findViewById( R.id.recycler );
        error_layout = findViewById( R.id.error_layout );
        admin_control_layout = findViewById( R.id.admin_control_layout2 );
        activate_lv = findViewById( R.id.activate_lv );
        suspand_lv = findViewById( R.id.suspand_lv );
        delete_lv = findViewById( R.id.delete_lv );
        edit_lv = findViewById( R.id.edit_lv );
        call = findViewById( R.id.call_lv );
        WhatsApp = findViewById( R.id.message_lv );
        spm = new SharedPrefs_model( this );

        configureToolbar();

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );


        getResponseprofile();
        getResponseAdmin();
        if (spm.getRole().equalsIgnoreCase( "owner" )) {
            admin_control_layout.setVisibility( View.GONE );
        }


        activate_lv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_pstatus = "active";
                getResponse_pstatus();
            }
        } );
        suspand_lv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_pstatus = "deactive";
                getResponse_pstatus();
            }
        } );
        delete_lv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponse_pstatusdelete();
            }
        } );

        edit_lv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( Admin_ownerProfile.this, Edit_owner.class );
                i.putExtra( "uid", uidi );
                startActivity( i );

            }
        } );

        call.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone = str_mobile;

                Intent i = new Intent( Intent.ACTION_DIAL );
                i.setData( Uri.parse( "tel:" + Phone ) );
                startActivity( i );

//             Intent callIntent = new Intent(Intent.ACTION_CALL);
//             callIntent.setData(Uri.parse(Phone));
//             if (ActivityCompat.checkSelfPermission(Admin_ownerProfile.this,
//                     Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                 return;
//             }
//             startActivity(callIntent);
            }
        } );
        WhatsApp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse( "smsto:" + Phone );
//                Intent i = new Intent( Intent.ACTION_SENDTO, uri );
//                i.setPackage( "com.whatsapp" );
//                startActivity( Intent.createChooser( i, "" ) );
                openWhatsApp(v);
            }
        } );

    }

    private void getResponse_pstatusdelete() {
        progress_layout.setVisibility( View.VISIBLE );

        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();

        login_interface api = retrofit.create( login_interface.class );
        Call<String> Call = api.delete_owner( o_uid );
        Call.enqueue( new Callback<String>() {

            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i( "ResponseString", response.body().toString() );
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i( "ResponseString", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        Writetv_pstatus( jsonresponse );

                    } else {
                        Log.i( "ResponseString", "Returned empty response" );

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                Log.i( "retrofail", "Failed" );
            }
        } );
    }

    private void getResponse_pstatus() {
        progress_layout.setVisibility( View.VISIBLE );

        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();

        login_interface api = retrofit.create( login_interface.class );
        Call<String> Call = api.update_owner_status( o_uid, str_pstatus );
        Call.enqueue( new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i( "ResponseString", response.body().toString() );
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i( "ResponseString", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        Writetv_pstatus( jsonresponse );

                    } else {
                        Log.i( "ResponseString", "Returned empty response" );

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                Log.i( "retrofail", "Failed" );
            }
        } );

    }

    private void Writetv_pstatus(String response) {
        try {
            JSONObject obj = new JSONObject( response );
            if (obj.optString( "status" ).equalsIgnoreCase( "true" )) {
                progress_layout.setVisibility( View.GONE );
                Toast.makeText( Admin_ownerProfile.this, o_uid + str_pstatus, Toast.LENGTH_SHORT ).show();
                Intent i = new Intent( Admin_ownerProfile.this, AdminPanelActivity.class );
                startActivity( i );
            } else if (obj.optString( "status" ).equalsIgnoreCase( "false" )) {
                progress_layout.setVisibility( View.GONE );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getResponseprofile() {
        progress_layout.setVisibility( View.VISIBLE );

        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();

        login_interface api = retrofit.create( login_interface.class );
        Call<String> Call = api.get_profile_uid( o_uid );
        Call.enqueue( new Callback<String>() {

            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i( "ResponseString", response.body().toString() );
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i( "ResponseString", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        Writetv_profile( jsonresponse );

                    } else {
                        Log.i( "ResponseString", "Returned empty response" );

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                Log.i( "retrofail", "Failed" );
            }
        } );
    }

    private void Writetv_profile(String response) {
        try {
            JSONObject obj = new JSONObject( response );
            if (obj.optString( "status" ).equalsIgnoreCase( "true" )) {

                JSONArray dataArray = obj.getJSONArray( "data" );
                JSONObject dataobj = dataArray.getJSONObject( 0 );
                str_profilepic = dataobj.getString( "image" );
                str_name = dataobj.getString( "name" );
                str_mobile = dataobj.getString( "mobile" );
                str_role = dataobj.getString( "role" );
                str_ostatus = dataobj.getString( "p_status" );

                if (str_ostatus.equalsIgnoreCase( "active" )) {
                    suspand_lv.setVisibility( View.VISIBLE );
                } else if (str_ostatus.equalsIgnoreCase( "deactive" )) {
                    activate_lv.setVisibility( View.VISIBLE );

                }

                Glide.with( this ).load( login_interface.JSON_URL + str_profilepic ).placeholder( R.drawable.dummylogo ).into( profilepic );
                name.setText( (CharSequence) str_name );
                mobile.setText( (CharSequence) str_mobile );
                role.setText( "LCD Wall " + str_role );
                progress_layout.setVisibility( View.GONE );

            } else if (obj.optString( "status" ).equalsIgnoreCase( "false" )) {
                progress_layout.setVisibility( View.GONE );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //get escapers_uid
    private void getResponseAdmin() {
        progress_layout.setVisibility( View.VISIBLE );
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();

        login_interface api = retrofit.create( login_interface.class );
        Call<String> Call = api.get_escapers_uid( o_uid );
        Call.enqueue( new Callback<String>() {

            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                Log.i( "Response_String", response.body().toString() );
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i( "Response_String", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        Writetvadmin( jsonresponse );

                    } else {
                        error_layout.setVisibility( View.VISIBLE );
                        Log.i( "Response_String", "Returned empty response" );

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                error_layout.setVisibility( View.VISIBLE );
                Log.i( "retrofail", "Failed" );
            }
        } );
    }

    private void Writetvadmin(String response) {
        try {
            JSONObject obj = new JSONObject( response );
            if (obj.optString( "status" ).equalsIgnoreCase( "true" )) {
                ArrayList<billescapers_list_model> retroModelArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray( "data" );
                for (int i = 0; i < dataArray.length(); i++) {
                    billescapers_list_model retroModel = new billescapers_list_model();

                    JSONObject dataobj = dataArray.getJSONObject( i );
                    retroModel.setBid( dataobj.getString( "bid" ) );
                    retroModel.setAmount( dataobj.getString( "e_amount" ) );
                    retroModel.setName( dataobj.getString( "e_name" ) );
                    retroModel.setImage( dataobj.getString( "e_image" ) );
                    retroModel.setAddress( dataobj.getString( "e_village" ) + dataobj.getString( "e_district" ) );
                    retroModel.setNeyojakavargam( dataobj.getString( "e_neyojakavargam" ) );
                    retroModel.setMobile1( dataobj.getString( "e_mobile1" ) );
                    retroModel.setMobile2( dataobj.getString( "e_mobile2" ) );
                    retroModel.setPincode( dataobj.getString( "e_pincode" ) );
                    retroModel.setCreated_on( dataobj.getString( "e_created_on" ) );
                    retroModel.setO_name( dataobj.getString( "name" ) );
                    retroModel.setStatus( dataobj.getString( "status" ) );
                    retroModelArrayList.add( retroModel );
                }
                progress_layout.setVisibility( View.GONE );
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
                recyclerView.setLayoutManager( gridLayoutManager );
                for_adminpendingbillescaper_list_adapter adapter = new for_adminpendingbillescaper_list_adapter( retroModelArrayList, this );
                recyclerView.setAdapter( adapter );

            } else if (obj.optString( "status" ).equalsIgnoreCase( "false" )) {
                progress_layout.setVisibility( View.GONE );
                error_layout.setVisibility( View.VISIBLE );
                Toast.makeText( Admin_ownerProfile.this, obj.optString( "message" ) + "", Toast.LENGTH_SHORT ).show();
            } else {
                progress_layout.setVisibility( View.GONE );
                error_layout.setVisibility( View.VISIBLE );
                Toast.makeText( Admin_ownerProfile.this, "Error in server", Toast.LENGTH_SHORT ).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //toolbar
    private void configureToolbar() {
        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle( "Owner Profile" );
        toolbar.setNavigationIcon( getResources().getDrawable( R.drawable.ic_baseline_arrow_back_ios_24 ) );
        setSupportActionBar( toolbar );
    }


    public void openWhatsApp(View view) {
        PackageManager pm = getPackageManager();
        try {
            Intent waIntent = new Intent( Intent.ACTION_SEND );
            waIntent.setType( "text/plain" );
            String text = "This is  a Test"; // Replace with your own message.

            PackageInfo info = pm.getPackageInfo( "com.whatsapp", PackageManager.GET_META_DATA );
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage( "com.whatsapp" );

            waIntent.putExtra( Intent.EXTRA_TEXT, text );
            startActivity( Intent.createChooser( waIntent, "Share with" ) );

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText( this, "WhatsApp not Installed", Toast.LENGTH_SHORT )
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void openWhatsApp1(View view) {
        PackageManager pm = getPackageManager();
        try {


            String toNumber = "91"+Phone; // Replace with mobile phone number without +Sign or leading zeros, but with country code.
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            Intent sendIntent = new Intent( Intent.ACTION_SENDTO, Uri.parse( "whatsapp:" + "" + toNumber + "?body=" + "" ) );
            sendIntent.setPackage( "com.whatsapp" );
            startActivity( sendIntent );
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText( Admin_ownerProfile.this, "it may be you dont have whats app", Toast.LENGTH_LONG ).show();

        }


    }
}