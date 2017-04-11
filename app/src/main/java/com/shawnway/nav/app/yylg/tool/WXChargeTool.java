package com.shawnway.nav.app.yylg.tool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.testmy.MyRetfofit;
import com.shawnway.nav.app.yylg.testmy.WXPayBean;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.service.GetPrepayIdResult;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.Util;
import com.switfpass.pay.utils.XmlUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class WXChargeTool {

    //wxdedfac7c04f7ee9f
    private static final String TAG = "WXChargeTool";
    private int money;
    private Activity mContext;
    private String orderNo;

    public WXChargeTool() {
    }

    public WXChargeTool(Activity context, int money, String orderNo1) {
        this.money = money;
        this.orderNo = orderNo1;
        mContext = context;
        MyApplication.getInstance().setTradeNum(orderNo1);

    }

    public void startPay() {
        //new GetPrepayIdTask().execute();
        wxPay();
    }

    public void startWxPay(WXPayBean wxPayBean) {
//        new GetPrepayIdTask(wxPayBean).execute();
//        wxPay();
    }
    public void wxPay()
    {

        Map<String,String> param=new HashMap<>();
        String body = "充值" + money + "元";
        param.put("out_trade_no", orderNo);
        param.put("body", body);
        param.put("mch_create_ip", "127.0.0.1");

        MyRetfofit.getInstance().getApi()
                .requestPayPar(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WXPayBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ceshi", e.toString());

                    }

                    @Override
                    public void onNext(final WXPayBean wxPayBean) {
                        Log.d("dsml", "chengggggg");
                        Log.d("debug", wxPayBean.toString());
                        if (wxPayBean.getWxstatus().equalsIgnoreCase("0")) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Looper.prepare();
//                                    RequestMsg msg = new RequestMsg();
//                                    msg.setMoney(Double.parseDouble(/*money.getText().toString()*/money + ""));
//                                    msg.setTokenId(wxPayBean.getToken_id());
//                                    msg.setTradeType(MainApplication.WX_APP_TYPE);
//                                    msg.setAppId(Constants.APP_ID);//wxd3a1cdf74d0c41b3   wx2a5538052969956e  威富通测试用的收钱方
//                                    PayPlugin.unifiedAppPay(mContext, msg);
//                                    Looper.loop();
//
//                                }
//                            }).start();
                            RequestMsg msg = new RequestMsg();
                            //msg.setMoney(Double.parseDouble(/*money.getText().toString()*/2 + ""));
                            msg.setMoney(0.01);
                            msg.setTokenId(wxPayBean.getToken_id());
                            msg.setTradeType(MainApplication.WX_APP_TYPE);
                            msg.setAppId(Constants.APP_ID);//wxd3a1cdf74d0c41b3   wx2a5538052969956e  威富通测试用的收钱方
                            PayPlugin.unifiedAppPay(mContext, msg);
                            Log.d("cdss",msg.toString());
 //                           new GetPrepayIdTask(wxPayBean).execute();
                        }


                    }
                });


    }


    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;

        private String accessToken;


        public GetPrepayIdTask(String accessToken) {
            this.accessToken = accessToken;
        }

        public GetPrepayIdTask() {

        }

        @Override
        protected void onPreExecute() {
//            dialog =
//                    ProgressDialog.show(mContext,
//                            mContext.getString(R.string.app_tip),
//                            mContext.getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
//            if (dialog != null) {
//                dialog.dismiss();
//            }
            if (result == null) {
//                Toast.makeText(mContext, mContext.getString(R.string.get_prepayid_fail), Toast.LENGTH_LONG).show();
            } else {
                if (result.get("status").equalsIgnoreCase("0")) // 成功
                {


//                    Toast.makeText(mContext, R.string.get_prepayid_succ, Toast.LENGTH_LONG).show();
                    RequestMsg msg = new RequestMsg();
                    msg.setMoney(Double.parseDouble(/*money.getText().toString()*/money + ""));
                   msg.setTokenId(result.get("token_id"));

                    msg.setTradeType(MainApplication.WX_APP_TYPE);
                    msg.setAppId(Constants.APP_ID);//wxd3a1cdf74d0c41b3   wx2a5538052969956e  威富通测试用的收钱方

                    PayPlugin.unifiedAppPay(mContext, msg);

                } else {
//                    Toast.makeText(mContext, mContext.getString(R.string.get_prepayid_fail, result.get("status")), Toast.LENGTH_LONG)
//                            .show();
               }

            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String,String> doInBackground(Void... params)// TODO:获取Token_id以完成支付
        {
            Log.d(TAG,"微信支付预下单。。。");
           // 统一预下单接口
            //            String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", accessToken);
           String url = "https://pay.swiftpass.cn/pay/gateway";
           //            String entity = getParams();

            String entity = null;
           try {
                entity = getParams();
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
            }

            Log.d(TAG, "doInBackground, url = " + url);
            Log.d(TAG, "doInBackground, entity = " + entity);

            GetPrepayIdResult result = new GetPrepayIdResult();


            byte[] buf = Util.httpPost(url, entity);
            if (buf == null || buf.length == 0)
            {
                return null;
           }
            String content = new String(buf);
            Log.d(TAG, "doInBackground, content = " + content);
            Log.d(TAG, "微信支付的订单信息：" + content);
            result.parseFrom(content);
            try
            {

                return XmlUtils.parse(content);//状态码竟然有四百，是因为传入的商品的总价有小数，商品总价在这里是不能有小数的，因为其单位是分
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

       }
    }



    private String genNonceStr()
    {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 组装参数
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getParams() throws UnsupportedEncodingException {

        Map<String, String> params = new HashMap<String, String>();
        Random ran = new Random();
        String string="充值"+money+"元";
//        String str = new String(string.getBytes(),"UTF-8");
        //params.put("body", "充值 "+money+" 元");//TODO: 商品名称
        params.put("body",string); //TODO: 商品名称
        params.put("service", "unified.trade.pay"); // 支付类型
        params.put("version", "2.0"); // 版本

                //750291000067
        //商户号：'755437000006'
        //密钥：'7daa4babae15ae17eee90c9e'
        //appid：wx2a5538052969956e

        //750291000067 可用
        params.put("mch_id", "750291000067"); //TODO: 威富通商户号 750291000067  测试用的商户号：755437000006
        //        params.put("mch_id", mchId.getText().toString()); // 威富通商户号
        params.put("notify_url", Constants.WEBCHAT_NOTIFYURL); // 后台通知url,支付返回结果
        params.put("nonce_str", genNonceStr()); // 随机数
//        String out_trade_no = genOutTradNo();
        params.put("out_trade_no", orderNo); //订单号，后台给我
        Log.i("hehui", "out_trade_no-->" + orderNo);
        params.put("mch_create_ip", "127.0.0.1"); // 订单生成的机器ip地址,IP的话理应是用户手机IP，但微信实际上并没有做验证，所以默认为127.0.0.1都可以
        params.put("total_fee", (money*100)+""/*money.getText().toString()*/); // 总金额
//        params.put("device_info", "WP10000100001"); // 手Q反扫这个设备号必须要传1ff9fe53f66189a6a3f91796beae39fe 威富通支付分配的终端设备号
        params.put("limit_credit_pay", 0+""/*credit_pay.getText().toString()*/); // 是否限制信用卡支付， 0：不限制（默认），1：限制


        String sign = createSign("dbfacb210adab5e606a9bc8c", params); //TODO: 测试用的密钥 9d101c97133837e13dde2d32a5054abb   一直测试用的是 7daa4babae15ae17eee90c9e  威富通密钥 dbfacb210adab5e606a9bc8c

        //350135f70a5447524ed82e9017e2b958
        //params.put("sign", sign); // sign签名
        params.put("sign", sign);
        Log.d("WXChargeTool","sign");
      //C8D7340439A8E31F8CBF765DFF1FB402
        Log.d("WXChargeTool","sign");

        return XmlUtils.toXml(params);
    }

    public String createSign(String signKey, Map<String, String> params)
    {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        buf.append("&key=").append(signKey);
        String preStr = buf.toString();
        String sign = "";
        // 获得签名验证结果
        try
        {
            sign = MD5.md5s(preStr).toUpperCase();
        }
        catch (Exception e)
        {
            sign = MD5.md5s(preStr).toUpperCase();
        }
        return sign;
    }


    private String genOutTradNo()
    {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());

    }

}
