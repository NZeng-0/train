package com.android.train.api;

public class ApiResponse<T> {
    private int code;
    private String msg;
    private T rows;

    public int getCode() { return code; }
    public String getMsg() { return msg; }
    public T getRows() { return rows; }
}

