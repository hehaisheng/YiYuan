package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Cache;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eiffel on 2015/11/11.
 */
public class OrderAdapter extends BaseAdapter {
    private final Context mContext;
    ArrayList<CarItemBean> mData;
    private DecimalFormat dlf = new DecimalFormat("0.00");

    HashMap<Integer,View> lmap = new HashMap<Integer,View>();

    public OrderAdapter(ArrayList<CarItemBean> orders, Context context){
        mData=orders;
        mContext=context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CarItemBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView=LayoutInflater.from(mContext).inflate(R.layout.layout_itemt_order_simple,parent,false);
        CirclerImageView img = (CirclerImageView) convertView.findViewById(R.id.order_good_img);
        TextView aside= (TextView) convertView.findViewById(R.id.aside);
        TextView name= (TextView) convertView.findViewById(R.id.name);
        CarItemBean item=getItem(position);
        String imgUrl = item.getGood().getThumbnail();
        img.setImage(imgUrl);
        name.setText("（第"+item.getGood().getCycle()+"期）"+item.getGood().getProdName());
        aside.setText("￥"+dlf.format(item.calWorth()));

        return convertView;
    }
}
