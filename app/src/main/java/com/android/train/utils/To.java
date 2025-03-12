package com.android.train.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 类型装换工具栏
 */
public class To {
    public static int toInt(String value) {
        return Integer.parseInt(value);
    }

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

}
