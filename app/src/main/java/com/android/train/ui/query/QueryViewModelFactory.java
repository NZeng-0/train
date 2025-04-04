package com.android.train.ui.query;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.RelationService;

public class QueryViewModelFactory implements ViewModelProvider.Factory {
    private final RelationService relationService;
    private Context context;
    public QueryViewModelFactory(Context context, RelationService relationService) {
        this.context = context;
        this.relationService = relationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(QueryViewModel.class)) {
            return (T) new QueryViewModel(context, relationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

