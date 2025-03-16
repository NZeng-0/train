package com.android.train.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.train.api.service.UserService;
import com.android.train.model.AjaxResult;
import com.android.train.model.UserRequest;
import com.android.train.pojo.User;
import com.android.train.utils.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final UserService userService;

    public MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    public MutableLiveData<String> msgLiveData = new MutableLiveData<>();
    public MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> navigateLiveData = new MutableLiveData<>();

    public AuthRepository(UserService userService) {
        this.userService = userService;
    }

    // 注册并自动登录
    public void registerAndLogin(UserRequest userRequest) {
        userService.registerUser(userRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<Void>> call, @NonNull Response<AjaxResult<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<Void> result = response.body();
                    if (result.isSuccess()) {
                        msgLiveData.postValue("注册成功，正在自动登录...");
                        login(userRequest); // 直接调用登录
                    } else {
                        msgLiveData.postValue(result.getMsg());
                    }
                } else {
                    msgLiveData.postValue("注册失败，请稍后重试");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<Void>> call, @NonNull Throwable t) {
                msgLiveData.postValue("网络错误，请稍后重试");
            }
        });
    }

    // 登录
    public void login(UserRequest userRequest) {
        userService.login(userRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<String>> call, @NonNull Response<AjaxResult<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<String> result = response.body();
                    if (result.isSuccess()) {
                        getUserInfo();
                        tokenLiveData.postValue(result.getData());
                    } else {
                        msgLiveData.postValue("登录失败：" + result.getMsg());
                    }
                } else {
                    msgLiveData.postValue("请稍后重试！");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<String>> call, @NonNull Throwable t) {
                msgLiveData.postValue("网络错误，请稍后重试");
            }
        });
    }

    // 获取用户信息
    private void getUserInfo() {
        userService.getUserInfo().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<User>> call, @NonNull Response<AjaxResult<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<User> result = response.body();
                    if (result.isSuccess()) {
                        userLiveData.postValue(result.getData());
                        navigateLiveData.postValue(true);
                        msgLiveData.postValue("登录成功");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<User>> call, @NonNull Throwable t) {
                Log.e("AuthViewModel", "获取用户信息失败: " + t.getMessage());
            }
        });
    }

    public LiveData<Boolean> getNavigateLiveData() {
        return navigateLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
