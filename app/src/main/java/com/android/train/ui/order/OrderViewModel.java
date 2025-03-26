package com.android.train.ui.order;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.api.AjaxResult;
import com.android.train.api.service.RelationService;
import com.android.train.pojo.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private final RelationService relationService;
    private Context context;

    public OrderViewModel(Context context, RelationService relationService) {
        this.context = context;
        this.relationService = relationService;
    }

    public void loadOrderList(String id) {
        relationService.getOrderList(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<List<Order>>> call, @NonNull Response<AjaxResult<List<Order>>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<List<Order>>> call, @NonNull Throwable t) {

            }
        });
    }
}