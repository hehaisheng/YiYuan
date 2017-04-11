package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.GoodListAdapter;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

/**
 * Created by Eiffel on 2016/3/21.
 */
public class CustomBuyFragment extends Fragment implements MyRecyclerView.onLoadMoreListener {

    private GoodListAdapter mAdapter;
    private int mNextPage=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cusbuy,container,false);
        mAdapter=new GoodListAdapter(getActivity());

        MyRecyclerView recyclerView= (MyRecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(mAdapter);
        recyclerView.enableLoadMore();
        recyclerView.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(mAdapter.getLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadMore();
    }

    @Override
    public void onLoadMore() {
        String searchType="progress";
        String categoryId="-1";
        if (searchType!=null||categoryId!=null) {
            Response.Listener listerer = new Response.Listener<GoodListGsonResponse>() {
                @Override
                public void onResponse(GoodListGsonResponse response) {
                    if (response.getBody().getPage() != mNextPage)
                        return;

                    if (!Utils.noListData(response.getBody().getPage(), response.getBody().getTotalPage(), response.getBody().getDrawCycleDetailsList())) {

                      mAdapter.addAll(response.getBody().getDrawCycleDetailsList());
                        mAdapter.notifyDataSetChanged();
                        mNextPage = response.getBody().getPage() + 1;

                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
                }
            };
            String defCateId="-1";
            if (categoryId .equals( defCateId))
                Hook.getInstance(getContext()).catagory(searchType, "1", "NORMAL", mNextPage, Constants.DEF_PAGE_SIZE, listerer, errorListener);
            else
                Hook.getInstance(getContext()).catagory(searchType, categoryId,  "NORMAL", mNextPage, Constants.DEF_PAGE_SIZE, listerer, errorListener);
        }
    }
}
