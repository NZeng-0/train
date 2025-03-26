package com.android.train.ui.order;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.RelationService;
import com.android.train.ui.query.QueryViewModel;

public class OrderViewModelFactory implements ViewModelProvider.Factory {
    private final RelationService relationService;
    private Context context;
    public OrderViewModelFactory(Context context, RelationService relationService) {
        this.context = context;
        this.relationService = relationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
            return (T) new OrderViewModel(context, relationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

