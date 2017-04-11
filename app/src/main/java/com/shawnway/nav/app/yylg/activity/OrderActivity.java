package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.adapter.OrderAdapter;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.bean.AssertResponseWraper;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.bean.RememberMeResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.resp.PayResponse;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.GPS;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.PayHook;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.Bank;
import com.shawnway.nav.app.yylg.view.NoScrollListView;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Eiffel on 2015/11/11.
 */
public class OrderActivity extends MFragmentActivity implements View.OnClickListener {
    private static final String TAG = "OrderActivity";
    ArrayList<CarItemBean> mData;
    private OrderAdapter mAdapter;
    private View listViewWrapper;
    BroadcastReceiver receiver;
    private DecimalFormat df1 = new DecimalFormat("0.00");
    private AssertResponseWraper.BankBody mBankBody;
    private CarItemBean emptyCarItemBean;
    private SweetAlertDialog sweetAlertDialog;
    private  boolean isFisrt=true;
    ZProgressHUD loading;

    public static void getInstance(Activity context, @Nullable ArrayList<CarItemBean> orders, int reqCode) {
        Intent intent = new Intent(context, OrderActivity.class);
        String orderstr = new Gson().toJson(orders);
        intent.putExtra("order", orderstr);
        context.startActivityForResult(intent, reqCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(OrderActivity.this);
        setContentView(R.layout.activity_order);
        initLoadView();
        initReceiver();
        authenticate();

    }
    public void initLoadView()
    {
        loading = ZProgressHUD.getInstance(this);
        loading.setMessage("支付中");
        loading.setCancelable(false);
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Constants.ACTION_PAY_SUCCESS:
                        OrderActivity.this.setResult(RESULT_OK);
                        OrderActivity.this.finish();
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter(Constants.ACTION_PAY_SUCCESS);
        registerReceiver(receiver, filter);
    }


    private void authenticate() {
        Hook.getInstance(this).rememberMe(new Listener<RememberMeResponse>() {
            @Override
            public void onResponse(RememberMeResponse rememberMeResponse) {
                initOrders(getIntent().getExtras());
                refresh();
                openfolder();
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void refresh() {
        initOrderList();
        setTotal();
        initView();
    }


    private void initOrders(Bundle bundle) {
        String order = null;
        if (bundle != null) {
            order = bundle.getString("order");
        }
        if (order != null) {
            Log.d(TAG, order);

            Gson gson = new Gson();

            try {
                mData = gson.fromJson(order, new TypeToken<ArrayList<CarItemBean>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "parse json error,String is " + order);
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
        if (mData == null || mData.size() == 0) {
            //若没传入order，则默认购物车内商品作为订单内容
            mData = ((MyApplication) getApplication()).getData();
        }

        cleanUpZero(mData);
    }

    private void cleanUpZero(ArrayList<CarItemBean> list) {
        int i = 0;
        while (i < list.size()) {
            CarItemBean bean = list.get(i);
            if (bean.getBuyed() == 0)
                list.remove(i);
            else
                i++;
        }
    }


    private void initView() {
        initTopTotal();
        setAssert();
        initToolbar();
    }

    //总价
    private void initTopTotal() {
        TextView tot = (TextView) findViewById(R.id.amount);
        tot.setText(getString(R.string.coin, df1.format(mTotalPrize) /*mTotalPrize + ""*/));
    }

    private void setAssert() {

        VolleyTool.getInstance(this).sendGsonRequest(this, AssertResponseWraper.class, Constants.RETRIEVEBALANCE_URL, new Listener<AssertResponseWraper>() {
            @Override
            public void onResponse(AssertResponseWraper response) {
                if (!Utils.handleResponseError(OrderActivity.this, response)) {
                    mBankBody = response.getBody();
                    Bank bank = (Bank) findViewById(R.id.banks);
                    bank.setAssert(response.getBody(), String.valueOf(mTotalPrize));
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void setTotal() {
        double total = 0;
        for (int i = 0; i < mData.size(); i++) {
            total += mData.get(i).calWorth();
        }
        mTotalPrize = total;
    }

    public double mTotalPrize;//合计

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_order));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }

    private void initOrderList() {
        listViewWrapper = findViewById(R.id.orderListContainer);
        NoScrollListView listView = (NoScrollListView) findViewById(R.id.orderList);
        mAdapter = new OrderAdapter(mData, this);
        listView.setAdapter(mAdapter);
    }

    //ZProgressHUD loading;

    private void summit() {
//        loading = ZProgressHUD.getInstance(this);
//        loading.setMessage("支付中");
//        loading.setCancelable(false);

        try {
            if (StringUtils.isBlank(getPayType())) {
                ToastUtil.showShort(this, getString(R.string.tips_selectpayway));
            } else {
                if (getPayType().equals("BALANCE") && mTotalPrize > Double.valueOf(mBankBody.balance)) {//如果要支付的商品总价大于余额，则提示充值
                    ToastUtil.showShort(this, getResources().getString(R.string.insufficient_balance_please_recharge));
                    ChargeActivity.getInstance(this, "OrderActivity");
                    return;
                }

                loading.show();
                Thread.sleep(10);


                PayHook payHook = PayHook.getInstance(OrderActivity.this, PayHook.BUYGOOD, mData);
                final PayHook.PayParamsBean payinfo = payHook.new PayParamsBean();
                payinfo.setItems(mData);
                payinfo.setPaymentType(getPayType());
                payinfo.setLocation(GPS.getLocalLocation(this));

                payHook.prepayOrder(Constants.PAY_URL, payinfo, new Response.Listener<PayResponse>() {
                    @Override
                    public void onResponse(final PayResponse payType) {
                        if (!Utils.handleError(OrderActivity.this, payType, "支付失败：")) {
                            loading.dismiss();
                            PayHook.getInstance(OrderActivity.this, PayHook.BUYGOOD, mData).afterPrepayOrder(payType);
                        } else {
                            AppUtils.updateCartList(mData, OrderActivity.this, new Listener<GoodListGsonResponse>() {
                                @Override
                                public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                                    boolean change = false;
                                    loading.dismiss();
                                    ArrayList<GoodDetail> newlist = goodListGsonResponse.getBody().getDrawCycleDetailsList();
                                    try {
                                        for (int i = 0; i < mData.size(); i++) {
                                            for (int j = 0; j < newlist.size(); j++) {
                                                GoodBean good = mData.get(i).getGood();
                                                GoodBean newgood = newlist.get(j);
                                                if (good.getDrawCycleID() == newgood.getDrawCycleID()) {
                                                    if (!good.equals(newgood)) {
                                                        if (good.getFinalRslt() != null || good.getLeftCnt() == 0) {
                                                            //若返回数据中包含揭晓结果或剩余数量已为0，则清除该记录
                                                            emptyCarItemBean = mData.get(i);
                                                            mData.remove(i);
                                                        } else {
                                                            good.clone(newgood);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (IndexOutOfBoundsException e) {
                                        Log.e(TAG, "car update outofbound");
                                        e.printStackTrace();
                                    }
                                    refresh();
                                    mAdapter.notifyDataSetChanged();
                                    showErrorDialog(payType.getHeader().error);
                                }
                            }, new ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                }
                            });
                        }
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.d(TAG, "PayResultActivity:支付时网络出错，" + VolleyErrorHelper.getMessage(volleyError, OrderActivity.this));
                        String errorMsg = VolleyErrorHelper.getMessage(volleyError, OrderActivity.this);
                        if (errorMsg.contains("no cycle item found.")) {
                            showErrorDialog("购买结果未返回,请进入购买记录确认详情!");
                        } else
                        {
                            showErrorDialog("本次支付未成功,请重新选择!");
                        }
//                        ToastUtil.showShort(OrderActivity.this, VolleyErrorHelper.getMessage(volleyError, OrderActivity.this));
//                        PayResultActivity.getInstance(OrderActivity.this, TradeoutWrapper.PayStatue.STATUE_FAILEDQUERY, mData);
                    }
                });
            }
        } catch (NullPointerException e) {
            Log.d(TAG, e.getStackTrace().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 并发错误提示框
     *
     * @param errorMsg
     */
    private void showErrorDialog(String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            Toast.makeText(OrderActivity.this,errorMsg,Toast.LENGTH_LONG).show();
            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(final SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    finish();
                }
            }).setContentText(errorMsg).show();
        }
    }

    private String getPayType() {
        Log.d(TAG, "in OrderAct:" + ((Bank) findViewById(R.id.banks)).getPayType());
        return ((Bank) findViewById(R.id.banks)).getPayType();
    }

    private void setToClose(View view) {
        closefolder();
        view.setTag(false);
    }

    private void setToOpen(View view) {
        view.setTag(true);
        openfolder();
    }

    private void closefolder() {
        listViewWrapper.setVisibility(View.GONE);
        ImageView arrow = (ImageView) findViewById(R.id.orderArr);
        arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arr_up_gray));
    }

    private static int ANIMALTION_TIME = 300;

    private void openfolder() {
        listViewWrapper.setVisibility(View.VISIBLE);
        TranslateAnimation ta = new TranslateAnimation(0, 0, -listViewWrapper.getHeight() / 2,
                0);
        ta.setInterpolator(new DecelerateInterpolator());
        ta.setDuration(ANIMALTION_TIME);
        ta.setFillAfter(true);
        listViewWrapper.startAnimation(ta);
        ImageView arrow = (ImageView) findViewById(R.id.orderArr);
        arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arr_down_gray));

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTION_REQUEST_LOGIN && resultCode == RESULT_OK) {
            refresh();
            if (loading != null)
                loading.dismiss();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN) {
            finish();
        }
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.orderListTitle:
                try {
                    boolean current = (boolean) view.getTag();
                    if (current) {
                        setToClose(view);
                    } else {
                        setToOpen(view);
                    }
                } catch (Exception e) {
                    setToClose(view);
                }
                break;
            case R.id.top_back:
                finish();
                break;
            case R.id.submit:
                if(isFisrt)
                {
                    summit();
                    isFisrt=false;
                }else {
                    Toast.makeText(OrderActivity.this,"不能点击频繁",Toast.LENGTH_SHORT).show();
                }


        }
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
