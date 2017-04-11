package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.GrantRecordAdapter;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.bean.GrantHisBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.implemen.PageListIterator;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.RanUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eiffel on 2016/2/15.
 */
public class GrantRecordFragment extends ListFragmentNoPull{

    @Override
    protected GrantRecordAdapter getAdapter() {
        if (mAdapter==null)
            mAdapter=new GrantRecordAdapter(getActivity());
        return (GrantRecordAdapter)mAdapter;
    }

    @Override
    public void onLoadMore() {

        if(!Constants.DEBUG){
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

                    ArrayList list = new ArrayList<GrantRecordAdapter.GrantRecordBean>(){};

                    for(int i = 0;i<20;i++){
                        GrantRecordAdapter.GrantRecordBean bean = new GrantRecordAdapter.GrantRecordBean();
                        bean.payee = "138"+RanUtils.ranN(4)+"125"+RanUtils.ran_str_1();
                        int money = RanUtils.ran(50)+1;
                        bean.amount = money+"";
                        bean.productName = "第"+(int)(RanUtils.ran(5)+1)+"期 "+"消费劵￥"+money;
                        bean.grantDate = "2016-0"+(int)(RanUtils.ran(9)+1)+"-"+(int)(RanUtils.ran(2)+1)+(int)(RanUtils.ran(9)+1)+" "+"22:01:22";
                        list.add(bean);
                    }
                    return list;
                }
            });
        }else{
            //已完成TODO: 参数
            Map<String, String> param = new HashMap<>();
            param.put("page", mNextPage + "");
            param.put("pageSize", Constants.DEF_PAGE_SIZE + "");
            VolleyTool.getInstance(getContext()).sendGsonRequest(this,MGrantRecordWrapper.class, Constants.GRANTRECORD_URL, param, new Response.Listener<MGrantRecordWrapper>() {
                @Override
                public void onResponse(MGrantRecordWrapper response) {

                    if (!Utils.handleResponseError(getActivity(), response)) {
                        MGrantRecBody body = response.getBody();
                        //检测是否所需页
                        if (mNextPage != body.page)
                            return;
                        //处理错误，无错误则进行成功操作
                        if (!Utils.noListData(body.page, body.totalPage, body.grantRecordList)) {
                            int start = mAdapter.getListSize();
                            getAdapter().addAll(response.getBody().grantRecordList);
                            int count = mAdapter.getListSize() - start;
                            getAdapter().notifyItemRangeInserted(start, count);
                            mNextPage = body.page + 1;
                        }

                    }

                    onRefreshComplete();
                    onLoadmoreComplete();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
                    onRefreshComplete();
                    onLoadmoreComplete();
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            onLoadMore();
        } else if (requestCode== Constants.ACTION_REQUEST_LOGIN){
            getActivity().finish();
        }

    }

    private class MGrantRecordWrapper extends ResponseGson<MGrantRecBody> {
    }

    private class MGrantRecBody {
        int page;
        int pageSize;
        int totalPage;
        int totalRecord;
        ArrayList<GrantRecordAdapter.GrantRecordBean> grantRecordList;
    }

    public static Fragment getInstance() {
        return new GrantRecordFragment();
    }
}
