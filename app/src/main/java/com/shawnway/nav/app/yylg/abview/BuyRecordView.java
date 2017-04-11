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
import com.shawnway.nav.app.yylg.activity.CustomInfoActivity;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.BuyRecordWrapper.BuyRecordBean;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

/**
 * Created by Eiffel on 2015/12/3.
 */
public class BuyRecordView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "RecordItemView";
    private final Context mContext;

    public static final int HAVE_BANNER = 1;//用于某一用户购买记录页
    public static final int HAVE_SHOW_DETAIL = 2;//用于登陆用户购物历史页
    public static final int NO_BANNER = 3;//用于某一顾客获得奖品页
    private int mTheme;
    BuyRecordBean bean;

    public BuyRecordView(int theme, Context context) {
        this(theme, context, null, 0);
    }

    public BuyRecordView(int theme, Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mTheme = theme;
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        LayoutInflater.from(getContext()).inflate(R.layout.layout_item_buyrecord, this, true);
        switch (theme) {
            case HAVE_BANNER:
                View banner = findViewById(R.id.buy_record_banner);
                banner.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    private void notifyDataChange() {
        //根据商品状态分类显示商品信息
        //初始化共有信息部分
       CirclerImageView img=(CirclerImageView) findViewById(R.id.goods_img);
        img.setImage(bean.getThumbnail());
        img.setOnClickListener(this);

        TextView name=(TextView) findViewById(R.id.goods_name);
        name.setTextSize(15);
        name.setText(getContext().getString(R.string.good_name, bean.getCycle(), bean.getProdName()));
        name.setOnClickListener(this);

        if (bean.getBuyUnit() == 10)//服务器少了这个字段(已完成TODO)
            findViewById(R.id.good_ten_label).setVisibility(View.VISIBLE);

        Log.d(TAG, "noifyDatachange in RecorItem");
        switch (bean.getDrawStatus()) {
            //progressbar
            case GoodBean.TYPE_PROGRESS:
            case GoodBean.TYPE_CALCULATING:
            case GoodBean.TYPE_COUNTDOWN:
                findViewById(R.id.wraper_good_detail).setVisibility(VISIBLE);
                findViewById(R.id.wraper_good_detail_announced).setVisibility(GONE);
                ((TextView) findViewById(R.id.tv_joined)).setText(String.valueOf(bean.getPuredCnt()));
                ((TextView) findViewById(R.id.tv_total)).setText(String.valueOf(bean.getTotCnt()));
                ((TextView) findViewById(R.id.tv_left)).setText(String.valueOf(bean.getLeftCnt()));

                float progress = (float) bean.getPuredCnt() / bean.getTotCnt();
                ((ProgressBar) findViewById(R.id.progressbar)).setProgress((int) (progress * 1000));
                findViewById(R.id.label_onprogress).setVisibility(View.VISIBLE);
                break;
            //announced
            case GoodBean.TYPE_RELEAVED:
                findViewById(R.id.wraper_good_detail).setVisibility(GONE);
                findViewById(R.id.wraper_good_detail_announced).setVisibility(VISIBLE);
                TextView winner=(TextView)findViewById(R.id.winner);
                winner.setText(bean.getWinnerName());
                winner.setOnClickListener(this);

                ((TextView)findViewById(R.id.luckycode)).setText(bean.getFinalRslt());
                findViewById(R.id.label_announced).setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }

    }

    private void updateLabel() {
        findViewById(R.id.label_onprogress).setVisibility(mTheme == HAVE_BANNER ? VISIBLE : GONE);
        findViewById(R.id.label_announced).setVisibility(mTheme==NO_BANNER?VISIBLE:GONE);
    }


    public void setData(BuyRecordBean data) {
        bean = data;
        Log.d(TAG, "setting");
        notifyDataChange();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_record_check_mycode:
                break;
            case R.id.winner:
                if (bean.getCustomerID()!=0) {
                    CustomInfoActivity.getInstance(getContext(), bean.getCustomerID());
                }
                break;
            case R.id.goods_img:
            case R.id.goods_name:
                DetailActivity.getInstance(getContext(),bean.getDrawCycleId());
                break;
        }
    }
}
