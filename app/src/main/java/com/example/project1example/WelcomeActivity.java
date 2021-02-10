package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1example.model.SharedPrefs_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WelcomeActivity extends AppCompatActivity {
    ImageView img,bg;
    Handler handler;
    TextView textView;
    SharedPrefs_model preferenceModel;
    SharedPreferences sharedPreferences;

    String loginstatus,mobilenum;
    boolean isLogged;
    Animation welcomanim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);
        preferenceModel=new SharedPrefs_model( this );

        sharedPreferences = this.getSharedPreferences("loggeduser", MODE_PRIVATE);
        isLogged=preferenceModel.getIslogged();
        mobilenum=preferenceModel.getMobile();


        if (!mobilenum.equalsIgnoreCase( null )|| !mobilenum.equalsIgnoreCase( "" )){
            getResponse(mobilenum);

        }

        welcomanim= AnimationUtils.loadAnimation( this,R.anim.welcome_anim );
        textView=findViewById( R.id.welcometext );
        textView.setAnimation( welcomanim );
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mobilenum.equalsIgnoreCase( null )||mobilenum.equalsIgnoreCase( "" )){
                    Intent i = new Intent( WelcomeActivity.this, LoginPage.class );
                    startActivity( i );
                    finish();
                }
                else {
                if (loginstatus.equalsIgnoreCase( "logged_out" )){
                    Toast.makeText( WelcomeActivity.this, "your session is expired please login again", Toast.LENGTH_SHORT ).show();
                    Intent i = new Intent(WelcomeActivity.this,LoginPage.class);
                    startActivity(i);
                    finish();
                }
                else {
                    if (isLogged && preferenceModel.getRole().equalsIgnoreCase( "admin" )) {
                        Intent i = new Intent( WelcomeActivity.this, AdminPanelActivity.class );
                        startActivity( i );
                        finish();
                    } else if (isLogged && preferenceModel.getRole().equalsIgnoreCase( "owner" )) {
                        Intent j = new Intent( WelcomeActivity.this, Dashboard.class );
                        startActivity( j );
                        finish();
                    } else {
                        Intent i = new Intent( WelcomeActivity.this, LoginPage.class );
                        startActivity( i );
                        finish();
                    }
                }
            }
            }

        },3000);

    }


    private void getResponse(String mobilenum) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(login_interface.JSON_URL)
                .addConverterFactory( ScalarsConverterFactory.create())
                .build();

        login_interface api = retrofit.create(login_interface.class);

        Call<String> call = api.check_login_status(mobilenum);

        call.enqueue(new Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("retrofit_response", response.body().toString());
                        String jsonresponse = response.body().toString();
                        writeTv(jsonresponse);
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
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

                loginstatus=dataobj.getString( "login_status" );

            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                Toast.makeText(WelcomeActivity.this, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WelcomeActivity.this, "Error in server", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}