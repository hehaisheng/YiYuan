package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.LstAnnounAdapter;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/12/31.
 */
public class AllCyclesAnnounceFragment extends ListFragment {


    private static final String TAG = "AllCyclesAnnounceFragment";
    private long mProdId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProdId=getArguments().getLong("proid");
    }

    @Override
    protected LstAnnounAdapter getAdapter() {
        if (mAdapter==null){
            mAdapter=new LstAnnounAdapter(getContext());
        }
        return (LstAnnounAdapter)mAdapter;
    }

    @Override
    public void onLoadMore() {
        Map<String,String> params=new HashMap<>();
        params.put("productId", String.valueOf(mProdId));
        params.put("page", String.valueOf(mNextPage));
        params.put("pageSize", String.valueOf(Constants.DEF_PAGE_SIZE));
        params.put("DrawStatus","ANNOUNCED");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,GoodListGsonResponse.class, Constants.AllCYCLE, params, new Response.Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse response) {
                //不是本页数据，舍弃，不取消刷新状态
                if (response.getBody().getPage() != mNextPage)
                    return;
                //检测list是否为空表
                if (!Utils.noListData(response.getBody().getPage(), response.getBody().getTotalPage(), response.getBody().getDrawCycleDetailsList())) {
                    LstAnnounAdapter adapter = getAdapter();
                    int start = adapter.getListSize();
                    adapter.addAll(response.getBody().getDrawCycleDetailsList());

                    int count = adapter.getListSize() - start;
                    adapter.notifyItemRangeChanged(start, count);
                    mNextPage = response.getBody().getPage() + 1;

                }
                onRefreshComplete();
                onLoadmoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(getActivity());
                onRefreshComplete();
                onLoadmoreComplete();
            }
        });
    }
}
