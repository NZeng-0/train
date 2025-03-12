package com.android.train.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    // 获取 Retrofit 实例
    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            // 创建 OkHttpClient，添加日志拦截器
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS) // 连接超时
                    .readTimeout(10, TimeUnit.SECONDS)    // 读取超时
                    .writeTimeout(10, TimeUnit.SECONDS);   // 写入超时


            // 创建 Retrofit 实例
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // 设置 Base URL
                    .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析 JSON
                    .client(httpClient.build()) // 设置 OkHttpClient
                    .build();
        }
        return retrofit;
    }
}
