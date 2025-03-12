package com.android.train.service;

import com.android.train.pojo.StationInfo;
import com.android.train.utils.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StationService {

    // 获取车站列表
    @GET("train/station/list")
    Call<ApiResponse<List<StationInfo>>> getStationList();

}