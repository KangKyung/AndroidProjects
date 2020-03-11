package com.example.momsee.Retrofit;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import io.reactivex.Observable;


public interface INodeJS {



    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password")String password);

    @POST("register_child")
    @FormUrlEncoded
    Observable<String> registerUser_child(@Field("child_name")String child_name,
                                         @Field("email")String email,
                                         @Field("child_age")String child_age);
    @POST("login_child")
    @FormUrlEncoded
    Observable<String> loginUser_child(@Field("email") String email,
                        @Field("child_name") String child_name);
    @POST("lock_unlock")
    @FormUrlEncoded
    Observable<String> lock_unlock(@Field("email")String email);

    @POST("Bitmap_bytearray")
    @FormUrlEncoded
    Observable<String> bitmap_bytearray(String bytearray);

    @POST("extract_parent_name")
    @FormUrlEncoded
    Observable<String> extract_parent_name(@Field("email")String email);

    @POST("extract_cName")
    @FormUrlEncoded
    Observable<String> extract_cName(@Field("email")String email);


    @POST("mission_add")
    @FormUrlEncoded
    Observable<String> mission_add(@Field("email")String email,
                                    @Field("child_name")String child_name,
                                   @Field("mission_cont")String content,
                                   @Field("mission_time")Integer time);
    @POST("mission_receive")
    @FormUrlEncoded
    Observable<String> mission_receive(@Field("email")String email);

    @POST("mission_receive_mission_cont")
    @FormUrlEncoded
    Observable<String> mission_receive_mission_cont(@Field("email")String email,
                                                    @Field("child_name")String child_name);

    @POST("mission_receive_mission_time")
    @FormUrlEncoded
    Observable<String> mission_receive_mission_time(@Field("email")String email,
                                                    @Field("child_name")String child_name);

    @POST("image_add")
    @FormUrlEncoded
    Observable<String> image_add(@Field("email")String email,
                                 @Field("child_name")String child_name,
                                 @Field("mission_image")String mission_image);

    @POST("image_load")
    @FormUrlEncoded
    Observable<String> image_load(@Field("email")String email);
}
