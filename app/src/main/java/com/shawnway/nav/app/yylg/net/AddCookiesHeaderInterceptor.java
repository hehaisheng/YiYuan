package com.shawnway.nav.app.yylg.net;

import android.util.Log;
import android.widget.Toast;

import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.tool.CookieUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Kevin on 2016/12/2
 */

public class AddCookiesHeaderInterceptor implements Interceptor {
    private Request request;

    /**
     * 拦截器加入头部
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        String JSESSIONID = CookieUtil.getJSessionID(MyApplication.getInstance());
//        Response response = chain.proceed(chain.request().newBuilder().build());
//        String JSESSIONID = getCookieFromResponse(response);
        // 去掉cookie末尾的分号
        Log.d("AddCookiesHeaderInterce", JSESSIONID);
        if (JSESSIONID != null && !JSESSIONID.isEmpty()) {
            request = chain.request().newBuilder()
                    .addHeader("Cookie", JSESSIONID)
                    .build();
        } else request = chain.request().newBuilder().build();

        return chain.proceed(request);
    }


    /**
     * 从Response中获取cookie(需要记录的是登录后的Cookie)
     * 后期重写登录注册才使用这个JSESSIONID
     *
     * @param response
     * @return
     */
    private String getCookieFromResponse(Response response) {
        String cookieFromResponse = "";
        //直接访问的Response（没有缓存）
        String networkResponseHeader = response.networkResponse().headers().toString();
        //重定向访问的Response
        String priorResponseHeader = response.priorResponse().headers().toString();
        Pattern pattern = Pattern.compile("JSESSIONID.*?\n");
        Matcher netWorkMatcher = pattern.matcher(networkResponseHeader);
        if (netWorkMatcher.find()) {
            cookieFromResponse = netWorkMatcher.group();
            Log.w(TAG, "cookie from netWorkResponse " + cookieFromResponse);
        }
        Matcher priorMatcher = pattern.matcher(priorResponseHeader);
        if (priorMatcher.find()) {
            cookieFromResponse = priorMatcher.group();
            Log.w(TAG, "cookie from priorResponse " + cookieFromResponse);
        }
        return cookieFromResponse;
    }
}
