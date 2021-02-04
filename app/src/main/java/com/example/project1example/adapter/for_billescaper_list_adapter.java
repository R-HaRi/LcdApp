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
import com.example.project1example.OwnerPageActivity;
import com.example.project1example.R;
import com.example.project1example.login_interface;
import com.example.project1example.model.billescapers_list_model;
import com.example.project1example.model.owner_list_model;
import com.example.project1example.utils.ExpandListener;
import com.example.project1example.utils.ExpandableLinearLayout;

import java.util.ArrayList;
import java.util.Objects;

import io.armcha.elasticview.ElasticView;

public class for_billescaper_list_adapter extends RecyclerView.Adapter<for_billescaper_list_adapter.MyViewHolder> {
    Context context1;
    ArrayList<billescapers_list_model> retroModelArrayList;

    RecyclerView recyclerView;
    int lastExpandedCardPosition;

    public for_billescaper_list_adapter(ArrayList<billescapers_list_model> retroModelArrayList, Context context1,RecyclerView recyclerView) {
        this.retroModelArrayList = retroModelArrayList;
        this.context1 = context1;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context1).inflate(R.layout.for_billescapers,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(retroModelArrayList.get(i).getName());
        myViewHolder.amount.setText("Rs. "+retroModelArrayList.get(i).getAmount());
        myViewHolder.mobilenumber1.setText(retroModelArrayList.get(i).getMobile1());
        myViewHolder.mobilenumber2.setText(retroModelArrayList.get(i).getMobile2());
        myViewHolder.village.setText(retroModelArrayList.get(i).getAddress());
        myViewHolder.district.setText(retroModelArrayList.get(i).getDistrict());
        myViewHolder.neyojakavargam.setText(retroModelArrayList.get(i).getNeyojakavargam());
        myViewHolder.pincode.setText(retroModelArrayList.get(i).getPincode());
        myViewHolder.created_on.setText(retroModelArrayList.get(i).getCreated_on());
        Glide.with(context1).load(login_interface.JSON_URL + retroModelArrayList.get(i).getImage()).placeholder(R.drawable.dummylogo).into(myViewHolder.profile_img);



        if (retroModelArrayList.get(i).isExpanded()) {
            myViewHolder.expandableView.setVisibility(View.VISIBLE);
            myViewHolder.expandableView.setExpanded(true);
        } else {
            myViewHolder.expandableView.setVisibility(View.GONE);
            myViewHolder.expandableView.setExpanded(false);
        }/////expandview

    }

    @Override
    public int getItemCount() {
        return retroModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,amount,mobilenumber1,mobilenumber2,village,district,neyojakavargam,pincode,created_on;
        ImageView profile_img;
        CardView card1;
        ExpandableLinearLayout expandableView;

        ExpandListener expandListener = new ExpandListener() {
            @Override
            public void onExpandComplete() {
                if (lastExpandedCardPosition != getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition) != null) {
                    ((ExpandableLinearLayout) Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)).itemView.findViewById(R.id.card2)).setExpanded(false);
                    retroModelArrayList.get(lastExpandedCardPosition).setExpanded(false);
                    ((ExpandableLinearLayout) Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)).itemView.findViewById(R.id.card2)).toggle();
                } else if (lastExpandedCardPosition != getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition) == null) {
                    retroModelArrayList.get(lastExpandedCardPosition).setExpanded(false);
                }
                lastExpandedCardPosition = getAdapterPosition();
            }

            @Override
            public void onCollapseComplete() {
                card1.setCardBackgroundColor(context1.getResources().getColor(R.color.colorWhite));
            }
        };/////expandview


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            mobilenumber1 = itemView.findViewById(R.id.mobilenumber1);
            mobilenumber2 = itemView.findViewById(R.id.mobilenumber2);
            village= itemView.findViewById(R.id.villagerv);
            district= itemView.findViewById(R.id.district);
            neyojakavargam = itemView.findViewById(R.id.neyojakavargam);
            pincode = itemView.findViewById(R.id.pincode);
            created_on = itemView.findViewById(R.id.created_on);
            profile_img = itemView.findViewById(R.id.profile_img);
            card1 = itemView.findViewById(R.id.card1);
            expandableView = itemView.findViewById(R.id.card2);

            expandableView.setExpandListener(expandListener);
            initializeClicks();
        }
        private void initializeClicks() {
            itemView.setOnClickListener(view -> {
                if (expandableView.isExpanded()) {
                    expandableView.setExpanded(false);
                    expandableView.toggle();
                    retroModelArrayList.get(getAdapterPosition()).setExpanded(false);
                    card1.setCardBackgroundColor(context1.getResources().getColor(R.color.colorWhite));
                } else {
                    expandableView.setExpanded(true);
                    retroModelArrayList.get(getAdapterPosition()).setExpanded(true);
                    expandableView.toggle();
                    card1.setCardBackgroundColor(Color.rgb(254, 218, 195));
                }
            });
        }
    }
}
