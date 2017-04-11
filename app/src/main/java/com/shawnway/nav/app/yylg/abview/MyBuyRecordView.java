package com.shawnway.nav.app.yylg.abview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CheckMyCodeActivity;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper.MyBuyRecordBean;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

/**
 * Created by Eiffel on 2015/12/3.
 */
public class MyBuyRecordView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "RecordItemView";
    private final Context mContext;
    MyBuyRecordWrapper.MyBuyRecordBean bean;

    public MyBuyRecordView(Context context) {
        this(context, null);
    }

    public MyBuyRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyBuyRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        LayoutInflater.from(getContext()).inflate(R.layout.layout_item_mbuyrecord, this, true);
    }

    private void notifyDataChange() {
        //根据商品状态分类显示商品信息
        //初始化共有信息部分

        if(bean.buyUnit == 10)//购买记录少了buyUnit字段(已完成TODO)
            findViewById(R.id.good_ten_label).setVisibility(View.VISIBLE);
        ((CirclerImageView) findViewById(R.id.buy_record_pic)).setImage(bean.thumbnail);
        ((CirclerImageView) findViewById(R.id.buy_record_pic)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.getInstance(mContext,bean.drawCycleId);
            }
        });
        ((TextView) findViewById(R.id.buy_record_gname)).setText(getContext().getString(R.string.good_name, bean.cycle, bean.prodName));
        ((TextView) findViewById(R.id.goods_total)).setText(getContext().getString(R.string.good_total, bean.totCnt));
        ((TextView) findViewById(R.id.buy_record_num)).setText(String.valueOf(bean.totalPurch));
        findViewById(R.id.buy_record_check_mycode).setOnClickListener(this);
        Log.d(TAG, "noifyDatachange in RecorItem");
        if (bean.drawStatus.equals(MyBuyRecordBean.STA_OPNE)||bean.drawStatus.equals(MyBuyRecordBean.STA_CALCULATING)) {
            //progressbar
            float progress = ((float) bean.puredCnt) / bean.totCnt;
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
            progressBar.setProgress((int) (progress * 1000));
            progressBar.setVisibility(VISIBLE);
            Log.d(TAG, "initing good item: progress=" + progress);

            //leftCount
            TextView leftCnt = (TextView) findViewById(R.id.goods_left);
            leftCnt.setVisibility(VISIBLE);
            leftCnt.setText(getContext().getString(R.string.good_left, bean.leftCnt));
            //owner wrapper
            findViewById(R.id.buy_record_owner).setVisibility(GONE);
        } else {
            //progressbar
            findViewById(R.id.progressbar).setVisibility(GONE);
            //leftCount
            findViewById(R.id.goods_left).setVisibility(GONE);
            //ower wrapper
            findViewById(R.id.buy_record_owner).setVisibility(VISIBLE);
            ((TextView) findViewById(R.id.buy_record_owner_name)).setText(bean.winnerName);
            ((TextView) findViewById(R.id.buy_record_owner_num)).setText(String.valueOf(bean.puredCnt));
            ((TextView) findViewById(R.id.buy_record_owner_code)).setText(bean.finalRslt);
            ((TextView) findViewById(R.id.buy_record_owner_time)).setText(bean.announceDate);
        }

    }

    public void setData(MyBuyRecordBean data) {
        bean = data;
        Log.d(TAG, "setting");
        notifyDataChange();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_record_check_mycode:
//                CheckMyCodeActivity.getInstance(getContext(), bean);
                break;
        }
    }
}
