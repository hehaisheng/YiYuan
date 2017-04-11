package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.resp.PayResponse;
import com.shawnway.nav.app.yylg.tool.PayHook;

/**
 * Created by Eiffel on 2015/11/26.
 */
public class WeChatResponseWrapper extends PayResponse<WeChatPayBody> implements PayHook.PayType {

    @Override
    public String getPayType() {
        return PayHook.TYPE_WECHAT;
    }
}
