package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.CashOutRecordResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class CashOutRecordAdapter extends MCyclerAdapter<CashOutRecordResponse.CashOutRecordBean>{


    private final int TYPE_ITEM = 1;
    private final int TYPE_HEADER = 0;
    private CashOutRecordResponse.CashOutBody mData;
    private Context mContext;
    private DecimalFormat dlf = new DecimalFormat("0.00");

    public CashOutRecordAdapter( Context context) {
        super(new ArrayList<CashOutRecordResponse.CashOutRecordBean>(), context);
        mContext = context;
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
    public CashOutRecordResponse.CashOutRecordBean getItem(int pos) {
        return super.getItem(pos-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderHolder(inflater.inflate(R.layout.layout_header_cashoutrec,parent,false));
            case TYPE_ITEM:
                return new ItemHolder(inflater.inflate(R.layout.layout_item_cashoutrec,parent,false));
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
                CashOutRecordResponse.CashOutRecordBean bean=getItem(position);
                String time = bean.createTime;
                time = time.substring(0,time.length()-3);
                item.time.setText(time);

                String num = bean.no;
                if(num.length()>3){
                    num = num.substring(num.length()-3,num.length());
                }
                item.way.setText(num+" "+bean.withdrawType);

                String amount = dlf.format(bean.amount);
                item.amount.setText(amount);

                String status = bean.withdrawStatus;
                if(status.equals("REQUESTED")){
                    status = "未到账";
                    item.status.setText(status);
                }else if(status.equals("DONE")){
                    status = "已到账";
                    item.status.setText(status);
                    item.status.setTextColor(mContext.getResources().getColor(R.color.appcolor));
                }
                break;
        }
    }

    public void setHeaderData(CashOutRecordResponse.CashOutBody headerData) {
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
        TextView status;
        public ItemHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.time);
            way= (TextView) itemView.findViewById(R.id.way);
            amount= (TextView) itemView.findViewById(R.id.amount);
            status = (TextView) itemView.findViewById(R.id.status);
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
