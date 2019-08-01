package com.lee.androidappx.core.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 19:21
 * Description  :  封装了网络请求类
 */
public class NetWorkManager {

    private static volatile NetWorkManager singleton;
    private static final int RESPONSE_CODE = 5;
    private Retrofit mRetrofit;

    private NetWorkManager() {
        init();
    }

    private void init() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(RESPONSE_CODE, TimeUnit.SECONDS)
                .writeTimeout(RESPONSE_CODE, TimeUnit.SECONDS)
                .readTimeout(RESPONSE_CODE, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ApiService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static NetWorkManager getInstance() {
        if (singleton == null) {
            synchronized (NetWorkManager.class) {
                if (singleton == null) {
                    singleton = new NetWorkManager();
                }
            }
        }
        return singleton;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}
