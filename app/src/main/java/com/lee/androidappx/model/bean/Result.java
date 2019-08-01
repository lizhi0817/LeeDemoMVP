package com.lee.androidappx.model.bean;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 23:32
 * Description  :
 */
public class Result<T> {
    public String status = " -1";
    public String message = "请求失败";
    public T result;

    public Result(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
