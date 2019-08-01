package com.lee.androidappx.view.login;

import com.lee.androidappx.base.BasePresenter;
import com.lee.androidappx.core.http.IRequest;
import com.lee.androidappx.model.DataCall;

import io.reactivex.Observable;

/**
 * Created :  LiZhIX
 * Date :  2019/8/1 19:09
 * Description  :   登录的网络请求
 */
public class LoginPresenter extends BasePresenter<IRequest> {

    public LoginPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Observable mObservable(Object... args) {
        return iRequest.login((String) args[0], (String) args[1]);
    }
}
