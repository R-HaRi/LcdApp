package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPanelActivity extends AppCompatActivity {
    Button addowner,getownerlist,aceptbillesc,all_billescapers;
    SharedPreferences sharedPreferences;
    Boolean isLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        sharedPreferences = this.getSharedPreferences("loggeduser", MODE_PRIVATE);
        isLogged = sharedPreferences.getBoolean("islogged", false);
        addowner = findViewById(R.id.addowner);
        getownerlist = findViewById(R.id.getownerlist);
        aceptbillesc = findViewById(R.id.aceptbillesc);
        all_billescapers = findViewById(R.id.all_billescapers);
        addowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminPanelActivity.this,Owner_Register.class);
                startActivity(i);

            }
        });
        getownerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminPanelActivity.this,OwnersListActivity.class);
                startActivity(i);

            }
        });
        aceptbillesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminPanelActivity.this,Pending_escapers_list.class);
                startActivity(i);

            }
        });
    }

}