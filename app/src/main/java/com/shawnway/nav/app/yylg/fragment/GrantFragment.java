package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.GrantCfmActivity;
import com.shawnway.nav.app.yylg.adapter.GrantHistoryAdapter;
import com.shawnway.nav.app.yylg.bean.GrantHisBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2016/1/15.
 * <p>
 * 赠与金额，填写被赠与人号码，显示本账号赠与他人的记录
 */
public class GrantFragment extends MyFragment implements View.OnClickListener {
    private static final int REQUEST_GRANT = 501;
    private static final int MAX_HISTORY = 10;
    EditText inputer;
    private MyRecyclerView list;
    private ArrayList<GrantHis> data;
    private GrantHistoryAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grant, container, false);

        inputer = (EditText) view.findViewById(R.id.inputer_payee);

        setList(view);
        setListener(view);

        return view;
    }

    private void setListener(View view) {
        view.findViewById(R.id.submit).setOnClickListener(this);
    }

    private void setList(View view) {
        list = (MyRecyclerView) view.findViewById(R.id.recyclerview);
        list.enableLoadMore();
        list.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        refreshData();

        mAdapter = new GrantHistoryAdapter(data, getContext());
        mAdapter.setItemListener(this);
        list.setLayoutManager(mAdapter.getLayoutManager(getContext()));

        mAdapter.notifyDataSetChanged();
    }

    private void refreshData() {//已完成TODO:赠与金额的被赠与者数据
        data = new ArrayList<>();
        String url = Constants.GRANT_HIS_URL;
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, GrantWrapper.class, url, new Response.Listener<GrantWrapper>() {
            @Override
            public void onResponse(GrantWrapper response) {
                PayeeList body = response.getBody();
                GrantHis[] pairs = body.getPairs();
                if (pairs == null) {
                    return;
                }
                for (int i = 0; i < pairs.length; i++) {
                    data.add(pairs[i]);
                }
                list.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    private class GrantWrapper extends ResponseGson<PayeeList> {
        @Override
        public PayeeList getBody() {
            return super.getBody();
        }
    }

    public class PayeeList {
        public GrantHis[] getPairs() {
            return payeeList;
        }

        public void setPairs(GrantHis[] pairs) {
            this.payeeList = pairs;
        }

        private GrantHis[] payeeList;
    }

    public class GrantHis {

        public String payee;
        public String time;
        public double amount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getPayee() {
            return payee;
        }

        public void setPayee(String payee) {
            this.payee = payee;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit://赠与按钮，确认赠与的是这个号码
                GrantCfmActivity.getRtnInstance(getActivity(), Utils.getEditTextStr(inputer), REQUEST_GRANT);
                break;
            case R.id.btn_delete:
                int pos = (int) v.getTag();
                mAdapter.removeRec(pos);
                break;
            case R.id.tv_payee:
                pos = (int) v.getTag();
                inputer.setText(mAdapter.getItem(pos).getPayee());
                inputer.requestFocus();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_OK)
            getActivity().finish();
        else {
            refreshData();
            mAdapter.notifyDataSetChanged();
        }
    }


}
