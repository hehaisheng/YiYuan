package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.LstAnnounAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.LastestAnnounceResponse;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/1 0001.
 */
public class RealLstAnnounFragment extends ListFragment {
    private static final String TAG = "RealLstAnnounFragment";
    public static final int REALLST_PAGESIZE = 50;
    private ArrayList<GoodDetail> mDatas = new ArrayList<>();
    private String physicalId;

    @Override
    protected LstAnnounAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new LstAnnounAdapter(mDatas,getContext());
        return (LstAnnounAdapter) mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        physicalId = getArguments().getString("value","1");
        CloseActivityUtil.add(getActivity());
        handler.postDelayed(run,5*1000);//有网络的时候，有数据的时候才更新
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(run);
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            Hook.getInstance(getContext()).getRealLatestPageAnnounce(1, REALLST_PAGESIZE,physicalId,
                    new Response.Listener<LastestAnnounceResponse>() {
                        @Override
                        public void onResponse(LastestAnnounceResponse lastestAnnounceResponse) {
                            if (!Utils.handleResponseError(getActivity(), lastestAnnounceResponse)) {
                                LastestAnnounceResponse.LastestAnnounceBody body = lastestAnnounceResponse.getBody();
                                ArrayList<GoodDetail> data = new ArrayList<GoodDetail>();
                                data.addAll(body.getDrawCycleDetailsList());
                                for(int i = 0;i<data.size();i++){
                                    if (data.get(i).getDrawCycleID() != mDatas.get(i).getDrawCycleID()) {//最新的一个数据不一致，说明数据已经更新
                                        mDatas.remove(i);
                                        mDatas.add(i, data.get(i));
                                        Log.d(TAG, "有数据变化时刷新："+mDatas.get(i).getDrawCycleID()+"..."+mDatas.get(i).getDrawStatus());
                                        getAdapter().notifyItemChanged(i);
                                    } else if(data.get(i).getDrawStatus().equals(mDatas.get(i).getDrawStatus())==false){
                                        mDatas.remove(i);
                                        mDatas.add(i,data.get(i));
                                        getAdapter().notifyItemChanged(i);
                                    }
                                }
                                onRefreshComplete();
                                onLoadmoreComplete();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showNetError(getContext());
                        }
                    });
            handler.postDelayed(this, 1000 * 10);//每十秒刷新一次页面，几秒刷一次页面好呢？
        }
    };

    @Override
    public void onLoadMore() {
        if (!added) {
            Hook.getInstance(getContext()).getRealLatestPageAnnounce(mNextPage, REALLST_PAGESIZE, physicalId,
                    new Response.Listener<LastestAnnounceResponse>() {
                        @Override
                        public void onResponse(LastestAnnounceResponse response) {
                            //不是本页数据，舍弃，不取消刷新状态
                            if (response.getBody().getPage() != mNextPage)
                                return;
                            if (!Utils.noListData(response.getBody().getPage(), response.getBody().getTotalPage(), response.getBody().getDrawCycleDetailsList())) {
                                int start = mAdapter.getListSize();
                                mDatas.addAll(response.getBody().getDrawCycleDetailsList());
                                int count = mAdapter.getListSize() - start;
                                mAdapter.notifyItemRangeChanged(start, count);
                                mNextPage = response.getBody().getPage() + 1;
                            }
                            added = true;
                            onRefreshComplete();
                            onLoadmoreComplete();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showNetError(getContext());
                        }
                    });

        }
    }

    @Override
    public void onRefresh() {
        added = false;
        mDatas.clear();
        super.onRefresh();
    }

    private boolean added = false;

}
