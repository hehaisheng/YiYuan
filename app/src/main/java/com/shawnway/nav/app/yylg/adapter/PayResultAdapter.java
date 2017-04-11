package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Eiffel on 2015/11/11.
 */
public class PayResultAdapter extends BaseAdapter {
    private OnItemClickListener onItemClickListener;
    private final Context mContext;
    ArrayList<CarItemBean> mData;
    private DecimalFormat dlf = new DecimalFormat("0.00");

    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public PayResultAdapter(ArrayList<CarItemBean> orders, Context context) {
        mData = orders;
        mContext = context;
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pay_result, parent, false);
        CirclerImageView img = (CirclerImageView) convertView.findViewById(R.id.order_good_img);
        TextView aside = (TextView) convertView.findViewById(R.id.aside);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        final CarItemBean item = getItem(position);
        String imgUrl = item.getGood().getThumbnail();
        img.setImage(imgUrl);
        name.setText("（第" + item.getGood().getCycle() + "期）" + item.getGood().getProdName());
        aside.setText(item.getBuyed() + "");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(item);
            }
        });

        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(CarItemBean carItemBean);
    }
}
