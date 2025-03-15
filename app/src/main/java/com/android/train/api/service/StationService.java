package com.android.train.api.service;

import com.android.train.pojo.StationInfo;
import com.android.train.api.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StationService {

    // 获取车站列表
    @GET("train/station/list")
    Call<ApiResponse<List<StationInfo>>> getStationList();

    // 通过名称查询车站列表
    @GET("train/station/list")
    Call<ApiResponse<List<StationInfo>>> getStationByName(@Query("name") String name);
}