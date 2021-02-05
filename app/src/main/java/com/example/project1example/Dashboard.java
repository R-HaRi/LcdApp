package com.example.project1example;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1example.model.SharedPrefs_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Dashboard extends AppCompatActivity {
    CardView search_owners,bill_escapers;
    TextView name,mobile,role;
    ImageView profilepic;
    RelativeLayout progress_layout,p_status_warning;
    SharedPrefs_model spm;
    LinearLayout dashboard_data;
    String Base_URL;
    RelativeLayout adv_lay1, adv_lay2,adv_lay3,bannerad;
    ImageView adimg1,adimg2,adimg3,adimg4;

    TextView advte1,advte2,advte3,advte4;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        search_owners=findViewById(R.id.search_owners);
        bill_escapers=findViewById(R.id.bill_escapers);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        role=findViewById(R.id.role);
        profilepic=findViewById(R.id.profilepic);
        progress_layout=findViewById(R.id.progress_layout);



        adv_lay1 =findViewById( R.id.adver1 );
        adv_lay2 =findViewById( R.id.adver2 );
        adv_lay3=findViewById( R.id.adver3 );
        bannerad=findViewById( R.id.adver4 );

        adimg1=findViewById( R.id.adv1 );
        adimg2=findViewById( R.id.adv2 );

        adimg3=findViewById( R.id.adv3 );
        adimg4=findViewById( R.id.adv4 );

        advte1=findViewById( R.id.weblink1 );
        advte2=findViewById( R.id.weblink2);
        advte3=findViewById( R.id.weblink3);
        advte4=findViewById( R.id.weblink4);




        p_status_warning=findViewById(R.id.p_status_warning);
        dashboard_data=findViewById(R.id.dashboard_data);
        spm = new SharedPrefs_model(this);


        if (spm.getP_status().equalsIgnoreCase("deactive")){
            dashboard_data.setVisibility(View.GONE);
            p_status_warning.setVisibility(View.VISIBLE);
        }else {
            dashboard_data.setVisibility(View.VISIBLE);
            p_status_warning.setVisibility(View.GONE);
        }

        getAddData( "1" );
        getAddData( "2" );
        getAddData( "3" );
        getAddData( "4" );


        Glide.with(this).load(login_interface.JSON_URL + spm.getImage()).placeholder(R.drawable.dummylogo).into(profilepic);
            name.setText(spm.getName());
            mobile.setText(spm.getMobile());
            role.setText("LED Wall "+spm.getRole());


        search_owners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ownersearch=new Intent(Dashboard.this,OwnersListActivity.class);
                startActivity(ownersearch);
            }
        });
        bill_escapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Dashboard.this,BillEscapersActivity.class);
                    i.putExtra("uid",spm.getUid());
                startActivity(i);
            }
        });



        adimg1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,WebView.class);
                 intent.putExtra( "link",advte1.getText().toString());
                 Log.d( "link",advte1.getText().toString() );
                startActivity( intent );
            }
        } );

    }
    protected void getAddData(String adnum) {
        progress_layout.setVisibility( View.VISIBLE );
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();
        login_interface api = retrofit.create( login_interface.class );
        Call<String> call = api.get_ad_id( adnum );

        call.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        progress_layout.setVisibility( View.GONE );
                        Log.i( "onsuccess", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        writeAddData( jsonresponse,adnum );
                    } else {
                        Log.i( "onEmptyResponse", "Returned empty response" );
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                Log.i( "retrofail", "Failed" );
            }
        } );
    }

    private void writeAddData(String jsonresponse,String adnum) {
        try {
            JSONObject obj = new JSONObject( jsonresponse );
            if (obj.optString( "status" ).equalsIgnoreCase( "true" )) {
                JSONArray dataArray = obj.getJSONArray( "data" );
                JSONObject dataobj = dataArray.getJSONObject( 0 );

                switch (adnum){
                    case "1":
                        advte1.setText( dataobj.getString( "weblink" ) );
                        Glide.with( getApplicationContext() ).load( Base_URL + dataobj.getString( "image" ) ).placeholder(R.drawable.dummylogo).into( adimg1 );
                        break;

                    case "2":
                        advte2.setText( dataobj.getString( "weblink" ) );
                        Glide.with( getApplicationContext() ).load( Base_URL + dataobj.getString( "image" ) ).placeholder(R.drawable.bg_gradient).into( adimg2 );
                        break;

                    case "3":
                        advte3.setText( dataobj.getString( "weblink" ) );
                        Glide.with( getApplicationContext() ).load( Base_URL + dataobj.getString( "image" ) ).placeholder(R.drawable.camera).into( adimg3 );
                        break;
                    case "4":
                        advte4.setText( dataobj.getString( "weblink" ) );
                        Glide.with( getApplicationContext() ).load( Base_URL + dataobj.getString( "image" ) ).placeholder(R.drawable.bg_gradient ).into( adimg4 );
                        break;

                }

            } else if (obj.optString( "status" ).equalsIgnoreCase( "false" )) {
                Toast.makeText( getApplicationContext(), "failure", Toast.LENGTH_SHORT ).show();

            } else {
                Toast.makeText( getApplicationContext(), "Something went wrong please try again later", Toast.LENGTH_SHORT ).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}