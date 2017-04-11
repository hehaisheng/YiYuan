package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.resp.PayResponse;
import com.shawnway.nav.app.yylg.tool.PayHook;

/**
 * Created by Eiffel on 2015/12/1.
 */
public class BalancePayResponseWrapper extends PayResponse<BalancePayResponseWrapper.BalanceResponseBody> {
    @Override
    public String getPayType() {
        return PayHook.TYPE_BALANCE;
    }

    public class BalanceResponseBody{

    }

}