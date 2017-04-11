package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/8.
 */
public class BuyCodeItemAdapter extends MCyclerAdapter<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean.DrawDetailsBean> {
    private  final int TYPE_HEADER = 0;
    private  final int TYPE_ITEM = 1;
    MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean headerData;

    public BuyCodeItemAdapter(MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean headerData, Context context) {
        this(new ArrayList<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean.DrawDetailsBean>(), headerData, context);
    }

    public BuyCodeItemAdapter(ArrayList<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean.DrawDetailsBean> list, MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean headerData, Context context) {
        super(list, context);
        this.headerData = headerData;
    }

    ;

    @Override
    public int getDisplayType(int postion) {
        if (postion == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                HeaderHolder holder = new HeaderHolder(inflater.inflate(R.layout.layout_buycode_frag_header, parent, false));
                return holder;
            default:
                return new ItemHolder(inflater.inflate(R.layout.layout_item_buyedcode, parent, false));
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_HEADER:
                HeaderHolder headerHolder= (HeaderHolder) holder;
                headerHolder.goodImage.setImage(headerData.getThumbnail());
                headerHolder.name.setText(context.getString(R.string.good_name_with_cycle,headerData.getCycle(),headerData.getProdName()));
                headerHolder.amount.setText(headerData.getTotalPurch()+"");
                break;
            case TYPE_ITEM:
                ItemHolder _holder = (ItemHolder) holder;
                MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean.DrawDetailsBean data = list.get(position - 1);
                String purchSummary = data.getPurchSummary();
                String millis = purchSummary.substring(20,23);
                purchSummary = purchSummary.replace("购买了","参与了").replace("份","人次").replace(millis," ");
                _holder.date.setText(purchSummary);
                _holder.codes.setText(data.getDrawNumbers());
                break;
        }

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        CirclerImageView goodImage;
        TextView name;
        TextView amount;


        public HeaderHolder(View itemView) {
            super(itemView);
            goodImage= (CirclerImageView) itemView.findViewById(R.id.goods_img);
            name= (TextView) itemView.findViewById(R.id.title);
            amount= (TextView) itemView.findViewById(R.id.amount);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView codes;

        public ItemHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.text);
            codes = (TextView) view.findViewById(R.id.text1);
        }
    }
}
