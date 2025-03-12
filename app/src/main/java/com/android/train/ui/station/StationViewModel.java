package com.android.train.ui.station;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.pojo.StationInfo;
import com.android.train.service.StationService;
import com.android.train.utils.ApiResponse;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationViewModel extends ViewModel {

    private final MutableLiveData<List<StationInfo>> stationList = new MutableLiveData<>();
    private final MutableLiveData<List<String>> errorMessage = new MutableLiveData<>();

    private final StationService stationService;

    public StationViewModel(StationService stationService) {
        this.stationService = stationService;
    }

    public void loadStationList() {
        stationService.getStationList().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<StationInfo>>> call,
                                   @NonNull Response<ApiResponse<List<StationInfo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<StationInfo>> apiResponse = response.body();
                    if (apiResponse.getCode() == 200 && apiResponse.getRows() != null) {
                        Log.d("HomeViewModel", "成功获取车站列表: " + apiResponse.getRows().toString() + " 个车站");
                        stationList.setValue(apiResponse.getRows());
                    } else {
                        Log.e("HomeViewModel", "接口返回失败");
                    }
                } else {
                    Log.e("HomeViewModel", "加载车站列表失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<StationInfo>>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "请求失败：" + t.getMessage());
            }
        });
    }

    public void search(String name) {
        stationService.getStationByName(name).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ApiResponse<List<StationInfo>>> call,
                    @NonNull Response<ApiResponse<List<StationInfo>>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    stationList.setValue(response.body().getRows());
                } else {
                    stationList.setValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ApiResponse<List<StationInfo>>> call,
                    @NonNull Throwable t) {
                stationList.setValue(Collections.emptyList());
            }
        });
    }

    public LiveData<List<StationInfo>> getStationList() {
        return stationList;
    }
}