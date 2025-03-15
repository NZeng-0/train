package com.android.train.api.service;

import com.android.train.model.UserRequest;
import com.android.train.model.AjaxResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/train/user")
    Call<AjaxResult<Void>> registerUser(@Body UserRequest userRequest);
}
