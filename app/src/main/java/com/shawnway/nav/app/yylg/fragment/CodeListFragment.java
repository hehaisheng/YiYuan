package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.CodeListAdapter;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2016/3/29.
 */
public class CodeListFragment  extends ListFragment {

    private static final String TAG = "CodeListFragment";
    private long mId;
    private Map mParams;

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
        mId=getArguments().getLong("id");
    }

    @Override
    public void onLoadMore() {
        mParams = new HashMap();
        mParams.put("drawCycleId", mId);
        Log.d(TAG, "乐购码id："+mId);
        if (!added) {
            VolleyTool.getInstance(getContext()).sendGsonRequest(this, CodeListResponse.class, Constants.WINNERCODE,mParams, new Response.Listener<CodeListResponse>() {
                @Override
                public void onResponse(CodeListResponse codeListResponse) {
                    if (!Utils.handleResponseError(getActivity(), codeListResponse)) {
                        Log.d(TAG, "乐购码id："+codeListResponse.getBody().drawCycleId);
                        Log.d(TAG, "乐购码中奖者参与人次：" + codeListResponse.getBody().totalPurch);
                        Log.d(TAG, "乐购码：" + codeListResponse.getBody().drawDetails);
                        CodeListAdapter adapter=getAdapter();
                        adapter.setHeaderData(codeListResponse.getBody().totalPurch);
                        adapter.addAll(codeListResponse.getBody().drawDetails);

                        getAdapter().notifyDataSetChanged();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError,getContext()));
                }
            });

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
    protected CodeListAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new CodeListAdapter(getActivity());
        return (CodeListAdapter) mAdapter;
    }

    private class CodeListResponse extends ResponseGson<CodeListBody>{

    }

    private class CodeListBody {
        public int cycle;
        public int drawCycleId;
        public ArrayList<MyBuyRecordWrapper.MyBuyRecordBean.DrawDetail>drawDetails;
        public String drawStatus;
        public int leftCnt;
        public String prodName;
        public int puredCnt;
        public String thumbnail;
        public int totCnt;
        public int totalPurch;
    }

}
