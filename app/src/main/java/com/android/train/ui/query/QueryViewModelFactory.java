package com.android.train.ui.query;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.RelationService;

public class QueryViewModelFactory implements ViewModelProvider.Factory {
    private final RelationService relationService;

    public QueryViewModelFactory(RelationService relationService) {
        this.relationService = relationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(QueryViewModel.class)) {
            return (T) new QueryViewModel(relationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

