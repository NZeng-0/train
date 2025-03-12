package com.android.train.utils;

public class ApiResponse<T> {
    private int code;
    private String msg;
    private T rows;

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public T getRows() { return rows; }
}

