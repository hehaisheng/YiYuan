package com.shawnway.nav.app.yylg.tool;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.PayResultActivity;
import com.shawnway.nav.app.yylg.bean.BalancePayResponseWrapper;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.bean.PointPayResponseWrapper;
import com.shawnway.nav.app.yylg.bean.TradeoutWrapper;
import com.shawnway.nav.app.yylg.bean.WeChatPayBody;
import com.shawnway.nav.app.yylg.bean.WeChatResponseWrapper;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.resp.PayResponse;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/30.
 */
public class PayHook extends Activity {


    public static final String TYPE_BALANCE = "BALANCE";//余额
    public static final String TYPE_WECHAT = "WECHAT";
    public static final String TYPE_POINT = "POINT";//福分
    public static final String TYPE_BANKCARD = "BANKCARD";

    public static final int CHARGE = 1;
    public static final int BUYGOOD = 2;


    private static final String TAG = "PayHook";
    private String mPayType;
    private Activity mContext;
    private static PayHook mPayHook;
    private int resultKind;
    private PayParamsBean mDatas;//支付信息
    private List<CarItemBean> mCarDatas;//购物车商品信息

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        CloseActivityUtil.add(this);
        setContentView(R.layout.layout_payhook);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static PayHook getInstance(Activity activity, int resultkind, List<CarItemBean> mData) {
        if (mPayHook == null) {
            mPayHook = new PayHook(activity, resultkind, mData);
        } else if (resultkind != -1) {
            mPayHook.setResultKind(resultkind);
        }
        return mPayHook;
    }

    /**
     * @param activity
     * @param resultkind 支付商品的类型，供验证支付结果页进行区别显示内容
     * @return
     */
    public static PayHook getInstance(Activity activity, int resultkind) {
        if (mPayHook == null) {
            mPayHook = new PayHook(activity, resultkind);
        } else if (resultkind != -1) {
            mPayHook.setResultKind(resultkind);
        }
        return mPayHook;
    }

    public static PayHook getInstance(Activity activity) {
        return getInstance(activity, -1);
    }

    private PayHook(Activity activity, int resultkind, List<CarItemBean> mData) {
        this(activity, resultkind);
        mCarDatas = mData;
    }

    private PayHook(Activity activity, int resultKind) {
        mContext = activity;
        this.resultKind = resultKind;
    }

    //pay step1
    public void prepayOrder(String payUrl, PayParamsBean s, Response.Listener<PayResponse> listener, Response.ErrorListener errorListener) {
        Class clazz;
        mDatas = s;
        Log.d(TAG, "in PayHook:" + s.paymentType);
        switch (s.paymentType) {
            case TYPE_BALANCE:
                clazz = BalancePayResponseWrapper.class;
                break;
            case TYPE_POINT:
                clazz = PointPayResponseWrapper.class;
                break;
            default:
                Log.d(TAG, "pay with a wrong paytype" + s.paymentType);
                return;
        }
        VolleyTool.getInstance(mContext).sendGsonPostRequest(mContext, clazz, payUrl, new Gson().toJson(s),
                listener, errorListener,new DefaultRetryPolicy(Constants.DEF_PAYTIMEOUT*1000,  DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                      DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }




    /**
     * @param resp
     */
    //pay step2
    public void afterPrepayOrder(PayResponse resp) {
        mPayType = resp.getPayType();
        switch (resp.getPayType()) {
            case TYPE_BALANCE:
                handlePayResult(resp);
                break;
            case TYPE_POINT:
                handlePayResult(resp);
                break;
        }

    }

    private void handlePayResult(PayResponse resp) {
        if (resp.getCode().equals(VolleyTool.SUCCESS))
            PayResultActivity.getInstance(mContext, TradeoutWrapper.PayStatue.STATUE_PAYED, mCarDatas);

    }

    private String mTradeNo;

    private void goWeChat(WeChatResponseWrapper resp, Activity activity) {

        WeChatPayBody body = resp.getBody();

        mTradeNo = body.outTradeNo;
        Log.d(TAG, "将APP注册到微信中。。。");
        IWXAPI api = WXAPIFactory.createWXAPI(activity, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        Log.d(TAG, "APP注册到微信成功。。。");
        PayReq req = new PayReq();
        req.appId = body.appid;
        req.nonceStr = body.noncestr;
        req.partnerId = body.partnerid;
        req.prepayId = body.prepayid;
        req.timeStamp = body.timestamp;
        req.sign = body.sign;
        req.packageValue = body.pack;
        req.extData = body.outTradeNo;
        api.sendReq(req);
        Log.d(TAG, "发送微信请求。。。");
    }


    /**
     * 验证支付结果
     *
     * @param listener
     * @param errorlistener
     */
    public void queryOrder(Response.Listener<TradeoutWrapper> listener, Response.ErrorListener errorlistener) {
        if (mPayType == null) return;
        switch (mPayType) {
            case TYPE_WECHAT:
                query(listener, errorlistener);
                break;
        }
    }


    private void query(Response.Listener<TradeoutWrapper> listener, Response.ErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("outTradeNo", mTradeNo);
        VolleyTool.getInstance(mContext).sendGsonRequest(mContext, TradeoutWrapper.class, Constants.WECHAT_TRADEOUT, params, listener, errorListener);
    }


    public int getResultKind() {
        return resultKind;
    }

    private void setResultKind(int resultKind) {
        this.resultKind = resultKind;
    }


    /**
     * 不同的支付方式若有不同bean需要继承该方法
     */
    public interface PayType {
        String getPayType();
    }


    public class PayParamsBean {
        Item[] items;
        String paymentType;
        private String location;

        public void setItems(ArrayList<CarItemBean> orders) {
            items = new Item[orders.size()];
            for (int i = 0; i < orders.size(); i++) {
                CarItemBean data = orders.get(i);
                Item item = new Item(data);
                items[i] = item;
            }
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }
    }

    public class Item {
        long cycleId;
        int count;

        public Item(CarItemBean data) {

            count = data.getBuyed();
            cycleId = data.getGood().getDrawCycleID();
        }
    }


}
