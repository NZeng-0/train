package com.android.train.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.android.train.pojo.User;
import com.android.train.repository.AuthRepository;
import com.android.train.model.UserRequest;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<String> getToken() {
        return authRepository.tokenLiveData;
    }

    public LiveData<String> getMessage() {
        return authRepository.msgLiveData;
    }
    public LiveData<Boolean> getNavigateLiveData() {
        return authRepository.getNavigateLiveData();
    }
    public LiveData<User> getUserLiveData() {
        return authRepository.getUserLiveData();
    }

    // 直接调用注册+登录
    public void registerAndLogin(UserRequest userRequest) {
        authRepository.registerAndLogin(userRequest);
    }

    // 直接调用登录
    public void login(UserRequest userRequest) {
        authRepository.login(userRequest);
    }
}
