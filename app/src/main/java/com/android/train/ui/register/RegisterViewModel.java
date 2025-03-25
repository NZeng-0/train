package com.android.train.ui.register;

import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.android.train.model.UserRequest;

public class RegisterViewModel extends ViewModel {

    // 收集表单数据
    public UserRequest collectFormData(
            String username,
            String password,
            Integer certType,
            String realName,
            String cardNumber,
            String phone,
            String email
    ) {
        return new UserRequest(username, password, realName, certType, cardNumber, phone, email);
    }

    // 验证用户名
    public boolean validateUsername(String username) {
        return username != null && username.length() >= 6 && username.length() <= 30;
    }

    // 验证密码
    public boolean validatePassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 30;
    }

    // 验证确认密码
    public boolean validateConfirmPassword(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    // 验证真实姓名
    public boolean validateRealName(String realName) {
        return realName != null && !realName.isEmpty();
    }

    // 验证证件号码
    public boolean validateCardNumber(String cardNumber) {
        return cardNumber != null && !cardNumber.isEmpty();
    }

    // 验证手机号码
    public boolean validatePhone(String phone) {
        return phone != null && phone.length() == 11;
    }

    // 验证邮箱
    public boolean validateEmail(String email) {
        return email == null || email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 验证服务条款是否勾选
    public boolean validateAgreement(boolean isAgreed) {
        return isAgreed;
    }
}