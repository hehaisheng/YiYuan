package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnway.nav.app.yylg.adapter.BuyCodeItemAdapter;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;

/**
 * Created by Eiffel on 2015/12/8.
 */
public class CheckMyCodeFragment extends ListFragment {
    MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData() {
        bean = (MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean) getArguments().getSerializable("bean");
    }

    @Override
    public void onLoadMore() {
        if (!added) {
            getAdapter().addAll(bean.getDrawDetails());
            getAdapter().notifyDataSetChanged();
            onRefreshComplete();
            onLoadmoreComplete();
            added=true;
        }
    }

    @Override
    public void onRefresh() {
        added=false;
        super.onRefresh();
    }

    private boolean added = false;


    @Override
    protected BuyCodeItemAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new BuyCodeItemAdapter( bean,getActivity());
        return (BuyCodeItemAdapter) mAdapter;
    }
}
