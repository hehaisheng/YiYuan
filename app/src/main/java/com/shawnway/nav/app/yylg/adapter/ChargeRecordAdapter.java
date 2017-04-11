package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ChargeRecordResponse;
import com.shawnway.nav.app.yylg.bean.ChargeRecordResponse.ChargeRecordBean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/15.
 */
public class ChargeRecordAdapter  extends MCyclerAdapter<ChargeRecordResponse.ChargeRecordBean>{


    private final int TYPE_ITEM = 1;
    private final int TYPE_HEADER = 0;
    private ChargeRecordResponse.ChargeBody mData;

    public ChargeRecordAdapter( Context context) {
        super(new ArrayList<ChargeRecordResponse.ChargeRecordBean>(), context);
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
    public ChargeRecordBean getItem(int pos) {
        return super.getItem(pos-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderHolder(inflater.inflate(R.layout.layout_header_chargerec,parent,false));
            case TYPE_ITEM:
                return new ItemHolder(inflater.inflate(R.layout.layout_item_chargerec,parent,false));
        }
        return null;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)){
            case TYPE_HEADER:
                if (mData!=null) {
                    HeaderHolder header = (HeaderHolder) holder;
//                    header.amount.setText(mData.amount);
                }
                break;
            case TYPE_ITEM:
                ItemHolder item= (ItemHolder) holder;
                ChargeRecordBean bean=getItem(position);
                String time = bean.createdDate;
//                time = time.substring(0,time.length()-3);
                item.time.setText(time);
                String desc = bean.description;
                if(desc.equals("后台充值")){
                    desc = "系统充值";
                }
                item.way.setText(desc);
                item.amount.setText(bean.amount);
                break;
        }
    }

    public void setHeaderData(ChargeRecordResponse.ChargeBody headerData) {
        this.mData= headerData;
       try {
           notifyItemChanged(0);
       }catch (Exception e ){
           e.printStackTrace();
       }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView way;
        TextView amount;
        public ItemHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.time);
            way= (TextView) itemView.findViewById(R.id.way);
            amount= (TextView) itemView.findViewById(R.id.amount);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView amount;

        public HeaderHolder(View view) {
            super(view);
            amount=(TextView)view.findViewById(R.id.tv_amount);
        }
    }
}
