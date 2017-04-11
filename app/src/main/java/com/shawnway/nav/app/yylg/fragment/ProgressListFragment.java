package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.GoodListAdapter;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 11/6/2015.
 */
public class  ProgressListFragment extends ListFragment {

    private String mUrl;
    public static ProgressListFragment getInstance(String url){
        ProgressListFragment fragment=new ProgressListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl=getArguments().getString("url");
    }

    @Override
    public void onLoadMore() {
        Listener listerer = new Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse response) {
                if (mNextPage!=response.getBody().getPage()||mNextPage > response.getBody().getTotalPage() || response.getBody().getDrawCycleDetailsList().size() == 0) {
                    return;
                }
                getAdapter().addAll(response.getBody().getDrawCycleDetailsList());
                getAdapter().notifyDataSetChanged();
                mNextPage = response.getBody().getPage() + 1;
                onRefreshComplete();
                onLoadmoreComplete();
            }
        };
        ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(getActivity());
                onRefreshComplete();
                onLoadmoreComplete();
            }
        };
        Map<String,String>params=new HashMap<>();
        params.put("page",mNextPage+"");
        params.put("pageSize",20+"");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,GoodListGsonResponse.class, mUrl,params, listerer, errorListener);

    }


    @Override
    protected GoodListAdapter getAdapter() {
        if (mAdapter == null) mAdapter = new GoodListAdapter(getActivity());
        return (GoodListAdapter) mAdapter;
    }



}
