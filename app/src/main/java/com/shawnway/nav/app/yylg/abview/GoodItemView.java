package com.shawnway.nav.app.yylg.abview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.TimeUtils;
import com.shawnway.nav.app.yylg.view.CirclerImageView;
import com.shawnway.nav.app.yylg.view.CustomDigitalClock;
import com.shawnway.nav.app.yylg.view.MyProgressBar;

import cn.iwgang.countdownview.CountdownView;

/**
 * 这是具有倒计时功能及进度条功能的商品视图
 */
public class GoodItemView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "LayoutAdapter";

    private final Context mContext;

    private View revingWrap;
    private View revedWrap;
    CustomDigitalClock remainTime;
    private GoodBean bean;

    private long down;
    private boolean isDowning = true;

    private Handler handler = new Handler();
    private Runnable runOne = new Runnable() {
        @Override
        public void run() {
            setToCalculating();
        }
    };

    public GoodItemView(Context context) {
        this(context, null);
    }

    public GoodItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    private void notifyDataChange() {
        /**
         * FIXME
         * RemainTime在服务端不提供，改用CallOffDate，CallOffDate是商品满人/截止的日期，后台不做倒计时，
         * 所以
         * ①当有CallOffDate而没finalResult时显示“正在计算”，
         * ②当有finalResult时则显示结果，
         * ③两者都没时显示进度条
         * */
        if(bean.getDrawStatus().equals(GoodBean.TYPE_CLOSE)){
            if(bean.getLeftCnt() == 0){
                bean.setDrawStatus(GoodBean.TYPE_RELEAVED);
            }else{
                bean.setDrawStatus(GoodBean.TYPE_PROGRESS);
            }
        }
        if (bean != null && !bean.getDisplayType().equals(GoodBean.TYPE_PROGRESS))
            initToCountdownStyle();
        else {
            initToProgressStyle();
        }
    }


    private void initToProgressStyle() {
        removeAllViews();
        LayoutInflater.from(mContext).inflate(R.layout.layout_home_progress_item, this, true);

        findViewById(R.id.panel_content).setOnClickListener(this);

        CirclerImageView icon = (CirclerImageView) findViewById(R.id.goods_img);
        icon.setImage(bean.getThumbnail());

        TextView name = (TextView) findViewById(R.id.goods_name);
        name.setText(getContext().getString(R.string.good_worth, bean.getPrice()));

        MyProgressBar progressBar = (MyProgressBar) findViewById(R.id.progressbar);
        progressBar.setDate(bean.getTotCnt(),bean.getPuredCnt(),bean.getLeftCnt());

        updateLableIcon();

    }

    private void updateLableIcon() {
        if (bean.getBuyUnit() == 10) findViewById(R.id.good_ten_label).setVisibility(View.VISIBLE);
        else findViewById(R.id.good_ten_label).setVisibility(View.GONE);
    }


    private void initToCountdownStyle() {
        removeAllViews();

        LayoutInflater.from(mContext).inflate(R.layout.layout_item_good_releaving,this, true);

        findViewById(R.id.panel_content).setOnClickListener(this);

        revingWrap = findViewById
                (R.id.result_revealing_wrapper);
        revedWrap = findViewById(R.id.result_revealed_wrapper);

        updateLableIcon();

        updataStatus(bean);
        initFixView();
        initDymView();
    }


    private void initFixView() {
        setGoodContent(bean);
    }

    private void initDymView() {
        switch (bean.getDisplayType()) {
            case GoodBean.TYPE_CALCULATING://正在计算中
            case GoodBean.TYPE_COUNTDOWN:
                setToRevealing(bean);
                break;
            case GoodBean.TYPE_RELEAVED://已揭晓
                setToRevealed();
                break;
            default:
                break;
        }

    }

    private void updataStatus(GoodBean data) {
        if (data.getFinalRslt()!=null)
            data.setDrawStatus(GoodBean.TYPE_RELEAVED);
        else if(data.getDrawDate() != null || data.getDrawStatus() == GoodBean.TYPE_COUNTDOWN)
            data.setDrawStatus(GoodBean.TYPE_COUNTDOWN);
        else
        {
            Log.e(TAG,"lack of needed infor");
            data.setDrawStatus(GoodBean.TYPE_CALCULATING);
        }

    }

    private void setToRevealed() {
        Log.d(TAG, "set to revealed");
        revedWrap.setVisibility(View.VISIBLE);
        revingWrap.setVisibility(View.GONE);

        ((TextView)findViewById(R.id.result_revealed_name)).setText(bean.getWinnerName());
    }

    private void setToRevealing(GoodBean data) {
        revedWrap.setVisibility(View.GONE);
        revingWrap.setVisibility(View.VISIBLE);

        boolean isDown = (data.getDrawStatus().equals(GoodBean.TYPE_COUNTDOWN))?true:false;
        if(isDown && isDowning){//只有在倒计时的时候才能够进行日期的转换，不然会发生空指针异常，因为不是倒计时的状态时没有drawDate字段
            String serverTime = data.getServerTime();
            long currentTimeMillis = TimeUtils.dataOne(serverTime);
            String drawDate = data.getDrawDate();//开奖时间
            long date = TimeUtils.dataOne(drawDate);
            down = date - currentTimeMillis;
            setupToCountDown(down);
            handler.postDelayed(runOne,down);
            if(down<0){
                setToCalculating();
            }
        }else {
            setToCalculating();
        }
    }

    private void setToCalculating() {
        findViewById(R.id.result_revealing_countdown).setVisibility(View.GONE);
        findViewById(R.id.result_revealing_countdown_hourtime).setVisibility(View.GONE);
        findViewById(R.id.result_revealing_text).setVisibility(VISIBLE);
    }

    private void setupToCountDown(long time) {
        CountdownView mCvCountdownView;
        if(time >= 1000*60*60){
            //如果倒计时时间超过一个小时，那么就显示时分秒和毫秒
            mCvCountdownView = (CountdownView)findViewById(R.id.result_revealing_countdown_hourtime);
            mCvCountdownView.setVisibility(View.VISIBLE);
        }else{
            mCvCountdownView = (CountdownView)findViewById(R.id.result_revealing_countdown);
            mCvCountdownView.setVisibility(View.VISIBLE);
        }
        mCvCountdownView.start(time); // 传一个毫秒值，

    }

    private void setGoodContent(GoodBean data) {
        final CirclerImageView itemImage = (CirclerImageView) findViewById(R.id.result_goods_pic);
        TextView itemText = (TextView) findViewById(R.id.result_goods_name);
        String url = data.getThumbnail();
        Log.d(TAG, "have url:" + url);
        itemImage.setImage(url);
        setTitle(data, itemText);
    }


