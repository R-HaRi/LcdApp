package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project1example.model.SharedPrefs_model;

public class WelcomeActivity extends AppCompatActivity {
    ImageView img,bg;
    Handler handler;
    TextView textView;
    SharedPrefs_model preferenceModel;
    SharedPreferences sharedPreferences;

    boolean isLogged;
    Animation welcomanim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);
        preferenceModel=new SharedPrefs_model( this );

        sharedPreferences = this.getSharedPreferences("loggeduser", MODE_PRIVATE);
        isLogged=preferenceModel.getIslogged();
        welcomanim= AnimationUtils.loadAnimation( this,R.anim.welcome_anim );
        textView=findViewById( R.id.welcometext );
        textView.setAnimation( welcomanim );
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                Log.d("role","123"+preferenceModel.getRole());
                Log.d("role","123"+preferenceModel.getIslogged());

                if(isLogged && preferenceModel.getRole().equalsIgnoreCase( "admin" )){
                    Intent i = new Intent( WelcomeActivity.this, AdminPanelActivity.class );
                    startActivity( i );
                    finish();
                }
                    else if(isLogged && preferenceModel.getRole().equalsIgnoreCase( "owner" )){
                        Intent j = new Intent( WelcomeActivity.this, Dashboard.class );
                        startActivity( j );
                        finish();
                    }

                else {
                    Intent i = new Intent(WelcomeActivity.this,LoginPage.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);

    }
}