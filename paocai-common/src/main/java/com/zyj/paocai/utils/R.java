package com.zyj.paocai.utils;

import com.zyj.paocai.constant.Constant;

import java.io.Serializable;

/**
 * 统一返回格式
 *
 * @author lulx
 * @date 2022-03-15 21:47
 **/
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

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

    public static <T> R<T> ok() {
        return ok(Constant.SUCCESS_CODE, "success");
    }

    public static <T> R<T> ok(T data) {
        return ok(Constant.SUCCESS_CODE, "success", data);
    }

    public static <T> R<T> ok(String msg) {
        return ok(Constant.SUCCESS_CODE, msg);
    }

    public static <T> R<T> ok(int code, String msg) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> ok(int code, String msg, T data) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }


    public static <T> R<T> error() {
        return ok(Constant.ERROR_CODE, "error");
    }

    public static <T> R<T> error(String msg) {
        return ok(Constant.ERROR_CODE, msg);
    }

    public static <T> R<T> error(int code, String msg) {
        R<T> r = new R<>();
        r.code = code;
        r.msg = msg;
        return r;
    }


}
