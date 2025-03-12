package com.android.train.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.service.StationService;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final StationService stationService;

    public HomeViewModelFactory(StationService stationService) {
        this.stationService = stationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(stationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

