package com.android.train.ui.home;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.pojo.StationInfo;
import com.android.train.service.StationService;
import com.android.train.utils.ApiResponse;
import com.android.train.utils.DateUtils;
import com.android.train.utils.TypeTo;
import com.github.gzuliyujiang.wheelpicker.DatePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.impl.UnitDateFormatter;
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> departureCity = new MutableLiveData<>("北京");
    private final MutableLiveData<String> destinationCity = new MutableLiveData<>("上海");
    private final MutableLiveData<String> selectedMonth = new MutableLiveData<>();
    private final MutableLiveData<String> selectedDay = new MutableLiveData<>();
    private final MutableLiveData<List<StationInfo>> stationList = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MediatorLiveData<String> formattedDate = new MediatorLiveData<>();

    private final StationService stationService;

    public HomeViewModel(StationService stationService) {
        this.stationService = stationService;

        // 初始化日期
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth.setValue(String.valueOf(currentMonth));
        selectedDay.setValue(String.valueOf(currentDay));

        // 监听 selectedMonth 和 selectedDay 的变化
        formattedDate.addSource(selectedMonth, month -> updateFormattedDate());
        formattedDate.addSource(selectedDay, day -> updateFormattedDate());
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
                        errorMessage.setValue("接口返回失败: " + apiResponse.getMsg());
                    }
                } else {
                    errorMessage.setValue("加载车站列表失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<StationInfo>>> call, @NonNull Throwable t) {
                Log.e("HomeViewModel", "请求失败：" + t.getMessage());
                errorMessage.setValue("网络错误：" + t.getMessage());
            }
        });
    }


    public void swapText() {
        String temp = departureCity.getValue();
        departureCity.setValue(destinationCity.getValue());
        destinationCity.setValue(temp);
    }

    private void updateFormattedDate() {
        String month = selectedMonth.getValue();
        String day = selectedDay.getValue();
        if (month != null && day != null) {
            // 格式化日期为 "MM月dd日"
            String formatted = DateUtils.getFormattedDate(
                    TypeTo.toInt(selectedMonth.getValue()),
                    TypeTo.toInt(selectedDay.getValue())
            );
            formattedDate.setValue(formatted);
        }
    }

    public LiveData<String> getFormattedDate() {
        return formattedDate;
    }

    public void showDatePicker(Context context) {
        DatePicker picker = new DatePicker((Activity) context);
        picker.setBodyWidth(200);
        DateWheelLayout wheelLayout = picker.getWheelLayout();

        // 设置日期模式为 月日 选择
        wheelLayout.setDateMode(DateMode.MONTH_DAY);
        wheelLayout.setDateFormatter(new UnitDateFormatter());

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 计算 15 天后的日期
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH) + 1;
        int maxDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 设置日期范围
        DateEntity minDate = DateEntity.target(currentYear, currentMonth, currentDay);
        DateEntity maxDate = DateEntity.target(maxYear, maxMonth, maxDay);
        wheelLayout.setRange(minDate, maxDate);

        // 设置日期选择监听器
        picker.setOnDatePickedListener((year, month, day) -> {
            selectedMonth.setValue(String.valueOf(month));
            selectedDay.setValue(String.valueOf(day));
        });

        // 显示日期选择器
        picker.show();
    }

    public LiveData<String> getSelectedMonth() {
        return selectedMonth;
    }

    public LiveData<String> getSelectedDay() {
        return selectedDay;
    }

    public LiveData<String> getDepartureCity() {
        return departureCity;
    }

    public LiveData<String> getDestinationCity() {
        return destinationCity;
    }

    public LiveData<List<StationInfo>> getStationList() {
        return stationList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setDepartureCity(String city) {
        departureCity.setValue(city);
    }

    public void setDestinationCity(String city) {
        destinationCity.setValue(city);
    }
}