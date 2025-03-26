package com.android.train.ui.ticket;

import android.content.Context;
import android.os.CountDownTimer;
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
import com.android.train.utils.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketViewModel extends ViewModel {

    private final MutableLiveData<String> remainingTime = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isTimeout = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> success = new MutableLiveData<>(false);

    private CountDownTimer countDownTimer;

    private Context context;
    private RelationService relationService;

    public TicketViewModel(Context context, RelationService relationService) {
        this.context = context;
        this.relationService = relationService;
        startTimer();
    }

    private void startTimer() {
        // 60秒倒计时
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                remainingTime.setValue(seconds / 60 + "分" + seconds % 60 + "秒");
            }

            public void onFinish() {
                remainingTime.setValue("订单超时");
                isTimeout.setValue(true);
            }
        }.start();
    }

    public LiveData<String> getRemainingTime() {
        return remainingTime;
    }

    public LiveData<Boolean> getIsTimeout() {
        return isTimeout;
    }

    public LiveData<Boolean> getSuccess(){
        return success;
    }

    public void cancelSeat(String id) {
        relationService.cancelSeatTicket(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<Void>> call, @NonNull Response<AjaxResult<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<Void> result = response.body();
                    if (result.isSuccess()) {
                        Log.e("TicketViewModel", "取消成功");
                    } else {
                        Log.e("TicketViewModel", "取消失败");
                    }
                } else {
                    Log.e("TicketViewModel", "请求失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<Void>> call, @NonNull Throwable t) {
                Log.e("TicketViewModel", "请求失败：" + t.getMessage());
            }
        });
    }

    public void createOrder(Order order) {
        relationService.createOrder(order).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<AjaxResult<String>> call,
                    @NonNull Response<AjaxResult<String>> response)
            {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<String> result = response.body();
                    if (result.isSuccess()) {
                        success.setValue(true);
                    } else {
                        success.setValue(false);
                    }
                } else {
                    success.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<AjaxResult<String>> call, Throwable t) {
                success.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}