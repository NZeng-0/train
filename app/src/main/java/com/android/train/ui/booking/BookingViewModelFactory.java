package com.android.train.ui.booking;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.RelationService;

public class BookingViewModelFactory implements ViewModelProvider.Factory {
    private final RelationService relationService;

    public BookingViewModelFactory(RelationService relationService) {
        this.relationService = relationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BookingViewModel.class)) {
            return (T) new BookingViewModel(relationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

