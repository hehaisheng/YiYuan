package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.allinpay.appayassistex.APPayAssistEx;
import com.shawnway.nav.app.yylg.fragment.CarFragment;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.PaaCreator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class BankCardChargeActivity extends Activity {

    private static final String TAG = "BankCardChargeActivity";
    private int money;
    private Activity mContext;
    private String orderNo;
    private Long userId;//通联支付的会员Id
    public static String isSuccess = "false";

    public BankCardChargeActivity(){}

    public BankCardChargeActivity(Activity context, int money, String orderNo){
        mContext = context;
        this.money = money;
        this.orderNo = orderNo;
    }

    public BankCardChargeActivity(Activity context, int money, String orderNo,Long userId){
        mContext = context;
        this.money = money;
        this.orderNo = orderNo;
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        CloseActivityUtil.add(BankCardChargeActivity.this);
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * 通联支付，开始支付
     * @param
     */
    public void startPay() {
        Log.d(TAG, "通联支付传递到开始支付的userId："+userId);
        JSONObject payData = PaaCreator.randomPaa(money, orderNo,userId);
        APPayAssistEx.startPay(mContext, payData.toString(), APPayAssistEx.MODE_PRODUCT);//TODO:MODE_PRODUCT 生产环境 ，MODE_DEBUG 测试环境
    }

    /**
     * 通联支付，支付结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (APPayAssistEx.REQUESTCODE == requestCode) {
            if (null != data) {
                String payRes = null;
                String payAmount = null;
                String payTime = null;
                String payOrderId = null;
                try {
                    JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
                    payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
                    payAmount = resultJson.getString("payAmount");
                    payTime = resultJson.getString("payTime");
                    payOrderId = resultJson.getString("payOrderId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("MypayResult", "payRes: " + payRes + "  payAmount: " + payAmount + "  payTime: " + payTime + "  payOrderId: " + payOrderId);
                if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
                    isSuccess = "true";
                    showAppayRes("支付成功！");
                }else {
                    showAppayRes("支付失败！");
                }
            }
        }
    }

    public void showAppayRes(String res) {
        new AlertDialog.Builder(mContext)
                .setMessage(res)
                .setPositiveButton("确定", null)
                .show();
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
