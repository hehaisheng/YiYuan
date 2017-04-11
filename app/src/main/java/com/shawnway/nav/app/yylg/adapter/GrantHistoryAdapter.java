package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.GrantHisBean;
import com.shawnway.nav.app.yylg.fragment.GrantFragment;
import com.shawnway.nav.app.yylg.net.VolleyTool;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 2016/2/5.
 */
public class GrantHistoryAdapter extends MCyclerAdapter<GrantFragment.GrantHis> {
    private static final String TAG = "GrantHistoryAdapter";
    private View.OnClickListener mListener;
    private ArrayList<GrantFragment.GrantHis> list;
    private DecimalFormat df1 = new DecimalFormat("0.00");

    public GrantHistoryAdapter(List<GrantFragment.GrantHis> list, Context context) {
        super(list, context);
        this.list = (ArrayList<GrantFragment.GrantHis>) list;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Override
    public int getDisplayType(int postion) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.layout_item_deleteable, parent, false));//赠与金额的历史记录中的item条目布局,不让客户删除记录，就删掉布局中的img控件
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "binding  ->" + position);
        ItemHolder _holder = (ItemHolder) holder;
        _holder.payee.setText(list.get(position).getPayee());
        _holder.payee.setTag(position);
        _holder.time.setText(list.get(position).getTime());
        String money = df1.format(list.get(position).getAmount());
        _holder.amount.setText(money);
    }

    public void removeRec(int pos){
        remove(pos);//UI上面删除
        notifyDataSetChanged();

    }

    private class ItemHolder extends RecyclerView.ViewHolder  {
        TextView payee;
        TextView time;
        TextView amount;

        public ItemHolder(View itemView) {
            super(itemView);

            payee = (TextView) itemView.findViewById(R.id.tv_payee);
            time = (TextView) itemView.findViewById(R.id.tv_grant_time);
            amount = (TextView) itemView.findViewById(R.id.tv_grant_amount);
            if (payee!=null) {
                payee.setOnClickListener(mListener);
            }
        }


    }

    public void setItemListener(View.OnClickListener listener){
        mListener=listener;
    }
}
