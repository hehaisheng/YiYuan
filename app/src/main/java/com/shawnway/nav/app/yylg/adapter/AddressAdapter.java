package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.AddressEditActivity;
import com.shawnway.nav.app.yylg.bean.AddressBean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/17.
 */
public class AddressAdapter extends MCyclerAdapter<AddressBean> implements MCyclerAdapter.CycleItemClilkListener, View.OnClickListener {
    private  final int TYPE_HEADER = 0;
    private  final int TYPE_ITEM = 1;
    private  final String TAG = "AddressAdpater";

    public AddressAdapter(Context context) {
        this(new ArrayList<AddressBean>(), context);
    }

    public AddressAdapter(ArrayList<AddressBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"list size:"+list.size());
        return list.size() == 0?  super.getItemCount() + 1:super.getItemCount();
    }

    @Override
    public int getDisplayType(int postion) {
        if (postion == 0 && list.size() == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d(TAG, "creating viewtype:" + viewType);

        switch (viewType) {
            case TYPE_HEADER:
                view = inflater.inflate(R.layout.layout_item_add_address, parent, false);
                Log.d(TAG, "created header");
                return new HeaderHolder(view, this);
            default:
                view = inflater.inflate(R.layout.layout_item_address, parent, false);
                view.findViewById(R.id.ship_address_edit).setOnClickListener(this);
                return new ItemHolder(view, this);
        }
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "binding data of pos:" + position);

        switch (getDisplayType(position)) {
            case TYPE_HEADER:
                if (list.size() == 0)
                    holder.itemView.setVisibility(View.VISIBLE);
                else
                    holder.itemView.setVisibility(View.GONE);
                break;
            case TYPE_ITEM:
                AddressBean bean = list.get(position);
                ItemHolder itemHolder = (ItemHolder) holder;

                itemHolder.name.setText("收 货 人："+bean.getReceiver());
                itemHolder.phone.setText("手机号码："+bean.getCellphone());
                itemHolder.address.setText(bean.getAddress());

                itemHolder.edit.setTag(bean);
                break;
        }
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }


    @Override
    public void onCycleItemClick(View view, int position) {
        switch (getDisplayType(position)) {
            case TYPE_HEADER:
                AddressEditActivity.getInstance((Activity)context);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ship_address_edit:
                AddressEditActivity.getInstance((Activity)context, (AddressBean) v.getTag());
                break;
        }
    }


    private class HeaderHolder extends BaseHolder {
        public HeaderHolder(View view, CycleItemClilkListener listener) {
            super(view, listener);
        }
    }

    private class ItemHolder extends BaseHolder {
        TextView name;
        TextView phone;
        TextView address;
        View edit;

        public ItemHolder(View itemView, CycleItemClilkListener listener) {
            super(itemView, listener);
            name = (TextView) itemView.findViewById(R.id.ship_address_name);
            phone = (TextView) itemView.findViewById(R.id.ship_address_mobile);
            address = (TextView) itemView.findViewById(R.id.ship_address_detail);

            edit=itemView.findViewById(R.id.ship_address_edit);
        }
    }
}
