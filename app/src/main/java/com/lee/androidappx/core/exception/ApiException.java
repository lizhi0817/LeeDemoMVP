package com.lee.androidappx.core.exception;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 22:45
 * Description  :  封装了自定义的异常
 */
public class ApiException extends Exception {
    private String code;//
    private String displayMessage;//提示的消息

    public ApiException(String code, String displayMessage) {
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public ApiException(String code, String message, String displayMessage) {
        super(message);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}