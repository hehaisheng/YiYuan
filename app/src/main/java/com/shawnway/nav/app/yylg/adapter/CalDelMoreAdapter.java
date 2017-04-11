package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.fragment.CalDetFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class CalDelMoreAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<CalDetFragment.CalDelResponse.CalDelBody.CalInfoItem> mData;


    public CalDelMoreAdapter(ArrayList<CalDetFragment.CalDelResponse.CalDelBody.CalInfoItem> orders, Context context){
        mData=orders;
        mContext=context;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CalDetFragment.CalDelResponse.CalDelBody.CalInfoItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.layout_item_caldelmore_simple,parent,false);
            holder=new ViewHolder();
            holder.time= (TextView) convertView.findViewById(R.id.time);
            holder.code= (TextView) convertView.findViewById(R.id.code);
            holder.customer= (TextView) convertView.findViewById(R.id.custom);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        CalDetFragment.CalDelResponse.CalDelBody.CalInfoItem item=getItem(position);
        holder.time.setText(item.purchDate+" "+item.purchTime);
        holder.code.setText(item.result);
        holder.customer.setText(item.customerNme);

        return convertView;
    }


    class ViewHolder{
        TextView time;
        TextView code;
        TextView customer;
    }


}
