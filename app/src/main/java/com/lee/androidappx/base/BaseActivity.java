package com.lee.androidappx.base;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lee.androidappx.R;
import com.lee.androidappx.core.receiver.NetWorkChangReceiver;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 19:00
 * Description  :    Activity基础类
 */
public abstract class BaseActivity extends AppCompatActivity {


    private long exitTime = 0;
    // 加载框
    public Dialog mLoadDialog;
    private boolean isRegistered = false;
    private NetWorkChangReceiver mNetWorkChangReceiver;

    final RxPermissions mRxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(getLayoutRes());
        //隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //注册网络状态监听广播
        mNetWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this.mNetWorkChangReceiver, filter);
        isRegistered = true;
        // Must be done during an initialization phase like onCreate
        mRxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });
    }

    protected abstract int getLayoutRes();

    // 抽象 - 初始化方法，可以对数据进行初始化
    protected abstract void initData();

    //更改应用程序字体大小
    @Override
    protected void onResume() {
        super.onResume();

        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //保存页面的数据
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle
            persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        //如果savedInstanceState里面有数据的话 根据具体的业务逻辑做响应的操作
        if (savedInstanceState != null) {

        }
    }

    protected abstract void destoryData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryData();
        //解绑
        if (isRegistered) {
            unregisterReceiver(mNetWorkChangReceiver);
        }
    }

    /**
     * 初始化加载框
     */
    private void initLoad() {
        mLoadDialog = new ProgressDialog(this);// 加载框
        mLoadDialog.setCanceledOnTouchOutside(false);
        mLoadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (mLoadDialog.isShowing() && keyCode == KeyEvent.KEYCODE_BACK) {
                    cancelLoadDialog();//加载消失的同时
                    mLoadDialog.cancel();
                }
                return false;
            }
        });
    }

    public void cancelLoadDialog() {

    }

    @Override
    public Resources getResources() {
        //禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

    // 监听返回键，点击两次退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 5000) {
                ToastUtils.showShort(R.string.base_activity_exit);
                exitTime = System.currentTimeMillis();
            } else {
                ActivityUtils.finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

