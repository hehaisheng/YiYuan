package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.SelctCycAdapter;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2016/3/22.
 */
public class SelectCycFragment extends ListFragment {
    private  GoodBean mBean;
    public boolean isFisrt=true;
    public int cyeleCount=Constants.DEF_PAGE_SIZE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBean = (GoodBean) getArguments().getSerializable("bean");
    }

    @Override
    protected SelctCycAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new SelctCycAdapter(getContext());
        return (SelctCycAdapter) mAdapter;
    }

    @Override
    public void onLoadMore() {


        if(isFisrt)
        {
            cyeleCount=judgePageSize();
            isFisrt=false;
        }

            Map<String,String> params=new HashMap<>();
            params.put("productId", String.valueOf(mBean.getProductID()));
            params.put("page", String.valueOf(mNextPage));
           // params.put("pageSize", String.valueOf(Constants.DEF_PAGE_SIZE));
            params.put("pageSize", String.valueOf(cyeleCount));
            if (Constants.DEBUG)
                params.put("DrawStatus","OPEN");

            VolleyTool.getInstance(getContext()).sendGsonRequest(this,GoodListGsonResponse.class, Constants.AllCYCLE, params, new Response.Listener<GoodListGsonResponse>() {
                @Override
                public void onResponse(GoodListGsonResponse response) {
                    //不是本页数据，舍弃，不取消刷新状态
                    if (response.getBody().getPage() != mNextPage)
                        return;
                    //检测list是否为空表
                    if (!Utils.noListData(response.getBody().getPage(), response.getBody().getTotalPage(), response.getBody().getDrawCycleDetailsList())) {
                        SelctCycAdapter adapter = getAdapter();
                        int start = adapter.getListSize();
                        if(start<200)
                        {
                            adapter.addAll(response.getBody().getDrawCycleDetailsList());
                            Log.d("dkjslhc",response.getBody().getDrawCycleDetailsList().get(0).getCycle()+"");
                            Log.d("cdkjs",response.getBody().getTotalRecord()+"");
                            int count = adapter.getListSize() - start;
                            adapter.notifyItemRangeChanged(start, count);
                            mNextPage = response.getBody().getPage() + 1;
                        }


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

    public int judgePageSize()
    {
        Map<String,String> params=new HashMap<>();
        params.put("productId", String.valueOf(mBean.getProductID()));
        params.put("page", String.valueOf(mNextPage));
        params.put("pageSize", String.valueOf(Constants.DEF_PAGE_SIZE));
        //params.put("pageSize", String.valueOf(20));
        if (Constants.DEBUG)
            params.put("DrawStatus","OPEN");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,GoodListGsonResponse.class, Constants.AllCYCLE, params,new Response.Listener<GoodListGsonResponse>()
        {
            @Override
            public void onResponse(GoodListGsonResponse response) {
                if (!Utils.noListData(response.getBody().getPage(), response.getBody().getTotalPage(), response.getBody().getDrawCycleDetailsList())) {

                   //int  tempCycle = response.getBody().getDrawCycleDetailsList().get(0).getCycle();
                    int tempCycle=response.getBody().getTotalRecord();
                    if(tempCycle >=200)
                    {

                        MyApplication.getInstance().setCycle(200);
                    }
                    else
                    {
                        MyApplication.getInstance().setCycle(tempCycle);
                    }
                }


            }

        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(getActivity());
                onRefreshComplete();
                onLoadmoreComplete();

            }

        }

        );
        return MyApplication.getInstance().getCycle();
    }
}
