package com.shawnway.nav.app.yylg.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Kevin on 2016/12/30
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    protected Activity mContent;

    protected T mPresenter;

    @Override
    public void onAttach(Activity context) {
        if (context != null)
            mContent = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getResourceId(),
                container,
                false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = getmPresenter();
        initDataAndEvents();
        initDataAndEvents(savedInstanceState);
    }


    /**
     * 布局id
     *
     * @return
     */
    public abstract int getResourceId();

    /**
     * 获取Presenter
     *
     * @return
     */
    public T getmPresenter() {
        return mPresenter;
    }

    /**
     * 初始化数据和事件
     */
    protected void initDataAndEvents() {

    }

    /**
     * 初始化数据和事件
     *
     * @param savedInstanceState 保存的状态
     */
    protected void initDataAndEvents(Bundle savedInstanceState) {

    }

}
