package com.example.project1example.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1example.AdminPanelActivity;
import com.example.project1example.Owner_Register;
import com.example.project1example.R;
import com.example.project1example.login_interface;
import com.example.project1example.model.SharedPrefs_model;
import com.example.project1example.model.billescapers_list_model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class for_adminpendingbillescaper_list_adapter extends RecyclerView.Adapter<for_adminpendingbillescaper_list_adapter.MyViewHolder> {
    Context context1;
    String bid;
    ArrayList<billescapers_list_model> retroModelArrayList;
    SharedPrefs_model prefsModel;


    public for_adminpendingbillescaper_list_adapter(ArrayList<billescapers_list_model> retroModelArrayList, Context context1) {
        this.retroModelArrayList = retroModelArrayList;
        this.context1 = context1;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context1).inflate(R.layout.for_pendingbillescapers, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        prefsModel=new SharedPrefs_model( context1 );
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(retroModelArrayList.get(i).getName());
        myViewHolder.village.setText(retroModelArrayList.get(i).getAddress());
        myViewHolder.district.setText(retroModelArrayList.get(i).getDistrict());
        myViewHolder.amount.setText(retroModelArrayList.get(i).getAmount() + " .Rs");
        myViewHolder.mobilenumber1.setText(retroModelArrayList.get(i).getMobile1());
        myViewHolder.created_on.setText(retroModelArrayList.get(i).getCreated_on());
        myViewHolder.status.setText(retroModelArrayList.get(i).getStatus());
        myViewHolder.owner_name.setText(retroModelArrayList.get(i).getO_name());
        bid = retroModelArrayList.get(i).getBid();


        if (retroModelArrayList.get(i).getStatus().equalsIgnoreCase("pending")){
            myViewHolder.status.setTextColor(Color.parseColor("#DC1010"));

            if (prefsModel.getRole().equalsIgnoreCase( "admin" )){
                myViewHolder.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context1);

                    builder.setMessage("Do you want to Accept the Bill escaper?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getResponse();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(context1, "you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Pending Billescaper");
                    alert.show();
                }
            });
        }

        }

    }

    private void getResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(login_interface.JSON_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        login_interface api = retrofit.create(login_interface.class);
        Call<String> call = api.update_billescaper_status(bid, "accepted");
        Toast.makeText(context1, bid, Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();

                        writeTv(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void writeTv(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.optString("status").equalsIgnoreCase("true")) {
                Toast.makeText(context1, obj.optString("message"), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context1, AdminPanelActivity.class);
                context1.startActivity(i);


            } else if (obj.optString("status").equalsIgnoreCase("false")) {
                Toast.makeText(context1, obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context1, "Error in server", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return retroModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, village,district, amount, mobilenumber1, created_on,owner_name,status;
        CardView card1;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            village = itemView.findViewById(R.id.village);
            district = itemView.findViewById(R.id.district);
            amount = itemView.findViewById(R.id.amount);
            mobilenumber1 = itemView.findViewById(R.id.mobilenumber1);
            created_on = itemView.findViewById(R.id.created_on);
            card1 = itemView.findViewById(R.id.card1);
            status = itemView.findViewById(R.id.status);
            owner_name = itemView.findViewById(R.id.owner);

        }

    }
}
