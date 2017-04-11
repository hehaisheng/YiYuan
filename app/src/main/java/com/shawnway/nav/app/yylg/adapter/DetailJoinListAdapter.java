package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CustomInfoActivity;
import com.shawnway.nav.app.yylg.bean.JoinBean;
import com.shawnway.nav.app.yylg.tool.GlideUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailJoinListAdapter extends MCyclerAdapter<JoinBean> implements MCyclerAdapter.CycleItemClilkListener {

    private static final int TYPE_ITEM = 1;
    private static final String TAG = "DetailJoinListAdapter";


    public DetailJoinListAdapter(Context context) {
        super(new ArrayList<JoinBean>(), context);
    }


    @Override
    public int getDisplayType(int postion) {
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            default:
                view = inflater.inflate(R.layout.layout_item_detailjoin, parent, false);
                final ItemViewHolder holder = new ItemViewHolder(view, this);
                holder.customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.getCycleItemClilkListener().onCycleItemClick(holder.customer, holder.getAdapterPosition());
                    }
                });
                return holder;
        }
    }


    @Override
    public void onBindData(ViewHolder holder, final int position) {
        switch (getDisplayType(position)) {
            case TYPE_ITEM:
                final JoinBean bean = list.get(position);
                ItemViewHolder itemholder = (ItemViewHolder) holder;
                itemholder.customer.setText(bean.getCustomerName());
                itemholder.purecount.setText(Html.fromHtml(context.getString(R.string.detail_join_count, bean.getPurchCount())));
                itemholder.jointime.setText(bean.getPurchDate());
                itemholder.loc.setText(String.format("(%s)", TextUtils.isEmpty(bean.getLocation()) ? "暂无地址信息" : bean.getLocation()));
                itemholder.avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomInfoActivity.getInstance(context, bean.getCustomerID());
                    }
                });
                if (TextUtils.equals(bean.getImgUrl(), "")) {
                    GlideUtils.loadImage(context, R.drawable.portrait, itemholder.avatar);
                } else
                    GlideUtils.loadImage(context, bean.getImgUrl(), itemholder.avatar);

                break;
        }
    }


    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager llm = new GridLayoutManager(context, 1);
        llm.setSmoothScrollbarEnabled(true);
        return llm;
    }


    @Override
    public void onCycleItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_name:
                CustomInfoActivity.getInstance(context, list.get(position).getCustomerID());
        }
    }


    private class ItemViewHolder extends BaseHolder {
        private TextView oneday;
        private TextView customer;
        private TextView purecount;
        private TextView jointime;
        private TextView loc;
        private CircleImageView avatar;

        public ItemViewHolder(View itemView, CycleItemClilkListener listener) {
            super(itemView, listener);
            oneday = (TextView) itemView.findViewById(R.id.tv_date);
            customer = (TextView) itemView.findViewById(R.id.tv_name);
            purecount = (TextView) itemView.findViewById(R.id.tv_joininfo);
            jointime = (TextView) itemView.findViewById(R.id.tv_jointime_begin);
            loc = (TextView) itemView.findViewById(R.id.tv_joint_loc);
            avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

}
