package com.shawnway.nav.app.yylg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.LastWinnerBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.UrlBean;
import com.shawnway.nav.app.yylg.fragment.CalDetFragment;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.net.GsonRequest;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.GlideUtils;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.tool.TimeUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.view.MyProgressBar;
import com.shawnway.nav.app.yylg.view.MySlideShowView;
import com.shawnway.nav.app.yylg.view.TabButton;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.util.HashMap;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;



public class DetailActivity extends MyActivity implements OnClickListener {
    private static final String TAG = "DetailActicity";
    private GoodDetail mGoodBean;
    private boolean isDowning = true;

    long down;
    long mDrawId;
    private ZProgressHUD loading;

    public static void getInstance(Context context, GoodBean goodBean) {
        getInstance(context, goodBean.getDrawCycleID());
    }

    public static void getInstance(Context context, long drawID) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", drawID);
        Log.d(TAG, "id:" + drawID);
        context.startActivity(intent);
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            refreshHeaderData();
            handler.postDelayed(this, 10 * 1000);//每十秒刷新一次页面，几秒刷一次页面好呢？
        }
    };

    private Runnable runOne = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "倒计时时间一到，就显示计算中的布局");
            setupToCalDown(mGoodBean);
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(run);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(DetailActivity.this);
        setContentView(R.layout.activity_detail);
        loading = ZProgressHUD.getInstance(this).setMessage("加载中");
        loading.show();
        mGoodBean = new GoodDetail();
        Log.d(TAG, "oncreate:" + getIntent().getExtras().getLong("id"));
        mDrawId = getIntent().getExtras().getLong("id");
        mGoodBean.setDrawCycleID(mDrawId);
        initView();
        regReceiver();
    }

    private void regReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getAction().equals(Constants.ACTION_CART_NUM_CHANGE) || intent.getAction().equals(Constants.ACTION_CART_CHANGE)) {
                        updateCarNum();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "false to handle broadcast:" + e.getLocalizedMessage());
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_CART_NUM_CHANGE);
        filter.addAction(Constants.ACTION_CART_CHANGE);
    }

    private void initView() {
        initToolbar();
        requestHeaderData();
        updateCarNum();

    }

    private String getDisplayType(GoodDetail headerdata) {
        return headerdata.getDisplayType();
    }


    private void requestHeaderData() {
        Log.d(TAG, "requesting header");

        GsonRequest<DetailResponseWrapper> request = new GsonRequest<>(Constants.DETAIL_URL + "?drawcycleId=" + mGoodBean.getDrawCycleID(), DetailResponseWrapper.class, this, new Response.Listener<DetailResponseWrapper>() {
            @Override
            public void onResponse(DetailResponseWrapper response) {
                if (response.getBody().drawCycleDetails == null) {
                    ToastUtil.showShort(DetailActivity.this, "抱歉该商品无数据");
                    return;
                }
                mGoodBean = response.getBody().drawCycleDetails;
                //更新头部期数
                TextView cycleText = (TextView) findViewById(R.id.action_important_common);
                cycleText.setText(getString(R.string.cycle, mGoodBean.getCycle()));
                //更新内容区数据
                Log.d(TAG, response.getBody().drawCycleDetails.toString());
                initSlider(response.getBody().drawCycleDetails.productImages);
                init2Stutue(response.getBody().drawCycleDetails);
                if (mGoodBean.getDrawStatus().equals(GoodBean.TYPE_COUNTDOWN)) {//只有在倒计时的时候才刷新页面
                    String serverTime = mGoodBean.getServerTime();
                    long currentTimeMillis = TimeUtils.dataOne(serverTime);
                    String drawDate = mGoodBean.getDrawDate();//开奖时间，只有倒计时状态的时候才有开奖时间
                    long date = TimeUtils.dataOne(drawDate);
                    down = date - currentTimeMillis;
                    handler.postDelayed(run, down);//进入详情页面三秒之后开始刷新页面数据
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismis();
                ToastUtil.showNetError(DetailActivity.this);
            }
        });
        VolleyTool.getInstance(this).getRequestQueue().add(request);
    }

    /**
     * 获取上期获得者
     */
    private void initLastWinPrize(int visiable) {
        final LinearLayout layoutLastWin = (LinearLayout) findViewById(R.id.layout_good_detail_lastwin);
        if (visiable == View.VISIBLE) {
            RetrofitManager.getInstance()
                    .getApi()
                    .getLastWinPrize(mGoodBean.getDrawCycleID())
                    .compose(ThreadTransformer.<LastWinnerBean>applySchedulers())
                    .subscribe(new BaseSubscriber<LastWinnerBean>() {
                        @Override
                        public void onSuccess(final LastWinnerBean lastWinnerBean) {
                            ImageView userImage = (ImageView) findViewById(R.id.good_detail_userimage);
                            if (lastWinnerBean.getBody().getWinnerName() != null) {
                                layoutLastWin.setVisibility(View.VISIBLE);
                                if (TextUtils.equals(lastWinnerBean.getBody().getImgUrl(), "")) {
                                    GlideUtils.loadImage(DetailActivity.this, R.drawable.portrait, userImage);
                                } else
                                    GlideUtils.loadImage(DetailActivity.this, lastWinnerBean.getBody().getImgUrl(), userImage);
                                userImage.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CustomInfoActivity.getInstance(DetailActivity.this, lastWinnerBean.getBody().getWinnerId());
                                    }
                                });
                                findViewById(R.id.good_detail_winnerName).setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CustomInfoActivity.getInstance(DetailActivity.this, lastWinnerBean.getBody().getWinnerId());
                                    }
                                });

                                ((TextView) findViewById(R.id.good_detail_winnerName)).setText(lastWinnerBean.getBody().getWinnerName());
                                ((TextView) findViewById(R.id.good_detail_announceDate)).setText(lastWinnerBean.getBody().getAnnounceDate());
                                ((TextView) findViewById(R.id.good_detail_purDate)).setText(lastWinnerBean.getBody().getPurDate());
                                ((TextView) findViewById(R.id.good_detail_finalRslt)).setText(lastWinnerBean.getBody().getFinalRslt());
                                ((TextView) findViewById(R.id.good_detail_winnerLocation)).setText(String.format("(%s)", TextUtils.isEmpty(lastWinnerBean.getBody().getLocation()) ? "暂无地址信息" : lastWinnerBean.getBody().getLocation()));
                            } else {
                                layoutLastWin.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        } else {
            layoutLastWin.setVisibility(View.GONE);
            ZProgressHUD.dismis();
        }
    }

    private void refreshHeaderData() {
        Log.d(TAG, "requesting header");
        GsonRequest<DetailResponseWrapper> request = new GsonRequest<>(Constants.DETAIL_URL + "?drawcycleId=" + mGoodBean.getDrawCycleID(), DetailResponseWrapper.class, this, new Response.Listener<DetailResponseWrapper>() {
            @Override
            public void onResponse(DetailResponseWrapper response) {
                if (response.getBody().drawCycleDetails == null) {
                    ToastUtil.showShort(DetailActivity.this, "抱歉该商品无数据");
                    return;
                }
                mGoodBean = response.getBody().drawCycleDetails;
                //更新头部期数
                TextView cycleText = (TextView) findViewById(R.id.action_important_common);
                cycleText.setText(getString(R.string.cycle, mGoodBean.getCycle()));
                //更新内容区数据
                Log.d(TAG, response.getBody().drawCycleDetails.toString());
                findViewById(R.id.layout_countdown).setVisibility(View.GONE);//倒计时完毕的时候，干掉倒计时的显示
                init2Stutue(response.getBody().drawCycleDetails);
                if (mGoodBean.getDrawStatus().equals(GoodBean.TYPE_RELEAVED)) {//如果是已揭晓的时候，干掉计算中的显示
                    findViewById(R.id.layout_calculating).setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(DetailActivity.this);
            }
        });
        VolleyTool.getInstance(this).getRequestQueue().add(request);
    }

    private void init2Stutue(GoodDetail goodDetail) {
        ((TextView) findViewById(R.id.detail_title)).setText(getString(R.string.detail_good_title_text, goodDetail.getCycle(), goodDetail.getProdName()));
        int leftCnt = goodDetail.getLeftCnt();
        int totCnt = goodDetail.getTotCnt();
        int puredCnt = goodDetail.getPuredCnt();
        boolean isFull = (leftCnt == 0 && puredCnt == totCnt) ? true : false;
        boolean isDown = (goodDetail.getDrawStatus().equals(GoodBean.TYPE_COUNTDOWN)) ? true : false;
        switch (getDisplayType(goodDetail)) {
            case GoodBean.TYPE_PROGRESS:
                //显示进度条
                setupToProgress(goodDetail);
                break;
            case GoodBean.TYPE_CALCULATING:
                //显示计算中独有布局
                setupToCalDown(goodDetail);
                break;
            case GoodBean.TYPE_COUNTDOWN:
                String serverTime = mGoodBean.getServerTime();
                Log.d(TAG, "服务器的系统时间：" + serverTime);
                long currentTimeMillis = TimeUtils.dataOne(serverTime);
                String drawDate = goodDetail.getDrawDate();//开奖时间，只有倒计时状态的时候才有开奖时间
                Log.d(TAG, "得到的drawDate的时间：" + drawDate);
                long date = TimeUtils.dataOne(drawDate);
                down = date - currentTimeMillis;
                Log.d(TAG, "倒计时时间：" + down);
                if (down < 0) {
                    isDowning = false;
                }
                if (isDowning && isDown) {
                    //显示倒计时布局
                    setupToCountDown(down);
                    handler.postDelayed(runOne, down);//倒计时一完就显示计算中的布局
                } else {
                    setupToCalDown(goodDetail);
                }
                break;
            case GoodBean.TYPE_RELEAVED:
                //显示已揭晓的布局
                setupToReleased();
                initLastWinPrize(View.GONE);
                break;
            case GoodBean.TYPE_CLOSE://其实close的数据买完了之后状态就变成了ANNOUNCED，所以close的数据以买完就没有办法分辨了
                if (isFull) {
                    goodDetail.setDrawStatus("ANNOUNCED");
                    setupToReleased();//显示已经揭晓的布局
                    initLastWinPrize(View.GONE);
                } else {
                    goodDetail.setDrawStatus("OPEN");
                    setupToProgress(goodDetail);
                }
                break;
            case GoodBean.TYPE_ERROR:
                findViewById(R.id.layout_machinebreakdown).setVisibility(View.VISIBLE);
            default:
                if (isFull)
                    setupToReleased();//显示已经揭晓的布局
                setupToProgress(goodDetail);
                break;
        }

        //方式二此状态下不显示计算详情
        updateCalDetailBtn(goodDetail);

        //更新底部栏
        setupBottomBar(goodDetail);

        //更新actionbar
        updateToolbar(goodDetail.getDisplayType());

        //更新icon
        updateStaIcon(goodDetail.getDisplayType());


        ((TextView) findViewById(R.id.tv_progressbar_worth)).setText(getString(R.string.detail_good_progressbar_worth, String.valueOf(goodDetail.getPrice())));

        if (!TextUtils.equals(getDisplayType(goodDetail), GoodBean.TYPE_RELEAVED)) {
            initLastWinPrize(View.VISIBLE);
        }
        loading.dismis();

    }

    private void setupToCountDown(long time) {

        Log.d(TAG, "显示时倒计时的时间：" + time);
        findViewById(R.id.layout_countdown).setVisibility(View.VISIBLE);
        CountdownView mCvCountdownView;
        if (time >= 1000 * 60 * 60) {
            //如果倒计时时间超过一个小时，那么就显示时分秒和毫秒
            mCvCountdownView = (CountdownView) findViewById(R.id.layout_countdown_hourtime);
            mCvCountdownView.setVisibility(View.VISIBLE);
        } else {
            mCvCountdownView = (CountdownView) findViewById(R.id.layout_countdown_time);
            mCvCountdownView.setVisibility(View.VISIBLE);
        }

        mCvCountdownView.start(time); // 传一个毫秒值，
    }

    private void setupBottomBar(GoodDetail bean) {
        if (bean.getDisplayType().equals(GoodBean.TYPE_PROGRESS)) {
            findViewById(R.id.layout_buyNow).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_goCurrent).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_buyNow).setVisibility(View.GONE);
            findViewById(R.id.layout_goCurrent).setVisibility(View.VISIBLE);

            setLastCycle(bean.latestDrawCycleID);

        }
    }

    private void setupToProgress(GoodDetail goodDetail) {
        findViewById(R.id.layout_progressBar).setVisibility(View.VISIBLE);
        MyProgressBar progressBar = (MyProgressBar) findViewById(R.id.progressbar);
        progressBar.setDate(goodDetail.getTotCnt(), goodDetail.getPuredCnt(), goodDetail.getLeftCnt());
    }

    private void setupToCalDown(GoodDetail goodDetail) {
        findViewById(R.id.layout_calculating).setVisibility(View.VISIBLE);
        try {
            ((AnimationDrawable) ((ImageView) findViewById(R.id.detail_calculating)).getDrawable()).start();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        findViewById(R.id.layout_goCurrent).setVisibility(View.VISIBLE);
    }

    private void setupToReleased() {
        RetrofitManager.getInstance()
                .getApi()
                .getCustomerImage(mGoodBean.winnerId)
                .compose(ThreadTransformer.<UserFragment.ImageBean>applySchedulers())
                .subscribe(new BaseSubscriber<UserFragment.ImageBean>() {
                    @Override
                    public void onSuccess(UserFragment.ImageBean imageBean) {

                        findViewById(R.id.layout_revealed).setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.tv_revealed_onName)).setText(mGoodBean.getWinnerName());
                        ((TextView) findViewById(R.id.tv_revealed_annTime)).setText(getString(R.string.detail_good_releaved_annt, mGoodBean.getAnnounceDate()));
                        ((TextView) findViewById(R.id.tv_revealed_buyTime)).setText(getString(R.string.detail_good_releaved_buyt, mGoodBean.winnerPurchedDate));
                        ((TextView) findViewById(R.id.tv_revealed_winnerJoinCnt)).setText(getString(R.string.detail_good_releaved_wnnjoin, mGoodBean.winnerPartCnt) + "人次");
                        ((TextView) findViewById(R.id.tv_revealed_onLuck)).setText(mGoodBean.getFinalRslt());
                        String temp="(暂无地址信息)";
                        ((TextView) findViewById(R.id.tv_city)).setText(((mGoodBean.winnerLocation==null||mGoodBean.winnerLocation.length()<=0)?temp:"("+mGoodBean.winnerLocation+")"));

                        CircleImageView userImage = (CircleImageView) findViewById(R.id.iv_revealed_avatar);
                        userImage.setOnClickListener(DetailActivity.this);
                        if (TextUtils.equals(imageBean.getBody().getPathUrl(), "")) {
                            GlideUtils.loadImage(DetailActivity.this, R.drawable.portrait, userImage);
                        } else
                            GlideUtils.loadImage(DetailActivity.this, imageBean.getBody().getPathUrl(), userImage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private void updateStaIcon(String statue) {
        switch (statue) {
            case GoodBean.TYPE_PROGRESS:
                findViewById(R.id.detail_title_state_going).setVisibility(View.VISIBLE);
                findViewById(R.id.detail_title_state_calting).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_announced).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_countdown).setVisibility(View.GONE);
                break;
            case GoodBean.TYPE_COUNTDOWN:
                findViewById(R.id.detail_title_state_going).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_calting).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_announced).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_countdown).setVisibility(View.VISIBLE);
                break;
            case GoodBean.TYPE_CALCULATING:
                findViewById(R.id.detail_title_state_going).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_calting).setVisibility(View.VISIBLE);
                findViewById(R.id.detail_title_state_announced).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_countdown).setVisibility(View.GONE);
                break;
            case GoodBean.TYPE_RELEAVED:
                findViewById(R.id.detail_title_state_going).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_calting).setVisibility(View.GONE);
                findViewById(R.id.detail_title_state_announced).setVisibility(View.VISIBLE);
                findViewById(R.id.detail_title_state_countdown).setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 控制是否显示计算详情按钮
     *
     * @param goodDetail
     */
    private void updateCalDetailBtn(GoodDetail goodDetail) {
        //在进行中的商品、已经揭晓的商品
        if (goodDetail.getDisplayType().equals(GoodBean.TYPE_PROGRESS) || goodDetail.getDisplayType().equals(GoodBean.TYPE_RELEAVED)
                || (goodDetail.drawType != null && goodDetail.drawType.equals(CalDetFragment.TYPE_2)))
            findViewById(R.id.bt_willReveal_calaDet).setVisibility(View.GONE);
        else
            findViewById(R.id.bt_willReveal_calaDet).setVisibility(View.VISIBLE);

    }

    private void updateToolbar(String status) {
        //更新actionbar
        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(status == GoodBean.TYPE_PROGRESS ? R.string.title_activity_detail : R.string.title_activity_opendetail));
    }

    private void setLastCycle(long lastDrawId) {

        if (mGoodBean.latestDrawCycle != 0)
            ((TextView) findViewById(R.id.tv_currentNow)).setText(getString(R.string.detail_gocurent_cycle, mGoodBean.latestDrawCycle + ""));
        else {
            Map<String, String> params = new HashMap<>();
            params.put("drawcycleId", lastDrawId + "");
            VolleyTool.getInstance(this).sendGsonRequest(this, DetailResponseWrapper.class, Constants.DETAIL_URL, params, new Response.Listener<DetailResponseWrapper>() {
                @Override
                public void onResponse(DetailResponseWrapper response) {
                    ((TextView) findViewById(R.id.tv_currentNow)).setText(getString(R.string.detail_gocurent_cycle, response.getBody().drawCycleDetails.getCycle() + ""));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
        }
    }


    private void updateCarNum() {
        int cartSize = ((MyApplication) getApplication()).getData().size();
        TabButton carReleased = ((TabButton) findViewById(R.id.cart));
        TabButton carProgress = (TabButton) findViewById(R.id.cart2);
        carReleased.setMessageNumber(cartSize);
        carProgress.setMessageNumber(cartSize);
    }

    /**
     * 给图片的URL地址，然后将这些图片轮播显示
     *
     * @param urls 图片的URL地址
     */
    private void initSlider(String[] urls) {
        MySlideShowView slider = (MySlideShowView) findViewById(R.id.slider);
        if (Constants.dummy)//本地调试
            slider.initPic(UrlBean.urls);
        else slider.initPic(urls);
    }


    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        TextView cycleText = (TextView) findViewById(R.id.action_important_common);
        cycleText.setVisibility(View.VISIBLE);
        cycleText.setOnClickListener(this);
        cycleText.setText(getString(R.string.cycle, mGoodBean.getCycle()));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_addGood2Cart:

                AppUtils.addTocart(this, mGoodBean);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.top_back:
                finish();
                break;
            case R.id.top_cart:
                MainActivity.getInstance(this, 3);
                break;
            case R.id.action_important_common:
                SelectCycActivity.getInstance(mGoodBean, this);
                break;
            case R.id.bt_joinNow:
                AppUtils.addTocart(this, mGoodBean);
                MainActivity.getInstance(this, 3);
                break;
            case R.id.bt_layout_viewDetail:
                WebViewActivity.getInstance(this, R.string.title_activity_viewdetail, null,
                        Constants.VIEWDETAIL_URL + "?productID=" + mGoodBean.getProductID(), true, true, true);
                break;
            case R.id.bt_layout_joinDetail:
                JoinListActivity.getInstance(this, JoinListActivity.class, mGoodBean);
                break;
            case R.id.bt_layout_shareDetail:
                ShareListActivity.getInstance(this, mGoodBean.getProductID());
                break;
            case R.id.bt_layout_winRecord:
                SelectCycActivity.getInstance(mGoodBean, this);
                break;
            case R.id.cart_icon_wrapper:
            case R.id.cart:
            case R.id.cart2:
                MainActivity.getInstance(this, 3);
                break;
            case R.id.bt_willReveal_calaDet:
                CalDetailCountDownActivity.getInstance(this, CalDetailCountDownActivity.class, mGoodBean);
                break;
            case R.id.bt_revealed_calaDet:
                CalDetailActivity.getInstance(this, CalDetailActivity.class, mGoodBean);//查看mGoodsBean的计算详情
                break;
            case R.id.tv_currentNow://点击查看正在进行中的下一期的商品
                DetailActivity.getInstance(this, mGoodBean.latestDrawCycleID);
                finish();
                break;
            case R.id.tv_revealed_onName:
            case R.id.iv_revealed_avatar:
                CustomInfoActivity.getInstance(this, mGoodBean.winnerId);
                break;
            case R.id.tv_getcode:
                CodeListActivity.getInstance(this, mGoodBean.getDrawCycleID());
                break;
            default:
                break;
        }

    }


    class DetailResponseWrapper extends ResponseGson<DetailResponseBody> {

    }

    class DetailResponseBody {
        GoodDetail drawCycleDetails;
    }


//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config = new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config, res.getDisplayMetrics());
//        return res;
//    }

}
