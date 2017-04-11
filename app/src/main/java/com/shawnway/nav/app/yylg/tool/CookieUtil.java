package com.shawnway.nav.app.yylg.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.volley.NetworkResponse;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class CookieUtil {
    private static final String TAG = "CookieUtil";
    private static String mCookies;
    private static String mToken;
    private static String mJSESSIONID;

    public static void setLocalCookies(String cookies) {
        mCookies = cookies;
    }

    public static String getLocalCookies(Context context) {
        if (mCookies == null) {
            mCookies = (String) Utils.getParam(context, Constants.COOKIES_STORE_NAME, "");
        }
        return mCookies;
    }

    public static void apply(Context context) {
        Utils.setParam(context, Constants.COOKIES_STORE_NAME, mCookies);
    }

    public static void saveCookie(NetworkResponse response, Context context) {
        try {
            String cookieFromResponse = "";
            String mHeader = response.headers.toString();
            Log.w("LOG",
                    "get headers in parseNetworkResponse "
                            + response.headers.toString());
            // 使用正则表达式从reponse的头中提取cookie内容的子串
            Pattern pattern = Pattern.compile("Set-Cookie.*?,");
            Matcher m = pattern.matcher(mHeader);
            if (m.find()) {
                cookieFromResponse = m.group();
                Log.w("LOG", "cookie from server " + cookieFromResponse);
            }
            // 去掉cookie末尾的分号
            if (cookieFromResponse != null && !cookieFromResponse.isEmpty()) {
                cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
                Log.d(TAG, "after subString of Cookie:" + cookieFromResponse);
                mCookies = cookieFromResponse;
                if (mCookies.contains(Constants.JSESSIONID)) {
                    mJSESSIONID = mCookies;
                    applyJSessionID(context);
                }
                apply(context);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private static void applyJSessionID(Context context) {
        Utils.setParam(context, Constants.JSESSIONID, mJSESSIONID);
    }

    public static String getJSessionID(Context context) {
        if (mJSESSIONID == null) {
            mJSESSIONID = (String) Utils.getParam(context, Constants.JSESSIONID, "");
        }
        return mJSESSIONID;

    }

}
