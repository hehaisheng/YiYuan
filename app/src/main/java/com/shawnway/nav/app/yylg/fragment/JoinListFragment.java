package com.shawnway.nav.app.yylg.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.DetailJoinListAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.JoinBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/12/8.
 */
public class JoinListFragment extends ListFragmentNoPull {

    private DetailJoinListAdapter mAdapter;
    private GoodBean bindedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindedData = (GoodBean) getArguments().getSerializable("bean");
    }

    @Override
    protected DetailJoinListAdapter getAdapter() {
        if (mAdapter==null){
            mAdapter = new DetailJoinListAdapter( getContext());
        }
        return mAdapter;
    }

    @Override
    public void onLoadMore() {
        Map<String, String> parms = new HashMap<>();
        parms.put("page", String.valueOf(mNextPage));
        parms.put("drawcycleId", String.valueOf(bindedData.getDrawCycleID()));
        parms.put("pageSize", String.valueOf(Constants.DEF_PAGE_SIZE));
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,JoinResponseWraper.class, Constants.PATICIPATION_URL, parms, new Response.Listener<JoinResponseWraper>() {
            @Override
            public void onResponse(JoinResponseWraper response) {

                if (!Utils.handleResponseError(getActivity(), response)) {
                    successGetItem(response);
                }
                onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    ToastUtil.showShort(getContext(), "" + volleyError.networkResponse.statusCode);
                } catch (NullPointerException e) {
                    ToastUtil.showShort(getContext(), "" + volleyError.getStackTrace().toString());
                }
                onRefreshComplete();
            }
        });
    }



    private void successGetItem(JoinResponseWraper response) {
        try {
            if (mNextPage > response.getBody().totalPage || response.getBody().participationDetailsList.size() == 0) {
//            ToastUtil.show(DetailActivity.this, "该页无内容，请尝试其他分类", 1000);
                return;
            }
            mAdapter.addAll(response.getBody().participationDetailsList);
            mAdapter.notifyDataSetChanged();
            mNextPage = response.getBody().page + 1;
        } catch (NullPointerException e) {
            mAdapter.notifyDataSetChanged();

        }
    }


    class SpacesItemDecoration extends MyRecyclerView.ItemDecoration {

        private int space;
        private GridLayoutManager.LayoutParams lp;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        }
    }

    private class JoinResponseWraper extends ResponseGson<JoinBody> {
    }

    private class JoinBody {
        int page;
        int pageSize;
        int totalPage;
        int totalRecord;

        ArrayList<JoinBean> participationDetailsList;
    }
}
