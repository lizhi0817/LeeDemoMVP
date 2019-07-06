package com.lee.androidappx.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

/**
 * Created :  LiZhIX
 * Date :  2019/7/6 8:45
 * Description  :   网络监听状态
 */
public class NetWorkChangReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            LogUtils.d("wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        LogUtils.d(NetworkUtils.getNetworkType() + "WIFI");
                        ToastUtils.showLong("WIFI状态");
                    }else{
                        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            ToastUtils.showLong("您当前使用的是移动数据，注意流量哦！");
                            LogUtils.d("您当前使用的是移动数据，注意流量哦！");
                        }
                    }
                } else {
                    LogUtils.d(NetworkUtils.getNetworkType() + "断开");
                    ToastUtils.showLong("网络似乎断开了");
                }
            }
        }
    }
}