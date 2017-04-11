package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 2016/3/22.
 */
public class SelctCycAdapter extends MCyclerAdapter<GoodDetail> implements MCyclerAdapter.CycleItemClilkListener {

    public SelctCycAdapter(Context context ,List<GoodDetail> list){
        super(list,context);
    }

    public SelctCycAdapter(Context context) {
        super(new ArrayList<GoodDetail>(),context);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context,3);
    }

    @Override
    public int getDisplayType(int postion) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.item_select_cyc,parent,false),this);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
         ItemHolder _holder= (ItemHolder) holder;
        _holder.cycle.setText(context.getString(R.string.cycle,getItem(position).getCycle()));
        _holder.cycle.setTextColor(context.getResources().getColor(getItem(position).getDisplayType().equals( GoodBean.TYPE_PROGRESS) ? R.color.appcolor:R.color.app_textColor));

    }

    @Override
    public void onCycleItemClick(View view, int position) {
        DetailActivity.getInstance(context, getItem(position));
    }

    private class ItemHolder extends BaseHolder {
        TextView cycle;

        public ItemHolder(View view,CycleItemClilkListener listener) {
            super(view,listener);
            cycle= (TextView) view;
        }
    }
}
