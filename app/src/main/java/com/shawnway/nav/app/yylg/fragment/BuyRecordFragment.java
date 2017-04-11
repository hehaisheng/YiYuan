package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.BuyRecordAdapter;
import com.shawnway.nav.app.yylg.bean.BuyRecordWrapper;
import com.shawnway.nav.app.yylg.bean.BuyRecordWrapper.BuyRecordBean;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodListGson;
import com.shawnway.nav.app.yylg.implemen.PageListIterator;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Eiffel on 2016/3/21.
 */
public class BuyRecordFragment extends ListFragmentNoPull {

    private static final String TAG = "BuyRecordFragment";
    private long mCustomerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomerId = getArguments().getLong("id");
    }

    public static BuyRecordFragment getInstance(int userid,String buyNum) {
        BuyRecordFragment fragment = new BuyRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", userid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected BuyRecordAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new BuyRecordAdapter(getContext());
            ((BuyRecordAdapter) mAdapter).setHasBanner(true);
        }
        return (BuyRecordAdapter) mAdapter;
    }

    String[] status = {GoodBean.TYPE_PROGRESS,GoodBean.TYPE_RELEAVED};
    final ArrayList<BuyRecordWrapper.BuyRecordBody> datas = new ArrayList<>();

    @Override
    public void onLoadMore() {

        if (!Constants.DEBUG) {
            insertNextPage(new PageListIterator() {
                @Override
                public int getPage() {
                    //请求的分页，用来对比是否大于本地的mNextPage变量，大于才添加进列表，否则视为滞后重复的请求
                    return mNextPage;
                }

                @Override
                public int getTotalPage() {
                    return Integer.MAX_VALUE;
                }

                @Override
                public ArrayList getList() {
                    //填充假数据，ArrayList的填充类型跟正式环境时wrapper里的ArrayList一致
                    ArrayList list= new ArrayList<BuyRecordBean>(){};
                    //完成BuyRecordBean的数据构造（已完成TODO）

                    Random ran = new Random();
                    for(int i = 0;i<20;i++){
                        BuyRecordBean bean = new BuyRecordBean();
                        bean.setDrawStatus("OPEN");
                        bean.setDrawCycleId(10);//某期商品的id
                        bean.setCustomerID(1002);//用户id
                        bean.setProdName("商品名字");//商品名
                        bean.setCycle(4);//第几期
                       int pured =  ran.nextInt(5);
                        bean.setPuredCnt(pured);//已参与人数
                        bean.setTotCnt(60);//总需人数
                        bean.setLeftCnt(60 - pured);//剩余人数
                        bean.setThumbnail("http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2Ftestforyiyuanlegou%2F2016123017252200028_600x600.jpg");//icon的url
                        list.add(bean);
                    }
                    return list;
                }
            });
            //数据加载完毕
            onRefreshComplete();
            onLoadmoreComplete();
        } else {
            lock();
            Map<String, String> param = new HashMap<>();
            param.put("customerID",mCustomerId+"");
            param.put("page", mNextPage + "");
            param.put("pageSize", Constants.DEF_PAGE_SIZE + "");
            VolleyTool.getInstance(getContext()).sendGsonRequest(this, BuyRecordWrapper.class, Constants.CUSTOMER_INFO_HISTORY, param, new Response.Listener<BuyRecordWrapper>() {
                @Override
                public void onResponse(BuyRecordWrapper response) {
                    if (!Utils.handleResponseError(false, getActivity(), response)) {
                        BuyRecordWrapper.BuyRecordBody body = response.getBody();
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

    private void loadAnnounceData(final ArrayList<BuyRecordWrapper.BuyRecordBody> datas) {
        Map<String, String> param = new HashMap<>();
        param.put("customerID",mCustomerId+"");
        param.put("page", mNextPage + "");
        param.put("pageSize", Constants.DEF_PAGE_SIZE/*buyNum*/ + "");
        param.put("drawStatus",status[1]);
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, BuyRecordWrapper.class, Constants.CUSTOMER_INFO_HISTORY, param, new Response.Listener<BuyRecordWrapper>() {
            @Override
            public void onResponse(BuyRecordWrapper response) {
                if (!Utils.handleResponseError(false, getActivity(), response)) {
                    BuyRecordWrapper.BuyRecordBody body = response.getBody();
                    datas.add(body);
                    Log.d(TAG, "第二次数据加载。。。" + datas.size());
                    unlock();
                    insertNextPage2(datas);
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

    boolean locked = true;

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }
}
