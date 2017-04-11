package com.shawnway.nav.app.yylg.resp;

import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.tool.PayHook;

/**
 * Created by Eiffel on 2015/12/1.
 */
public abstract  class PayResponse<T> extends ResponseGson<T> implements PayHook.PayType {

    @Override
    abstract public String getPayType() ;

}