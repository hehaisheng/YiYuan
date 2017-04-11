package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.abview.BuyRecordView;
import com.shawnway.nav.app.yylg.abview.MyBuyRecordView;
import com.shawnway.nav.app.yylg.bean.BuyRecordWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 2015/11/12.
 */
public class BuyRecordAdapter extends MCyclerAdapter<BuyRecordWrapper.BuyRecordBean> {
    private static final String TAG = "MyBuyRecordAdapter";
    private final int TYPE_ITEM = 0;
    private boolean hasBanner;

    public BuyRecordAdapter(List<BuyRecordWrapper.BuyRecordBean> list, Context context) {
        super(list, context);
    }

    public BuyRecordAdapter(Context context) {
        super(new ArrayList<BuyRecordWrapper.BuyRecordBean>(), context);
    }

    @Override
    public int getDisplayType(int postion) {
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                BuyRecordView view=new BuyRecordView(hasBanner?BuyRecordView.HAVE_BANNER:BuyRecordView.NO_BANNER,context);//布局竟然自定义了一个View，叫BuyRecordView，状态太多，xml文件不好控制
                return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.setIsRecyclable(false);

        BuyRecordView item = (BuyRecordView) itemHolder.itemView;
        BuyRecordWrapper.BuyRecordBean bean = list.get(position);
        item.setData(bean);
        Log.d(TAG,"binding");

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    public void setHasBanner(boolean hasBanner) {
        this.hasBanner = hasBanner;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView goodtotal;
        TextView goodbuyed;
        TextView owner;
        TextView ownerBuyed;
        TextView ownerCode;
        TextView time;

        TextView detialMyCode;

        public ItemHolder(View view) {
            super(view);
            img= (ImageView) view.findViewById(R.id.duobao_record_pic);
            name= (TextView) view.findViewById(R.id.duobao_record_gname);
            goodtotal= (TextView) view.findViewById(R.id.goods_total);
            goodbuyed= (TextView) view.findViewById(R.id.duobao_record_num);
            owner= (TextView) view.findViewById(R.id.duobao_record_owner_name);
            ownerBuyed= (TextView) view.findViewById(R.id.duobao_record_owner_num);
            ownerCode= (TextView) view.findViewById(R.id.duobao_record_owner_code);
            time= (TextView) view.findViewById(R.id.duobao_record_owner_time);

            detialMyCode= (TextView) view.findViewById(R.id.duobao_record_check_mycode);
//            detialMyCode.setOnClickListener(MyBuyRecordAdapter.this);
        }
    }
}