//    // 默认缓存位置data/data/包名/cache/volley,默认大小5MB
//    // 修改缓存默认存储位置和存储区大小帖子上面有介绍
//    private String setImage(String url, final ImageView itemImage) {
//
//        RequestQueue requestQueue = VolleyTool.getInstance(mContext)
//                .getRequestQueue();
//        ImageRequest request = (ImageRequest) requestQueue
//                .add(new ImageRequest(url, new Response.Listener<Bitmap>() {
//
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        itemImage.setImageBitmap(response);
//                    }
//                }, 0, 0, Bitmap.Config.RGB_565, null));
//        request.setTag(MainActivity.class.getSimpleName());
//        request.setShouldCache(true);
//        return url;
//    }

    private void setCountDown(final GoodBean item) {
        CustomDigitalClock remainTime = (CustomDigitalClock) findViewById(R.id.result_revealing_countdown);
        long t = item.getRemainTime();
        if (t != 0) {
            remainTime.setEndTime(t);
            remainTime.setClockListener(new CustomDigitalClock.ClockListener() {

                @Override
                public void timeEnd() {
                    item.setDrawStatus(GoodBean.TYPE_RELEAVED);
                    setToRevealed();
                }

                @Override
                public void remainFiveMinutes() {

                }
            });
            remainTime.setVisibility(View.VISIBLE);
        }
    }

    private void setTitle(GoodBean data, TextView itemText) {
        itemText.setText("（第"+data.getCycle()+"期）"+data.getProdName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_add_cart:
                try {
                    AppUtils.addTocart(((Activity) mContext), bean);
                } catch (ClassCastException e) {
                    Log.d(TAG, e.getLocalizedMessage());
                }
                break;
            default:
                DetailActivity.getInstance(mContext,bean);
                break;
        }
    }

    public void setData(GoodBean good) {

        if (!good.equals(bean)) {
            this.bean = good;
            Log.d(TAG,"refresh good item:"+good.toString());
            notifyDataChange();
        }
    }


}
