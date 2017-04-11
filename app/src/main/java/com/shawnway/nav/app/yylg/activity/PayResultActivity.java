package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.PayResultAdapter;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.bean.PurchaseDetailsBean;
import com.shawnway.nav.app.yylg.bean.TradeoutWrapper;
import com.shawnway.nav.app.yylg.bean.TradeoutWrapper.PayStatue;
import com.shawnway.nav.app.yylg.bean.TradeoutWrapper.TradeoutBody;
import com.shawnway.nav.app.yylg.fragment.CheckMyCodeFragment;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.MyBuyRecordsActivity;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.PayHook;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.NoScrollListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Eiffel on 2015/11/30.
 */
public class PayResultActivity extends FragmentActivity implements View.OnClickListener {

    private List<CarItemBean> mCarDatas;//购物车商品信息
    private List<CarItemBean> mSaveCarDatas = new ArrayList<>();//保存购物车信息，用来做判断跳转到哪里
    private static final String TAG = "PayResultActivity";
    private TradeoutBody mStatue = new TradeoutBody();

    public static void getInstance(Activity activity, @Nullable PayStatue orderStatues, List<CarItemBean> mData) {
        Intent i = new Intent(activity, PayResultActivity.class);
        if (orderStatues != null)
            i.putExtra("sta", orderStatues);
        if (mData != null) {
            i.putExtra("carData", (Serializable) mData);
        }
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCarDatas = (List<CarItemBean>) getIntent().getSerializableExtra("carData");
        mSaveCarDatas.addAll(mCarDatas);
        Log.d(TAG, "支付结果中购物车的信息：" + mCarDatas);
        CloseActivityUtil.add(PayResultActivity.this);
        setContentView(R.layout.activity_payresult);
        initData();
        initToolBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            PayStatue sta = (PayStatue) b.getSerializable("sta");
            if (sta != null) {
                Log.d(TAG, "支付结果：" + sta.toString());
                mStatue.setOrderStatus(sta.toString());
                initView();
                return;
            }
        }
        //没向该活动传递支付状态，故调用PayHook的验证接口
        final ZProgressHUD loading = ZProgressHUD.getInstance(this);
        loading.setMessage(getString(R.string.pay_checking));
        loading.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PayHook.getInstance(PayResultActivity.this).queryOrder(new Response.Listener<TradeoutWrapper>() {
                    @Override
                    public void onResponse(TradeoutWrapper tradeoutWrapper) {
                        if (!Utils.handleResponseError(PayResultActivity.this, tradeoutWrapper)) {
                            Log.d(TAG, "支付成功。。。");
                            mStatue = tradeoutWrapper.getBody();
                            initView();
                            loading.dismiss();
                        } else {
                            Log.d(TAG, "支付失败。。。");
                            mStatue.setOrderStatus(PayStatue.STATUE_FAILEDQUERY.toString());
                            initView();
                            loading.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //验证结果异常
                        Log.d(TAG, "支付时候网络有问题。。。");
                        mStatue.setOrderStatus(PayStatue.STATUE_FAILEDQUERY.toString());
                        initView();
                        loading.dismiss();
                    }
                });
            }
        }, getResources().getInteger(R.integer.pay_retrieve_delay));

    }

    private void initView() {
        if (mStatue == null || mStatue.getOrderStatus() == null) return;
        Log.d(TAG, new Gson().toJson(mStatue));
        switch (mStatue.getOrderStatus()) {
            case STATUE_PAYED:
            case STATUE_SUCCESS:
                upDateCarDatas();//保存购物车数据
                findViewById(R.id.bt_continue).setVisibility(View.VISIBLE);

                View successWrapper = findViewById(R.id.layout_pay_success);
                TextView stateTV = (TextView) successWrapper.findViewById(R.id.textview_message);
                successWrapper.setVisibility(View.VISIBLE);
                switch (PayHook.getInstance(this).getResultKind()) {
                    case PayHook.CHARGE:
                        stateTV.setText(getString(R.string.CHARGE_SUCCESS));
                        findViewById(R.id.bt_continue).setVisibility(View.VISIBLE);
                        findViewById(R.id.bt_checkresult).setVisibility(View.VISIBLE);
                        break;
                    case PayHook.BUYGOOD:
                        stateTV.setText(getString(R.string.payresult_pay_success));
                        final PayResultAdapter orderAdapter = new PayResultAdapter((ArrayList<CarItemBean>) mSaveCarDatas, this);
                        final ListView listView = (ListView) findViewById(R.id.payresult_orderList);
                        orderAdapter.setOnItemClickListener(new PayResultAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(CarItemBean carItemBean) {
                                CheckMyCodeActivity.getInstance(PayResultActivity.this, carItemBean.getGood().getDrawCycleID());
                            }
                        });
                        listView.setAdapter(orderAdapter);
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        Date d = new Date();
                        String date = sd.format(d.getTime());
                        ((TextView) findViewById(R.id.pay_result_date)).setText(date);
                        findViewById(R.id.layout_pay_result_orderlist).setVisibility(View.VISIBLE);
                        findViewById(R.id.bt_continue).setVisibility(View.VISIBLE);
                        findViewById(R.id.bt_goRecord).setVisibility(View.VISIBLE);
                        break;
                }

                Intent intent = new Intent(Constants.ACTION_PAY_SUCCESS);
                sendBroadcast(intent);
                break;
            case STATUE_NOTPAY:
                findViewById(R.id.bt_repay).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_pay_notpayed).setVisibility(View.VISIBLE);
                break;
            case STATUE_FAILEDQUERY:

                View wrapper = findViewById(R.id.layout_pay_failed);
                wrapper.setVisibility(View.VISIBLE);

                TextView textView = (TextView) wrapper.findViewById(R.id.textview_message);

                switch (PayHook.getInstance(this).getResultKind()) {
                    case PayHook.CHARGE:
                        findViewById(R.id.bt_checkresult).setVisibility(View.VISIBLE);
                        textView.setText(getString(R.string.payresult_query_charge_failed));
                        break;
                    case PayHook.BUYGOOD:
                        findViewById(R.id.bt_goRecord).setVisibility(View.VISIBLE);
                        findViewById(R.id.bt_continue).setVisibility(View.VISIBLE);
                        textView.setText(getString(R.string.payresult_query_buy_failed));
                        break;
                }
                break;

        }
    }


    protected void initToolBar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_pay_result));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.bt_continue://继续购物
                MainActivity.getInstance(this, 0);
                break;
            case R.id.bt_goRecord:
                MyBuyRecordsActivity.start(this);
                break;
            case R.id.bt_checkresult:
                AccountDetailsActivity.getInstance(this, AccountDetailsActivity.class);
                break;
            case R.id.bt_repay:
                finish();
                break;
        }
    }

    public void upDateCarDatas() {
        AppUtils.updateCartList(mCarDatas, PayResultActivity.this, new Response.Listener<GoodListGsonResponse>() {
            @Override
            public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                boolean change = false;
                Log.d(TAG, "更新购物车的商品数量");
                ArrayList<GoodDetail> newlist = goodListGsonResponse.getBody().getDrawCycleDetailsList();//现在请求数据的时候最新的购物车商品
                try {
                    int size = mCarDatas.size();
                    for (int i = 0; i < size; ) {
                        mCarDatas.remove(i);
                        Log.d(TAG, "去除购物车中所有的商品，还剩：" + mCarDatas.size() + "个，分别是：" + mCarDatas);
                        change = true;
                        size = mCarDatas.size();
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e(TAG, "car update outofbound");
                    e.printStackTrace();
                }
                if (change) {
                    Log.d(TAG, "购买成功，购物车状态已自动更新");
                    changeTabNum();
                    saveCartData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    private void changeTabNum() {
        Intent intent = new Intent(Constants.ACTION_CART_NUM_CHANGE);
        PayResultActivity.this.sendBroadcast(intent);
    }

    public void saveCartData() {
        Gson gson = new Gson();
        String cartData = gson.toJson(mCarDatas);
        Log.d(TAG, "支付成功后保存购物车数据:" + cartData);
        Utils.setParam(this, "cart", cartData);
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }
}
