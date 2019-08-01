package com.lee.androidappx.base;

import com.lee.androidappx.core.exception.CustomException;
import com.lee.androidappx.core.exception.ResponseTransformer;
import com.lee.androidappx.core.http.NetWorkManager;
import com.lee.androidappx.model.DataCall;
import com.lee.androidappx.model.bean.Result;

import java.lang.reflect.ParameterizedType;

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
public abstract class BasePresenter<T> {
    private DataCall mDataCall;
    private boolean running;
    private Disposable disposable;
    protected T iRequest;

    public BasePresenter(DataCall dataCall) {
        mDataCall = dataCall;
        Class<T> tClass = getTClass();
        iRequest = NetWorkManager.getInstance().create(tClass);
    }

    protected abstract Observable mObservable(Object... args);

    public void reqeust(Object... args) {
        if (running) {
            return;
        }

        running = true;

        disposable = mObservable(args)
                .compose(ResponseTransformer.handleResult()) //添加了一个全局的异常-观察者
                .compose(new ObservableTransformer() {
                    @Override
                    public ObservableSource apply(Observable upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        running = false;
                        if (result.status.equals("0000")) {
                            mDataCall.onSuccess(result.result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        running = false;
                        mDataCall.onFaild(CustomException.handleException(throwable));
                    }
                });
    }

    /**
     * 获取泛型对相应的Class对象
     *
     * @return
     */
    private Class<T> getTClass() {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        //返回表示此类型实际类型参数的 Type 对象的数组()，想要获取第二个泛型的Class，所以索引写1
        return (Class<T>) type.getActualTypeArguments()[0];//<T>
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