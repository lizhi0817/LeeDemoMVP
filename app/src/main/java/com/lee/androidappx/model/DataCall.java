package com.lee.androidappx.model;

import com.lee.androidappx.core.exception.ApiException;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 22:23
 * Description  :
 */
public interface DataCall<T> {

    void onSuccess(T data);

    void onFaild(ApiException e);
}
