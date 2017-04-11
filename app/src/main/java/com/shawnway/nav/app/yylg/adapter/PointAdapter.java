package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.bean.AssertResponseWraper;
import com.shawnway.nav.app.yylg.bean.PointRecordBean;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/19.
 */
public class PointAdapter extends MCyclerAdapter<PointRecordBean> implements MCyclerAdapter.CycleItemClilkListener {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PointAdapter(Context context) {
        this(new ArrayList<PointRecordBean>(), context);
    }

    public PointAdapter(ArrayList<PointRecordBean> list, Context context) {
        super(list, context);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount()+1;//额外header
    }

    @Override
    public int getDisplayType(int postion) {
        if (postion == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = inflater.inflate(R.layout.layout_header_balance, parent, false);
                initHeader(view);
                return new HeaderHolder(view);
            default:
                view = inflater.inflate(R.layout.layout_item_balance_record, parent, false);
                return new RecordItemHolder(view, this);
        }
    }


    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_ITEM:
                PointRecordBean bean=list.get(position-1);
                RecordItemHolder itemHolder= (RecordItemHolder) holder;
                itemHolder.time.setText(bean.getCreatedDate());
                itemHolder.fromwhere.setText(bean.getDescription());
                itemHolder.detail.setText(bean.getPoint());
                break;
        }

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }

    private void initHeader(final View view) {
        VolleyTool.getInstance(context).sendGsonRequest(context,AssertResponseWraper.class, Constants.RETRIEVEBALANCE_URL, new Response.Listener<AssertResponseWraper>() {
            @Override
            public void onResponse(AssertResponseWraper response) {
                if (response.getHeader().code.equals(VolleyTool.SUCCESS)) {
                    TextView balance = (TextView) view.findViewById(R.id.value);
                    String value = response.getBody().point;
                    balance.setText(value == null ? "0" : value);
                } else {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }


    @Override
    public void onCycleItemClick(View view, int position) {

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    private class RecordItemHolder extends BaseHolder {
        TextView time;
        TextView fromwhere;
        TextView detail;

        public RecordItemHolder(View view, CycleItemClilkListener listener) {
            super(view, listener);
            time = (TextView) view.findViewById(R.id.time);
            fromwhere = (TextView) view.findViewById(R.id.fromwhere);
            detail = (TextView) view.findViewById(R.id.detail);
        }
    }






}
