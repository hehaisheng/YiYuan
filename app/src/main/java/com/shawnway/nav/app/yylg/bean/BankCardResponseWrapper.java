package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.resp.PayResponse;
import com.shawnway.nav.app.yylg.tool.PayHook;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class BankCardResponseWrapper extends PayResponse<BankCardPayBody> implements PayHook.PayType {

    @Override
    public String getPayType() {
        return PayHook.TYPE_WECHAT;
    }
}