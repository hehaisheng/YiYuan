package com.shawnway.nav.app.yylg.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Kevin on 2016/12/2
 */

public class OkHttpClientManager {
    private static OkHttpClient okHttpClient;

    /**
     * 获取OkHttpClientManager
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClientManager.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    AddCookiesHeaderInterceptor addCookiesHeaderInterceptor = new AddCookiesHeaderInterceptor();
                    builder.addInterceptor(addCookiesHeaderInterceptor);
                    //超时时间
                    builder.connectTimeout(15, TimeUnit.SECONDS);//15S连接超时
                    builder.readTimeout(20, TimeUnit.SECONDS);//20s读取超时
                    builder.writeTimeout(20, TimeUnit.SECONDS);//20s写入超时
                    //错误重连
                    builder.retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }


}
