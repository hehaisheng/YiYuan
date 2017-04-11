package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.PointAdapter;
import com.shawnway.nav.app.yylg.bean.PointRecordBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PointFragment extends ListFragment {

    private static final int DEF_PAGE_SIZE = 10;
    private static final String TAG = "PointFragment";


    @Override
    protected PointAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new PointAdapter(getContext());
        return (PointAdapter) mAdapter;
    }


    @Override
    public void onLoadMore() {
        Map<String, String> param = new HashMap<>();
        param.put("page", mNextPage + "");
        param.put("pageSize", DEF_PAGE_SIZE + "");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,BalanceResponseWrapper.class, Constants.MY_BALANCE_URL, param, new Response.Listener<BalanceResponseWrapper>() {
            @Override
            public void onResponse(BalanceResponseWrapper response) {
                if (!Utils.handleResponseError(getActivity(), response)) {
                    //不是本页数据，舍弃，不取消刷新状态
                    if (mNextPage != response.getBody().page)
                        return;

                    if (!Utils.noListData(response.getBody().page, response.getBody().totalPage, response.getBody().historyDetailsList)) {
                        int beflength = mAdapter.getListSize();
                        getAdapter().addAll(response.getBody().historyDetailsList);
                        getAdapter().notifyItemRangeInserted(beflength + 1, mAdapter.getListSize() - beflength);
                        mNextPage = response.getBody().page + 1;
                    }
                }
                onRefreshComplete();
                onLoadmoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(getContext());
                onRefreshComplete();
                onLoadmoreComplete();
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== Constants.ACTION_REQUEST_LOGIN&&resultCode == Activity.RESULT_OK) {
            Log.d(TAG,"onLoginResult");
            onRefresh();
        } else if (requestCode== Constants.ACTION_REQUEST_LOGIN){
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class BalanceResponseWrapper extends ResponseGson<BalanceResponseBody> {
    }

    private class BalanceResponseBody {
        int page;
        int pageSize;
        int totalPage;
        int totalRecord;
        String avalPoint;
        ArrayList<PointRecordBean> historyDetailsList;
    }
}
