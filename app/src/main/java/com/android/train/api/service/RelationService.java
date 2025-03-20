package com.android.train.api.service;

import com.android.train.api.ApiResponse;
import com.android.train.pojo.Relation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RelationService {
    // 获取车站列表
    @GET("train/train/list")
    Call<ApiResponse<List<Relation>>> getRelationList(
            @Query("pageNum") int pageNum,          // 分页页码
            @Query("pageSize") int size,            // 分页大小
            @Query("startRegion") String departure, // 出发地
            @Query("endRegion") String arrival,     // 到达地
            @Query("saleTime") String saleTime      // 到达地
    );
}
