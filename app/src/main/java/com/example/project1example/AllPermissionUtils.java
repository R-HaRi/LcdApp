package com.example.project1example;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AllPermissionUtils {

    Context context;


    public AllPermissionUtils(Context context) {
        this.context = context;
    }

    public boolean checkPermission(String permission, int code) {
        if (ContextCompat.checkSelfPermission(context,
                permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return askPermission(permission, code);
        }
    }

    public boolean askPermission(String permission, int code) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{permission}, code);
        return true;
    }

}
