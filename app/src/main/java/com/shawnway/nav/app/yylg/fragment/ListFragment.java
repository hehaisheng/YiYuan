package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGson;
import com.shawnway.nav.app.yylg.implemen.PageListIterator;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 *Created by Eiffel on 2015/11/8.
 *
 *封装了RecyclerView的碎片，实现统一的加载中、下拉刷新、加载更多、无数据时的列表风格
 *需要注意的是该碎片封装了服务器分页的功能，在请求分页数据时要使用该类中的成员变量{@link #mNextPage},
 *
 * 主要用法有两种：A、需要分页 {@link ShareContentFragment}
 *              B、不需要分页{@link CheckMyCodeFragment},改情况需要重载onRefresh并自行上锁
 */
public abstract class ListFragment extends MyFragment implements MyRecyclerView.onLoadMoreListener,
        PullRefreshLayout.OnRefreshListener, View.OnClickListener {
    protected static final int START_PAGE_INDEX = 1;
    protected MyRecyclerView recyclerView;
    private View noDataWrapper;
    private View loadingWrapper;
    protected MCyclerAdapter<?> mAdapter;

    protected int mNextPage = START_PAGE_INDEX;
    private PullRefreshLayout mPullToRefreshLayout;
    private final String TAG="ListFragment";

    private View titleWrapper;
    private TextView tvTitleCenter;
    private View view;

    abstract protected MCyclerAdapter<?> getAdapter();
    @Override
    public abstract void onLoadMore();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recyclerlist, container, false);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recyclerview);

        noDataWrapper = view.findViewById(R.id.layout_nodata);
        loadingWrapper = view.findViewById(R.id.layout_loading);
//        mAdapter=new GoodListAdapter(getActivity());

        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.enableLoadMore();
        recyclerView.setOnLoadMoreListener(this);

        mPullToRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);
        mPullToRefreshLayout.setOnRefreshListener(this);

        titleWrapper = view.findViewById(R.id.recyclerlist_toolbar);
        tvTitleCenter = (TextView) view.findViewById(R.id.top_text_center);
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

    private void bindAdapter() {

        recyclerView.setAdapter(mAdapter);
    }

    public void onRefresh() {
        //@warn
        // 在已发送mNextPage=START_PAGE_INDEX的请求后若在请求结果返回前就调用这个
        // 方法则有可能导致旧请求的结果覆盖掉onRefresh时新发出的请求，因为mNextPage符合传入adapter的条件
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



    protected boolean insertNextPage(PageListIterator iterator){
        //检测是否所需页
        if (mNextPage != iterator.getPage())
            return false;//no need to insert
        //先处理错误，无错误则进行成功操作
        if (!Utils.noListData(iterator.getPage(), iterator.getTotalPage(), iterator.getList())) {
            int beflength = mAdapter.getListSize();
            //需要通过getAdapter来添加数据，否则需要自行强转
            Log.d(TAG,"adding");
            getAdapter().addAll(iterator.getList());
            getAdapter().notifyItemRangeInserted(beflength + 1, mAdapter.getListSize() - beflength);
            mNextPage = iterator.getPage()+ 1;
            return true;
        }
        return false;
    }

    protected boolean insertNextPage2(List<GoodListGson> list){

        if(list.size() == 1){
            GoodListGson body = list.get(0);
            return insertNextPage(body);
        }else if(list.size() == 2){
            //body
            GoodListGson body1 = list.get(0);
            GoodListGson body2 = list.get(1);
            //list数据
            ArrayList<GoodDetail> list1 = body1.getDrawCycleDetailsList();
            ArrayList<GoodDetail> list2 = body2.getDrawCycleDetailsList();
            //两个list数据整合到一起
            ArrayList goodBeans = new ArrayList<>();
            goodBeans.addAll(list1);
            goodBeans.addAll(list2);
            //检测是否所需页
            int page = 1;
            if (mNextPage != page)
                return false;//no need to insert
            if (!(Utils.noListData(body1.getPage(), body1.getTotalPage(), body1.getList()) && Utils.noListData(body2.getPage(), body2.getTotalPage(), body2.getList()))) {
                int beflength = mAdapter.getListSize();
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

    protected boolean isRefreshing;

    //暂时无用
    public void addData(Object _data) {
        throw new IllegalArgumentException("should Override public void addData() before using it ");
    }

    public void onRefreshComplete() {
        if (isRefreshing && getContext()!=null) {
            loadingWrapper.setVisibility(View.GONE);

            if (getAdapter().getItemCount() != 0) {
                noDataWrapper.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in));

            } else {
                recyclerView.setVisibility(View.GONE);
                noDataWrapper.setVisibility(View.VISIBLE);
            }
            mPullToRefreshLayout.setRefreshing(false);
            isRefreshing = false;
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

    /**
     * 获取改碎片中recyclerview的adapter
     * 一般需要将返回结果强制转换以获取MyCyclerAdapter的完整功能
     * 并且需要在此初始化adapter
     * @sample
     *    MyAdapter extend MCyclerAdapter<?>
     *    MyAdapter getAdapter(){
     *        if(mAdapter==null)
     *          mAdapter=new MyAdapter();
     *        return (MyAdapter)mAdapter;
     *    }
     */



    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        public SpacesItemDecoration(int position) {
        }
    }


}
