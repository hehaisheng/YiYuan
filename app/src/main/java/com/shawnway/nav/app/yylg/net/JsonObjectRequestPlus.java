package com.shawnway.nav.app.yylg.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shawnway.nav.app.yylg.tool.CookieUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/11.
 */
public class JsonObjectRequestPlus extends JsonObjectRequest {

    private static final String TAG = "JsonObjectRequestPlus";
    private Context mContext;
    private Map<String, String> sendHeader = new HashMap<String, String>(1);

    public JsonObjectRequestPlus(int method, String url, JSONObject jsonRequest,Context context, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        try {
            mContext = context;
            sendHeader.putAll(super.getHeaders());
        } catch (AuthFailureError authFailureError) {
            Log.d(TAG, authFailureError.getLocalizedMessage());
        }
    }


    public JsonObjectRequestPlus(int method, String url, String jsonStr,Context context, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws JSONException {
        this(method, url, new JSONObject(jsonStr),context ,listener, errorListener);

    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
//        sendHeader.put("Content-Type","application/json");
        return sendHeader;
    }

    public void setSendCookie(String cookie) {
        sendHeader.put("Cookie", cookie);

        try {
            Log.d(TAG, getHeaders().toString());
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
    }

    public void setSendCookie() {
        String localCookieStr = CookieUtil.getLocalCookies(mContext);
        if (!localCookieStr.equals("")) {
            setSendCookie(localCookieStr);
        }
    }


}
