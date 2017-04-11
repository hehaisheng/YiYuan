package com.shawnway.nav.app.yylg.bean;

import android.util.Log;

import com.shawnway.nav.app.yylg.bean.ResponseGson;

/**
 * Created by Eiffel on 2015/12/1.
 */
public class TradeoutWrapper extends ResponseGson<TradeoutWrapper.TradeoutBody> {

    public static class TradeoutBody{
        private static final String TAG = "TradeoutBody";
        String outTradeNo;
        String orderStatus;


        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public PayStatue getOrderStatus() {
            if (orderStatus==null) return PayStatue.STATUE_FAILEDQUERY;
            if (orderStatus.equals(PayStatue.STATUE_PAYED.toString()))
                return PayStatue.STATUE_PAYED;
            if (orderStatus.equals(PayStatue.STATUE_SUCCESS.toString()))
                return PayStatue.STATUE_SUCCESS;
            if (orderStatus.equals(PayStatue.STATUE_NOTPAY.toString()))
                return PayStatue.STATUE_NOTPAY;
            if (orderStatus.equals(PayStatue.STATUE_FAILEDQUERY.toString()))
                return PayStatue.STATUE_FAILEDQUERY;
            Log.e(TAG,"PayStatue not equal to enum PayStatue: "+orderStatus);
            return PayStatue.STATUE_FAILEDQUERY;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

    }

    public static enum PayStatue{

        STATUE_NOTPAY("NOTPAY"),STATUE_PAYED("PAYED"),STATUE_FAILEDQUERY("FAILED"),STATUE_SUCCESS("SUCCESS");

        // 定义私有变量
        private String sta ;

        // 构造函数，枚举类型只能为私有
        private PayStatue( String _nSta) {
            this .sta=_nSta;
        }

        @Override
        public String toString() {
            return sta;
        }
    }
}

