package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.bean.BuyRecordWrapper;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGson;
import com.shawnway.nav.app.yylg.implemen.PageListIterator;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 2015/12/15.
 */
public abstract class ListFragmentNoPull extends MyFragment implements MyRecyclerView.onLoadMoreListener, View.OnClickListener {
    protected static final int START_PAGE_INDEX = 1;
    private static final String TAG = "ListFragmentNoPull";
    private MyRecyclerView recyclerView;
    private View noDataWrapper;
    private View loadingWrapper;
    protected MCyclerAdapter<?> mAdapter;

    protected int mNextPage = START_PAGE_INDEX;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerlist_without_refresh, container, false);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerview);
        noDataWrapper = view.findViewById(R.id.layout_nodata);
        loadingWrapper = view.findViewById(R.id.layout_loading);
//        mAdapter=new GoodListAdapter(getActivity());

        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.enableLoadMore();
        recyclerView.setOnLoadMoreListener(this);

        initListener();
        return view;
    }

    private void initListener() {
        noDataWrapper.findViewById(R.id.bt_click_torefresh).setOnClickListener(this);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }


    abstract protected MCyclerAdapter<?> getAdapter();

    @Override
    public abstract void onLoadMore();



    private void bindAdapter() {

        recyclerView.setAdapter(mAdapter);
    }

    public void onRefresh() {
        isRefreshing = true;
        mNextPage = START_PAGE_INDEX;
        mAdapter = null;
        mAdapter = getAdapter();
        recyclerView.setLayoutManager(mAdapter.getLayoutManager(getActivity()));
        bindAdapter();
        onLoadMore();
    }


    public void onLoading() {
        recyclerView.setVisibility(View.GONE);
        loadingWrapper.setVisibility(View.VISIBLE);
        noDataWrapper.setVisibility(View.GONE);
    }


    protected int getDataSize(){
        return getAdapter().getListSize();
    }


    private boolean isRefreshing;



    public void onRefreshComplete() {
        loadingWrapper.setVisibility(View.GONE);

        if (getDataSize() != 0) {
            noDataWrapper.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
            noDataWrapper.setVisibility(View.VISIBLE);
        }
        isRefreshing = false;
    }

    protected boolean insertNextPage(PageListIterator iterator){
        //检测是否所需页
        if (mNextPage != iterator.getPage())
            return false;//no need to insert
        //先处理错误，无错误则进行成功操作
        if (!Utils.noListData(iterator.getPage(), iterator.getTotalPage(), iterator.getList())) {
            int beflength = mAdapter.getListSize();
            //需要通过getAdapter来添加数据，否则需要自行强转
            Log.d(TAG, "adding");
            getAdapter().addAll(iterator.getList());
            getAdapter().notifyItemRangeInserted(beflength + 1, mAdapter.getListSize() - beflength);
            mNextPage = iterator.getPage()+ 1;
            return true;
        }
        return false;
    }

    protected boolean insertNextPage2(List<BuyRecordWrapper.BuyRecordBody> list){

        if(list.size() == 1){
            BuyRecordWrapper.BuyRecordBody body = list.get(0);
            return insertNextPage(body);
        }else if(list.size() == 2){
            //body
            BuyRecordWrapper.BuyRecordBody body1 = list.get(0);
            BuyRecordWrapper.BuyRecordBody body2 = list.get(1);
            //list数据
            ArrayList<BuyRecordWrapper.BuyRecordBean> list1 = body1.getList();
            ArrayList<BuyRecordWrapper.BuyRecordBean> list2 = body2.getList();
            //两个list数据整合到一起
            ArrayList goodBeans = new ArrayList<>();
            goodBeans.addAll(list1);
            goodBeans.addAll(list2);
            Log.d(TAG, "两个list数据的长度："+goodBeans.size());
            //检测是否所需页
            int page = 1;
            if (mNextPage != page) {
                return false;//no need to insert
            }
            if (!(Utils.noListData(body1.getPage(), body1.getTotalPage(), body1.getList()) && Utils.noListData(body2.getPage(), body2.getTotalPage(), body2.getList()))) {
                int beflength = mAdapter.getListSize();
                Log.d(TAG, "两个list数据的页面更新。。。");
                getAdapter().addAll(goodBeans);
                getAdapter().notifyItemRangeInserted(beflength + 1, mAdapter.getListSize() - beflength);
                mNextPage = page + 1;
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    public  void onLoadmoreComplete(){
        recyclerView.onLoadComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_click_torefresh:
                onRefresh();
                break;
        }
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        public SpacesItemDecoration(int position) {
        }
    }

}
