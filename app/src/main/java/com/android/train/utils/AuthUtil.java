package com.android.train.utils;

import android.content.Context;

public class AuthUtil {
    public static boolean isLoggedIn(Context context) {
        String token = PreferencesUtil.getString(context, "token");
        return token != null && !token.isEmpty();
    }
}
