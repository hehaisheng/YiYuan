package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.ShareDetialItemAdapter;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.PrizeShowResponse;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/7.
 */
public class ShareDetialFragment extends ListFragment {
    private static final String TAG = "ShareDetailFragment";
    ShareListWrapper.ShareBean bean;

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
        bean = (ShareListWrapper.ShareBean) getArguments().getSerializable("bean");
        long prizeShowId;
        if (bean == null) {
            if (isAdded())
                ZProgressHUD.getInstance(getContext()).setMessage(getString(R.string.loading)).show();
            prizeShowId = getArguments().getLong("id");
            Log.d(TAG, "prizeshowId:" + prizeShowId);
            Map<String, String> params = new HashMap<>();
            params.put("prizedShowId", String.valueOf(prizeShowId));
            VolleyTool.getInstance(getContext()).sendGsonRequest(this, PrizeShowResponse.class,
                    Constants.PRIZESHOW, params,
                    new Response.Listener<PrizeShowResponse>() {
                        @Override
                        public void onResponse(PrizeShowResponse response) {
                            if (getContext() != null) {
                                getAdapter().setHeaderData(response.getBody());
                                bean = response.getBody();
                                onLoadMore();
                                ZProgressHUD.dismis();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            onLoadMore();
                            if (isAdded())
                                ToastUtil.showShort(getContext(), getString(R.string.error_system));
                            ZProgressHUD.dismis();
                        }
                    });

        }
    }


    @Override
    public void onLoadMore() {
        if (!added && bean != null) {
            getAdapter().addAll(bean.images);
            getAdapter().notifyDataSetChanged();
            onRefreshComplete();
            onLoadmoreComplete();
            added = true;
        }
    }

    @Override
    public void onRefresh() {
        added = false;
        super.onRefresh();
    }

    private boolean added = false;


    @Override
    protected ShareDetialItemAdapter getAdapter() {
        if (mAdapter == null && bean != null)
            mAdapter = new ShareDetialItemAdapter(bean, getActivity());
        else if (mAdapter == null)
            mAdapter = new ShareDetialItemAdapter(getActivity());
        return (ShareDetialItemAdapter) mAdapter;
    }
}
