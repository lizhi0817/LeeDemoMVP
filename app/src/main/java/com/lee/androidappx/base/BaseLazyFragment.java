package com.lee.androidappx.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.lee.androidappx.MainActivity;

/**
 * Created :  LiZhIX
 * Date :  2019/7/6 9:03
 * Description  :    Fragment懒加载
 */
public abstract class BaseLazyFragment extends Fragment {
    private String mTextviewContent;
    private MainActivity mMainActivity;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setLayoutView(inflater, container, savedInstanceState);
    }

    /**
     * 设置布局
     *
     * @return
     * @param inflater
     * @param container
     */
    protected abstract View setLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

            LogUtils.d(mTextviewContent + "可见,加载数据");
        }
    }


    protected abstract void loadData();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //页面销毁,恢复标记
        isViewCreated = false;
        isUIVisible = false;

        LogUtils.d(mTextviewContent + "销毁了");
    }


}
