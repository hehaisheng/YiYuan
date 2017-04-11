package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.base.BaseFragment;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsFragment extends BaseFragment<MyBuyRecordsPresenter> implements MyBuyRecordsContract.IMyBuyRecordsView, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rv_mybuy_records)
    RecyclerView rvMybuyRecords;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.loading_view_stub)
    ViewStub loadingViewStub;
    @BindView(R.id.nodata_view_stub)
    ViewStub noDataViewStub;

    private MyBuyRecordsAdapter myBuyRecordsAdapter;
    private List<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean> list;

    private static String ARG_DRAWSTATUS = "drawStatus";
    private int PAGE_NO = 1; //分页起始页数
    private int PAGE_SIZE = 5;//分页大小
    private String drawStatus;
    private int REQUEST_CODE_MYBUYRECORDS = 0;
    private View mNoDataView;
    private View mLoadingView;
    private TextView noDataDataText;
    private TextView loadingDataText;
    private String tip = "";


    @Override
    public int getResourceId() {
        return R.layout.fragment_mybuy_records;
    }

    /**
     * @param drawStatus 购买记录的状态 null代表全部，open代表已揭晓，annount代表进行中
     * @return
     */
    public static MyBuyRecordsFragment newInstance(String drawStatus) {

        Bundle args = new Bundle();
        args.putString(ARG_DRAWSTATUS, drawStatus);
        MyBuyRecordsFragment fragment = new MyBuyRecordsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MyBuyRecordsPresenter getmPresenter() {
        return new MyBuyRecordsPresenter(mContent, this);
    }

    @Override
    protected void initDataAndEvents() {
        getIntentArg();
        initView();
    }

    /**
     * 获得Intent参数
     */
    private void getIntentArg() {
        drawStatus = getArguments().getString(ARG_DRAWSTATUS, "");
    }

    /**
     * 初始化布局
     */
    private void initView() {
            initRecyclerView();
            initViewStub();
            initswipeRefresh();
    }


    private void initswipeRefresh() {
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        swipeRefresh.setOnRefreshListener(this);
    }

    private void initViewStub() {
        mLoadingView = loadingViewStub.inflate();
        loadingDataText = (TextView) mLoadingView.findViewById(R.id.no_data_text);
        showViewStub(loadingViewStub, loadingDataText, "");

        mNoDataView = noDataViewStub.inflate();
        noDataDataText = (TextView) mNoDataView.findViewById(R.id.no_data_text);
    }

    /**
     * 显示viewStub
     *
     * @param msg
     */
    private void showViewStub(ViewStub mViewStub, TextView dataText, String msg) {
        dataText.setText(msg);
        mViewStub.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏viewStub
     */
    private void dismissViewStub(ViewStub mViewStub) {
        mViewStub.setVisibility(View.GONE);
    }

    /**
     * 初始化RV
     */
    private void initRecyclerView() {
        rvMybuyRecords.setLayoutManager(new LinearLayoutManager(mContent,
                LinearLayoutManager.VERTICAL,
                false));
        list = new ArrayList<>();
        myBuyRecordsAdapter = new MyBuyRecordsAdapter(R.layout.layout_item_mbuyrecord, list);

        rvMybuyRecords.setAdapter(myBuyRecordsAdapter);

        myBuyRecordsAdapter.setOnLoadMoreListener(this);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        getMyBuyRecords();
    }

    /**
     * 获取我的购买记录
     */
    private void getMyBuyRecords() {
        MyBuyRecordsBean condition = new MyBuyRecordsBean();
        condition.setPageNo(PAGE_NO);
        condition.setPageSize(PAGE_SIZE);
        condition.setDrawStatus(drawStatus);
        mPresenter.getMyBuyRecords(condition);
    }

    /**
     * 添加我的购买记录
     *
     * @param myBuyRecordsBean
     */
    @Override
    public void addMyBuyRecords(MyBuyRecordsBean myBuyRecordsBean) {
        if (myBuyRecordsBean.getBody().getPurchaseDetailsList().size() > 0) {
            if (PAGE_NO == 1 && swipeRefresh.isRefreshing()) {
                myBuyRecordsAdapter.setNewData(myBuyRecordsBean.getBody().getPurchaseDetailsList());
            } else {
                myBuyRecordsAdapter.addData(myBuyRecordsBean.getBody().getPurchaseDetailsList());
            }
            PAGE_NO++;
        } else {
            if (PAGE_NO == 1) {
                showNoDataLayout();
            }
            myBuyRecordsAdapter.loadComplete();
        }
        swipeRefresh.setRefreshing(swipeRefresh.isRefreshing() ? false : false);
    }

    /**
     * 显示没有数据
     */
    private void showNoDataLayout() {
        showViewStub(noDataViewStub, noDataDataText, "很可惜没有记录~");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {
        dismissViewStub(loadingViewStub);
    }

    @Override
    public void errorLoading() {
        myBuyRecordsAdapter.loadComplete();
        loadingDataText.setText("加载失败,请点击重试");
        loadingDataText.setOnClickListener(this);
    }

    @Override
    public void starLoginActivity() {
        String dwp = (String) Utils.getParam(mContent, "dwp", "");
        setTip(TextUtils.isEmpty(dwp) ? "还没登录，请登录~" : "登录过时,需重新登录~");
        if (TextUtils.equals(drawStatus, "")) {
            Toast.makeText(mContent, tip, Toast.LENGTH_SHORT).show();
            LoginActivity.getInstance(mContent, REQUEST_CODE_MYBUYRECORDS, true);
            mContent.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_data_text:
                myBuyRecordsAdapter.openLoadMore(PAGE_SIZE);
                loadingDataText.setText("加载中...");
                getMyBuyRecords();
                break;
        }
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        PAGE_NO = 1;
        dismissViewStub(noDataViewStub);
        showViewStub(loadingViewStub, loadingDataText, "");
        getMyBuyRecords();
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

}
