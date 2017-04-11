package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.shawnway.nav.app.yylg.abview.MyBuyRecordView;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper.MyBuyRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 2015/11/12.
 */
public class MyBuyRecordAdapter extends MCyclerAdapter<MyBuyRecordWrapper.MyBuyRecordBean> {
    private static final String TAG = "MyBuyRecordAdapter";
    private final int TYPE_ITEM = 0;

    public MyBuyRecordAdapter(List<MyBuyRecordWrapper.MyBuyRecordBean> list, Context context) {
        super(list, context);
    }

    public MyBuyRecordAdapter(Context context) {
        super(new ArrayList<MyBuyRecordBean>(), context);
    }

    @Override
    public int getDisplayType(int postion) {
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                MyBuyRecordView view=new MyBuyRecordView(context);
                return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.setIsRecyclable(false);

            MyBuyRecordView item = (MyBuyRecordView) itemHolder.itemView;
            MyBuyRecordWrapper.MyBuyRecordBean bean = list.get(position);
            item.setData(bean);
            Log.d(TAG,"binding");
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View view) {
            super(view);
        }
    }
}
