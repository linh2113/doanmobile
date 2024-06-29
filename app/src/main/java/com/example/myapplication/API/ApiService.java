package com.example.myapplication.API;

import com.example.myapplication.model.ChangePasswordResponse;

import retrofit2.Call;
import retrofit2.http.POST;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

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
}
