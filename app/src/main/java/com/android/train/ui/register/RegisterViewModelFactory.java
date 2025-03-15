package com.android.train.ui.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.train.api.service.UserService;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private final UserService userService;

    public RegisterViewModelFactory(UserService userService) {
        this.userService = userService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(userService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}