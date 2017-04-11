package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.LstAnnounAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.LastestAnnounceResponse;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/13.
 */
public class LstAnnounFragment extends ListFragment{
    private static final String TAG = "最新揭晓";
    public static final int LST_PAGE = 1;
    public static final int LST_PAGESIZE = 50;
    private ArrayList<GoodDetail> mDatas = new ArrayList<>();

    public LstAnnounFragment(){}
    @Override
    protected LstAnnounAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new LstAnnounAdapter(mDatas,getContext());
        return (LstAnnounAdapter) mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        handler.postDelayed(run,5*1000);//有网络的时候，有数据的时候才更新
        View view = super.onCreateView(inflater,container,savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacks(run);
    }
//
//    private Handler handler = new Handler();
//    private Runnable run = new Runnable() {
//        @Override
//        public void run() {//刷新
//            Hook.getInstance(getContext()).getLatestPageAnnounce(LST_PAGE, LST_PAGESIZE,
//                    new Response.Listener<LastestAnnounceResponse>() {
//                        @Override
//                        public void onResponse(LastestAnnounceResponse response) {
//                            if (!Utils.handleResponseError(getActivity(), response)) {
//                                LastestAnnounceResponse.LastestAnnounceBody body = response.getBody();
//                                ArrayList<GoodDetail> data = new ArrayList<GoodDetail>();
//                                data.addAll(body.getDrawCycleDetailsList());
//                                if (mDatas.size() == 0 || data.size() == 0) {
//                                    return;
//                                }
//                                if (data.size() > mDatas.size()) {
//                                    int addCount = data.size() - mDatas.size();
//                                    for (int i = 0; i < addCount;i++) {
//                                        mDatas.add(data.get(i));
//                                    }
//                                }
//                                for (int i = 0; i < data.size(); i++) {
//                                    if (data.get(i).getDrawCycleID() != mDatas.get(i).getDrawCycleID()) {//商品的id不一致，说明商品数据已经改变
//                                        mDatas.remove(i);
//                                        mDatas.add(i, data.get(i));
//                                        Log.d(TAG, "商品改变，替换改商品");
//                                        getAdapter().notifyItemChanged(i);
//                                        continue;//用这个是为了不进行多次数据的改变从而影响页面的多次变化
//                                    } else if (data.get(i).getDrawStatus().equals(mDatas.get(i).getDrawStatus()) == false) {//商品的状态已经改变，那么这个商品在adapter中的位置并没有发生改变，则只要修改这个
//                                        mDatas.remove(i);
//                                        mDatas.add(i, data.get(i));
//                                        Log.d(TAG, "状态不一致，倒计时时间结束:"+mDatas.get(i).getDrawStatus());
//                                        getAdapter().notifyItemChanged(i);
//                                        continue;
//                                    }
//                                }
//                            }
//                            onRefreshComplete();
//                            onLoadmoreComplete();
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            ToastUtil.showNetError(getContext());
//                        }
//                    });
//            handler.postDelayed(this, 1000 * 60);//每十秒刷新一次页面，几秒刷一次页面好呢？
//        }
//    };

    @Override
    public void onLoadMore() {//第一次加载数据
        if (!added) {
            Hook.getInstance(getContext()).getLatestPageAnnounce(mNextPage, LST_PAGESIZE,
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
                        mNextPage = response.getBody().getPage() + 2100000000;
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
