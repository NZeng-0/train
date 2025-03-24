package com.android.train.ui.ticket;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TicketViewModel extends ViewModel {

    private final MutableLiveData<String> remainingTime = new MutableLiveData<>();
    private CountDownTimer countDownTimer;

    public TicketViewModel() {
        startTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) { // 60秒倒计时
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                remainingTime.setValue(seconds / 60 + "分" + seconds % 60 + "秒");
            }

            public void onFinish() {
                remainingTime.setValue("订单超时");
            }
        }.start();
    }

    public LiveData<String> getRemainingTime() {
        return remainingTime;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}