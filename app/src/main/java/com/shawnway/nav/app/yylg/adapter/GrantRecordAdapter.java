package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.GrantRecordAdapter.GrantRecordBean;
import com.shawnway.nav.app.yylg.bean.GrantHisBean;

/**
 * Created by Eiffel on 2016/2/15.
 */
public class GrantRecordAdapter extends MCyclerAdapter<GrantRecordBean>{

    private Context mContenxt;

    public GrantRecordAdapter(Context context){
        this(new ArrayList<GrantRecordBean>(),context);
    }

    public GrantRecordAdapter(List<GrantRecordBean> beans, Context context) {
        super(beans, context);
        mContenxt = context;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        lm.setSmoothScrollbarEnabled(true);
        return lm;
    }

    @Override
    public int getDisplayType(int postion) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_grantrecord_item, parent, false);
        return new GrantItemHolder(view);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        GrantRecordBean bean = getItem(position);
        GrantItemHolder _holder = (GrantItemHolder) holder;
        _holder.tvGranter.setText("被赠与者: " + bean.payee);
        if(bean.amount == null){//在赠送商品的时候，amount默认值都不给，就给我个空。所以必须先判断，以防止空指针
            if (bean.productName != null) {
                _holder.tvGoods.setVisibility(View.VISIBLE);
                _holder.tvGoods.setText("赠与商品:（第" + bean.circle + "期）" + bean.productName);
            }
        }else if(bean.amount.length()>1&&!bean.amount.equals("0")){
            _holder.tvMoney.setVisibility(View.VISIBLE);
            _holder.tvMoney.setText("赠与金额: ￥"+bean.amount);
        }
        _holder.tvTime.setText(bean.grantDate);
    }

    /**
     * "grantRecordList": [
     {
     "winprizeId": 0,
     "grantDate": "2016-03-04 11:28:41",
     "payee": "廖淑慧",
     "amount": 1000
     },
     */
    public static class GrantRecordBean {

        public String payee;//被赠与者
        public String amount;//赠与金额
        public int winprizeId;//中奖商品ID
        public String grantDate;//赠送时间
        public String productName;//赠与商品
        public int circle;//赠送商品的期数
    }

    public class GrantItemHolder extends RecyclerView.ViewHolder{

        TextView tvGranter;
        TextView tvMoney;
        TextView tvGoods;
        TextView tvTime;

        public GrantItemHolder(View itemView) {
            super(itemView);
            tvGranter = (TextView)itemView.findViewById(R.id.layout_grantrecord_item_granter);
            tvMoney = (TextView)itemView.findViewById(R.id.layout_grantrecord_item_money);
            tvGoods = (TextView)itemView.findViewById(R.id.layout_grantrecord_item_goods);
            tvTime = (TextView)itemView.findViewById(R.id.layout_grantrecord_item_time);

        }
    }
}
