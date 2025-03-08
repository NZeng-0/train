package com.android.train.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> departureCity = new MutableLiveData<>("北京");
    private final MutableLiveData<String> destinationCity = new MutableLiveData<>("上海");

    public HomeViewModel() {
    }
    public void swapText() {
        // 获取当前文本
        String tempDestination = departureCity.getValue();

        // 交换文本
        departureCity.setValue(destinationCity.getValue());
        destinationCity.setValue(tempDestination);
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