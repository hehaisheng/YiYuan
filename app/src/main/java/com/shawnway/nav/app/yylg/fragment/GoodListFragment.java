package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.GoodListAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodListGson;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.implemen.PageListIterator;
import com.shawnway.nav.app.yylg.implemen.ResponseListener;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.JsonUtil;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 11/6/2015.
 */
public class GoodListFragment extends ListFragment {


    private static final String TAG = "所有商品";
    private String defCateId = "-1";
    private int sizestyle;//区分item的大小布局
    private Map<String, String> mParams;
    private String mUrl;
    private ResponseListener mResponseListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle != null) {
            sizestyle = bundle.getInt("sizestyle", GoodListAdapter.STYLE_BIG_SIZE);
            mParams= JsonUtil.jsonToMap(bundle.getString("param"));
            mUrl=bundle.getString("url", Constants.BASE_CATAGORY_URL);
        }
    }

    String[] status = {GoodBean.TYPE_PROGRESS,GoodBean.TYPE_CLOSE};

    @Override
    public void onLoadMore() {
        lock();
        final ArrayList<GoodListGson> datas = new ArrayList<>();

        if (mParams==null)
            mParams=new HashMap<>();
        mParams.put("page", String.valueOf(mNextPage));
        mParams.put("pageSize", String.valueOf(Constants.DEF_PAGE_SIZE));
        mParams.put("viewMode","NORMAL");
        Log.d(TAG, "所有商品的参数："+mParams);
//            mParams.put("drawStatus",status[0]);
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, GoodListGsonResponse.class, mUrl,mParams, new Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                if (mResponseListener!=null)mResponseListener.onDeliverResponse(goodListGsonResponse);
                if (!Utils.handleResponseError(getActivity(),goodListGsonResponse)) {
                    insertNextPage(goodListGsonResponse.getBody());
                }
                onRefreshComplete();
                onLoadmoreComplete();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mResponseListener!=null) mResponseListener.onError(volleyError);
                onRefreshComplete();
                onLoadmoreComplete();
                ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError,getContext()));
            }
        });

    }

    private void loadCloseData(final ArrayList<GoodListGson> datas) {
        if (mParams==null)
            mParams=new HashMap<>();
        mParams.put("page", String.valueOf(mNextPage));
        mParams.put("pageSize", String.valueOf(Constants.DEF_PAGE_SIZE));
        mParams.put("drawStatus", status[1]);
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, GoodListGsonResponse.class, mUrl,mParams, new Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                if (mResponseListener!=null)mResponseListener.onDeliverResponse(goodListGsonResponse);
                if (!Utils.handleResponseError(getActivity(), goodListGsonResponse)) {
//                    insertNextPage(goodListGsonResponse.getBody());
                    GoodListGson body = goodListGsonResponse.getBody();
                    datas.add(body);
                    insertNextPage2(datas);
                }
                onRefreshComplete();
                onLoadmoreComplete();
            }
        }, new ErrorListener() {
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
//        mParams.put("drawStatus","OPEN");//先注释掉，看看数据没有过滤的时候有没有已经下架的商品，应该有才对的 TODO:加入下架了并且没有出售完毕的商品，然后数据量一多就会出现加载很慢的bug
        VolleyTool.getInstance(getContext()).getRequestQueue().cancelAll(this);
        onRefresh();
    }

    public void setResponseListener(ResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }
}
