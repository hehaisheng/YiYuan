package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.MyWinRecAdapter;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyWinRecordFragment extends ListFragment {

    private static final String TAG = "MyWinRecordFragment";
    private int winRecordDel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winRecordDel = (int) Utils.getParam(getContext(), "winRecordDel", 0);
    }

    @Override
    public void onLoadMore() {
        Map<String, String> param = new HashMap<>();
        param.put("page", mNextPage + "");
        param.put("pageSize", Constants.DEF_PAGE_SIZE + "");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, MWinRecResponseWrapper.class, Constants.MY_WIN_URL, param, new Response.Listener<MWinRecResponseWrapper>() {
            @Override
            public void onResponse(MWinRecResponseWrapper response) {

                if (!Utils.handleResponseError(getActivity(), response)) {
                    MWinRecBody body = response.getBody();
                    //检测是否所需页
                    if (mNextPage != body.page)
                        return;
                    //处理错误，无错误则进行成功操作
                    if (!Utils.noListData(body.page, body.totalPage, body.winPrizeList)) {
                        int start = mAdapter.getListSize();
                        getAdapter().addAll(response.getBody().winPrizeList);
                        //删除已经转到余额的项目
                        for (int i = 0; i < getAdapter().getListSize(); i++) {
                            if (getAdapter().getItem(i).getWinprizeId() == winRecordDel) {
                                getAdapter().notifyItemRemoved(i);
                            }
                        }
                        Log.d(TAG, "added:" + getAdapter().getListSize());
                        int count = mAdapter.getListSize() - start;
                        getAdapter().notifyItemRangeInserted(start, count);
                        mNextPage = body.page + 1;
                    }

                }

                onRefreshComplete();
                onLoadmoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
                onRefreshComplete();
                onLoadmoreComplete();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "resultcode is " + resultCode + "  ;requestcode is " + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode != MyWinRecAdapter.REQUEST_CHOOSE_EARN) {
            Log.d(TAG, "onLoginResult");
            onRefresh();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN) {
            getActivity().finish();
        } else if (resultCode == Activity.RESULT_OK && requestCode == MyWinRecAdapter.REQUEST_CHOOSE_EARN) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }

    }


    @Override
    protected MyWinRecAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new MyWinRecAdapter(getActivity(), this);
        return (MyWinRecAdapter) mAdapter;
    }

    private class MWinRecResponseWrapper extends ResponseGson<MWinRecBody> {
    }

    private class MWinRecBody {
        int page;
        int pageSize;
        int totalPage;
        ArrayList<WinRecBean> winPrizeList;
    }


}
