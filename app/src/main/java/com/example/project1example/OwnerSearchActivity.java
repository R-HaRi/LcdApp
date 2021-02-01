package com.example.project1example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class OwnerSearchActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    RelativeLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_search);
        progressbar=findViewById(R.id.recycle_progressbar);

    }

}