package com.android.train.ui.ticket;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.RelationService;
import com.android.train.ui.query.QueryViewModel;

public class TicketViewModelFactory implements ViewModelProvider.Factory {
    private final RelationService relationService;
    private Context context;
    public TicketViewModelFactory(Context context, RelationService relationService) {
        this.context = context;
        this.relationService = relationService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TicketViewModel.class)) {
            return (T) new TicketViewModel(context, relationService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

