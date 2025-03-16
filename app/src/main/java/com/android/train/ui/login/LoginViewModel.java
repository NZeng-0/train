package com.android.train.ui.login;

import androidx.lifecycle.ViewModel;

import com.android.train.model.UserRequest;

public class LoginViewModel extends ViewModel {

    public UserRequest collectFormData(String username, String password) {
        return new UserRequest(username, password);
    }

    public boolean validateUsername(String username) {
        return username != null;
    }

    // 验证密码
    public boolean validatePassword(String password) {
        return password != null;
    }
}