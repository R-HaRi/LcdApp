package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Edit_owner extends AppCompatActivity {
    String o_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_owner);
        o_uid = getIntent().getStringExtra("uid");
    }
}