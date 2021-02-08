package com.example.project1example;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project1example.adapter.for_adminbillescaper_list_adapter;
import com.example.project1example.model.billescapers_list_model;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class AdvertisementFragment1 extends Fragment {
    View view;

    private boolean permission = false;
    int img_click;
    String val_logo;


    ImageView i2;

    EditText editText;
    Button post, update;
    int i;
    String WebUrl;
    RelativeLayout progress_layout;
    String Base_URL;
    private final int CAMERA_RESULT = 101, GALLERY_RESULT = 102;
    String imagePath;


    public AdvertisementFragment1(int i) {
        this.i = i;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_advertisement1, container, false );
        i2 = view.findViewById( R.id.addadvert );
        editText = view.findViewById( R.id.weblink );
        progress_layout = view.findViewById( R.id.progress_layout );

        getAddData( String.valueOf( i ) );

        i2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with((Activity) getContext())
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        } );


        try {//for converting image to base64 encoding
            if (i2.getDrawable() == null) {
                val_logo = "";
//                        Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap image = ((BitmapDrawable) i2.getDrawable()).getBitmap();
                ByteArrayOutputStream byteA = new ByteArrayOutputStream();
                image.compress( Bitmap.CompressFormat.JPEG, 100, byteA );
                val_logo = Base64.encodeToString( byteA.toByteArray(), Base64.DEFAULT );
            }
        } catch (Exception e) {
//                    Toast.makeText(Add_Listing.this, "Please select a logo", Toast.LENGTH_SHORT).show();
        }




        post = view.findViewById( R.id.postAdd );
        post.setVisibility( View.GONE );
        update = view.findViewById( R.id.update );


//        post.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebUrl=editText.getText().toString().trim();
//                Log.i("edittext","123"+ WebUrl);
//                getresponse( String.valueOf( i ),imageUri.toString(),WebUrl );
//            }
//        } );


        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebUrl = editText.getText().toString().trim();
                Log.i( "edittext", "123" + WebUrl );
                getresponse1( String.valueOf( i ), val_logo, WebUrl, imagePath );

            }
        } );


        return view;
    }


    protected void getAddData(String adnum) {
        progress_layout.setVisibility( View.VISIBLE );
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();
        login_interface api = retrofit.create( login_interface.class );
        Call<String> call = api.get_ad_id( adnum );

        call.enqueue( new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        progress_layout.setVisibility( View.GONE );
                        Log.i( "onsuccess", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        writeAddData( jsonresponse );
                    } else {
                        Log.i( "onEmptyResponse", "Returned empty response" );
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                Log.i( "retrofail", "Failed" );
            }
        } );
    }

    private void writeAddData(String jsonresponse) {
        try {
            JSONObject obj = new JSONObject( jsonresponse );
            if (obj.optString( "status" ).equalsIgnoreCase( "true" )) {
                JSONArray dataArray = obj.getJSONArray( "data" );
                JSONObject dataobj = dataArray.getJSONObject( 0 );

                editText.setText( dataobj.getString( "weblink" ) );
                imagePath = dataobj.getString( "image" );
                Glide.with( getContext() ).load( Base_URL + imagePath ).into( i2 );


            } else if (obj.optString( "status" ).equalsIgnoreCase( "false" )) {
                Toast.makeText( getContext(), "failure", Toast.LENGTH_SHORT ).show();

            } else {
                Toast.makeText( getContext(), "Something went wrong please try again later", Toast.LENGTH_SHORT ).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    protected  void getresponse(String adnum,String image,String weburl){
//        progress_layout.setVisibility(View.VISIBLE);
//        Base_URL = login_interface.JSON_URL;
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Base_URL).
//                addConverterFactory( ScalarsConverterFactory.create()).build();
//               login_interface api=retrofit.create( login_interface.class );
//               Call<String> call=api.add_advertisement( adnum,image,weburl );
//               call.enqueue( new Callback<String>() {
//                   @Override
//                   public void onResponse(Call<String> call, Response<String> response) {
//                       if (response.isSuccessful()) {
//                           progress_layout.setVisibility(View.GONE);
//                           if (response.body() != null) {
//
//                               progress_layout.setVisibility(View.GONE);
//                               Log.i("onsuccess", response.body().toString());
//                               String jsonresponse = response.body().toString();
//                               Writetv(jsonresponse);
//                           } else {
//                               Log.i("onEmptyResponse", "Returned empty response");
//                           }
//                       }
//                   }
//
//                   @Override
//                   public void onFailure(Call<String> call, Throwable t) {
//                       progress_layout.setVisibility(View.GONE);
//                       Log.i("retrofail", "Failed");
//                   }
//               } );
//    }

    private void Writetv(String response) {
        try {
            JSONObject obj = new JSONObject( response );
            if (obj.optString( "status" ).equalsIgnoreCase( "true" )) {
                Toast.makeText( getContext(), "Successfully updated", Toast.LENGTH_SHORT ).show();

            } else if (obj.optString( "status" ).equalsIgnoreCase( "false" )) {
                Toast.makeText( getContext(), "failure", Toast.LENGTH_SHORT ).show();

            } else {
                Toast.makeText( getContext(), "Something went wrong please try again later", Toast.LENGTH_SHORT ).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void getresponse1(String adnum, String image, String weburl, String imagepath) {
        progress_layout.setVisibility( View.VISIBLE );
        Base_URL = login_interface.JSON_URL;
        Retrofit retrofit = new Retrofit.Builder().baseUrl( Base_URL ).
                addConverterFactory( ScalarsConverterFactory.create() ).build();

        login_interface api = retrofit.create( login_interface.class );
        Call<String> Call = api.update_advertisement( adnum, image, weburl, "" );
        Call.enqueue( new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    progress_layout.setVisibility( View.GONE );
                    if (response.body() != null) {

                        Log.i( "onsuccess", response.body().toString() );
                        String jsonresponse = response.body().toString();
                        Writetv( jsonresponse );

                    } else {
                        progress_layout.setVisibility( View.GONE );
                        Log.i( "onEmptyResponse", "Returned empty response" );

                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                progress_layout.setVisibility( View.GONE );
                Log.i( "retrofail", "Failed" );
            }
        } );
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            i2.setImageURI(fileUri);

//            //You can get File object from intent
//            val file:File = ImagePicker.getFile(data)!!
//
//                    //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), "ImagePicker error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }





















}