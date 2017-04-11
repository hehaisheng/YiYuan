package com.shawnway.nav.app.yylg.base;

import android.app.Activity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Kevin on 2016/11/29
 */

public class BasePresenter<T> {
    private Activity mActivity;
    protected T IView;
    private CompositeSubscription compositeSubscription;

    protected BasePresenter(Activity mActivity, T IView) {
        this.mActivity = mActivity;
        this.IView = IView;
    }

    /**
     * 将所有订阅者加入到订阅者组
     *
     * @param subscriptions
     */
    protected void addSubscriber(Subscription... subscriptions) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        for (Subscription subscription : subscriptions) {
            compositeSubscription.add(subscription);
        }
    }

    /**
     * 将订阅者组解绑
     */
    protected void unSubScriber() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();

        }
    }


    /**
     * 销毁时
     */
    protected void releaseView() {
        if (IView != null) {
            this.IView = null;
        }
        unSubScriber();
    }

}
