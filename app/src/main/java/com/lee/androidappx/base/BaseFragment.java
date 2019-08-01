package com.lee.androidappx.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 19:00
 * Description  :  Fragment基础类
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setLayoutView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        findViewById(view);
        setViewData(view);
        setClickEvent(view);
    }

    /**
     * 设置布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    public abstract View setLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * findViewById
     */
    public void findViewById(View view) {
    }

    /**
     * setViewData
     */
    public void setViewData(View view) {
    }

    /**
     * setClickEvent
     */
    public void setClickEvent(View view) {
    }


}
