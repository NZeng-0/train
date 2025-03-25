package com.android.train.api.service;

import com.android.train.model.UserRequest;
import com.android.train.api.AjaxResult;
import com.android.train.pojo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {
    @POST("/train/user")
    Call<AjaxResult<Void>> registerUser(@Body UserRequest userRequest);

    @POST("/train/user/login")
    Call<AjaxResult<String>> login(@Body UserRequest userRequest);

    @GET("/train/user/login")
    Call<AjaxResult<String>> logout();

    @GET("/train/user/info")
    Call<AjaxResult<User>> getUserInfo();
}
