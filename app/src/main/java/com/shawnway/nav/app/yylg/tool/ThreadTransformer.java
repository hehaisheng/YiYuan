package com.shawnway.nav.app.yylg.tool;

import rx.Observable;
import rx.Observable.Transformer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kevin on 2016/12/3
 */

public class ThreadTransformer {

    /**
     * 线程转换器
     * <p>
     * （哪个Observer调用即将该Observer变换让其自动添加切换线程）
     *
     * @param <T>
     * @return
     */
    public static <T> Transformer<T, T> applySchedulers() {
        return new Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            }
        };
    }

}
