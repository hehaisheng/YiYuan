package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.BuyRecordAdapter;
import com.shawnway.nav.app.yylg.adapter.EarnRecordAdapter;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.bean.BuyRecordWrapper;
import com.shawnway.nav.app.yylg.bean.EarnRecordWrapper;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2016/3/23.
 */
public class EarnRecordFragment extends ListFragmentNoPull {

    private static final String TAG = "BuyRecordFragment";
    private long mCustomerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomerId = getArguments().getLong("id");
    }

    public static EarnRecordFragment getInstance(long userid) {
        EarnRecordFragment fragment = new EarnRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected EarnRecordAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new EarnRecordAdapter(getContext());
            ((EarnRecordAdapter) mAdapter).setHasBanner(false);
        }
        return (EarnRecordAdapter) mAdapter;
    }

    @Override
    public void onLoadMore() {

        final Map<String, String> param = new HashMap<>();
        param.put("page", mNextPage + "");
        param.put("pageSize", Constants.DEF_PAGE_SIZE + "");
        param.put("customerId", mCustomerId + "");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, EarnRecordWrapper.class, Constants.CUSTOMER_INFO_WINPRIZE, param, new Response.Listener<EarnRecordWrapper>() {
            @Override
            public void onResponse(EarnRecordWrapper response) {
                if (!Utils.handleResponseError(false, getActivity(), response)) {
                    EarnRecordWrapper.EarnRecordBody body = response.getBody();
                    insertNextPage(body);
                }
                //数据加载完毕
                onRefreshComplete();
                onLoadmoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
                //数据加载完毕
                onRefreshComplete();
                onLoadmoreComplete();
            }
        });
    }
}
