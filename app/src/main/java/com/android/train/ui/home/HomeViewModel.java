package com.android.train.ui.home;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.utils.DateUtils;
import com.android.train.utils.PreferencesUtil;
import com.android.train.utils.To;
import com.github.gzuliyujiang.wheelpicker.DatePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.impl.UnitDateFormatter;
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout;

import java.util.Calendar;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> departureCity = new MutableLiveData<>();
    private final MutableLiveData<String> destinationCity = new MutableLiveData<>();
    private final MutableLiveData<String> selectedMonth = new MutableLiveData<>();
    private final MutableLiveData<String> selectedDay = new MutableLiveData<>();
    private final MediatorLiveData<String> formattedDate = new MediatorLiveData<>();

    public HomeViewModel() {
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

    public void swapText(Context context) {
        String temp = departureCity.getValue();
        departureCity.setValue(destinationCity.getValue());
        destinationCity.setValue(temp);

        String start = PreferencesUtil.getString(context, "destinationCity");
        String end = PreferencesUtil.getString(context, "departureCity");

        PreferencesUtil.putString(context,"destinationCity", end);
        PreferencesUtil.putString(context,"departureCity", start);
    }

    private void updateFormattedDate() {
        String month = selectedMonth.getValue();
        String day = selectedDay.getValue();
        if (month != null && day != null) {
            // 格式化日期为 "MM月dd日"
            String formatted = DateUtils.getFormattedDate(
                    To.toInt(selectedMonth.getValue()),
                    To.toInt(selectedDay.getValue())
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
    public void setDepartureCity(String city) {
        departureCity.setValue(city);
    }
    public void setDestinationCity(String city) {
        destinationCity.setValue(city);
    }
}