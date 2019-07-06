package com.lee.androidappx.base;

import com.lee.androidappx.core.exception.CustomException;
import com.lee.androidappx.core.exception.ResponseTransformer;
import com.lee.androidappx.model.DataCall;
import com.lee.androidappx.model.bean.Data;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created :  LiZhIX
 * Date :  2019/7/3 19:01
 * Description  :    Base请求类
 */
public abstract class BasePresenter {
    private DataCall mDataCall;

    private boolean running;
    private Disposable disposable;

    public BasePresenter(DataCall dataCall) {
        mDataCall = dataCall;
    }

    protected abstract Observable mObservable(Object... args);

    public void reqeust(Object... args) {
        if (running) {
            return;
        }

        running = true;
        disposable = mObservable(args)
                .compose(ResponseTransformer.handleResult())//添加了一个全局的异常-观察者
                .compose(new ObservableTransformer() {
                    @Override
                    public ObservableSource apply(Observable upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Data>() {
                    @Override
                    public void accept(Data data) throws Exception {
                        running = false;
                        mDataCall.onSuccess(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        running = false;
                        mDataCall.onFaild(CustomException.handleException(throwable));
                    }
                });

    }

    public void cancelRequest() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void unBind() {
        mDataCall = null;
    }
}