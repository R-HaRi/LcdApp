package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Admin_editBillescapers extends AppCompatActivity {

    String e_bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_billescapers);
        e_bid = getIntent().getStringExtra("esc_bid");


    }
}