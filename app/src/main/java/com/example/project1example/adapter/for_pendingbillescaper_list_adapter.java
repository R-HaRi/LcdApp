package com.example.project1example.adapter;

import android.content.Context;
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
import com.example.project1example.R;
import com.example.project1example.login_interface;
import com.example.project1example.model.billescapers_list_model;
import com.example.project1example.utils.ExpandListener;
import com.example.project1example.utils.ExpandableLinearLayout;

import java.util.ArrayList;
import java.util.Objects;

public class for_pendingbillescaper_list_adapter extends RecyclerView.Adapter<for_pendingbillescaper_list_adapter.MyViewHolder> {
    Context context1;
    ArrayList<billescapers_list_model> retroModelArrayList;


    public for_pendingbillescaper_list_adapter(ArrayList<billescapers_list_model> retroModelArrayList, Context context1) {
        this.retroModelArrayList = retroModelArrayList;
        this.context1 = context1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context1).inflate(R.layout.for_pendingbillescapers,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(retroModelArrayList.get(i).getName());
        myViewHolder.address.setText(retroModelArrayList.get(i).getAddress());
        myViewHolder.amount.setText("Rs. "+retroModelArrayList.get(i).getAmount());
        myViewHolder.mobilenumber1.setText(retroModelArrayList.get(i).getMobile1());
        myViewHolder.created_on.setText(retroModelArrayList.get(i).getCreated_on());
        myViewHolder.status.setText(retroModelArrayList.get(i).getStatus());

        if (retroModelArrayList.get(i).getStatus().equalsIgnoreCase("pending")){
            myViewHolder.status.setTextColor(Color.parseColor("#DC1010"));
        }

    }

    @Override
    public int getItemCount() {
        return retroModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,address,amount,mobilenumber1,created_on,status;
        CardView card1;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            amount = itemView.findViewById(R.id.amount);
            mobilenumber1 = itemView.findViewById(R.id.mobilenumber1);
            created_on = itemView.findViewById(R.id.created_on);
            card1 = itemView.findViewById(R.id.card1);
            status = itemView.findViewById(R.id.status);

        }

    }
}
