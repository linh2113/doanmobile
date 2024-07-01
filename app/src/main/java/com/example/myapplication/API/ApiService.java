package com.example.myapplication.API;

import com.example.myapplication.model.ChangePasswordResponse;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.response.NewLoginResponse;
import com.example.myapplication.response.RegisterRespone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("exec")
    Call<ChangePasswordResponse> changePassword(
            @Field("type") String type,
            @Field("gmailAddress") String gmailAddress,
            @Field("token") String token,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword
    );
    @GET("macros/s/AKfycbxJpmPDl_ySg22T06AHwj62EuYM5Yz_S26j7efRJxliDl2S5klNX-vx1w6PHhhxG3VnfA/exec")
    Call<RegisterRespone> register(
            @Query("type") String type,
            @Query("gmailAddress") String gmailAddress,
            @Query("password") String password
    );
    @GET("macros/s/AKfycbxJpmPDl_ySg22T06AHwj62EuYM5Yz_S26j7efRJxliDl2S5klNX-vx1w6PHhhxG3VnfA/exec")
    Call<NewLoginResponse> newlogin(
            @Query("type") String type,
            @Query("gmailAddress") String gmailAddress,
            @Query("password") String password
    );
    @GET("macros/s/AKfycbxJpmPDl_ySg22T06AHwj62EuYM5Yz_S26j7efRJxliDl2S5klNX-vx1w6PHhhxG3VnfA/exec")
    Call<LoginResponse> login(
            @Query("type") String type,
            @Query("gmailAddress") String gmailAddress,
            @Query("token") String token
    );
}
