package com.shawnway.nav.app.yylg.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Kevin on 2016/11/29
 */

public abstract class BaseActivity<T extends BasePresenter> extends Activity {
    protected Activity mContent;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mContent = this;
        ButterKnife.bind(this);
        mPresenter = getPresenter();
        initDataAndEvents();
        initDataAndEventsWithState(savedInstanceState);
    }

    /**
     * 初始化数据和事件
     *
     * @param savedInstanceState 带保存状态
     */
    protected void initDataAndEventsWithState(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化数据和事件（不带保存状态）
     */
    protected void initDataAndEvents() {

    }

    /**
     * 获得Presenter
     *
     * @return
     */
    protected T getPresenter() {
        return mPresenter;
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.releaseView();
        }
    }

    /**
     * 设置布局显示
     *
     * @param views
     */
    protected void setVisiableView(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置布局隐藏
     */
    protected void setGoneView(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }
}
