package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.GoodListAdapter;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.bean.GoodListGson;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.implemen.ResponseListener;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.JsonUtil;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/15 0015.
 */
public class TenBlockListFragment extends ListFragment {
    private String defCateId = "-1";
    private int sizestyle;//区分item的大小布局
    private Map<String, String> mParams;
    private String mUrl;
    private ResponseListener mResponseListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = Constants.BASE_CATAGORY_URL;
    }


    @Override
    public void onLoadMore() {
        lock();
        final ArrayList<GoodListGson> datas = new ArrayList<>();
        if (mParams==null)
            mParams=new HashMap<>();
        mParams.put("page", String.valueOf(mNextPage));
        mParams.put("pageSize", String.valueOf(Constants.TEN_BLOCK_PAGE_SIZE));
        mParams.put("categoryId","1");
        mParams.put("searchBy","all");
        mParams.put("viewMode","TENBLOCK");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, GoodListGsonResponse.class, mUrl,mParams, new Response.Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                if (mResponseListener!=null)mResponseListener.onDeliverResponse(goodListGsonResponse);
                if (!Utils.handleResponseError(getActivity(), goodListGsonResponse)) {
                    insertNextPage(goodListGsonResponse.getBody());
                }
                onRefreshComplete();
                onLoadmoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mResponseListener!=null) mResponseListener.onError(volleyError);
                onRefreshComplete();
                onLoadmoreComplete();
                ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
            }
        });
    }

    private void loadCloseData(final ArrayList<GoodListGson> datas) {
        if (mParams==null)
            mParams=new HashMap<>();
        mParams.put("page", String.valueOf(mNextPage));
        mParams.put("pageSize", String.valueOf(Constants.TEN_BLOCK_PAGE_SIZE));
        mParams.put("categoryId","1");
        mParams.put("searchBy","all");
        mParams.put("viewMode","TENBLOCK");
        mParams.put("drawStatus", "CLOSED");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, GoodListGsonResponse.class, mUrl,mParams, new Response.Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                if (mResponseListener!=null)mResponseListener.onDeliverResponse(goodListGsonResponse);
                if (!Utils.handleResponseError(getActivity(), goodListGsonResponse)) {
                    GoodListGson body = goodListGsonResponse.getBody();
                    datas.add(body);
                    insertNextPage2(datas);
                }
                onRefreshComplete();
                onLoadmoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mResponseListener!=null) mResponseListener.onError(volleyError);
                onRefreshComplete();
                onLoadmoreComplete();
                ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
            }
        });
    }

    boolean locked = true;

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }

    @Override
    protected GoodListAdapter getAdapter() {
        if (mAdapter == null) mAdapter = new GoodListAdapter(getActivity(), sizestyle);
        return (GoodListAdapter) mAdapter;
    }


    public void reset(String searchBy, String categoryId) {
        if (mParams==null)
            mParams=new HashMap<>();
        mParams.put("searchBy",searchBy);
        mParams.put("categoryId",categoryId);
        VolleyTool.getInstance(getContext()).getRequestQueue().cancelAll(this);
        onRefresh();
    }

    public void setResponseListener(ResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }
}
