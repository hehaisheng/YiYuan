package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.WasteRecordResponse;
import com.shawnway.nav.app.yylg.bean.WasteRecordResponse.WasteRecordBean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/15.
 */
public class WasteRecAdapter extends MCyclerAdapter<WasteRecordBean>{


    private final int TYPE_ITEM = 1;
    private final int TYPE_HEADER = 0;
    private WasteRecordResponse.WasteBody headerData;

    public WasteRecAdapter( Context context) {
        super(new ArrayList<WasteRecordBean>(), context);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Override
    public int getDisplayType(int postion) {
        if (postion==0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public WasteRecordBean getItem(int pos) {
        return super.getItem(pos-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderHolder(inflater.inflate(R.layout.layout_header_wasterec,parent,false));
            case TYPE_ITEM:
                return new ItemHolder(inflater.inflate(R.layout.layout_item_wasterec,parent,false));
        }
        return null;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)){
            case TYPE_HEADER:
                if (headerData!=null) {
                    HeaderHolder header = (HeaderHolder) holder;
//                    header.amount.setText(headerData.amount);
                }
                break;
            case TYPE_ITEM:
                ItemHolder item= (ItemHolder) holder;
                WasteRecordBean bean=getItem(position);
                String time = bean.createdDate;
                item.time.setText(time);

                String money = bean.amount;
                int l = money.length();
                money = money.substring(1,l);
                item.amount.setText(money);
                break;
        }
    }

    public void setHeaderData(WasteRecordResponse.WasteBody headerData) {
        this.headerData = headerData;
        notifyItemChanged(0);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView amount;

        public ItemHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.time);
            amount = (TextView) itemView.findViewById(R.id.amount);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView amount;
        public HeaderHolder(View view) {
            super(view);
            amount= (TextView) view.findViewById(R.id.tv_amount);
        }
    }
}
