package com.example.project1example.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.project1example.Admin_ownerProfile;
import com.example.project1example.Dashboard;
import com.example.project1example.OwnerPageActivity;
import com.example.project1example.R;
import com.example.project1example.login_interface;
import com.example.project1example.model.owner_list_model;

import java.util.ArrayList;

import io.armcha.elasticview.ElasticView;

public class for_owner_list_adapter extends RecyclerView.Adapter<for_owner_list_adapter.MyViewHolder> {
    Context context1;
    ArrayList<owner_list_model> retroModelArrayList;

    public for_owner_list_adapter( ArrayList<owner_list_model> retroModelArrayList, Context context1) {
        this.retroModelArrayList = retroModelArrayList;
        this.context1 = context1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context1).inflate(R.layout.for_owner_list,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        owner_list_model Profile = retroModelArrayList.get( i );

        myViewHolder.p_name.setText(Profile.getName());
        myViewHolder.p_address.setText(Profile.getAddress()+"District".concat( Profile.getDistrict() ) );
        myViewHolder.p_neyojakavargam.setText(Profile.getNeyojakavargam());
        Glide.with(context1).load(login_interface.JSON_URL + Profile.getImage()).placeholder(R.drawable.dummylogo).into(myViewHolder.img);


        myViewHolder.vender_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent j = new Intent(context1, Admin_ownerProfile.class);
                j.putExtra("uid", retroModelArrayList.get(i).getUid());
                j.putExtra("list", Profile );

                context1.startActivity(j);
            }
        });

    }

    @Override
    public int getItemCount() {
        return retroModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView p_name,p_address,p_neyojakavargam;
        ImageView img;
        ElasticView vender_cardview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            p_name = itemView.findViewById(R.id.p_name);
            p_address = itemView.findViewById(R.id.p_address);
            p_neyojakavargam = itemView.findViewById(R.id.p_neyojakavargam);
            img = itemView.findViewById(R.id.img);
            vender_cardview = itemView.findViewById(R.id.vender_cardview);

        }
    }
}
