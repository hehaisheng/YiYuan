package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.AssertResponseWraper;
import com.shawnway.nav.app.yylg.bean.ChargeAlinpayWrapper;
import com.shawnway.nav.app.yylg.bean.WeChatChargeResponseWrapper;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.PayHook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.WXChargeTool;
import com.shawnway.nav.app.yylg.view.Bank;
import com.shawnway.nav.app.yylg.view.ClearEditView;
import com.shawnway.nav.app.yylg.view.MoneyPicker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 充值页面
 * Created by Eiffel on 2015/11/26.
 */
public class ChargeActivity extends MyActivity implements View.OnClickListener {
    private static final String TAG = "ChargeActivity";
    private String orderNo;
    private int amount;
    private String channel;
    private Activity mContext;
    private boolean isLock = true;

    public static String pageTag;

    private long userId;//通联支付的会员Id TODO:
    private long checkedId = -2;//校验获取通联支付的Id -2代表没有校验，-1代表因为网络问题校验失败 其他的数值就是校验成功后返回的通联支付会员Id

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, ChargeActivity.class);
        context.startActivity(intent);
    }

    public static void getInstance(Context context,String Tag) {
        pageTag = Tag;
        Intent intent = new Intent(context, ChargeActivity.class);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(ChargeActivity.this);
        setContentView(R.layout.activity_charge);
        mContext = this;
       // checkUserId();//因为校验需要时间，所以在已进入这个类的时候就进行校验
        setListener();
        initToolbar();
        setAssert();
    }

    private void setAssert() {
        VolleyTool.getInstance(this).sendGsonRequest(this, AssertResponseWraper.class, Constants.RETRIEVEBALANCE_URL, new Response.Listener<AssertResponseWraper>() {
            @Override
            public void onResponse(AssertResponseWraper response) {
                if (!Utils.handleResponseError(ChargeActivity.this, response)) {
                    ((TextView) findViewById(R.id.amount)).setText(getString(R.string.money, response.getBody().balance));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void setListener() {
        findViewById(R.id.submit).setOnClickListener(this);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_charge));
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
            case R.id.submit:
                charge();
                break;

        }
    }

    private void charge() {
        amount=((MoneyPicker) findViewById(R.id.moneyPicker)).getMoney();
        String deviceId=AppUtils.getDeviceId();
        channel=((Bank) findViewById(R.id.banks)).getPayType();

        if(amount == 0){
            ToastUtil.showShort(this,"充值金额不能为0！");
            return;
        }
        Map params = new HashMap();
        params.put("amout", BigDecimal.valueOf(amount));
        if(channel.equals(PayHook.TYPE_WECHAT)){
            params.put("payMethod","CHARGE_WECHAT");
        }else if(channel.equals(PayHook.TYPE_BANKCARD)){
            params.put("payMethod","CHARGE_BANK");
        }
        lock();
        //以下网络请求用于获取订单号
        VolleyTool.getInstance(this).sendGsonRequest(this, WeChatChargeResponseWrapper.class, Constants.WEBCHAT_REQUESTORDERNO, params, new Response.Listener<WeChatChargeResponseWrapper>() {
            @Override
            public void onResponse(WeChatChargeResponseWrapper response) {
                if (!Utils.handleResponseError(ChargeActivity.this, response)) {
                    orderNo = response.getBody().orederNo;
                    Log.d(TAG, "微信支付之前生成的订单号。。。"+orderNo);
                    unLock();
                    if ((isLock == false) && channel.equals(PayHook.TYPE_WECHAT)) {
                        //从后台获取订单号，开启工具类去处理
                        new WXChargeTool(mContext, Integer.valueOf(amount), orderNo).startPay();
                    }else if((isLock == false) && channel.equals(PayHook.TYPE_BANKCARD)){
                        //TODO:放上会员模式的用户ID，有的话直接放上，没有的话需要将姓名和身份证号码给后台，让后台产生一个ID返回来
                        checkUserId();
                        userId = initUserId();
                        Log.d(TAG, "最终支付之前的通联支付的会员Id：" + userId);
                        if(userId <= 0){
                            return;
                        }
                        new BankCardChargeActivity(mContext,Integer.valueOf(amount),orderNo,userId).startPay();
                    }
                } else {
                    orderNo = null;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    /**
     * 初始化用户的通联支付会员Id TODO:
     */
    private Long initUserId() {
        //校验当前用户是否是通联支付的会员，是的话返回会员号
        if(checkedId<-1){
            //不是的话，进行信息的填写，通过后台注册一个会员号
            return checkUserId();
        }else if(checkedId == -1){//如果是网络错误或者网络延迟导致的Id值不准确，就重新进行校验
            return checkUserId();
        }else{//因为校验需要时间，所以在已进入这个类的时候就进行校验，然后对校验结果进行判断
            return checkedId;
        }
    }

    /**
     * 校验当前用户的通联支付会员Id
     * @return 返回用户注册的通联支付的会员Id
     */
    private Long checkUserId() {
        VolleyTool.getInstance(ChargeActivity.this).sendGsonRequest(this, ChargeAlinpayWrapper.class, Constants.PAY_GET_USERID, new Response.Listener<ChargeAlinpayWrapper>() {
            @Override
            public void onResponse(ChargeAlinpayWrapper response) {
                ChargeAlinpayWrapper.ChargeAlinpayBody body = response.getBody();
                if(body.userId==null){
                    checkedId = -1;
                }else{
                    checkedId = Long.valueOf(body.userId);
                }
                Log.d(TAG, "校验通联支付UserId：" + checkedId);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                checkedId = -1;
                ToastUtil.showShort(ChargeActivity.this, "通联支付会员Id校验失败");
            }
        });
        return checkedId;
    }

    private long payUserId = -2;//-2是初始化值  -1代表注册的时候网络错误 其他的代表注册返回的Id
    /**
     * 通过在ddialog中输入用户的姓名和身份证号码来注册一个通联支付的会员Id
     */
    private Long registerUserId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_charge_alinpay, null);

        final ClearEditView cev_name = (ClearEditView) view.findViewById(R.id.dialog_charge_alinpay_name);
        final ClearEditView cev_id = (ClearEditView) view.findViewById(R.id.dialog_charge_alinpay_id);
        Button btnOk = (Button) view.findViewById(R.id.dialog_charge_alinpay_ok);
        Button btnCancle = (Button) view.findViewById(R.id.dialog_charge_alinpay_cancel);

        builder.setView(view);
        final AlertDialog dialog = builder.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cev_name.getText().toString().trim();
                String id = cev_id.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.showShort(ChargeActivity.this, getString(R.string.name_cannot_empty));
                    cev_name.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(id)){
                    ToastUtil.showShort(ChargeActivity.this,getString(R.string.IDCardNo_cannot_empty));
                    cev_id.requestFocus();
                    return;
                }
                Map<String,String> params = new HashMap();
                params.put("userName",name);
                params.put("pidCode",id);
                //TODO:注册通联支付Id的时间比较长，要在注册之后才能够返回Id的值
                VolleyTool.getInstance(ChargeActivity.this).sendGsonRequest(this, ChargeAlinpayWrapper.class, Constants.PAY_GET_USERID, params, new Response.Listener<ChargeAlinpayWrapper>() {
                    @Override
                    public void onResponse(ChargeAlinpayWrapper response) {
                        ChargeAlinpayWrapper.ChargeAlinpayBody body = response.getBody();
                        payUserId = Long.valueOf(body.userId);
                        Log.d(TAG, "注册通联支付的会员Id");
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        payUserId = -1;
                        ToastUtil.showShort(ChargeActivity.this,"通联支付信息验证错误");
                    }
                });
                //已完成TODO:给后台用户的信息用来注册通联支付的会员
                dialog.dismiss();
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return payUserId;
    }

    public void lock(){
        isLock = true;
    }

    public void unLock(){
        isLock = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ACTION_REQUEST_LOGIN && resultCode == RESULT_OK) {
            setAssert();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN) {
            finish();
        }
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
