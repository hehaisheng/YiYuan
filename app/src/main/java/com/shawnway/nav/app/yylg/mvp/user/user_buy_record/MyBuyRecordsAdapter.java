package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CheckMyCodeActivity;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.List;

import static android.view.View.VISIBLE;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsAdapter extends BaseQuickAdapter<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean> {

    public MyBuyRecordsAdapter(int layoutResId, List<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean myBuyRecordsBean) {
        baseViewHolder.setText(R.id.buy_record_gname, myBuyRecordsBean.getProdName());

        CirclerImageView proImage = baseViewHolder.getView(R.id.buy_record_pic);
        ImageView tenLabelImage = baseViewHolder.getView(R.id.good_ten_label);
        TextView prodName = baseViewHolder.getView(R.id.buy_record_gname);
        TextView goodsTotal = baseViewHolder.getView(R.id.goods_total);
        TextView buyRecordNum = baseViewHolder.getView(R.id.buy_record_num);
        ProgressBar progressBar = baseViewHolder.getView(R.id.progressbar);
        TextView leftCnt = baseViewHolder.getView(R.id.goods_left);
        LinearLayout buy_record_owner = baseViewHolder.getView(R.id.buy_record_owner);
        TextView buy_record_owner_name = baseViewHolder.getView(R.id.buy_record_owner_name);
        TextView buy_record_owner_num = baseViewHolder.getView(R.id.buy_record_owner_num);
        TextView buy_record_owner_code = baseViewHolder.getView(R.id.buy_record_owner_code);
        TextView buy_record_owner_time = baseViewHolder.getView(R.id.buy_record_owner_time);


        if (myBuyRecordsBean.getBuyUnit() == 10)//购买记录少了buyUnit字段(已完成TODO)
            tenLabelImage.setVisibility(VISIBLE);
        proImage.setImage(myBuyRecordsBean.getThumbnail());
        prodName.setText(mContext.getString(R.string.good_name, myBuyRecordsBean.getCycle() + "", myBuyRecordsBean.getProdName()));
        goodsTotal.setText(mContext.getString(R.string.good_total, myBuyRecordsBean.getTotCnt() + ""));
        buyRecordNum.setText(String.valueOf(myBuyRecordsBean.getTotalPurch()));

        proImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.getInstance(mContext, myBuyRecordsBean.getDrawCycleId());
            }
        });

        baseViewHolder.getView(R.id.buy_record_check_mycode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckMyCodeActivity.getInstance(mContext, myBuyRecordsBean);
            }
        });

        if (myBuyRecordsBean.getDrawStatus().equals(MyBuyRecordWrapper.MyBuyRecordBean.STA_OPNE) || myBuyRecordsBean.getDrawStatus().equals(MyBuyRecordWrapper.MyBuyRecordBean.STA_CALCULATING)) {
            //progressbar
            float progress = ((float) myBuyRecordsBean.getPuredCnt()) / myBuyRecordsBean.getTotCnt();
            progressBar.setProgress((int) (progress * 1000));
            progressBar.setVisibility(VISIBLE);

            //leftCount
            leftCnt.setVisibility(VISIBLE);
            leftCnt.setText(mContext.getString(R.string.good_left, myBuyRecordsBean.getLeftCnt()));
            //owner wrapper
            buy_record_owner.setVisibility(View.GONE);
        } else {
            //progressbar
            progressBar.setVisibility(View.GONE);
            //leftCount
            leftCnt.setVisibility(View.GONE);
            //ower wrapper
            buy_record_owner.setVisibility(VISIBLE);
            buy_record_owner_name.setText(myBuyRecordsBean.getWinnerName());
            buy_record_owner_num.setText(String.valueOf(myBuyRecordsBean.getPuredCnt()));
            buy_record_owner_code.setText(myBuyRecordsBean.getFinalRslt());
            buy_record_owner_time.setText(myBuyRecordsBean.getAnnounceDate());
        }
    }
}
