package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CashOutFormActivity;
import com.shawnway.nav.app.yylg.bean.SupportedBank;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2016/3/28.
 */
public class MySpinnerAdapter extends BaseAdapter {
    private final ArrayList<SupportedBank> mList;
    private final Context mContext;

    public MySpinnerAdapter(Context context, ArrayList<SupportedBank> supportedCashOutBank) {
        mList=supportedCashOutBank;
        mContext=context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SupportedBank getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
        convertView=_LayoutInflater.inflate(R.layout.spinner_item_layout, null);
        if(convertView!=null) {
            ImageView label = (ImageView) convertView
                    .findViewById(R.id.spinner_item_label);
            ImageView check = (ImageView) convertView
                    .findViewById(R.id.spinner_item_checked_image);
            label.setImageResource(getItem(position).getSrc());
        }
        return convertView;
    }
}
