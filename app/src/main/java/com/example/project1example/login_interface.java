package com.example.project1example;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface login_interface {
    String JSON_URL="http://LedApp.venuebees.net/";

    @POST("login.php")
    @FormUrlEncoded
    Call<String> login(@Field("mobile") String mobile, @Field("password") String password);

    @POST("register.php")
    @FormUrlEncoded
    Call<String> register(@Field("name") String name, @Field("mobile") String mobile, @Field("village") String village,@Field("district") String district,@Field("neyojakavargam") String neyojakavargam,@Field("pincode") String pincode,
                          @Field("password") String password,@Field("role") String role,@Field("image") String image,@Field("p_status") String p_status);


    @POST("get_owners_list.php")
    @FormUrlEncoded
    Call<String> get_owners_list(@Field("role") String role);

    @POST("get_profile_uid.php")
    @FormUrlEncoded
    Call<String> get_profile_uid(@Field("uid") String uid);


    @POST("get_escapers_uid.php")
    @FormUrlEncoded
    Call<String> get_escapers_uid(@Field("ref_id") String ref_id);

    @POST("get_escaper_profile_bid.php")
    @FormUrlEncoded
    Call<String> get_escaper_profile_bid(@Field("bid") String bid);

    @POST("register_escaper.php")
    @FormUrlEncoded
    Call<String> register_escaper(@Field("e_name") String name, @Field("e_mobile1") String mobile1,@Field("e_mobile2") String mobile2,@Field("e_amount") String amount, @Field("e_village") String village,@Field("e_district") String district,@Field("e_neyojakavargam") String neyojakavargam,@Field("e_pincode") String pincode,
                          @Field("ref_id") String ref_id,@Field("e_image") String image,@Field("status") String status,@Field("e_description") String description);

    @POST("pending_escapers.php")
    @FormUrlEncoded
    Call<String> pending_escapers(@Field("status") String status);

    @POST("update_billescaper_status.php")
    @FormUrlEncoded
    Call<String> update_billescaper_status(@Field("bid") String bid,@Field("status") String status);

    @POST("update_owner_status.php")
    @FormUrlEncoded
    Call<String> update_owner_status(@Field("uid") String uid,@Field("p_status") String p_status);

    @POST("delete_owner.php")
    @FormUrlEncoded
    Call<String> delete_owner(@Field("uid") String uid);

    @POST("all_billescapers.php")
    @FormUrlEncoded
    Call<String> all_billescapers(@Field("status") String status);


    @POST("add_advertisement.php")
    @FormUrlEncoded
    Call<String> add_advertisement(@Field("ad_number") String ad_number,@Field("image") String image,@Field("weblink") String weblink);

    @POST("update_advertisement.php")
    @FormUrlEncoded
    Call<String> update_advertisement(@Field("ad_number") String ad_number,@Field("image") String image,@Field("weblink") String weblink,@Field("imagepath") String imagepath);


    @POST("get_ad_id.php")
    @FormUrlEncoded
    Call<String> get_ad_id(@Field("ad_number") String ad_number);

    @POST("delete_billescapers.php")
    @FormUrlEncoded
    Call<String> delete_billescapers(@Field("bid") String bid);

    @POST("update_billescaperprofile.php")
    @FormUrlEncoded
    Call<String> update_billescaperprofile(@Field("bid")String bid,@Field("e_name") String name, @Field("e_mobile1") String mobile1,@Field("e_mobile2") String mobile2,@Field("e_amount") String amount, @Field("e_village") String village,@Field("e_district") String district,@Field("e_neyojakavargam") String neyojakavargam,@Field("e_pincode") TextInputEditText pincode);

}
