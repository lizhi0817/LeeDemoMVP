package com.lee.androidappx.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lee.androidappx.R;
import com.lee.androidappx.base.BaseActivity;
import com.lee.androidappx.core.exception.ApiException;
import com.lee.androidappx.model.DataCall;
import com.lee.androidappx.model.bean.LoginBean;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_phone_num)
    AppCompatEditText mLoginPhoneNum;       //手机号
    @BindView(R.id.login_pwd_et)
    AppCompatEditText mLoginPwdEt;          //密码
    @BindView(R.id.login_fast_registered)
    AppCompatTextView mLoginFastRegistered; //快速注册
    @BindView(R.id.login_login_bt)
    AppCompatButton mLoginLoginBt;          //登录
    @BindView(R.id.login_weixin)
    AppCompatImageView mLoginWeixin;        //微信登录
    @BindView(R.id.login_weibo)
    AppCompatImageView mLoginWeibo;         //微博登录
    @BindView(R.id.login_qq)
    AppCompatImageView mLoginQq;            //QQ登录
    @BindView(R.id.login_facebook)
    AppCompatImageView mLoginFacebook;      //FB登录
    @BindView(R.id.login_twitter)
    AppCompatImageView mLoginTwitter;       //推特登录
    private Unbinder mUnbinder;
    private boolean pasVisibile = false;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        mLoginPresenter = new LoginPresenter(new LoginDataCall());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void destroyData() {
        mUnbinder.unbind();

    }

    @OnClick({R.id.login_fast_registered, R.id.login_show_pwd, R.id.login_login_bt, R.id.login_weixin, R.id.login_weibo, R.id.login_qq, R.id.login_facebook, R.id.login_twitter})
    public void onClick(View v) {
        String phone = Objects.requireNonNull(mLoginPhoneNum.getText()).toString();
        String password = Objects.requireNonNull(mLoginPwdEt.getText()).toString();
        switch (v.getId()) {
            case R.id.login_fast_registered:
                intent(RegisteredActivity.class);
                break;
            case R.id.login_show_pwd:
                if (pasVisibile) {
                    //密码显示，则隐藏
                    mLoginPwdEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pasVisibile = false;
                } else {
                    //密码隐藏则显示
                    mLoginPwdEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pasVisibile = true;
                }
                break;
            case R.id.login_login_bt:
                if (!RegexUtils.isMobileExact(phone)) {
                    ToastUtils.showShort(R.string.login_correct_phone);
                    return;
                }
                if (password.trim().isEmpty()) {
                    ToastUtils.showShort(R.string.login_correct_pwd);
                    return;
                }
                mLoginPresenter.reqeust(phone, password);
                break;
            case R.id.login_weixin:
                break;
            case R.id.login_weibo:
                break;
            case R.id.login_qq:
                break;
            case R.id.login_facebook:
                break;
            case R.id.login_twitter:
                break;
        }
    }

    class LoginDataCall implements DataCall<LoginBean> {

        @Override
        public void onSuccess(LoginBean result) {
            LogUtils.d(result.toString());
        }

        @Override
        public void onFaild(ApiException e) {
            ToastUtils.showShort(e.getDisplayMessage());
        }
    }
}
