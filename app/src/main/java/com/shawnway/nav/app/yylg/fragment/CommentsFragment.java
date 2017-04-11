package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.CommentAdapter;
import com.shawnway.nav.app.yylg.bean.CommentsResponseWrapper;
import com.shawnway.nav.app.yylg.bean.CommentsResponseWrapper.CommentsBody;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper.ShareBean;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/12/9.
 */
public class CommentsFragment extends ListFragment {
    private static final String TAG = "CommentsFragment";
    ShareBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (ShareBean) getArguments().getSerializable("bean");
    }

    @Override
    protected CommentAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new CommentAdapter(getContext());
        }
        return (CommentAdapter) mAdapter;
    }

    @Override
    public void onLoadMore() {
        Map<String, String> param = new HashMap<>();
        param.put("page", mNextPage + "");
        param.put("pageSize", Constants.DEF_PAGE_SIZE + "");
        param.put("prizeShowId", String.valueOf(bean.prizeShowId));
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,CommentsResponseWrapper.class, Constants.COMMENTS_URL, param, new Response.Listener<CommentsResponseWrapper>() {
            @Override
            public void onResponse(CommentsResponseWrapper response) {

                if (!Utils.handleResponseError(getActivity(), response)) {
                    CommentsBody body = response.getBody();
                    //检测是否所需页
                    if (mNextPage != body.page)
                        return;
                    //先处理错误，无错误则进行成功操作
                    if (!Utils.noListData(body.page, body.totalPage, body.commentList)) {
                        int beflength = mAdapter.getListSize();
                        getAdapter().addAll(response.getBody().commentList);
                        getAdapter().notifyItemRangeInserted(beflength + 1, mAdapter.getListSize() - beflength);
                        mNextPage = response.getBody().page + 1;
                    }

                }

                onRefreshComplete();
                onLoadmoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(getContext());
                onRefreshComplete();
                onLoadmoreComplete();
            }
        });
    }

    @Override
    public void addData(Object _data) {
        getAdapter().add((CommentsResponseWrapper.CommentBean) _data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ACTION_REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onLoginResult");
            onRefresh();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN) {
            getActivity().finish();
        }
    }
}
