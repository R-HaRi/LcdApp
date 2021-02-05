package com.example.project1example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AllPermissionUtils permissionUtils;
    int LOCATION_PERMISSION_CODE = 121;
    int READ_STORAGE_PERMISSION_CODE = 122;
    int MY_PERMISSIONS_REQUEST_CALL_PHONE=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionUtils = new AllPermissionUtils(this);
    }

    public void locationPermissionClick(View view){

        if (permissionUtils.checkPermission( Manifest.permission.ACCESS_FINE_LOCATION,LOCATION_PERMISSION_CODE)) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        }else  if (permissionUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_STORAGE_PERMISSION_CODE)) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        }
        else  if (permissionUtils.checkPermission(Manifest.permission.CALL_PHONE,MY_PERMISSIONS_REQUEST_CALL_PHONE)) {
            Toast.makeText(MainActivity.this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(MainActivity.this, "No permission!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == READ_STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Call Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Call Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}