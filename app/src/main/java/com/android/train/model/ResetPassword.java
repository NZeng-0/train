package com.android.train.model;

public class ResetPassword {
    private String id;
    private String oldPassword;
    private String newPassword;

    public ResetPassword(String id, String oldPassword, String newPassword) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
