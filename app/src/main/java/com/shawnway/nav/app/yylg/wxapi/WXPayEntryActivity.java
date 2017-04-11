package com.shawnway.nav.app.yylg.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.ChargeActivity;
import com.shawnway.nav.app.yylg.activity.MainActivity;
import com.shawnway.nav.app.yylg.activity.OrderActivity;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.fragment.CarFragment;
import com.shawnway.nav.app.yylg.net.Api;
import com.shawnway.nav.app.yylg.testmy.MyRetfofit;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Eiffel on 2015/11/25.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    //
    private IWXAPI api;
    private TextView payResult;
    public static final int UPDATE_BALANCE=4;
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        payResult = (TextView) findViewById(R.id.tv_payresult);
        //将APP注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,false);
        api.handleIntent(getIntent(), this);
    }
    //
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
//
//    @Override
//    public void onReq(BaseReq req) {
//    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    public void onResp(BaseResp resp) {
//        Log.d(TAG, "onPayFinish, errCode =" + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == 0) // 支付成功
            {


              final String url= Api.BASE_URL+"orderResultQuery";
                String tradeNum1= MyApplication.getInstance().getTradeNum();
                final Map<String,String> map=new HashMap<>();
                map.put("out_trade_no",tradeNum1);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String strResult= HttpUtils.submitPostData(url,map,"utf-8");
//                        Log.d("fjdakjf",strResult);
//
//                    }
//                }).start();

                MyRetfofit.getInstance().getApi().jugdeWXResult(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("dskml","失败");

                            }

                            @Override
                            public void onNext(String s) {
                                if(s.equals("支付成功"))
                               {
                                   Intent intent=new Intent();
                                   intent.setAction(Constants.ACTION_UPDATE_BALANCE);
                                   sendBroadcast(intent);
                                   Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                }



                            }
                        });
//                payResult.setText("支付成功");
//                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
            }
            else if(resp.errCode==-2)
            {
                payResult.setText("支付失败");
                Toast.makeText(this, getString(R.string.action_cancel) + resp.errCode, Toast.LENGTH_SHORT)
                        .show();
            }
            else
            {

                Log.d("WXPayEntryActivity",resp.toString());
                Toast.makeText(this, getString(R.string.action_cancel) + resp.errCode, Toast.LENGTH_SHORT)
                        .show();
            }

            finish();
            if(ChargeActivity.pageTag.equals("OrderActivity")){
                OrderActivity.getInstance(WXPayEntryActivity.this, null, CarFragment.REQUEST_PAY_INCART);
            }else{
                MainActivity.getInstance(this, 4);
            }

//            PayResultActivity.getInstance(this, TradeoutWrapper.PayStatue.STATUE_SUCCESS);
//            finish();
//            ZProgressHUD loading=ZProgressHUD.getInstance(this);
//            loading.setMessage("支付验证中");
//            Map<String, String> params=new HashMap<>();
//            params.put("outTradeNo",(resp.).extData)
//            VolleyTool.getInstance(this).sendGsonRequest(WeChatTradeoutWrapper.class, Constants.WECHAT_TRADEOUT, params, new Response.Listener<WeChatTradeoutWrapper>() {
//                @Override
//                public void onResponse(WeChatTradeoutWrapper weChatTradeoutWrapper) {
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//
//                }
//            });

        }

    }

    private class WeChatTradeoutWrapper {
    }


}
