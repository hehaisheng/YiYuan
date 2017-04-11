package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CustomInfoActivity;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.tool.TimeUtils;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.ArrayList;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Eiffel on 2015/12/13.
 */
public class LstAnnounAdapter extends MCyclerAdapter<GoodDetail> implements MCyclerAdapter.CycleItemClilkListener {
    private static final String TAG = "最新揭晓";
    private final int ITEM_CALCULATING = 1;
    private final int ITEM_RELEAVED = 2;
    private final int ITEM_ERROR = 3;

    private Context mContext;

    public LstAnnounAdapter(Context context) {
        super(new ArrayList<GoodDetail>(), context);
        mContext = context;
    }

    public LstAnnounAdapter(ArrayList<GoodDetail> datas,Context context) {
        super(datas, context);
        mContext = context;
    }

    @Override
    public int getDisplayType(int postion) {
        if(list.get(postion).getFinalRslt() == null){
            if(list.get(postion).getDisplayType().equals(GoodDetail.TYPE_ERROR)){//如果是error的数据的时候就显示
                return ITEM_ERROR;
            }else{
                return ITEM_CALCULATING;
            }
        }else{
            return ITEM_RELEAVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.layout_item_goods_lastest, parent, false), this);
    }

    private Handler handler = new Handler();

    private Runnable runOne = new Runnable() {
        @Override
        public void run() {
            for(int i = 0;i<list.size();i++){
                if(list.get(i).getDrawStatus().equals(GoodBean.TYPE_COUNTDOWN)){
                    notifyItemChanged(i);//只更新某些需要更新的条目
                    Log.d("最新揭晓", "倒计时结束后的刷新，跟计算中无关。。。" + i);
                }else if(list.get(i).getDrawStatus().equals(GoodBean.TYPE_RELEAVED)){
                    notifyItemChanged(i);//只更新某些需要更新的条目
                    return;
                }
            }
        }
    };

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        final GoodDetail bean = (GoodDetail) list.get(position);
        String price = context.getString(R.string.good_worth, bean.getPrice());
        switch (getDisplayType(position)) {//根据bean的drawStatus显示不同的布局，分为倒计时、计算中、已揭晓
            case ITEM_CALCULATING://肯定是处于倒计时状态的数据
                ItemHolder calItem = (ItemHolder) holder;
                calItem.setIsRecyclable(false);//设置item不复用
                calItem.calWrapper.setVisibility(View.VISIBLE);
                calItem.releaveWrapper.setVisibility(View.GONE);
                calItem.calinfoWrapper.setVisibility(View.VISIBLE);
                if (bean.getBuyUnit() == 10) calItem.tenIcon.setVisibility(View.VISIBLE);
                calItem.image.setImage(bean.getThumbnail());
                calItem.goodname.setText(context.getString(R.string.good_name_with_cycle, bean.getCycle(), bean.getProdName()));
                calItem.goodworth.setText(price);
                calItem.goodname.setTag(bean.getDrawCycleID());
                showCountdownOrCalculating(calItem,bean);
                break;

            case ITEM_RELEAVED://已揭晓
                Log.d(TAG, "显示已揭晓," + bean.getProdName() + bean.getCycle());
                ItemHolder realItem = (ItemHolder) holder;
                realItem.setIsRecyclable(false);//设置item不复用
                realItem.countdownTv.setVisibility(View.GONE);
                realItem.countdownHour.setVisibility(View.GONE);
                realItem.countdown.setVisibility(View.GONE);
                realItem.calculating.setVisibility(View.GONE);
                realItem.calWrapper.setVisibility(View.GONE);
                realItem.releaveWrapper.setVisibility(View.VISIBLE);
                realItem.calinfoWrapper.setVisibility(View.GONE);
                realItem.boldBolder.setVisibility(View.GONE);
                if (bean.getBuyUnit() == 10) realItem.tenIcon.setVisibility(View.VISIBLE);
                realItem.image.setImage(bean.getThumbnail());
                realItem.goodworth2.setText(price);
                realItem.winnerName.setText(bean.getWinnerName());
                realItem.winnerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomInfoActivity.getInstance(mContext, bean.winnerId);//后台少了一个字段winnerId（已完成TODO）
                    }
                });
                realItem.num.setText(bean.winnerPartCnt + "");//数据是错误的，应该是winnerPartCnt（中奖者参与人次），但是少了这个字段（已完成TODO）
                realItem.annonTime.setText(bean.getAnnounceDate());
                realItem.goodname.setTag(bean.getDrawCycleID());
                break;

            case ITEM_ERROR://机器故障
                ItemHolder errorItem = (ItemHolder) holder;
                errorItem.setIsRecyclable(false);//设置item不复用
                errorItem.calWrapper.setVisibility(View.VISIBLE);
                errorItem.releaveWrapper.setVisibility(View.GONE);
                errorItem.calinfoWrapper.setVisibility(View.VISIBLE);
                if (bean.getBuyUnit() == 10) errorItem.tenIcon.setVisibility(View.VISIBLE);
                errorItem.image.setImage(bean.getThumbnail());
                errorItem.goodname.setText(context.getString(R.string.good_name_with_cycle, bean.getCycle(), bean.getProdName()));
                errorItem.goodname.setTag(bean.getDrawCycleID());
                errorItem.goodworth.setText(price);

                errorItem.machinebreakdown.setVisibility(View.VISIBLE);
                errorItem.countdownTv.setVisibility(View.GONE);
                errorItem.countdownHour.setVisibility(View.GONE);
                errorItem.countdown.setVisibility(View.GONE);
                errorItem.calculating.setVisibility(View.GONE);
                break;
        }
    }

    private void showCountdownOrCalculating(ItemHolder calItem, GoodDetail bean) {
        String serverTime = bean.getServerTime();
        Log.d(TAG, "服务器的系统时间："+serverTime);
        long currentTimeMillis = TimeUtils.dataOne(serverTime);
        String drawDate = bean.getDrawDate();//开奖时间，只有倒计时状态的时候才有开奖时间
        long date = TimeUtils.dataOne(drawDate);
        long down = date - currentTimeMillis;
        Log.d(TAG, "倒计时时间："+ down);
        if(down > 0){
            Log.d(TAG, "显示倒计时,"+bean.getProdName()+bean.getCycle());
            handler.postDelayed(runOne,down);
            calItem.countdownTv.setVisibility(View.VISIBLE);
            //显示倒计时布局
            if(down > 1000 * 60 * 60){//显示时分秒
                calItem.countdownHour.setVisibility(View.VISIBLE);
                calItem.countdownHour.start(down);
            }else{//显示分秒和毫秒
                calItem.countdown.setVisibility(View.VISIBLE);
                calItem.countdown.start(down);
            }
        }else if(down <= 0){
            //显示计算中的布局
            Log.d(TAG, "显示计算中,"+bean.getProdName()+bean.getCycle());
            calItem.calculating.setVisibility(View.VISIBLE);
            calItem.countdownTv.setVisibility(View.GONE);
            calItem.countdownHour.setVisibility(View.GONE);
            calItem.countdown.setVisibility(View.GONE);
            handler.removeCallbacks(runOne);
        }
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Override
    public void onCycleItemClick(View view, int position) {
        DetailActivity.getInstance(context,list.get(position));
    }

    private class ItemHolder extends BaseHolder {
        CirclerImageView image;

        TextView goodname;
        TextView goodworth;
        TextView goodworth2;
//        TextView luckNum;
        TextView annonTime;
        TextView winnerName;
        TextView num;

        ImageView tenIcon;
        //倒计时的计算中
        TextView calculating;
        //机器故障
        TextView machinebreakdown;
        //揭晓倒计时这几个字
        TextView countdownTv;
        //分秒和毫秒的倒计时
        CountdownView countdown;
        //时分秒的倒计时
        CountdownView countdownHour;

        ViewGroup calWrapper;
        ViewGroup releaveWrapper;
        ViewGroup calinfoWrapper;

        View boldBolder;

        public ItemHolder(View itemView, CycleItemClilkListener listener) {
            super(itemView, listener);
            image= (CirclerImageView) itemView.findViewById(R.id.result_goods_pic);
            goodname = (TextView) itemView.findViewById(R.id.result_goods_name);
            goodworth = (TextView) itemView.findViewById(R.id.result_goods_worth);
            goodworth2 = (TextView) itemView.findViewById(R.id.result_goods_worth2);
            calWrapper = (ViewGroup) itemView.findViewById(R.id.result_revealing_wrapper);
            releaveWrapper = (ViewGroup) itemView.findViewById(R.id.result_revealed_wrapper);
            calinfoWrapper = (ViewGroup) itemView.findViewById(R.id.result_goods_info_wrapper);
//            luckNum = (TextView) itemView.findViewById(R.id.result_revealed_code);
            annonTime = (TextView) itemView.findViewById(R.id.result_revealed_time);
            boldBolder=itemView.findViewById(R.id.bold_border);
            winnerName = (TextView) itemView.findViewById(R.id.result_revealed_name);
            num = (TextView) itemView.findViewById(R.id.result_revealed_num);

            tenIcon = (ImageView) itemView.findViewById(R.id.good_ten_label);

            calculating = (TextView) itemView.findViewById(R.id.result_revealing_calculating);
            machinebreakdown = (TextView) itemView.findViewById(R.id.result_revealing_machinebreakdown);
            countdownTv = (TextView) itemView.findViewById(R.id.result_revealing_countdown_tv);
            countdown = (CountdownView) itemView.findViewById(R.id.result_revealing_countdown);
            countdownHour = (CountdownView) itemView.findViewById(R.id.result_revealing_countdown_hourtime);
        }
    }
}
