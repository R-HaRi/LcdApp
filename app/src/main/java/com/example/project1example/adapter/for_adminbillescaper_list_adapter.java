package com.example.project1example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project1example.Admin_billescaperprofile;
import com.example.project1example.R;
import com.example.project1example.admin_pending_escaper_profile;
import com.example.project1example.login_interface;
import com.example.project1example.model.SharedPrefs_model;
import com.example.project1example.model.billescapers_list_model;
import com.example.project1example.model.owner_list_model;
import com.example.project1example.utils.ExpandListener;
import com.example.project1example.utils.ExpandableLinearLayout;

import java.util.ArrayList;
import java.util.Objects;

public class for_adminbillescaper_list_adapter extends RecyclerView.Adapter<for_adminbillescaper_list_adapter.MyViewHolder> {
    Context context1;
    ArrayList<billescapers_list_model> retroModelArrayList;
    SharedPrefs_model prefsModel;

    public for_adminbillescaper_list_adapter(ArrayList<billescapers_list_model> retroModelArrayList, Context context1) {
        this.retroModelArrayList = retroModelArrayList;
        this.context1 = context1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context1).inflate(R.layout.for_adminbillescapers, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        prefsModel = new SharedPrefs_model(context1);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        billescapers_list_model Profile = retroModelArrayList.get( i );

        myViewHolder.name.setText(Profile.getName());
        myViewHolder.address.setText(Profile.getAddress());
        myViewHolder.amount.setText("Rs. " + Profile.getAmount());
        myViewHolder.mobilenumber1.setText(Profile.getMobile1());
        myViewHolder.mobilenumber2.setText(Profile.getMobile2());
        Glide.with(context1).load(login_interface.JSON_URL + Profile.getImage()).placeholder(R.drawable.dummylogo).into(myViewHolder.profile_img);

        myViewHolder.status.setText( Profile.getStatus() );

        if (Profile.getStatus().equalsIgnoreCase( "pending" )){
            myViewHolder.status.setTextColor( Color.RED );
        }
        else {
            myViewHolder.status.setTextColor( Color.GREEN );

        }


        if (prefsModel.getRole().equalsIgnoreCase( "admin" )){

        myViewHolder.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Profile.getStatus().equalsIgnoreCase( "accepted" )){
                    Intent j = new Intent(context1, Admin_billescaperprofile.class);
                    j.putExtra("list", Profile);
                    j.putExtra( "uid",Profile.getBid() );
                    context1.startActivity(j);
                }

                else{
                    Intent j = new Intent(context1, admin_pending_escaper_profile.class);
                    j.putExtra("list", Profile);
                    j.putExtra( "uid",Profile.getBid() );
                    context1.startActivity(j);
                }
        }
        });

        }

    }

    @Override
    public int getItemCount() {
        return retroModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, amount, mobilenumber1, mobilenumber2,status;
        ImageView profile_img;
        CardView card1;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            amount = itemView.findViewById(R.id.amount);
            mobilenumber1 = itemView.findViewById(R.id.mobilenumber1);
            mobilenumber2 = itemView.findViewById(R.id.mobilenumber2);
            profile_img = itemView.findViewById(R.id.profile_img);
            card1 = itemView.findViewById(R.id.card1);
            status=itemView.findViewById( R.id.status1 );

        }

    }
}
