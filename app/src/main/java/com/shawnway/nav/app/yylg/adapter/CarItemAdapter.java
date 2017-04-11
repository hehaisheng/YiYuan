package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter.CycleItemClilkListener;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VHolder;
import com.shawnway.nav.app.yylg.view.CirclerImageView;
import com.shawnway.nav.app.yylg.view.NumberPicker;
import com.shawnway.nav.app.yylg.view.NumberPicker.OnAmountChangeListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CarItemAdapter extends MCyclerAdapter<CarItemBean> implements
        CycleItemClilkListener {

    private final int TYPE_ITEM = 0;
    private final String TAG = "CarItemAdapter";
    private final int NOTIFY_ITEM_CHECKED_STATE_CHANGE = 0;
    private final int NOTIFY_ITEM_NUMBER_CHANGE = 1;
    private Context mContext;

    public CarItemAdapter(List<CarItemBean> list, Context context) {
        super(list, context);
        mContext = context;
        setAmountChangeListener(new OnAmountChangeListener() {
            @Override
            public void onAmountChange(int amount, int tag) {
                Message msg = mCustomerHandler.obtainMessage(NOTIFY_ITEM_NUMBER_CHANGE);
                msg.arg1 = amount;
                msg.arg2 = tag;
                mCustomerHandler.sendMessage(msg);
            }
        });

    }

    @Override
    public int getDisplayType(int postion) {
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View view = inflater.inflate(R.layout.layout_cart, parent, false);
                final CarItemHolder holder = new CarItemHolder(view, this);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.getCycleItemClilkListener().onCycleItemClick(v, holder.getPosition());
                    }
                });
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.getCycleItemClilkListener().onCycleItemClick(v,holder.getPosition());
                    }
                });
                return holder;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindData(ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_ITEM:
                CarItemHolder viewHolder = (CarItemHolder) holder;
                viewHolder.setIsRecyclable(false);//设置不复用，以避免页面图片错乱
                CarItemBean item = list.get(position);
                Log.d(TAG, "binding cart data:" + "buyed->" + item.getBuyed());

                if(item.getBuyUnit() == 10){
                    viewHolder.tenIcon.setVisibility(View.VISIBLE);
                }
                viewHolder.numberPicker.setTag(position);
                viewHolder.numberPicker.setMaxValue(item.getGood().getLeftCnt());
                viewHolder.numberPicker.setGapValue(1);
                viewHolder.numberPicker.setCurNum(item.getBuyed());
                viewHolder.img.setImage(item.getGood().getThumbnail());
                viewHolder.price.setText(context.getString(R.string.price,item.calWorth()));
                viewHolder.title.setText(mContext.getString(R.string.detail_good_title_text,item.getGood().getCycle(),item.getGood().getProdName()));
                viewHolder.checkbox.setVisibility(item.isEditing() ? View.VISIBLE
                        : View.GONE);
                viewHolder.progress.setText(mContext.getString(R.string.cart_progress,item.getGood().getTotCnt(),item.getGood().getLeftCnt()));
                if (item.isChecked())

                    viewHolder.checkbox
                            .setImageResource(R.drawable.ic_radius_checkbox_checked);

                else

                    viewHolder.checkbox
                            .setImageResource(R.drawable.ic_radius_checkbox);

                break;

            default:
                break;
        }


    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    //该函数会清空item的选中状态
    public void updateDataStatus(boolean editing) {
        for (CarItemBean bean : list) {
            bean.setEditing(editing);
            bean.setChecked(false);
            notifyDataSetChanged();
        }
    }


    @Override
    public void onCycleItemClick(View view, int position) {
        final CarItemBean bean = list.get(position);

        switch (view.getId()) {
            case R.id.btn_delete:


                Dialog dlg=new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE).
                        setContentText(mContext.getString(R.string.confirm_delete)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sweetAlertDialog) {
                        bean.setChecked(true);
                        removeCheckedItem();
                        sweetAlertDialog.dismiss();
                    }
                }).setCancelText("取消");
                dlg.setCancelable(true);
                dlg.show();
                break;
            case R.id.image:
                if (bean.isEditing()) {

                    bean.setChecked(!bean.isChecked());
                    if (countChecked() == 0) {
                        updateDataStatus(false);
                        Message msg = mCustomerHandler.obtainMessage(NOTIFY_ITEM_CHECKED_STATE_CHANGE);
                        msg.obj = false;//将标题的editing状态设为false
                        mCustomerHandler.sendMessage(msg);
                    } else {
                        notifyItemChanged(position);
                        Message msg = mCustomerHandler.obtainMessage(NOTIFY_ITEM_CHECKED_STATE_CHANGE);
                        msg.obj = true;
                        mCustomerHandler.sendMessage(msg);
                    }

                } else {

                    DetailActivity.getInstance(mContext, list.get(position).getGood().getDrawCycleID());
                }
        }
    }

    public int countChecked() {
        int checkedItems = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                checkedItems++;
            }
        }
        return checkedItems;
    }


    public void setAmountChangeListener(NumberPicker.OnAmountChangeListener listener) {
        mAmountChangeListener = listener;
    }

    private OnAmountChangeListener mAmountChangeListener;

    public void removeCheckedItem() {

        for (int i =list.size()-1 ;i>=0;i--) {
            CarItemBean bean = list.get(i);
            if (bean.isChecked()) {
                list.remove(i);
                if (list.size()==0)
                {
                    list.clear();
                    Utils.removeParam(context,"cart");
                    SharedPreferences sp = context.getSharedPreferences("share_date", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                }
            }
        }
        Intent intent = new Intent(Constants.ACTION_CART_CHANGE);
        mContext.sendBroadcast(intent);
    }

    public void checkAll(Boolean shouldCheck) {
        for (CarItemBean bean : list) {
            bean.setChecked(shouldCheck);
        }
        notifyItemRangeChanged(0, getListSize());
    }

    class CarItemHolder extends BaseHolder {
        ImageButton checkbox;
        CirclerImageView img;
        TextView title;
        TextView progress;
        TextView price;
        NumberPicker numberPicker;
        View delete;
        ImageView tenIcon;

        public CarItemHolder(View itemView, CycleItemClilkListener listener) {
            super(itemView, listener,false);
            checkbox = VHolder.get(itemView, R.id.checkbox);
            img = VHolder.get(itemView, R.id.image);
            title = VHolder.get(itemView, R.id.name);
            price=VHolder.get(itemView,R.id.price);
            progress = VHolder.get(itemView, R.id.progress);
            numberPicker = VHolder.get(itemView, R.id.numberPicker);
            delete = VHolder.get(itemView, R.id.btn_delete);
            numberPicker.setAmountChangeListener(CarItemAdapter.this.mAmountChangeListener);
            tenIcon = VHolder.get(itemView, R.id.good_ten_label);
        }
    }


}
