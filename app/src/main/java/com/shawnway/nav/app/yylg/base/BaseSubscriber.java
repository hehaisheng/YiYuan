package com.shawnway.nav.app.yylg.base;

import android.util.Log;

import rx.Subscriber;
import rx.android.BuildConfig;

/**
 * Created by Kevin on 2016/11/16
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "BaseSubscriber";

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onError: ", e);
        }
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 请求成功重写抽象方法
     *
     * @param t
     */
    public abstract void onSuccess(T t);
}
