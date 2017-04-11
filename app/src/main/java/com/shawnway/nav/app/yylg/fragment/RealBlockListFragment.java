package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;

import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.adapter.RealBlockListAdapter;
import com.shawnway.nav.app.yylg.bean.RealBlockBean;
import com.shawnway.nav.app.yylg.implemen.PageListIterator;
import com.shawnway.nav.app.yylg.implemen.ResponseListener;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class RealBlockListFragment extends ListFragmentNoPull {

    private String value;//实体专区商品列表的第value个条目ood
    private ResponseListener mResponseListener;
    protected int mNextPage = 1;
    private Map<String, String> mParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle != null) {
            value=bundle.getString("value", Constants.VALUE);
        }
    }

    @Override
    protected MCyclerAdapter<?> getAdapter() {
        if (mAdapter == null) mAdapter = new RealBlockListAdapter(getActivity());
        return (RealBlockListAdapter) mAdapter;
    }

    @Override
    public void onLoadMore() {
        insertNextPage(new PageListIterator() {
            @Override
            public int getPage() {
                return mNextPage;
            }

            @Override
            public int getTotalPage() {
                return Integer.MAX_VALUE;
            }

            @Override
            public ArrayList getList() {
//                测试用的，已完成TODO:造一个实体区的bean,并将数据造好加入到list集合中
                ArrayList list = new ArrayList<RealBlockBean>();

                Random ran = new Random();
                for (int i = 0; i < 20; i++) {
                    RealBlockBean bean = new RealBlockBean();
                    bean.setProdName("Dior 迪奥 真我 淡香氛 香水");
                    bean.setPuredCnt(ran.nextInt(20));
                    bean.setTotCnt(50);
                    bean.setThumbnail("http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2Ftestforyiyuanlegou%2F2016123017252200028_600x600.jpg");
                    list.add(bean);
                }
                return list;
            }
        });
        //数据加载完毕
        onRefreshComplete();
        onLoadmoreComplete();
    }

    public void reset(String searchBy, String categoryId) {
        if (mParams==null)
            mParams=new HashMap<>();
        mParams.put("searchBy",searchBy);
        mParams.put("categoryId", categoryId);
        VolleyTool.getInstance(getContext()).getRequestQueue().cancelAll(this);
        onRefresh();
    }

    public void setResponseListener(ResponseListener mResponseListener) {
        this.mResponseListener = mResponseListener;
    }
}
