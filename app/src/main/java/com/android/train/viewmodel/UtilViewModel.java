package com.android.train.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UtilViewModel extends ViewModel {
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<String> departure = new MutableLiveData<>();
    private final MutableLiveData<String> destination = new MutableLiveData<>();

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }
    public LiveData<String> getDeparture() {
        return departure;
    }

    public LiveData<String> getDestination() {
        return destination;
    }
    public void setDeparture(String city) {
        departure.setValue(city);
    }
    public void setDestination(String city) {
        destination.setValue(city);
    }
}
