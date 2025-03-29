package com.android.train.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.train.api.service.UserService;
import com.android.train.api.AjaxResult;
import com.android.train.model.ResetPassword;
import com.android.train.model.UserRequest;
import com.android.train.pojo.User;
import com.android.train.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final UserService userService;

    public MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    public MutableLiveData<String> msgLiveData = new MutableLiveData<>();
    public MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> navigateLiveData = new MutableLiveData<>();

    private final Context context;

    public AuthRepository(Context context, UserService userService) {
        this.context = context;
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
                        msgLiveData.postValue("注册成功，正在登录...");
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
                        tokenLiveData.postValue(result.getData());
                        getUserInfo();
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

    // 登出
    public void logout() {
        userService.logout().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<String>> call, @NonNull Response<AjaxResult<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<String> result = response.body();
                    if (result.isSuccess()) {
                        msgLiveData.postValue("退出成功");
                    } else {
                        msgLiveData.postValue("退出失败：" + result.getMsg());
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

    // 修改密码
    public void changePassword(String id, String oldPwd, String newPwd) {
        userService.changePassword(new ResetPassword(id, oldPwd, newPwd)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AjaxResult<String>> call, @NonNull Response<AjaxResult<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AjaxResult<String> result = response.body();
                    if (result.isSuccess()) {
                        ToastUtil.showToast(context, "密码修改成功");
                    } else {
                        ToastUtil.showToast(context, "旧密码错误，修改失败");
                    }
                } else {
                    ToastUtil.showToast(context, "修改失败, 请稍后重试！");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AjaxResult<String>> call, @NonNull Throwable t) {
                ToastUtil.showToast(context, "网络错误，请稍后重试");
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
