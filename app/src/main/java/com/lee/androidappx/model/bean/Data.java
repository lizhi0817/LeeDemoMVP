package com.lee.androidappx.model.bean;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 23:32
 * Description  :
 */
public class Data<T> {
    int errorCode = -1;
    String errorMsg = "请求失败";
    T data;

    public Data(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


}
