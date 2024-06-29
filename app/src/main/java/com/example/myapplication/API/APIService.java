package com.example.myapplication.API;

import com.example.myapplication.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface APIService {
        //API: https://script.googleusercontent.com/macros/echo?user_content_key=WpIVh8sXrI9vdApq4Q0ctiFwTTVLrfoN3hDTYeWVFvxc_YwvT0WX6QJ-NCt_2V6ft0-YLQQ7wM-Zp4BvIC8Jy71_BD_Lxi6AOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa-iS52nz2VBwWCuyLVsxROqNGu3Cb7wX4BBXoxNsC-mDGxZ0eX5Qem8AJWCSlA6LfoTb8a1vEwXUPn3pECCFsF-KeI1lzEzaTo0JnAxiYl7Hr64PNQRkZZpgLUMKRgiLv3_lGQ8Nvefz1a7c5gtCMntj0Myx38PpJKr5CKcH5YgPoSaIrg8WiDhyS-vLTXzG0O-12Ayh4ue08WqJF7YBUxspt0h6A9mocpMM16etgj_oJel5lrQuTzZd-UdK3v3fZg&lib=Mg7DcDFymf8eRs11sY3j5MxI7YPL3rfOf
    Gson gson= new GsonBuilder().setDateFormat("yyyy-MM-dd:mm:ss").create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl("https://script.googleusercontent.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
    @GET("/macros/echo")
    Call<User> convertuser(@Query("user_content_key") String gmailAddress,
                           @Query("lib")String lib);
    @POST("/macros/echo")
    Call<User> updateUser(@Body User user);
}
