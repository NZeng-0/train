package com.android.train.ui.station;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.StationService;

public class StationViewModelFactory implements ViewModelProvider.Factory {
    private final StationService stationService;

    public StationViewModelFactory(StationService stationService) {
        this.stationService = stationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StationViewModel.class)) {
            return (T) new StationViewModel(stationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

