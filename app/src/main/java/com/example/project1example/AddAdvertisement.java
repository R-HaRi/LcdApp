package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddAdvertisement extends AppCompatActivity {

    Button Adv1,Adv2,Adv3,bannerad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_advertisement );

        Adv1=findViewById( R.id.add1 );
        Adv2=findViewById( R.id.add2 );
        Adv3=findViewById( R.id.add3 );
        bannerad=findViewById( R.id.bannerad );

        loadfragment( new AdvertisementFragment1(1) );

        Adv1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadfragment( new AdvertisementFragment1(1) );
            }
        } );

        Adv2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadfragment( new AdvertisementFragment1(2) );
            }
        } );

        Adv3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadfragment( new AdvertisementFragment1(3) );
            }
        } );

        bannerad.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadfragment( new AdvertisementFragment1(4) );

            }
        } );

    }
    public void loadfragment(Fragment fragment){
        FragmentManager fragmentManager =this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.advertisementframelaout,fragment);
        fragmentTransaction.commit();
    }
}