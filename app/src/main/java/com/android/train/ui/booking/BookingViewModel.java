package com.android.train.ui.booking;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.R;
import com.android.train.api.service.RelationService;
import com.android.train.api.AjaxResult;
import com.android.train.model.SeatInfo;
import com.android.train.pojo.Seat;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingViewModel extends ViewModel {
    private final MutableLiveData<String> selectedSeatNumber = new MutableLiveData<>(null);
    private final MutableLiveData<String> selectedSeatClass = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Boolean>> seatVisibility = new MutableLiveData<>();
    private final Map<Integer, SeatInfo> seatInfoMap = new HashMap<>();
    private final RelationService relationService;
    private MutableLiveData<Seat> seatLiveData = new MutableLiveData<>();

    public BookingViewModel(RelationService relationService) {
        this.relationService = relationService;
    }

    // Getters for LiveData
    public LiveData<Seat> getSeatLiveData() {
        return seatLiveData;
    }
    public LiveData<String> getSelectedSeatNumber() {
        return selectedSeatNumber;
    }

    public LiveData<String> getSelectedSeatClass() {
        return selectedSeatClass;
    }

    public LiveData<Map<Integer, Boolean>> getSeatVisibility() {
        return seatVisibility;
    }

    // Initialize seat information
    public void initSeatInfoMap() {
        seatInfoMap.put(R.id.seat_a, new SeatInfo("A", R.drawable.seat_a, R.drawable.seat_a_select));
        seatInfoMap.put(R.id.seat_b, new SeatInfo("B", R.drawable.seat_b, R.drawable.seat_b_select));
        seatInfoMap.put(R.id.seat_c, new SeatInfo("C", R.drawable.seat_c, R.drawable.seat_c_select));
        seatInfoMap.put(R.id.seat_d, new SeatInfo("D", R.drawable.seat_d, R.drawable.seat_d_select));
        seatInfoMap.put(R.id.seat_f, new SeatInfo("F", R.drawable.seat_f, R.drawable.seat_f_select));
    }

    // Get seat info
    public SeatInfo getSeatInfo(int seatId) {
        return seatInfoMap.get(seatId);
    }

    // Reset seat selection
    public void resetSeatSelection() {
        selectedSeatNumber.setValue(null);
    }

    // Set selected seat
    public void setSelectedSeat(int seatId) {
        SeatInfo info = seatInfoMap.get(seatId);
        if (info != null) {
            selectedSeatNumber.setValue(info.seatNumber);
        }
    }

    // Update seat visibility based on class
    public void updateSeatState(String level) {
        Map<Integer, Boolean> visibilityMap = new HashMap<>();
        visibilityMap.put(R.id.seat_a, false);
        visibilityMap.put(R.id.seat_b, false);
        visibilityMap.put(R.id.seat_c, false);
        visibilityMap.put(R.id.seat_d, false);
        visibilityMap.put(R.id.seat_f, false);

        switch (level) {
            case "二等":
                visibilityMap.put(R.id.seat_a, true);
                visibilityMap.put(R.id.seat_b, true);
                visibilityMap.put(R.id.seat_c, true);
                visibilityMap.put(R.id.seat_d, true);
                visibilityMap.put(R.id.seat_f, true);
                break;
            case "一等":
                visibilityMap.put(R.id.seat_a, true);
                visibilityMap.put(R.id.seat_c, true);
                visibilityMap.put(R.id.seat_d, true);
                visibilityMap.put(R.id.seat_f, true);
                break;
            case "商务":
                visibilityMap.put(R.id.seat_a, true);
                visibilityMap.put(R.id.seat_c, true);
                visibilityMap.put(R.id.seat_f, true);
                break;
        }

        selectedSeatClass.setValue(level);
        seatVisibility.setValue(visibilityMap);
        resetSeatSelection();
    }

    // Validate before submitting order
    public boolean validateOrderSubmission() {
        return selectedSeatNumber.getValue() != null;
    }

    // Helper method to mask ID card
    public static String maskIdCard(String idCard) {
        int length = idCard.length();
        if (idCard.length() < 7) {
            return idCard.substring(0, 3) + "*".repeat(length - 5) + idCard.substring(length - 2);
        }
        return idCard.substring(0, 4) + "*".repeat(length - 7) + idCard.substring(length - 3);
    }

    public void getSeatInfo(String id, String type, String number){
        type = toNumber(type);
        relationService.getSeatByTrainAndNumber(id, type, number).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<AjaxResult<Seat>> call,
                    @NonNull Response<AjaxResult<Seat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<Seat> res = response.body();
                    if (res.getCode() == 200 && res.getData() != null) {
                        seatLiveData.postValue(res.getData());
                    } else {
                        Log.e("BookingViewModel", "接口返回失败");
                    }
                } else {
                    Log.e("BookingViewModel", "获取座位失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<Seat>> call, @NonNull Throwable t) {
                Log.e("BookingViewModel", "请求失败：" + t.getMessage());
            }
        });
    }

    private String toNumber(String number) {
        switch(number){
            case "商务":
                return "0";
            case "一等":
                return "1";
            default:
                return "2";
        }
    }
}
