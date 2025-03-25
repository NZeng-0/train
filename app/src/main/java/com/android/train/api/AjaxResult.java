package com.android.train.api;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 网络请求响应结果封装类
 *
 * @param <T> 返回数据的类型
 */
public class AjaxResult<T> {

    // 状态码
    private int code;

    // 返回消息
    private String msg;

    // 返回数据
    private T data;

    // 扩展字段（可选）
    private Map<String, Object> extra;

    // 成功状态码
    public static final int CODE_SUCCESS = 200;

    // 警告状态码
    public static final int CODE_WARN = 601;

    // 错误状态码
    public static final int CODE_ERROR = 500;

    /**
     * 构造函数
     *
     * @param code 状态码
     * @param msg  返回消息
     */
    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数
     *
     * @param code 状态码
     * @param msg  返回消息
     * @param data 返回数据
     */
    public AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功结果
     *
     * @return 成功结果
     */
    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(CODE_SUCCESS, "操作成功");
    }

    /**
     * 返回成功结果
     *
     * @param msg 返回消息
     * @return 成功结果
     */
    public static <T> AjaxResult<T> success(String msg) {
        return new AjaxResult<>(CODE_SUCCESS, msg);
    }

    /**
     * 返回成功结果
     *
     * @param data 返回数据
     * @return 成功结果
     */
    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(CODE_SUCCESS, "操作成功", data);
    }

    /**
     * 返回成功结果
     *
     * @param msg  返回消息
     * @param data 返回数据
     * @return 成功结果
     */
    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult<>(CODE_SUCCESS, msg, data);
    }

    /**
     * 返回警告结果
     *
     * @param msg 返回消息
     * @return 警告结果
     */
    public static <T> AjaxResult<T> warn(String msg) {
        return new AjaxResult<>(CODE_WARN, msg);
    }

    /**
     * 返回警告结果
     *
     * @param msg  返回消息
     * @param data 返回数据
     * @return 警告结果
     */
    public static <T> AjaxResult<T> warn(String msg, T data) {
        return new AjaxResult<>(CODE_WARN, msg, data);
    }

    /**
     * 返回错误结果
     *
     * @return 错误结果
     */
    public static <T> AjaxResult<T> error() {
        return new AjaxResult<>(CODE_ERROR, "操作失败");
    }

    /**
     * 返回错误结果
     *
     * @param msg 返回消息
     * @return 错误结果
     */
    public static <T> AjaxResult<T> error(String msg) {
        return new AjaxResult<>(CODE_ERROR, msg);
    }

    /**
     * 返回错误结果
     *
     * @param msg  返回消息
     * @param data 返回数据
     * @return 错误结果
     */
    public static <T> AjaxResult<T> error(String msg, T data) {
        return new AjaxResult<>(CODE_ERROR, msg, data);
    }

    /**
     * 返回错误结果
     *
     * @param code 状态码
     * @param msg  返回消息
     * @return 错误结果
     */
    public static <T> AjaxResult<T> error(int code, String msg) {
        return new AjaxResult<>(code, msg);
    }

    /**
     * 是否为成功结果
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    /**
     * 是否为警告结果
     *
     * @return 是否警告
     */
    public boolean isWarn() {
        return code == CODE_WARN;
    }

    /**
     * 是否为错误结果
     *
     * @return 是否错误
     */
    public boolean isError() {
        return code == CODE_ERROR;
    }

    /**
     * 添加扩展字段
     *
     * @param key   键
     * @param value 值
     * @return 当前对象
     */
    public AjaxResult<T> putExtra(String key, Object value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    /**
     * 获取扩展字段
     *
     * @param key 键
     * @return 扩展字段值
     */
    @Nullable
    public Object getExtra(String key) {
        return extra != null ? extra.get(key) : null;
    }

    // Getter 和 Setter 方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AjaxResult<?> that = (AjaxResult<?>) o;
        return code == that.code &&
                Objects.equals(msg, that.msg) &&
                Objects.equals(data, that.data) &&
                Objects.equals(extra, that.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg, data, extra);
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", extra=" + extra +
                '}';
    }
}