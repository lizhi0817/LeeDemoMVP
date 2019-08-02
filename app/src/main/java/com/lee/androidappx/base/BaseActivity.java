package com.lee.androidappx.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.lee.androidappx.R;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 19:00
 * Description  :    Activity基础类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private long exitTime = 0;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式
        ImmersionBar.with(this).transparentBar().init();
        setContentView(getLayoutRes());
        //请求读写权限
        requestStoragePermission();
        //请求手机设备信息权限
        requestPhonePermission();
        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        BusUtils.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    public void onStop() {
        super.onStop();
        BusUtils.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyData();
        hideSoftKeyboard();
    }

    private void requestStoragePermission() {
        if (!PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            PermissionUtils.permission(PermissionConstants.STORAGE)
                    //当用户点击拒绝以后，每次打开APP都弹出请求权限
                    .rationale(new PermissionUtils.OnRationaleListener() {
                        @Override
                        public void rationale(ShouldRequest shouldRequest) {
                            shouldRequest.again(true);
                        }
                    })
                    .request();
        }

    }

    private void requestPhonePermission() {
        if (!PermissionUtils.isGranted(PermissionConstants.PHONE)) {
            PermissionUtils.permission(PermissionConstants.PHONE)
                    //当用户点击拒绝以后，每次打开APP都弹出请求权限
                    .rationale(new PermissionUtils.OnRationaleListener() {
                        @Override
                        public void rationale(ShouldRequest shouldRequest) {
                            shouldRequest.again(true);
                        }
                    })
                    .request();
        }

    }

    protected abstract int getLayoutRes();

    // 抽象 - 初始化方法，可以对数据进行初始化
    protected abstract void initData();

    protected abstract void initView();

    protected abstract void destroyData();

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

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 延迟执行
     */
    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        return HANDLER.postAtTime(r, this, uptimeMillis);
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }

    public void intent(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

}

