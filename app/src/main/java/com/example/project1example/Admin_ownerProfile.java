package com.example.project1example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.project1example.model.owner_list_model;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import  com.example.project1example.model.owner_list_model;

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
    LinearLayout  call, WhatsApp;
    SharedPrefs_model spm;
    Toolbar toolbar;
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 101;
    String Phone;
    BottomNavigationView navigationView;

   owner_list_model owner_list_model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_owner_profile );
        progress_layout = findViewById( R.id.progress_layout );



        navigationView=findViewById( R.id.navigationView );
        name = findViewById( R.id.name );
        mobile = findViewById( R.id.mobile );
        role = findViewById( R.id.role );
        profilepic = findViewById( R.id.profilepic );
        recyclerView = findViewById( R.id.recycler );
        error_layout = findViewById( R.id.error_layout );

        call = findViewById( R.id.call_lv );
        WhatsApp = findViewById( R.id.message_lv );
        spm = new SharedPrefs_model( this );

        owner_list_model = (owner_list_model) this.getIntent().getSerializableExtra( "list" );

        name.setText( owner_list_model.getName() );
        Glide.with( this ).load( login_interface.JSON_URL + owner_list_model.getImage() ).placeholder( R.drawable.dummylogo ).into( profilepic );
        mobile.setText( owner_list_model.getMobile() );
        role.setText( owner_list_model.getRole() );
        configureToolbar();
        str_ostatus=owner_list_model.getStatus();

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        o_uid = owner_list_model.getUid();
        getescapers_uid(o_uid);


        if (spm.getRole().equalsIgnoreCase( "owner" )) {
            navigationView.setVisibility( View.GONE );
        }

        //Make a call
        call.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone = owner_list_model.getMobile();

                Intent i = new Intent( Intent.ACTION_DIAL );
                i.setData( Uri.parse( "tel:" + Phone ) );
                startActivity( i );

            }
        } );


        //For whatsapp
        WhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager pm=getPackageManager();
                try {

                    String toNumber = owner_list_model.getMobile(); // contains spaces.
                    toNumber = toNumber.replace("+", "").replace(" ", "");

                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "HOLA!");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "WhatsApp no esta instalado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//navigation action
        if (str_ostatus.equalsIgnoreCase( "active" )) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.activate).setIcon(R.drawable.suspand);
            menu.findItem(R.id.activate).setTitle("Suspend");

        }

        else if (str_ostatus.equalsIgnoreCase( "deactive" )) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.activate).setIcon(R.drawable.activate);
            menu.findItem(R.id.activate).setTitle("Active");

        }


        //bottom navigation bar
        navigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                switch (id){
                    case R.id.edit:
                        Intent i = new Intent( Admin_ownerProfile.this, Edit_owner.class );
                        i.putExtra( "list", owner_list_model );
                        startActivity( i );
                        break;

                    case R.id.activate:

                        if (str_ostatus.equalsIgnoreCase( "active" )) {
                            Menu menu = navigationView.getMenu();
                            menu.findItem(R.id.activate).setIcon(R.drawable.suspand);
                            menu.findItem(R.id.activate).setTitle("Suspend");

                            str_pstatus = "deactive";
                            getResponse_pstatus();
                        }

                        else if (str_ostatus.equalsIgnoreCase( "deactive" )) {
                            Menu menu = navigationView.getMenu();
                            menu.findItem(R.id.activate).setIcon(R.drawable.activate);
                            menu.findItem(R.id.activate).setTitle("Active");
                            str_pstatus = "active";
                            getResponse_pstatus();
                        }
                        break;
                    case R.id.delete:
                        getResponse_pstatusdelete();
                        break;
                    case R.id.logout:

                        getlogout();
                        break;


                    default:

                        break;

                }


                return true;


            }
        } );

    }


//status updated
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

//status updated
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


//status updated
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


    //logout

    private void getlogout(){
         progress_layout.setVisibility( View.VISIBLE );

            Base_URL = login_interface.JSON_URL;
            Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                    addConverterFactory( ScalarsConverterFactory.create() ).build();

            login_interface api = retrofit.create( login_interface.class );
            Call<String> Call = api.logout( owner_list_model.getMobile() );
            Call.enqueue( new Callback<String>() {

                @Override
                public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                    Log.i( "ResponseString", response.body().toString() );
                    if (response.isSuccessful()) {
                        if (response.body() != null) {

                            Log.i( "ResponseString", response.body().toString() );
                            String jsonresponse = response.body().toString();
                            writeTv( jsonresponse );

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

        //logout
    private void writeTv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")){
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_ownerProfile.this, obj.optString("message"), Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(this, Admin_ownerProfile.class);
//                startActivity(i);
//                finish();

            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_ownerProfile.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                progress_layout.setVisibility(View.GONE);
                Toast.makeText(Admin_ownerProfile.this, "Error in server", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //get escapers_uid
    private void getescapers_uid(String uid) {
        progress_layout.setVisibility( View.VISIBLE );
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();

        login_interface api = retrofit.create( login_interface.class );
        Call<String> Call = api.get_escapers_uid( uid );
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

    //get escapers_uid
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

}