package com.android.train.ui.order;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.api.AjaxResult;
import com.android.train.api.ApiResponse;
import com.android.train.api.service.RelationService;
import com.android.train.pojo.Order;
import com.android.train.pojo.Relation;
import com.android.train.pojo.Seat;
import com.android.train.utils.NotificationUtil;
import com.android.train.utils.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {

    private final MutableLiveData<List<Order>> orderList = new MutableLiveData<>();

    private final RelationService relationService;
    private Context context;

    public OrderViewModel(Context context, RelationService relationService) {
        this.context = context;
        this.relationService = relationService;
    }

    public void loadOrderList(int status) {
        String id = PreferencesUtil.getString(context,"id");
        relationService.getOrderList(id, status).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Order>>> call, @NonNull Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Order>> apiResponse = response.body();
                    if (apiResponse.getCode() == 200 && apiResponse.getRows() != null) {
                        orderList.setValue(response.body().getRows());
                    } else {
                        Log.e("OrderViewModel", "接口返回失败");
                    }
                } else {
                    Log.e("OrderViewModel", "获取订单失败"+response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Order>>> call, @NonNull Throwable t) {
                Log.e("OrderViewModel", "获取订单失败"+t);
            }
        });
    }

    public void cancelOrder(String id, int status) {
        relationService.cancelOrder(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AjaxResult<String>> call, Response<AjaxResult<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<String> apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        NotificationUtil.sendNotification(context, 1001, "退票成功通知", "您已退票成功");
                        loadOrderList(status);
                    } else {
                        Log.e("OrderViewModel", "接口返回失败");
                    }
                } else {
                    Log.e("OrderViewModel", "订单取消失败"+response.message());
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                Log.e("OrderViewModel", "取消订单失败"+t);
            }
        });
    }

    public LiveData<List<Order>> getOrderList() {
        return orderList;
    }
}