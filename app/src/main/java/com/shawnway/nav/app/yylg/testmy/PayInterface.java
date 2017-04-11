package com.shawnway.nav.app.yylg.testmy;

import com.shawnway.nav.app.yylg.resp.PayResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017-03-09.
 */

public interface PayInterface {

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("customer/checkout")
    Call<PayResponse> postPay(@Body RequestBody paybean);//传入的参数为RequestBody

//    @POST("preOrder")
//    Observable<WXPayBean> requestPayPar(@Field("out_trade_no") String tradeNum,@Field("body") String orderbody,@Field("mch_create_ip") String mchip);

}
