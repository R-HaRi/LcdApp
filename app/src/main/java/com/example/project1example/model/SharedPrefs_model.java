package com.example.project1example.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs_model {

//    String name, uid, role, church_id, mobile,options,notes;
    Boolean islogged,isReload;


    Context context;
    SharedPreferences sharedPreferences;
    String name1 = "sp_name";
    String uid1 = "sp_uid";
    String role1 = "sp_role";
    String mobile1 = "sp_mobile";
    String islogged1 = "sp_login";
    String isReload1 = "sp_reload";
    String pwd = "sp_pwd";
    String village = "sp_address";
    String distict = "sp_distict";

    String pincode = "sp_pincode";
    String image = "sp_image";
    String neyojakavargam = "sp_neyojakavargam";
    String p_status = "p_status";


    SharedPreferences.Editor editor;

    public SharedPrefs_model(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.context = context;
    }

    public String getName() {
        return sharedPreferences.getString(name1, "");
    }

    public void setName(String name) {
        editor.putString(name1, name);
        editor.commit();
    }

    public String getUid() {
        return sharedPreferences.getString(uid1, "");
    }

    public void setUid(String uid) {
        editor.putString(uid1, uid);
        editor.commit();
    }

    public String getRole() {
        return sharedPreferences.getString(role1, "");
    }

    public void setRole(String role) {
        editor.putString(role1, role);
        editor.commit();
    }

    public String getMobile() {
        return sharedPreferences.getString(mobile1, "");
    }

    public void setMobile(String mobile) {
        editor.putString(mobile1, mobile);
        editor.commit();
    }

    public Boolean getIslogged() {
        return sharedPreferences.getBoolean(islogged1, false);
    }
    public void setIslogged(Boolean islogged) {
        editor.putBoolean(islogged1, islogged);
        editor.commit();
    }






    public String getPwd() {
        return sharedPreferences.getString(pwd, "");
    }

    public void setPwd(String pwd1) {
        editor.putString(pwd, pwd1);
        editor.commit();
    }



    public String getvillage() {

        return sharedPreferences.getString(village, "");
    }

    public void setvillage(String village1) {
        editor.putString(village, village1);
        editor.commit();
    }

    public String getPincode() {

        return sharedPreferences.getString(pincode, "");
    }

    public void setPincode(String pincode1) {
        editor.putString(pincode, pincode1);
        editor.commit();
    }

    public String getImage() {
        return sharedPreferences.getString(image, "");
    }

    public String getNeyojakavargam() {
        return sharedPreferences.getString(neyojakavargam, "");
    }

    public void setImage(String image1) {
        editor.putString(image, image1);
        editor.commit();
    }

    public void setNeyojakavargam(String neyojakavargam1) {
        editor.putString(neyojakavargam, neyojakavargam1);
        editor.commit();
    }

    public String getP_status() {
        return sharedPreferences.getString(p_status, "");
    }

    public void setP_status(String p_status1) {
        editor.putString(p_status, p_status1);
        editor.commit();
    }

    public String getDistict() {
        return sharedPreferences.getString(distict, "");
    }
    public void setDistict(String distict1) {
        editor.putString(distict, distict1);
        editor.commit();    }
}
